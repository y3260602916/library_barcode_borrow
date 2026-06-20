# ocr_api.py 修复版
import cv2
from pyzbar.pyzbar import decode, ZBarSymbol
import requests
import base64
from config import BAIDU_API_KEY, BAIDU_SECRET_KEY

def get_access_token():
    """获取百度API Token"""
    print("正在获取百度Token...")
    url = f"https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={BAIDU_API_KEY}&client_secret={BAIDU_SECRET_KEY}"
    try:
        res = requests.get(url, timeout=10)
        res.raise_for_status()
        token = res.json().get("access_token")
        print(f"✅ 百度Token获取成功: {token[:20]}...")
        return token
    except Exception as e:
        print(f"❌ 百度Token获取失败: {str(e)}")
        return None

def baidu_barcode_recognize(img_path):
    """百度OCR识别"""
    token = get_access_token()
    if not token:
        print("⚠️  百度Token无效，跳过百度识别")
        return None

    try:
        print("正在调用百度OCR...")
        with open(img_path, "rb") as f:
            img_base64 = base64.b64encode(f.read()).decode()

        url = f"https://aip.baidubce.com/rest/2.0/ocr/v1/qrcode?access_token={token}"
        headers = {"Content-Type": "application/x-www-form-urlencoded"}
        data = {"image": img_base64}

        resp = requests.post(url, headers=headers, data=data, timeout=15)
        res_json = resp.json()
        print(f"百度OCR返回结果: {res_json}")

        if "error_code" in res_json:
            print(f"❌ 百度OCR错误: {res_json['error_code']} {res_json['error_msg']}")
            return None

        code_list = res_json.get("codes_result", [])
        if not code_list:
            print("⚠️  百度OCR未识别到条码")
            return None

        raw_text = code_list[0].get("text", [])
        if not raw_text:
            print("⚠️  百度OCR识别结果为空")
            return None
        
        full_code = "".join(raw_text)
        print(f"✅ 百度OCR识别成功: {full_code}")
        return full_code

    except Exception as e:
        print(f"❌ 百度OCR识别异常: {str(e)}")
        return None

def local_barcode_recognize(img_path):
    """优化后的本地pyzbar识别，支持多种预处理方式和条码格式"""
    print("正在调用本地pyzbar识别...")
    try:
        # 读取图片
        img = cv2.imread(img_path)
        if img is None:
            print("❌ 图片读取失败")
            return None
        
        # 打印图片信息
        height, width = img.shape[:2]
        print(f"📷 图片尺寸: {width}x{height}")
        
        # 转换为灰度图
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # 定义尝试的预处理方式（增加更多选项）
        preprocess_methods = [
            ("原始灰度", gray),
            ("高斯模糊3x3", cv2.GaussianBlur(gray, (3, 3), 0)),
            ("高斯模糊5x5", cv2.GaussianBlur(gray, (5, 5), 0)),
            ("二值化80", cv2.threshold(gray, 80, 255, cv2.THRESH_BINARY_INV)[1]),
            ("二值化100", cv2.threshold(gray, 100, 255, cv2.THRESH_BINARY_INV)[1]),
            ("二值化127", cv2.threshold(gray, 127, 255, cv2.THRESH_BINARY_INV)[1]),
            ("二值化150", cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY_INV)[1]),
            ("二值化180", cv2.threshold(gray, 180, 255, cv2.THRESH_BINARY_INV)[1]),
            ("OTSU", cv2.threshold(cv2.GaussianBlur(gray, (3, 3), 0), 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)[1]),
            ("自适应阈值", cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 2)),
            ("中值滤波3x3", cv2.medianBlur(gray, 3)),
            ("中值滤波5x5", cv2.medianBlur(gray, 5)),
            ("Sobel边缘", cv2.Sobel(gray, cv2.CV_8U, 1, 0, ksize=3)),
        ]
        
        # 定义要尝试的条码格式（移除容易出错的UPC_E）
        symbol_types = [
            ("EAN-13", ZBarSymbol.EAN13),   # EAN-13（图书常用）
            ("UPC-A", ZBarSymbol.UPCA),      # UPC-A
            ("CODE-128", ZBarSymbol.CODE128),# Code 128
            ("EAN-8", ZBarSymbol.EAN8),      # EAN-8
            ("QRCODE", ZBarSymbol.QRCODE),   # 二维码
            ("CODE-39", ZBarSymbol.CODE39),  # Code 39
            ("CODE-93", ZBarSymbol.CODE93),  # Code 93
        ]
        
        # 遍历所有预处理方式
        for method_name, processed_img in preprocess_methods:
            # 对每种格式单独尝试，避免一个格式失败影响其他格式
            for format_name, symbol_type in symbol_types:
                try:
                    barcodes = decode(processed_img, symbols=[symbol_type])
                    if barcodes:
                        code_data = barcodes[0].data.decode("utf-8")
                        print(f"✅ 本地识别成功({method_name}, {format_name}): {code_data}")
                        return code_data
                except Exception as e:
                    # 单个格式识别失败不影响其他格式
                    print(f"⚠️  {format_name}格式识别失败: {str(e)}")
        
        print("❌ 本地未识别到条码")
        return None

    except Exception as e:
        print(f"❌ 本地识别异常: {str(e)}")
        return None

def barcode_recognize(img_path):
    """主入口：优先百度，失败切本地"""
    print(f"===== 开始识别图片: {img_path} =====")
    # 优先百度OCR
    baidu_code = baidu_barcode_recognize(img_path)
    if baidu_code:
        print("✅ 最终使用百度识别结果")
        return baidu_code
    
    # 百度失败，切换本地
    print("⚠️  切换为本地识别模式")
    local_code = local_barcode_recognize(img_path)
    if local_code:
        print("✅ 最终使用本地识别结果")
        return local_code
    
    print("❌ 所有识别方式均失败")
    return None
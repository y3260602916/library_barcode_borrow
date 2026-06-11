# ocr_api.py 修复版
import cv2
from pyzbar.pyzbar import decode
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
    """本地pyzbar识别 兜底"""
    print("正在调用本地pyzbar识别...")
    try:
        img = cv2.imread(img_path)
        if img is None:
            print("❌ 图片读取失败")
            return None
        
        # 图像预处理
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        blurred = cv2.GaussianBlur(gray, (3,3), 0)
        _, thresh = cv2.threshold(blurred, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

        barcodes = decode(thresh)
        if not barcodes:
            print("❌ 本地未识别到条码")
            return None

        code_data = barcodes[0].data.decode("utf-8")
        print(f"✅ 本地识别成功: {code_data}")
        return code_data

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
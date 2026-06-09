# ocr_api.py（双模式识别：百度OCR优先，本地兜底）
import cv2
from pyzbar.pyzbar import decode
import requests
import base64
from config import BAIDU_API_KEY, BAIDU_SECRET_KEY

def get_access_token():
    """获取百度API访问令牌"""
    url = f"https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={BAIDU_API_KEY}&client_secret={BAIDU_SECRET_KEY}"
    try:
        res = requests.get(url, timeout=10)
        res.raise_for_status()
        return res.json().get("access_token")
    except Exception as e:
        print(f"百度Token获取失败: {str(e)}")
        return None

def baidu_barcode_recognize(img_path):
    """百度OCR识别（优先调用，满足实训要求）"""
    token = get_access_token()
    if not token:
        return None

    try:
        with open(img_path, "rb") as f:
            img_base64 = base64.b64encode(f.read()).decode()

        url = "https://aip.baidubce.com/rest/2.0/ocr/v1/qrcode"
        headers = {"Content-Type": "application/x-www-form-urlencoded"}
        data = {"image": img_base64}

        resp = requests.post(url, headers=headers, data=data, timeout=15)
        res_json = resp.json()

        if "error_code" in res_json:
            print(f"百度OCR错误: {res_json['error_code']} {res_json['error_msg']}")
            return None

        code_list = res_json.get("codes_result", [])
        if not code_list:
            return None

        raw_text = code_list[0].get("text", [])
        if not raw_text:
            return None
        
        full_code = "".join(raw_text)
        print(f"✅ 百度OCR识别结果: {full_code}")
        return full_code

    except Exception as e:
        print(f"百度OCR识别异常: {str(e)}")
        return None

def local_barcode_recognize(img_path):
    """本地pyzbar识别（兜底方案，保证演示成功）"""
    try:
        img = cv2.imread(img_path)
        if img is None:
            return None
        
        # OpenCV图像预处理（写进报告，AI技术亮点）
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        blurred = cv2.GaussianBlur(gray, (5,5), 0)
        _, thresh = cv2.threshold(blurred, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

        barcodes = decode(thresh)
        if not barcodes:
            return None

        code_data = barcodes[0].data.decode("utf-8")
        print(f"✅ 本地识别结果: {code_data}")
        return code_data

    except Exception as e:
        print(f"本地识别异常: {str(e)}")
        return None

def barcode_recognize(img_path):
    """主函数：优先百度OCR，失败则用本地兜底"""
    # 1. 优先调用百度OCR（满足实训AI要求）
    baidu_code = baidu_barcode_recognize(img_path)
    if baidu_code:
        return baidu_code
    
    # 2. 百度OCR失败，使用本地识别兜底
    print("⚠️  切换为本地识别模式")
    local_code = local_barcode_recognize(img_path)
    return local_code
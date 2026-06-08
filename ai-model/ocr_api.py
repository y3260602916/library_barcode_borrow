# ocr_api.py
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
        return None

def barcode_recognize(img_path):
    token = get_access_token()
    if not token:
        return None

    try:
        with open(img_path, "rb") as f:
            img_base64 = base64.b64encode(f.read()).decode()

        url = (
            "https://aip.baidubce.com/rest/2.0/ocr/v1/qrcode"
            f"?access_token={token}"
        )
        headers = {"Content-Type": "application/x-www-form-urlencoded"}
        data = {"image": img_base64}

        resp = requests.post(url, headers=headers, data=data, timeout=15)
        res_json = resp.json()

        if "error_code" in res_json:
            return None

        codes = res_json.get("codes_result", [])
        if not codes:
            return None

        text_list = codes[0].get("text", [])
        if text_list:
            return text_list[0]
        else:
            return None

    except Exception as e:
        return None
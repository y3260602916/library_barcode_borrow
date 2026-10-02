# image_preprocess.py
import cv2
from config import TEMP_IMG_PATH

def preprocess_barcode_image(img_path, save_path=TEMP_IMG_PATH):
    """
    条码图片预处理：读取图片并转为灰度图保存。
    实际的多策略预处理（高斯模糊、二值化、OTSU等）在 ocr_api.py 的 local_barcode_recognize 中完成。
    :param img_path: 原始图片路径
    :param save_path: 预处理后图片保存路径
    :return: 保存成功返回路径，失败返回 None
    """
    img = cv2.imread(img_path)
    if img is None:
        print("❌ 错误：无法读取图片")
        return None
    # 转为灰度图
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    cv2.imwrite(save_path, gray)
    print(f"✅ 灰度化后已保存：{save_path}")
    return save_path

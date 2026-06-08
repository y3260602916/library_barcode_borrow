# image_preprocess.py
import cv2
from config import TEMP_IMG_PATH

def preprocess_barcode_image(img_path, save_path=TEMP_IMG_PATH):
    # 不做任何处理，直接复制原图
    img = cv2.imread(img_path)
    if img is None:
        print("❌ 错误：无法读取图片")
        return None
    cv2.imwrite(save_path, img)
    print(f"✅ 原图已保存：{save_path}")
    return save_path
# test_ai.py
import os
from config import TEMP_IMG_PATH
from image_preprocess import preprocess_barcode_image
from ocr_api import barcode_recognize

def test_ai_module(img_path):
    """
    本地测试AI识别模块（不用SpringBoot，直接传图片路径）
    :param img_path: 你电脑上任意一张条码图片的路径
    """
    print("=" * 50)
    print("📚 开始测试条码识别模块")
    print(f"原始图片路径：{img_path}")
    print("=" * 50)

    # 1. 预处理图片
    process_path = preprocess_barcode_image(img_path, TEMP_IMG_PATH)
    if not process_path:
        print("❌ 预处理失败，终止流程")
        return

    # 2. 调用API识别条码
    barcode = barcode_recognize(process_path)

    # 3. 清理临时文件
    if os.path.exists(TEMP_IMG_PATH):
        os.remove(TEMP_IMG_PATH)
        print("🧹 临时文件已清理")

    # 4. 输出最终结果
    print("\n" + "=" * 50)
    if barcode:
        print(f"🎉 测试成功！识别到的条码：{barcode}")
    else:
        print("❌ 测试失败，未识别到有效条码")
    print("=" * 50)

if __name__ == "__main__":
    # -------------------- 这里改成你电脑上的条码图片路径 --------------------
    # 比如：img_path = "D:/test_barcode.jpg"
    img_path = r"D:\library_barcode_borrow\ai-model\img\test.jpg"
    # -------------------------------------------------------------------
    test_ai_module(img_path)
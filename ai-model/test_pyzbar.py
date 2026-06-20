#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""测试 pyzbar 条码识别功能"""

import cv2
from pyzbar.pyzbar import decode, ZBarSymbol

def test_single_format(image_path, symbol_type, format_name):
    """测试单个条码格式"""
    try:
        img = cv2.imread(image_path)
        if img is None:
            print(f"❌ 图片读取失败")
            return None
        
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # 测试多种预处理方式
        preprocess_methods = [
            ("原始灰度", gray),
            ("高斯模糊", cv2.GaussianBlur(gray, (3, 3), 0)),
            ("二值化127", cv2.threshold(gray, 127, 255, cv2.THRESH_BINARY_INV)[1]),
        ]
        
        for method_name, processed_img in preprocess_methods:
            try:
                barcodes = decode(processed_img, symbols=[symbol_type])
                if barcodes:
                    code_data = barcodes[0].data.decode("utf-8")
                    print(f"✅ {format_name} 识别成功({method_name}): {code_data}")
                    return code_data
            except Exception as e:
                print(f"⚠️  {format_name} ({method_name})识别失败: {str(e)}")
        
        return None
    except Exception as e:
        print(f"❌ {format_name} 测试异常: {str(e)}")
        return None

def main():
    import sys
    if len(sys.argv) < 2:
        print("用法: python test_pyzbar.py <图片路径>")
        sys.exit(1)
    
    image_path = sys.argv[1]
    print(f"===== 测试图片: {image_path} =====")
    
    # 测试所有支持的格式
    formats_to_test = [
        ("EAN-13", ZBarSymbol.EAN13),
        ("UPC-A", ZBarSymbol.UPCA),
        ("CODE-128", ZBarSymbol.CODE128),
        ("EAN-8", ZBarSymbol.EAN8),
        ("QRCODE", ZBarSymbol.QRCODE),
    ]
    
    results = []
    for format_name, symbol_type in formats_to_test:
        result = test_single_format(image_path, symbol_type, format_name)
        if result:
            results.append((format_name, result))
    
    if results:
        print("\n✅ 识别成功的格式:")
        for fmt, code in results:
            print(f"  - {fmt}: {code}")
    else:
        print("\n❌ 所有格式均识别失败")

if __name__ == "__main__":
    main()
# main.py
import sys
from ocr_api import barcode_recognize
import sys
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

def main():
    if len(sys.argv) < 2:
        print("")  # 这里也不要打印文字，空输出即可
        return

    img_path = sys.argv[1]
    code = barcode_recognize(img_path)
    # 只输出条码字符串，供Java读取
    print(code if code else "")

if __name__ == "__main__":
    main()
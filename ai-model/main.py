# main.py
import sys
import io
from ocr_api import barcode_recognize
from ai_recommend import get_book_recommend

# 全局编码统一
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')
# 关键修复：设置标准输入编码为UTF-8
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')

def main():
    if len(sys.argv) < 2:
        print("")
        return

    func_type = sys.argv[1]

    # 1. 条码识别（原有逻辑不变）
    if func_type == "recognize":
        if len(sys.argv) < 3:
            print("")
            return
        img_path = sys.argv[2]
        code = barcode_recognize(img_path)
        print(code if code else "")

    # 2. AI推荐：从 标准输入 读取历史文本，不再取命令行参数
    elif func_type == "recommend":
        # 读取Java传入的整段历史文本
        history_text = sys.stdin.read().strip()
        rec_list = get_book_recommend(history_text)
        # 输出纯JSON
        import json
        print(json.dumps(rec_list, ensure_ascii=False))

if __name__ == "__main__":
    main()
# ai_recommend.py
import json
import dashscope
from dashscope import Generation
from config import DASHSCOPE_API_KEY

def get_book_recommend(user_history):
    dashscope.api_key = DASHSCOPE_API_KEY

    prompt = f"""
你是一个专业的图书管理员，需要根据用户的借阅历史，推荐5本适合的图书，并给出**个性化的推荐理由**。
用户借阅历史：{user_history}

要求：
1. 只返回标准 JSON 数组，不要任何额外内容、注释、markdown。
2. 每个对象必须包含：bookName, author, category, reason 四个字段。
3. `reason` 必须基于用户的借阅历史，写出具体、有针对性的推荐理由，而不是笼统的"推荐阅读此类书籍"。
   - 例如：用户借过《死魂灵》，推荐理由可以写"你曾借阅过俄国文学作品，这本《xxx》同样具有深刻的社会批判风格，延续你对经典文学的探索"。
4. 推荐的图书要和用户借阅过的图书在类别、风格或主题上相关。
"""

    try:
        response = Generation.call(
            model='qwen-turbo',
            messages=[
                {'role': 'user', 'content': prompt}
            ],
            result_format='json',
            temperature=0.7
        )

        if response.status_code == 200:
            content = response.output.choices[0].message.content
            return json.loads(content)
        else:
            print(f"百炼调用失败：{response.code} - {response.message}")
            return []
    except Exception as e:
        print(f"AI推荐异常：{str(e)}")
        return []
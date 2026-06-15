图书馆条码借阅系统 数据库结构说明
数据库名：library_db，基于 MySQL 8.0，共 5 张业务表，字符集 utf8mb4，支持完整中文、特殊字符存储，以下为逐表结构、字段释义、关联关系说明。
一、sys_user 用户表（师生 / 管理员）
作用：存储系统所有登录用户，区分学生与管理员角色，是整个系统的用户主体表。
字段名 类型 约束 字段说明
user_id int 主键、自增 用户唯一 ID
user_name varchar (30) 非空 用户真实姓名
user_account varchar (20) 非空、唯一索引 登录账号 / 学号，全局唯一
user_type tinyint 非空 用户类型：0 = 学生，1 = 管理员
create_time datetime 默认当前时间 账号创建时间
user_pwd varchar (100) 非空、默认 123456 登录密码，初始默认密码 123456
索引：user_account 唯一索引，保证账号不重复。
二、book_info 图书信息表
作用：核心图书数据表，存储图书基础信息、库存数据，条码为 AI 识别核心字段。
字段名 类型 约束 字段说明
book_id int 主键、自增 图书唯一 ID
book_barcode varchar (20) 非空、唯一索引 图书条码（AI 条码识别目标字段），全局唯一
book_name varchar (100) 非空 图书名称
author varchar (50) 可空 作者
category varchar (30) 可空 图书分类（计算机、文学、历史、科幻等）
total_stock int 非空、默认 0 图书总库存
remain_stock int 非空、默认 0 剩余可借库存（借阅 / 归还时同步变更）
price decimal (8,2) 可空 图书单价
索引：book_barcode 唯一索引，防止重复条码图书。
三、borrow_record 图书借阅记录表
作用：记录所有图书借阅、归还行为，关联用户与图书，支持逾期、罚金统计。
字段名 类型 约束 字段说明
record_id int 主键、自增 借阅记录唯一 ID
user_id int 非空、普通索引 关联 sys_user.user_id，借阅人 ID
book_id int 非空、普通索引 关联 book_info.book_id，借阅图书 ID
borrow_time datetime 默认当前时间 借阅操作时间
return_time datetime 可空 归还时间，为空代表图书未归还
is_overdue tinyint 默认 0 是否逾期：0 = 正常，1 = 逾期
fine_money decimal (8,2) 默认 0.00 逾期产生的罚金金额
外键关联
user_id → sys_user.user_id：删除 / 更新限制，级联不生效，保证数据完整性
book_id → book_info.book_id：同上
四、inventory_record 出入库记录表
作用：图书库存变动日志，记录管理员图书入库、出库操作。
字段名 类型 约束 字段说明
record_id int 主键、自增 出入库记录 ID
book_id int 非空 对应图书 ID
book_name varchar (100) 非空 图书名称（冗余字段，方便查询展示）
type tinyint 非空 操作类型：1 = 入库，2 = 出库
quantity int 非空 变动数量
operator varchar (50) 非空 操作人名称
operate_time datetime 默认当前时间 操作时间
remark varchar (200) 可空 备注信息
五、scan_record AI 条码识别日志表
作用：专门记录 AI 图片条码识别的全量日志，追溯每一次扫码行为、识别结果。
字段名 类型 约束 字段说明
scan_id int 主键、自增 识别记录 ID
img_path varchar (200) 非空 上传图片本地存储路径
scan_barcode varchar (20) 可空 AI 识别解析出的图书条码，识别失败则为空
scan_time datetime 默认当前时间 扫码识别时间
result varchar (20) 非空 识别结果：成功 / 失败
整体表关联关系总结
用户 ↔ 借阅记录：一对多（一个学生可产生多条借阅记录）
图书 ↔ 借阅记录：一对多（一本图书可被多人多次借阅）
图书 ↔ 出入库记录：一对多（一本图书可多次入库 / 出库）
条码识别日志：独立日志表，仅记录扫码行为，不直接关联业务表，可通过 scan_barcode 关联 book_info 图书表
核心业务逻辑对应表
账号登录、角色区分 → sys_user
图书管理（增删改查、库存） → book_info
借书 / 还书、逾期罚金 → borrow_record
图书盘点、入库出库 → inventory_record
图片 AI 扫码、识别追溯 → scan_record
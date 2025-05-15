-- 创建student学生表
USE springdemo2024;
DROP TABLE IF EXISTS Student;
CREATE TABLE Student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) COMMENT '姓名',
    student_id VARCHAR(50) COMMENT '学号',
    gender CHAR(1) COMMENT '性别',
    phone_number VARCHAR(20) COMMENT '电话号码',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
)engine = innodb default character set utf8;
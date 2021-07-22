DELETE FROM SHARDING_TEST;

INSERT INTO SHARDING_TEST (id, REG_DATE, name, SHARDING_KEY) VALUES
(1, '2021-07-09', '张三', 'test1@baomidou.com'),
(2, '2021-06-08', '李四', 'test2@baomidou.com'),
(3, '2020-05-07', '王五', 'test3@baomidou.com'),
(4, '2020-03-06', '赵六', 'test4@baomidou.com'),
(5, '2022-04-05', '周七', 'test5@baomidou.com');
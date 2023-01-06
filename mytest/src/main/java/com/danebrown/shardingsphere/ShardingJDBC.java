package com.danebrown.shardingsphere;

import com.danebrown.shardingsphere.dao.ShardingTest;
import com.danebrown.shardingsphere.mapper.ShardingTestMapper;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by danebrown on 2021/7/22
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@MapperScan("com.danebrown.shardingsphere")
@Log4j2
public class ShardingJDBC {
    private static final String MYBATIS_CONFIG = "org/mybatis/example/mybatis" + "-config.xml";
    @Autowired
    private ApplicationContext context;

    @Autowired
    private ShardingTestMapper mapper;

//    public void init() {
//        try {
//            InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG);
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    public ShardingTest generateShardingTest(){
        ShardingTest test = new ShardingTest();
        test.setName(UUID.randomUUID().toString());
        test.setRegDate(new Date());
        test.setShardingKey(UUID.randomUUID().toString());
        return test;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(){
        ShardingTest test = generateShardingTest();
        int result = mapper.insert(test);
        return result;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insert_reqnew(){
        ShardingTest test = generateShardingTest();
        test.setName("req_new"+test.getName());
        int result = mapper.insert(test);
        return result;
    }
    @Transactional(propagation = Propagation.NESTED)
    public int insert_nest(){
        ShardingTest test = generateShardingTest();
        test.setName("nest"+test.getName());
        int result = mapper.insert(test);
        return result;
    }
    public void runsql() {
        List<ShardingTest> list = mapper.selectList(null);
        log.info("selectList:[{}]",list);
    }
}

package com.danebrown.shardingsphere;

import com.danebrown.shardingsphere.dao.ShardingTest;
import com.danebrown.shardingsphere.mapper.ShardingTestMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    public void runsql() {
        List<ShardingTest> list = mapper.selectList(null);
        log.info("selectList:[{}]",list);
    }
}

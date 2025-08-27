package lintcode;


import org.jasypt.encryption.StringEncryptor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class AppTest 

{
    @Autowired
    ConfigurableEnvironment environment;

    @Autowired
            @Qualifier("jasyptStringEncryptor")
    StringEncryptor encryptor;

    @Autowired
    ApplicationContext applicationContext;
//
//    static {
//        System.setProperty("jasypt.encryptor.password", "password");
//    }

    @Test
    public void decryptEncript() {
        System.out.println(encryptor.encrypt("config-server"));
        Assert.assertEquals(encryptor.decrypt("5HHB8n649TNGdJxLjV1PFO8xPmrhWeDFWizBn1y0kPQesBA5jb+Oz1yEP9YLBBtA"), "chupacabras");

    }

}

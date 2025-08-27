package lintcode;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * Hello world!
 *
 */
@Configuration
@EnableEncryptableProperties
@SpringBootApplication
public class App implements CommandLineRunner
{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    @Autowired
            @Qualifier("jasyptStringEncryptor")
    StringEncryptor encryptor;
    public static void main( String[] args )
    {
        new SpringApplicationBuilder()
                //.environment(new StandardEncryptableEnvironment())
                .sources(App.class)
                .run(args);
    }
    @Value("${defaultPassword}")
    private String pwd;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(encryptor.encrypt("config-server"));
        System.out.println(encryptor.decrypt("5HHB8n649TNGdJxLjV1PFO8xPmrhWeDFWizBn1y0kPQesBA5jb+Oz1yEP9YLBBtA"));
        System.out.println(pwd);
    }
}

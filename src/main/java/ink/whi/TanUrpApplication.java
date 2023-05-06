package ink.whi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@EnableMongoRepositories
@ServletComponentScan
@SpringBootApplication
public class TanUrpApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TanUrpApplication.class).allowCircularReferences(true).run(args);
    }
}

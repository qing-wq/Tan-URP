package ink.whi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@MapperScan
@ServletComponentScan
@SpringBootApplication
public class TanUrpApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TanUrpApplication.class).allowCircularReferences(true).run(args);
    }
}
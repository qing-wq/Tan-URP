package ink.whi;

import ink.whi.web.hook.interceptor.GlobalInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: qing
 * @Date: 2023/5/2
 */
@MapperScan
@ServletComponentScan
@SpringBootApplication
public class TanUrpApplication implements WebMvcConfigurer {
    @Autowired
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor).addPathPatterns("/**").excludePathPatterns("/user/login");
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TanUrpApplication.class).allowCircularReferences(true).run(args);
    }
}

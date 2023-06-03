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
@MapperScan("ink.whi.service") // fixme: 包下面的接口类，在编译之后都会生成相应的实现类
@ServletComponentScan
@SpringBootApplication
public class TanUrpApplication implements WebMvcConfigurer {
    @Autowired
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/images/**")     // 图床
                .excludePathPatterns("/error")
                .excludePathPatterns("/user/login");   // 登录
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TanUrpApplication.class).allowCircularReferences(true).run(args);
    }
}

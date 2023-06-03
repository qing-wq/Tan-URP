package ink.whi.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Administrator
 * @Date: 2023/6/4
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            registry.addResourceHandler("/images/**").addResourceLocations("file:D:\\tmp\\tan-urp\\image\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

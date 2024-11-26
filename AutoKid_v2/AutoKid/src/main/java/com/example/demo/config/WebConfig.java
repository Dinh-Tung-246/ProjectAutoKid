package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //Cấu hình để khi truy cập vào URL html có thể đọc được css
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/autokid/**")
                .addResourceLocations("classpath:/templates/autokid/");

        registry.addResourceHandler("/autokid/shoping-cart/**")
                .addResourceLocations("classpath:/templates/autokid/");

        registry.addResourceHandler("/autokid/shop/**")
                .addResourceLocations("classpath:/templates/autokid/");

        registry.addResourceHandler("/autokid/account/**")
                .addResourceLocations("classpath:/templates/autokid/");

        registry.addResourceHandler("/admin/customer-management/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/admin/products/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/admin/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/admin/hoadon/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/admin/promotion/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/admin/staff/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/admin/ban-hang/**")
                .addResourceLocations("classpath:/templates/admin/");

        registry.addResourceHandler("/autokid/login/**")
                .addResourceLocations("classpath:/templates/autokid/");

//        registry.addResourceHandler("/autokid/checkout/**")
//                .addResourceLocations("classpath:/templates/autokid/");

        registry.addResourceHandler("/img/categories/**")
                .addResourceLocations("classpath:/static/img/categories/");
    }
}

package id.ac.ui.cs.advprog.mewingmenu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import id.ac.ui.cs.advprog.mewingmenu.utils.EndpointLogger;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private EndpointLogger endpointLogger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(endpointLogger);
    }
}

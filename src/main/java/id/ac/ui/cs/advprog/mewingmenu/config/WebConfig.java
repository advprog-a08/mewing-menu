package id.ac.ui.cs.advprog.mewingmenu.config;

import id.ac.ui.cs.advprog.mewingmenu.interceptor.AuthInterceptor;
import id.ac.ui.cs.advprog.mewingmenu.resolver.AuthenticatedUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import id.ac.ui.cs.advprog.mewingmenu.utils.EndpointLogger;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private EndpointLogger endpointLogger;

    private final AuthenticatedUserArgumentResolver argumentResolver;
    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthenticatedUserArgumentResolver argumentResolver, AuthInterceptor authInterceptor) {
        this.argumentResolver = argumentResolver;
        this.authInterceptor = authInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(endpointLogger);
    }
}

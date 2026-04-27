package demo.codeexample.config;

import demo.codeexample.filter.TenantFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TenantFilter tenantFilter;

    public WebConfig(TenantFilter tenantFilter) {
        this.tenantFilter = tenantFilter;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/{company}",
                c -> c.isAnnotationPresent(Controller.class)
                        && !c.isAnnotationPresent(RestController.class));
        // ↑ only applies to @Controller (web pages), not @RestController (API)
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantFilter);
    }
}

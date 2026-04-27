package demo.codeexample.config;

import demo.codeexample.security.NoCacheFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
        FilterRegistrationBean<NoCacheFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new NoCacheFilter());

        registration.addUrlPatterns(
                "/dashboard/*",
                "/projects/*",
                "/*/login",
                "/*/login/*",
                "/login/*",
                "/logout",
                "/*/logout"
        );

        registration.setOrder(1);

        return registration;
    }
}
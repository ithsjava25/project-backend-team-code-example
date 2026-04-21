package demo.codeexample.filter;

import demo.codeexample.company.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class TenantFilter implements HandlerInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) {
//
//        String[] parts = request.getRequestURI().split("/");
//        if (parts.length > 1) {
//            TenantContext.setTenant(parts[1]);
//            //Validate company name?
//            //Get more info from db?
//        } else
//            TenantContext.setTenant("");
//        return true;
//    }


    private static final Set<String> NON_TENANT_ROOTS = Set.of(
            "css", "js", "images", "oauth2", "login", "web",
            "aboutUs", "error", "favicon.ico", "actuator"
            );

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String uri = request.getRequestURI();
        String[] parts = uri.split("/");

        if (parts.length > 1) {
            String firstSegment = parts[1];

            TenantContext.setTenant(
                    NON_TENANT_ROOTS.contains(firstSegment) ? "" : firstSegment);
        }else {
            TenantContext.setTenant("");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        TenantContext.clear();
    }
}

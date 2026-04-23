package demo.codeexample.web.web;

import org.springframework.ui.Model;
import demo.codeexample.company.TenantContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute("company")
    public String addTenant(Model model){
        return TenantContext.getTenant();
    }

}

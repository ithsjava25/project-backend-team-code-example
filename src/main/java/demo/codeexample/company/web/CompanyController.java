package demo.codeexample.company.web;

import demo.codeexample.company.TenantContext;
import demo.codeexample.company.application.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping({"","/"})
    public String index(Model model) {
        String company = TenantContext.getTenant();

        model.addAttribute("welcomeMessage", "Neon Nights");
        model.addAttribute("company", company);

        return "home";
    }
}

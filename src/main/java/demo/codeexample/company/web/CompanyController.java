package demo.codeexample.company.web;

import demo.codeexample.company.TenantContext;
import demo.codeexample.company.application.CompanyService;
import demo.codeexample.project.ProjectLookup;
import demo.codeexample.project.domain.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final ProjectLookup projectLookup;

    @GetMapping({"","/"})
    public String index(Model model) {
        String company = TenantContext.getTenant();

        model.addAttribute("company", company);
        model.addAttribute("projects",projectLookup.findAllProjects());
        return "home";
    }

    @GetMapping("/aboutUs")
    public String aboutUs(Model model) {
        model.addAttribute("company", TenantContext.getTenant());
        return "aboutUs";
    }
}

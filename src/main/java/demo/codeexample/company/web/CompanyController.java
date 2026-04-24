package demo.codeexample.company.web;

import demo.codeexample.company.TenantContext;
import demo.codeexample.company.application.CompanyService;
import demo.codeexample.project.ProjectLookup;
import demo.codeexample.s3FileStorage.S3FileLookup;
import demo.codeexample.shared.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompanyController {

    private final ProjectLookup projectLookup;

    @GetMapping({"", "/"})
    public String index(@ModelAttribute("company") String companyName, Model model) {

        model.addAttribute("projects", projectLookup.findAllCompletedProjectsByCompany(companyName));
        model.addAttribute("films",projectLookup.findProjectByCategory(Category.FILM));
        model.addAttribute("series",projectLookup.findProjectByCategory(Category.SERIES));

        return "home";
    }

    @GetMapping("/aboutUs")
    public String aboutUs(Model model) {
        model.addAttribute("company", TenantContext.getTenant());
        return "aboutUs";
    }
}

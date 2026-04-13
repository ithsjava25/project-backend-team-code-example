package demo.codeexample.company.web;


import demo.codeexample.company.application.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@AllArgsConstructor
@RequestMapping
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping({"","/","index"})
    public String index(Model model) {
        model.addAttribute("welcomeMessage", "Neon Nights");
        model.addAttribute("companyName", "Hexagonal Film Productions");
        return "home";
    }

    @GetMapping("/aboutUs")
    public String home2() {
        return "aboutUs";
    }

}

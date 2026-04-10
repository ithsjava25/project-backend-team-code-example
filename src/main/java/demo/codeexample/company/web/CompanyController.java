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

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("welcomeMessage", "Neon Nights");
        model.addAttribute("companyName", "Hexagonal Film Productions");
        return "home";
    }

    @GetMapping("home2")
    public String home2() {
        return "home2";
    }
    @GetMapping("home3")
    public String home3() {
        return "home3";
    }
    @GetMapping("home4")
    public String home4() {
        return "home4";
    }
}

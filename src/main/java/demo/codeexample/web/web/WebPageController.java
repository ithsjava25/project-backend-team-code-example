package demo.codeexample.web.web;

import demo.codeexample.company.TenantContext;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebPageController {

    private final TemplateEngine templateEngine;

    @GetMapping("/aboutUs")
    @ResponseBody
    public String aboutUsPage() {
        String company = TenantContext.getTenant();
        String companyValue = company != null ? company : "";

        return render("aboutUs.jte", Map.of(
                "company", companyValue
        ));
    }

    private String render(String template, Map<String, Object> params) {
        var output = new StringOutput();
        templateEngine.render(template, params, output);
        return output.toString();
    }
}
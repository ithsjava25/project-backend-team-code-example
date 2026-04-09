package demo.codeexample.s3FileStorage.web;

import demo.codeexample.s3FileStorage.application.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.List;
import java.util.Map;

@Controller
public class FileController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileController.class);

    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/files")
    public String filesPage(Model model/*, @AuthenticationPrincipal OAuth2User user */, CsrfToken csrfToken) {
//        if (user == null) {
//            return "redirect:/login";
//        }
       // model.addAttribute("displayName", user.getAttribute("name"));
        model.addAttribute("isAuthenticated", true);
        model.addAttribute("csrfToken", csrfToken.getToken());
        return "files";
    }

    @GetMapping("/api/files")
    @ResponseBody
    public List<String> listFiles() {
        return s3Service.listFiles();
    }
    @DeleteMapping("/api/files")
    @ResponseBody
    public void deleteFile(@RequestParam String fileName) {
        s3Service.deleteFile("my-bucket", fileName);
    }

    @GetMapping("/api/files/upload-url")
    @ResponseBody
    public Map<String, String> getUploadUrl(@RequestParam String fileName, @RequestParam String contentType) {
        String url = s3Service.generatePresignedUploadUrl(fileName, contentType);
        return Map.of("url", url);
    }

    @GetMapping("/api/files/download-url")
    @ResponseBody
    public Map<String, String> getDownloadUrl(@RequestParam String fileName) {
        String url = s3Service.generatePresignedDownloadUrl(fileName);
        return Map.of("url", url);
    }

    @PostMapping("/api/files/callback")
    @ResponseBody
    public Map<String, String> uploadCallback(@RequestParam String fileName) {
        log.info("Callback received: File {} has been uploaded successfully", fileName);
        // Here you could perform further processing, like saving metadata to a database
        return Map.of("status", "success", "message", "Callback received for " + fileName);
    }
}

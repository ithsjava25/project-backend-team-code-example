package demo.codeexample.s3FileStorage.web;

import demo.codeexample.s3FileStorage.application.S3FileService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class S3FileController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(S3FileController.class);

    private final S3FileService s3Service;

    public S3FileController(S3FileService s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/files")
    public String filesPage(Model model) {

        model.addAttribute("isAuthenticated", true);
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

    @GetMapping("/api/files/project-poster/{projectId}")
    public String getProjectPoster(@PathVariable Long projectId) {
        try {
            // 1. Hämta listan säkert
            var keys = s3Service.findFileKeysByProjectId(projectId);

            // 2. Kolla om listan är null eller tom INNAN vi kör get(0) eller getFirst()
            if (keys == null || keys.isEmpty()) {
                log.warn("Ingen bild hittades i databasen för projekt {}", projectId);
                return "redirect:/images/Hexagonalt_filmproduktionslogotyp.png";
            }

            String fileName = keys.get(0);

            // 3. Generera URL
            String presignedUrl = s3Service.generatePresignedDownloadUrl(fileName);

            if (presignedUrl == null) {
                return "redirect:/images/Hexagonalt_filmproduktionslogotyp.png";
            }

            return "redirect:" + presignedUrl;

        } catch (Exception e) {
            // Detta gör att vi ser EXAKT vad som går fel i IntelliJ-konsolen
            log.error("Krasch vid hämtning av bild för projekt " + projectId, e);
            return "redirect:/images/Hexagonalt_filmproduktionslogotyp.png"; // Vid fel, visa placeholder istället för att ge 500
        }
    }

    @GetMapping("/api/files/project-media/{projectId}")
    @ResponseBody
    public List<Map<String, String>> getProjectMedia(@PathVariable Long projectId) {
        var keys = s3Service.findFileKeysByProjectId(projectId);

        return keys.stream().map(key -> {
            // 1. Generera URL:en och rör den inte mer (ingen toLowerCase här!)
            String url = s3Service.generatePresignedDownloadUrl(key);

            // 2. Extrahera filändelsen från 'key' för att använda i din switch
            String extension = "";
            int lastDot = key.lastIndexOf('.');
            if (lastDot > 0) {
                extension = key.substring(lastDot + 1).toLowerCase();
            }

            // 3. Din switch-sats för att bestämma typ
            String type = switch (extension) {
                case "mp4", "webm" -> "video";
                case "pdf"         -> "pdf";
                case "txt"         -> "text";
                case "jpg", "jpeg", "png", "gif" -> "image"; // Du kan vara specifik eller köra default
                default            -> "image";
            };

            return Map.of(
                    "url", url,
                    "type", type,
                    "name", key
            );
        }).toList();
    }

    @PostMapping("/api/files/callback")
    @ResponseBody
    @Transactional
    public Map<String, String> uploadCallback(
            @RequestParam Long projectId,
            @RequestParam String fileName,
            @RequestParam String contentType) {

        log.info("Callback received: Project {} saves file {} and has been uploaded successfully", projectId, fileName);
        // Here you could perform further processing, like saving metadata to a database
        s3Service.saveFileMetadata(projectId, fileName, contentType);

        return Map.of("status", "success", "message", "Callback received for " + fileName);
    }
}

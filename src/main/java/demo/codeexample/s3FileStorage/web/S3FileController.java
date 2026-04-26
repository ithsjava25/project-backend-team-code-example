package demo.codeexample.s3FileStorage.web;

import demo.codeexample.s3FileStorage.application.S3FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class S3FileController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(S3FileController.class);
    private final S3FileService s3Service;

    public S3FileController(S3FileService s3Service) {
        this.s3Service = s3Service;
    }

//    @GetMapping("/files")
//    public String filesPage(Model model) {
//
//        model.addAttribute("isAuthenticated", true);
//        return "files";
//    }

    @GetMapping
    public List<String> listFiles() {
        return s3Service.listFiles();
    }

    @DeleteMapping
    public void deleteFile(@RequestParam String fileName) {
        s3Service.deleteFile("my-bucket", fileName);
    }

    @GetMapping("/upload-url")
    public Map<String, String> getUploadUrl(@RequestParam String company, @RequestParam String projectTitle,
                                            @RequestParam Long projectId, @RequestParam String fileName,
                                            @RequestParam String contentType) {
        String url = s3Service.generatePresignedUploadUrl(company, projectTitle, projectId, fileName, contentType);
        return Map.of("url", url);
    }


    @GetMapping("/download-url")
    public Map<String, String> getDownloadUrl(@RequestParam String fileName) {
        String url = s3Service.generatePresignedDownloadUrl(fileName);
        return Map.of("url", url);
    }

    @GetMapping("/project-poster/{projectId}")
    public ResponseEntity<Void> getProjectPoster(@PathVariable Long projectId) {
        String url = s3Service.getProjectPosterKey(projectId)
                .map(s3Service::generatePresignedDownloadUrl)
                .orElse("/images/Hexagonalt_filmproduktionslogotyp.png");

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/project-media/{projectId}")
    public List<Map<String, String>> getProjectMedia(@PathVariable Long projectId) {
        var keys = s3Service.findFileKeysByProjectId(projectId);

        return keys.stream().map(key -> {
            String url = s3Service.generatePresignedDownloadUrl(key);

            String extension = "";
            int lastDot = key.lastIndexOf('.');
            if (lastDot > 0) {
                extension = key.substring(lastDot + 1).toLowerCase();
            }

            String type = switch (extension) {
                case "mp4", "webm"                -> "video";
                case "pdf"                        -> "pdf";
                case "txt"                        -> "text";
                case "jpg", "jpeg", "png", "gif"  -> "image";
                default                           -> "image";
            };

            return Map.of(
                    "url", url,
                    "type", type,
                    "name", key
            );
        }).toList();
    }

    @PostMapping("/callback")
    @Transactional
    public Map<String, String> uploadCallback(@RequestParam String company, @RequestParam String projectTitle,
                                              @RequestParam Long projectId, @RequestParam String fileName,
                                              @RequestParam String contentType) {

        log.info("Callback received: Project {} (Company: {}) saves file {}", projectId, company, fileName);
        s3Service.saveFileMetadata(company, projectTitle, projectId, fileName, contentType);

        return Map.of("status", "success");
    }
}

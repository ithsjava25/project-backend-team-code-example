package demo.codeexample.s3FileStorage.web;

import demo.codeexample.s3FileStorage.application.S3FileService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public S3FileController(S3FileService s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping
    public List<String> listFiles() {
        return s3Service.listFiles();
    }

    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<Void> deleteFile(@RequestParam String company, @RequestParam String fileKey) {
        log.info("Deleting file: {} for company: {}", fileKey, company);
        s3Service.deleteFile(bucketName, fileKey);

        return ResponseEntity.noContent().build();
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

    @GetMapping("/{projectId}/project/cover")
    public ResponseEntity<Void> getProjectCover(@PathVariable Long projectId) {
        String url = s3Service.getProjectKey(projectId, "cover")
                .map(s3Service::generatePresignedDownloadUrl)
                .orElse("/images/Hexagonalt_filmproduktionslogotyp.png");

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/{projectId}/project/background")
    public ResponseEntity<Void> getProjectPageBackground(@PathVariable Long projectId) {
        String url = s3Service.getProjectKey(projectId, "background")
                .map(s3Service::generatePresignedDownloadUrl)
                .orElse("/images/Hexagonalt_filmproduktionslogotyp.png");

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/{projectId}/project/trailer")
    public ResponseEntity<Void> getProjectTrailer(@PathVariable Long projectId) {
        return s3Service.getProjectKey(projectId, "trailer")
                .map(key -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(s3Service.generatePresignedDownloadUrl(key)))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());
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

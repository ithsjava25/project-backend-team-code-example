package demo.codeexample.s3FileStorage.application;

import demo.codeexample.s3FileStorage.S3FileLookup;
import demo.codeexample.s3FileStorage.domain.S3File;
import demo.codeexample.s3FileStorage.domain.S3FileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3FileService implements S3FileLookup {

    private static final String BUCKET_NAME = "my-bucket";
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3FileRepository s3FileRepository;

    @Value("${app.cors.allowed-origin:http://localhost:8080}")
    private String allowedOrigin;

    public S3FileService(S3Client s3Client, S3Presigner s3Presigner, S3FileRepository s3FileRepository) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.s3FileRepository = s3FileRepository;
    }

    @PostConstruct
    public void init() {
        try {
            s3Client.putBucketCors(bucketCorsRequest -> bucketCorsRequest
                    .bucket(BUCKET_NAME)
                    .corsConfiguration(conf -> conf
                            .corsRules(CORSRule.builder()
                                    .allowedOrigins(allowedOrigin)
                                    .allowedMethods("GET", "PUT", "POST", "DELETE", "HEAD")
                                    .allowedHeaders("*")
                                    .build())
                            .build())
                    .build());
        } catch (Exception e) {
            System.err.println("Could not set CORS: " + e.getMessage());
        }
    }

    public List<String> listFiles() {
        return s3Client.listObjectsV2Paginator(req -> req.bucket(BUCKET_NAME))
                .contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }

    public void deleteFile(String bucket, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key).build();

        s3Client.deleteObject(deleteObjectRequest);
    }

//    private String buildPath(String company, String projectTitle, Long projectId, String fileName) {
//        // Replace spaces with underscores and remove special characters from title
//        String safeTitle = projectTitle.replaceAll("[^a-zA-Z0-9]", "_");
//        return String.format("%s/%s%d/%s", company, safeTitle, projectId, fileName);
//    }
//
//    public String generatePresignedUploadUrl2(String company, String projectTitle, Long projectId, String fileName, String contentType) {
//        String fullPath = buildPath(company, projectTitle, projectId, fileName);
//
//        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .putObjectRequest(objectRequest -> objectRequest
//                        .bucket(BUCKET_NAME)
//                        .key(fullPath)
//                        .contentType(contentType)
//                        .build())
//                .build();
//
//        return s3Presigner.presignPutObject(presignRequest).url().toString();
//    }

    public String generatePresignedUploadUrl(String fileName, String contentType) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest -> objectRequest
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .build())
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    public String generatePresignedDownloadUrl(String fileName) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(objectRequest -> objectRequest.bucket(BUCKET_NAME)
                        .key(fileName)
                        .build())
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

//    public void saveFileMetadata2(String company, String projectTitle, Long projectId, String fileName, String contentType) {
//        String fullPath = buildPath(company, projectTitle, projectId, fileName);
//
//        S3File s3File = new S3File();
//        s3File.setProjectId(projectId);
//        s3File.setFileKey(fullPath); // We save the full path so download-url works automatically
//        s3File.setContentType(contentType);
//        s3FileRepository.saveAndFlush(s3File);
//    }

    public void saveFileMetadata(Long projectId, String fileKey, String contentType) {

        try {
            S3File s3File = new S3File();
            s3File.setProjectId(projectId);
            s3File.setFileKey(fileKey);
            s3File.setContentType(contentType);

            s3FileRepository.saveAndFlush(s3File); // saveAndFlush tvingar DB att skriva direkt
        } catch (Exception e) {
            System.err.println("DEBUG: Fel vid sparning: " + e.getMessage());
        }
    }

    @Override
    public List<String> findFileKeysByProjectId(Long projectId) {
        return s3FileRepository.findAllByProjectId(projectId)
                .stream()
                .map(S3File::getFileKey)
                .collect(Collectors.toList());
    }
}

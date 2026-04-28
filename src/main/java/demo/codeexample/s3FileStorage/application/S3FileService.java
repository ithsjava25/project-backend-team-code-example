package demo.codeexample.s3FileStorage.application;

import demo.codeexample.s3FileStorage.S3FileLookup;
import demo.codeexample.s3FileStorage.domain.S3File;
import demo.codeexample.s3FileStorage.domain.S3FileRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class S3FileService implements S3FileLookup {

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

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
                    .bucket(bucketName)
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
        return s3Client.listObjectsV2Paginator(req -> req.bucket(bucketName))
                .contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }

    @Transactional
    public void deleteFile(String bucket, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key).build();

        s3Client.deleteObject(deleteObjectRequest);
        s3FileRepository.deleteByFileKey(key);
    }

    private String buildPath(String company, String projectTitle, Long projectId, String fileName) {
        String[] parts = {company, projectTitle, fileName};

        for (int i = 0; i < parts.length; i++) {
            String washed = parts[i].toLowerCase();
            parts[i] = washed.replaceAll("'", "")
                             .replaceAll("[^a-z0-9.]", "_");
        }

        return String.format("%s/%s_%d/%s", parts[0], parts[1], projectId, parts[2]);
    }


    public String generatePresignedUploadUrl(String company, String projectTitle, Long projectId,
                                             String fileName, String contentType) {

        String filePath = buildPath(company, projectTitle, projectId, fileName);

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest -> objectRequest
                        .bucket(bucketName)
                        .key(filePath)
                        .contentType(contentType)
                        .build())
                .build();

        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    public String generatePresignedDownloadUrl(String fileName) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(objectRequest -> objectRequest.bucket(bucketName)
                        .key(fileName)
                        .build())
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }


    public void saveFileMetadata(String company, String projectTitle, Long projectId,
                                 String fileName, String contentType) {

        try {
            String fullS3Key = buildPath(company, projectTitle, projectId, fileName);

            S3File s3File = new S3File();
            s3File.setProjectId(projectId);
            s3File.setFileKey(fullS3Key);
            s3File.setContentType(contentType);
            s3FileRepository.saveAndFlush(s3File);

        } catch (Exception e) {
            System.err.println("DEBUG: Something went wrong when trying to save: " + e.getMessage());
        }
    }

    @Override
    public List<String> findFileKeysByProjectId(Long projectId) {
        return s3FileRepository.findAllByProjectId(projectId)
                .stream()
                .map(S3File::getFileKey)
                .collect(Collectors.toList());
    }

    public Optional<String> getProjectKey(Long projectId, String searchTerm) {
        return s3FileRepository.findFirstByProjectIdAndFileKeyContainingIgnoreCase(projectId, searchTerm)
                .map(S3File::getFileKey);
    }

}

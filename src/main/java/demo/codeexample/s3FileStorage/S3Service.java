package demo.codeexample.s3FileStorage;

import jakarta.annotation.PostConstruct;
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

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private static final String BUCKET_NAME = "my-bucket";

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @PostConstruct
    public void init() {
        try {
            s3Client.putBucketCors(bucketCorsRequest -> bucketCorsRequest
                    .bucket(BUCKET_NAME)
                    .corsConfiguration(conf -> conf
                            .corsRules(CORSRule.builder()
                                    .allowedOrigins("http://localhost:8080")
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
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
        return listObjectsV2Response.contents().stream()
                .map(S3Object::key)
                .toList();
    }

    public void deleteFile(String bucket, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key).build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public String generatePresignedUploadUrl(String fileName, String contentType) {
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequst -> objectRequst
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .contentType(contentType)
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
}

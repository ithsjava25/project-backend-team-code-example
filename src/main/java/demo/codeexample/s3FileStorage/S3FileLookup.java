package demo.codeexample.s3FileStorage;

import java.util.List;

public interface S3FileLookup {

    List<String> findFileKeysByProjectId(Long projectId);
}

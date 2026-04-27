package demo.codeexample.project;

import demo.codeexample.task.TaskResponseDto;

import java.util.List;

public interface TaskLookup {
    boolean isFinalTaskComplete(Long projectId);

    List<TaskResponseDto> getTasksByProjectId(Long projectId);
}

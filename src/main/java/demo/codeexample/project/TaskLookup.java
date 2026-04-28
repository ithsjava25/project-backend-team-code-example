package demo.codeexample.project;

import demo.codeexample.task.TaskResponseDto;
import demo.codeexample.task.TaskSummaryDto;

import java.util.List;

public interface TaskLookup {
    boolean isFinalTaskComplete(Long projectId);

    List<TaskSummaryDto> getTasksByProjectId(Long projectId);
}

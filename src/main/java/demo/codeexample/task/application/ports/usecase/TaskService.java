package demo.codeexample.task.application.ports.usecase;

import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.domain.Project;
import demo.codeexample.shared.LoggerAction;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskType;
import demo.codeexample.user.UserLookup;
import org.springframework.modulith.events.ApplicationModuleListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final LoggerLookup logger;
    private final UserLookup userLookup;



    public TaskService(TaskRepositoryPort taskRepository, UserLookup userLookup, LoggerLookup logger) {
        this.taskRepository = taskRepository;
        this.userLookup = userLookup;
        this.logger = logger;
    }

    @ApplicationModuleListener
    public void on (ProjectCreatedEvent event) {
        Long recruiterId = findByRole(event.employeeId(), "RECRUITER");
        Long directorId = findByRole(event.employeeId(), "DIRECTOR");
        Long editorId = findByRole(event.employeeId(), "EDITOR");

        Task RecruitingTask = createTask(TaskType.RECRUITING, "Recruit for " + event.title(), TaskStatus.ASSIGNED, )
    }

    private Long findByRole(Set<Long> ids, String role) {
        return ids.stream()
                .filter(id -> userLookup.validateUserRole(id, role))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Ingen anställd med rollen " + role + " hittades i projektet!"));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(Long id) {
    return taskRepository.findById(id);
    }

    @Override
    public Task createTask(TaskType taskType, String description, TaskStatus status,
                           LocalDateTime deadline, Long projectId, Long userId){

        Task task = new Task(null, taskType, description, status, deadline, projectId, userId);

        Task savedTask = taskRepository.save(task);
        logger.log(LoggerAction.TASK_CREATED, userId, "TASK", savedTask.getId());
        return savedTask;

    }
}

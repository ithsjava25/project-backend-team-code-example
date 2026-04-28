package demo.codeexample.task.application.ports.usecase;

import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.security.UserAuthHelper;
import demo.codeexample.shared.LoggerAction;
import demo.codeexample.shared.Role;
import demo.codeexample.task.application.ports.out.TaskUserPort;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskType;
import demo.codeexample.user.UserAuthPort;
import demo.codeexample.user.application.UserService;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final LoggerLookup logger;
    private final TaskUserPort userPort;
    private final UserAuthHelper  userAuthHelper;


    public TaskService(TaskRepositoryPort taskRepository, TaskUserPort userPort, LoggerLookup logger, UserAuthHelper userAuthHelper) {
            this.taskRepository = taskRepository;
            this.userPort = userPort;
            this.logger = logger;
        this.userAuthHelper = userAuthHelper;
    }

        public void handleProjectCreated (ProjectCreatedEvent event){
            Long recruiterId = findByRole(event.employeesId(), Role.RECRUITER);
            Long directorId = findByRole(event.employeesId(), Role.DIRECTOR);
            Long editorId = findByRole(event.employeesId(), Role.EDITOR);

            createTask(TaskType.RECRUITING, "Recruit for " + event.title(),
                    TaskStatus.ASSIGNED, event.recruitingDeadline(), event.projectId(), recruiterId);

            createTask(TaskType.RECORDING, "Record movie for " + event.title(),
                    TaskStatus.PENDING, event.recordingDeadline(), event.projectId(), directorId);

            createTask(TaskType.EDITING, "Editing scenes for " + event.title(),
                    TaskStatus.PENDING, event.editingDeadline(), event.projectId(), editorId);
        }

        public void acceptTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task.getStatus() != TaskStatus.ASSIGNED) {
            throw new IllegalStateException("Previous task is not completed.");
            }
        Long currentUserId = userAuthHelper.getCurrentUserId();
            if (!task.getUserId().equals(currentUserId)) {
                throw new AccessDeniedException("You are not assigned to this task!");
            }
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(task);
        }

    public void completeTask(Long taskId) {
        Task currentTask = taskRepository.findById(taskId).orElseThrow();

        Long currentUserId = userAuthHelper.getCurrentUserId();
        if (!currentTask.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not authorized to complete this task.");
        }
        if (currentTask.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only tasks 'In Progress' can be marked as completed.");
        }
        currentTask.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(currentTask);
        logger.log(
                LoggerAction.TASK_COMPLETED,
                currentUserId,
                "TASK",
                currentTask.getId(),
                currentTask.getProjectId(),
                currentTask.getTaskType() + " was completed at: " + LocalDateTime.now()
        );

        TaskType nextType = getNextTaskType(currentTask.getTaskType());
        if (nextType != null) {
            taskRepository.findByProjectIdAndTaskType(currentTask.getProjectId(), nextType)
                    .ifPresent(nextTask -> {
                        if (nextTask.getStatus() == TaskStatus.PENDING) {
                            nextTask.setStatus(TaskStatus.ASSIGNED);
                            taskRepository.save(nextTask);
                        }
                    });
        }
    }
    private TaskType getNextTaskType(TaskType current) {
        return switch (current) {
            case RECRUITING -> TaskType.RECORDING;
            case RECORDING -> TaskType.EDITING;
            case EDITING -> null;
        };
    }

        private Long findByRole (Set < Long > ids, Role role){
            return ids.stream()
                    .filter(id -> userPort.hasRole(id, role))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No " + role + " found."));
        }

        @Override
        public List<Task> findAll () {
            return taskRepository.findAll();
        }

        @Override
        public Optional<Task> findById (Long id){
            return taskRepository.findById(id);
        }

        @Override
        public Task createTask (TaskType taskType, String description, TaskStatus status,
                LocalDateTime deadline, Long projectId, Long userId){

            String employeeName = userPort.getEmployeeFullName(userId);

            Task task = new Task(null, taskType, description, status, deadline, projectId, userId);
            Task savedTask = taskRepository.save(task);

            logger.log(
                    LoggerAction.TASK_CREATED,
                    userId,
                    "TASK",
                    savedTask.getId(),
                    projectId,
                    taskType + "-task was created for: " + employeeName + ". In project: " + savedTask.getProjectId()
            );
            return savedTask;
        }
}

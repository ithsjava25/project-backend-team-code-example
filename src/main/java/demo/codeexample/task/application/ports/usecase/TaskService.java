package demo.codeexample.task.application.ports.usecase;

import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.shared.LoggerAction;
import demo.codeexample.shared.Role;
import demo.codeexample.task.application.ports.out.TaskUserPort;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final LoggerLookup logger;
    private final TaskUserPort userPort;

    public TaskService(TaskRepositoryPort taskRepository, TaskUserPort userPort, LoggerLookup logger) {
            this.taskRepository = taskRepository;
            this.userPort = userPort;
            this.logger = logger;
        }

        public void handleProjectCreated (ProjectCreatedEvent event){
            Long recruiterId = findByRole(event.employeesId(), Role.RECRUITER);
            Long directorId = findByRole(event.employeesId(), Role.DIRECTOR);
            Long editorId = findByRole(event.employeesId(), Role.EDITOR);

            createTask(TaskType.RECRUITING, "Recruit for " + event.title(),
                    TaskStatus.ASSIGNED, event.recruitingDeadline(), event.projectId(), recruiterId);

            createTask(TaskType.RECORDING, "Record movie for " + event.title(),
                    TaskStatus.ASSIGNED, event.recordingDeadline(), event.projectId(), directorId);

            createTask(TaskType.EDITING, "Editing scenes for " + event.title(),
                    TaskStatus.ASSIGNED, event.editingDeadline(), event.projectId(), editorId);
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

package demo.codeexample.project.application.usecase;

import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.TaskLookup;
import demo.codeexample.project.application.out.ProjectEventPort;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.SecurityPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;
import demo.codeexample.shared.Category;
import demo.codeexample.shared.LoggerAction;
import demo.codeexample.user.UserLookup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepositoryPort repository;
    @Mock
    private UserPort userPort;
    @Mock
    private ProjectEventPort projectEventPort;
    @Mock
    private SecurityPort securityPort;
    @Mock
    private ModelMapper mapper;
    @Mock
    private LoggerLookup logger;
    @Mock
    private TaskLookup taskLookup;
    @Mock
    private UserLookup userLookup;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_Success() {
        CreateProjectDto dto = new CreateProjectDto(
                "Epic Movie", "Desc", LocalDate.now(), Set.of(1L),
                Category.FILM, Genre.ACTION, "MyCompany",
                null, null, null
        );

        when(securityPort.hasRole("PRODUCER")).thenReturn(true);
        Project savedProject = Project.builder()
                .id(99L)
                .title("Epic Movie")
                .employeesId(Set.of(1L))
                .build();
        when(repository.save(any(CreateProjectDto.class))).thenReturn(savedProject);
        when(securityPort.getCurrentUserId()).thenReturn(10L);
        when(securityPort.getCurrentUserName()).thenReturn("Producer Fred");

        Project result = projectService.createProject(dto);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals("Epic Movie", result.getTitle());

        verify(logger, times(1)).log(
                eq(LoggerAction.PROJECT_CREATED),
                eq(10L),
                anyString(),
                anyLong(),
                anyLong(),
                contains("Epic Movie")
        );

        verify(projectEventPort, times(1)).publish(any(ProjectCreatedEvent.class));
    }
}
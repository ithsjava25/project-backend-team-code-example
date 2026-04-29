package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;
import demo.codeexample.shared.Category;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {ProjectController.class})
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ProjectUseCase projectUseCase;
    @MockitoBean
    private UserLookup userLookup;
    @MockitoBean
    private CurrentUserLookup currentUserLookup;

    @Test
    @WithMockUser(roles = "PRODUCER")
    void GetDashboardCompleted_ShouldShowCompletedProjectsForUser() throws Exception {
        // Arrange
        String companyName = "TestCompany";
        Long mockUserId = 123L;

        UserDto mockUser = new UserDto();
        mockUser.setId(mockUserId);
        when(currentUserLookup.getCurrentUser()).thenReturn(Optional.of(mockUser));

        var projects = List.of(new ProjectDto(), new ProjectDto());
        when(projectUseCase.findCompletedProjectsForUser(mockUserId, companyName)).thenReturn(projects);


        // Act & Assert
        mockMvc.perform(get("/dashboard/completed")
                        .flashAttr("company", companyName))
                .andExpect(status().isOk())
                .andExpect(view().name("producer/dashboard"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(model().attribute("projects", hasSize(2)));
    }

    @Test
    void CreateProject_ShouldReturnSuccess() throws Exception {
        CreateProjectDto dto = new CreateProjectDto(
                "TestTitle",
                "A testproject",
                LocalDate.of(2026,8,8),
                Set.of(1L,2L,3L),
                Category.FILM,
                Genre.ACTION,
                "TestCompany",
                LocalDateTime.of(2026, 6, 1, 12, 0), // recruitingDeadline
                LocalDateTime.of(2026, 7, 1, 12, 0), // recordingDeadline
                LocalDateTime.of(2026, 8, 1, 12, 0)  // editingDeadline
        );

        Project savedProject = Project.builder()
                .id(99L)
                .title("TestTitle")
                .companyName("TestCompany")
                .build();
        when(projectUseCase.createProject(any(CreateProjectDto.class))).thenReturn(savedProject);


        mockMvc.perform(post("/projects")
                .flashAttr("projectDto", dto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99));

    }
    @Test
    void createProject_ShouldReturnBadRequest_WhenTitleIsBlank() throws Exception {
        // 1. ARRANGE
        // Create the record with an empty string for the title
        CreateProjectDto brokenDto = new CreateProjectDto(
                "",               // TITLE IS BLANK!
                "A description",
                LocalDate.now(),
                Set.of(1L),
                Category.FILM,
                Genre.ACTION,
                "TestCompany",
                null, null, null
        );

        // 2. ACT & ASSERT
        mockMvc.perform(post("/projects")
                        .flashAttr("projectDto", brokenDto))
                .andDo(print())
                .andExpect(status().isBadRequest()); // Looking for 400

        // 3. VERIFY
        // This is the most important part: ensure the service was never touched!
        Mockito.verify(projectUseCase, times(0)).createProject(any());
    }
}
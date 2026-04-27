package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;
import demo.codeexample.security.JwtAuthenticationFilter;
import demo.codeexample.security.JwtService;
import demo.codeexample.security.OAuth2LoginSuccessHandler;
import demo.codeexample.shared.Category;
import demo.codeexample.user.UserLookup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest // Don't put the class here yet
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {ProjectController.class})
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ProjectUseCase projectUseCase;
    @MockitoBean
    private UserLookup userLookup;

    // Security dependencies required to load SecurityConfig
//    @MockitoBean
//    JwtService jwtService;
//    @MockitoBean
//    JwtAuthenticationFilter jwtAuthenticationFilter;
//    @MockitoBean
//    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Test
    void GetDashboardCompleted_ShouldShowCompletedProjects() throws Exception {
        // Arrange
        String companyName = "TestCompany";
        var projects = List.of(new ProjectDto(), new ProjectDto());
        when(projectUseCase.findAllCompletedProjectsByCompany(companyName)).thenReturn(projects);

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
}
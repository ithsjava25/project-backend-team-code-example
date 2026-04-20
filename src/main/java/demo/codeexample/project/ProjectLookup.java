package demo.codeexample.project;


import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Project;

import java.util.List;

public interface ProjectLookup {

    List<ProjectDto> findAllProjects();

    List<Project> findProjectByCategory(Category category);

}

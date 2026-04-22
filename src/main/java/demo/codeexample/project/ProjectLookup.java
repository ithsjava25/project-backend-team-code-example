package demo.codeexample.project;


import demo.codeexample.shared.Category;

import java.util.List;

public interface ProjectLookup {

    List<ProjectDto> findAllProjects();

    List<ProjectDto> findProjectByCategory(Category category);

}

package demo.codeexample.project;


import demo.codeexample.shared.Category;

import java.util.List;

public interface ProjectLookup {

    List<ProjectDto> findProjectByCategory(Category category);

    List<ProjectDto> findAllCompletedProjectsByCompany(String companyName);

}

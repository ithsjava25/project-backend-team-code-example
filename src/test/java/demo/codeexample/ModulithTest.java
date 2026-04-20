package demo.codeexample;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithTest {

    /**
     * Generates documentation for the modular structure of the application.
     * The method retrieves and analyzes the application's module definitions provided
     * by the {@code ApplicationModules} API using the entry point {@code CodeExampleApplication}.
     * It then invokes the {@code Documenter} to create textual documentation and
     * generate a PlantUML diagram representation of the modules.
     * <p>
     * The produced documentation provides a visual and textual overview of
     * the application's module boundaries and their relationships, helping
     * to understand and communicate the system's architectural structure.
     */
    @Test
    void createModuleDocumentation(){
        ApplicationModules modules = ApplicationModules.of(CodeExampleApplication.class);

        new Documenter(modules)
                .writeDocumentation()
                .writeModulesAsPlantUml();
    }
    /**
     * Validates the modular structure of the Spring Boot application.
     * <p>
     * This method retrieves the application modules defined in the Spring application
     * and performs verification to ensure compliance with the defined architectural rules.
     * It utilizes the {@code ApplicationModules} concept to manage and enforce module boundaries.
     */
    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(CodeExampleApplication.class);
        modules.verify();
    }
}

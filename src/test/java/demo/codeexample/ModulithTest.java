package demo.codeexample;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithTest {
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

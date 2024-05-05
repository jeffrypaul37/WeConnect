package com.example.weconnect.weConnectApp.model;


import com.webapp.weconnect.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {

    @Test
    void roleNoArgsConstructorShouldInitializeCorrectly() {
        // Arrange & Act
        Role role = new Role();

        // Assert
        assertEquals(null, role.getName(), "Name should be null for a newly instantiated role without parameters.");
    }

    @Test
    void roleArgsConstructorShouldSetFieldName() {
        // Arrange
        String roleName = "ADMIN";

        // Act
        Role role = new Role(roleName);

        // Assert
        assertEquals(roleName, role.getName(), "Role name should match the value passed to the constructor.");
    }

    @Test
    void setIdShouldCorrectlyAssignValue() {
        // Arrange
        Role role = new Role();
        Long id = 1L;

        // Act
        role.setId(id);

        // Assert
        assertEquals(id, role.getId(), "Role ID should match the value set by setId.");
    }

    @Test
    void setNameShouldCorrectlyAssignValue() {
        // Arrange
        Role role = new Role();
        String name = "USER";

        // Act
        role.setName(name);

        // Assert
        assertEquals(name, role.getName(), "Role name should match the value set by setName.");
    }
}


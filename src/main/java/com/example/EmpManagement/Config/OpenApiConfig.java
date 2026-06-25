package com.example.EmpManagement.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Employee Management API",
                version = "1.0",
                description = "Enterprise-grade stateless backend system for managing corporate identities, departments, and HR accounts.",
                contact = @Contact(
                        name = "Mayank",
                        email = "mayank06570@gmail.com"
                )
        ),
        // This tells Swagger: "Every endpoint requires this security scheme globally"
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Enter your JWT token here to authenticate your requests.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // No code needed here! The annotations do all the heavy lifting.
}
package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {
    @NotBlank(message = "First Name is required")
    @Size(max = 40, message = "First Name must be at most 40 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "First Name must contain only letters and spaces")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(max = 40, message = "Last Name must be at most 40 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Last Name must contain only letters and spaces")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid Email is required")
    @Size(max = 254, message = "Email must be at most 254 characters")
    private String email;

    @NotBlank(message = "Subject is required")
    @Size(max = 78, message = "Subject must be at most 78 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(max = 750, message = "Message must be at most 750 characters")
    private String message;
}
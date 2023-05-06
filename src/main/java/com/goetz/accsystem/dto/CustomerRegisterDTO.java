package com.goetz.accsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRegisterDTO(
    @Schema(example = "max.mustermann@gmail.com")
    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Password must not be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 320, message = "The maximal input must be <= 320 characters long")
    String email, 

    @Schema(example = "MaxMustermann1998!")
    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,}$", message = "The password must be at least eight characters long, contain at least one uppercase letter, at least one number, and at least one special character from the string !@#$&*.")
    String password, 

    @Schema(example = "Max")
    @NotNull(message = "Firstname must not be null")
    @NotBlank(message = "Firstname must not be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Numbers are not allowd")
    @Size(min = 3 , max = 50, message = "The input must be between 3 and 50 characters long.")
    String firstname, 

    @Schema(example = "Mustermann")
    @NotNull(message = "Lastname must not be null")
    @NotBlank(message = "Lastname must not be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Numbers are not allowd")
    @Size(min = 3 , max = 50, message = "The input must be between 3 and 50 characters long.")
    String lastname, 

    @Schema(example = "1998-06-16")
    @NotNull(message = "Birthday must not be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthday must be in the format yyyy-MM-dd")
    String birthday) {}
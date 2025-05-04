package ar.uba.fi.ingsoft1.todo_template.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.function.Function;

public record UserCreateDTO(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String email,
        @NotNull LocalDate birthDate,
        String firstName,
        String lastName,
        String avatarUrl
) implements UserCredentials {
    public User asUser(Function<String, String> encryptPassword) {
        return new User(
                username,
                encryptPassword.apply(password),
                "USER",
                email,
                firstName,
                lastName,
                avatarUrl,
                birthDate
        );
    }
}

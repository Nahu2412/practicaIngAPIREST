package ar.uba.fi.ingsoft1.todo_template.user;

import jakarta.validation.constraints.NotBlank;

public record UserProfileDTO(
        @NotBlank String username,
        String firstName,
        String lastName,
        String avatarUrl
) {
    public UserProfileDTO(User user) {
        this(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getAvatarUrl()
        );
    }
}

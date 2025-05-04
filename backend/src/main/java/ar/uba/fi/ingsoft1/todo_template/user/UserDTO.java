package ar.uba.fi.ingsoft1.todo_template.user;

public record UserDTO(
        String username,
        long id
) {
    UserDTO(User user){
        this(user.getUsername(), user.getId());
    }
}

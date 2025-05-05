package ar.uba.fi.ingsoft1.todo_template.user;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "1 - Users")
class UserRestController {
    private final UserService userService;

    @Autowired
    UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Create a new user")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<TokenDTO> signUp(
            @Valid @NonNull @RequestBody UserCreateDTO data
    ) throws MethodArgumentNotValidException {
        return userService.createUser(data)
                .map(tk -> ResponseEntity.status(HttpStatus.CREATED).body(tk))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping(value = "/{id}/profile", produces = "application/json")
    @Operation(summary = "Get user profile")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    public UserProfileDTO getUserProfile(
            @Valid @PathVariable @Positive long id
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return userService.getUserProfile(id);
    }

    @PutMapping("/{id}/profile")
    @Operation(summary = "Edit user profile")
    public ResponseEntity<UserProfileDTO> editUserProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileDTO userProfileDTO) {
        try {
            UserProfileDTO updatedUserProfile = userService.editUserProfile(id, userProfileDTO);
            return ResponseEntity.ok(updatedUserProfile);
        } catch (ItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @GetMapping(value = "/{id}/followers", produces = "application/json")
    @Operation(summary = "Get list of followers")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<UserDTO>> getFollowers(
            @Valid @PathVariable long id
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        var followers = userService.getFollowers(id);
        return ResponseEntity.ok(followers);
    }

    @GetMapping(value = "/{id}/following", produces = "application/json")
    @Operation(summary = "Get list of followings")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<UserDTO>> getFollowings(
            @Valid @PathVariable long id
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        var following = userService.getFollowing(id);
        return ResponseEntity.ok(following);
    }

    @PostMapping(value = "/{id}/follow/{target}", produces = "application/json")
    @Operation(summary = "Follow to other user")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserDTO> followAUser(
            @Valid @PathVariable long id,
            @Valid @PathVariable long target
    ) throws ItemNotFoundException{
        var user = userService.followTo(id,target);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(
            @RequestBody UserCreateDTO data
    ){
        userService.createAdmin(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/admin/{username}")
    @Operation(summary = "Eliminar a un admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAdmin(
            @PathVariable String username
    ){
        userService.deleteAdmin(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Eliminar a un usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(
            @PathVariable String username
    ) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }



}

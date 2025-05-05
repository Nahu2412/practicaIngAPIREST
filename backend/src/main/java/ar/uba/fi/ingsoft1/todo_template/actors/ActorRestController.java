package ar.uba.fi.ingsoft1.todo_template.actors;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actors")
@Tag(name = "Actors")
public class ActorRestController {
    private final ActorService actorService;

    @Autowired
    ActorRestController(ActorService actorService) {
        this.actorService = actorService;
    }


    @PostMapping(produces = "application/json")
    @Operation(summary = "Create a new actor")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    ActorDTO createActor(
            @Valid @RequestBody ActorCreateDTO actorCreate
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return actorService.createActor(actorCreate);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete an Actor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    void deleteActor(
            @Valid @PathVariable @Positive long id
    ) throws MethodArgumentNotValidException {
        actorService.deleteActor(id);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update an Actor")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404", description = "Actor not found", content = @Content)
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ActorDTO> putActor(
            @Valid @PathVariable @Positive long id,
            @Valid @RequestBody ActorCreateDTO actorCreate
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return ResponseEntity.of(actorService.updateActor(id, actorCreate));
    }
}

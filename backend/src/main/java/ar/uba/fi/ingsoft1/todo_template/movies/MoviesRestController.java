package ar.uba.fi.ingsoft1.todo_template.movies;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.todo_template.movies.MoviesDTO;
import ar.uba.fi.ingsoft1.todo_template.tasks.TaskCreateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ar.uba.fi.ingsoft1.todo_template.movies.MoviesService;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies")
public class MoviesRestController {

    private final MoviesService moviesService;

    @Autowired
    MoviesRestController(MoviesService moviesService){this.moviesService = moviesService;}

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get a list of movies")
    @ResponseStatus(HttpStatus.OK)
    Page<MoviesDTO> getMovies(
            @Valid @ParameterObject Pageable pageable
    ) throws MethodArgumentNotValidException {
        return moviesService.getMovies(pageable);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Movie")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMovie(
            @Valid @PathVariable @Positive long id
    ) throws MethodArgumentNotValidException{
        moviesService.deleteMovie(id);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a Movie")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    ResponseEntity<MoviesDTO> putMovie(
            @Valid @PathVariable @Positive long id,
            @Valid @RequestBody MovieCreateDTO movieCreate
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return ResponseEntity.of(movieService.updateTask(id, movieCreate));
    }
}

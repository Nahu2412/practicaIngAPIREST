package ar.uba.fi.ingsoft1.todo_template.movies;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.todo_template.projects.ProjectDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/movies")
@Tag(name = "Movies")
class MovieRestController {
    private final MovieService movieService;

    @Autowired
    MovieRestController(MovieService movieService){ this.movieService = movieService;}

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get a list of movies")
    @ResponseStatus(HttpStatus.OK)
    Page<MovieDTO> getMovies(
            @Valid @ParameterObject Pageable pageable
    ) throws MethodArgumentNotValidException {
        return movieService.getMovies(pageable);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Get a movie by its id")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    MovieDTO getMovie(
            @Valid @PathVariable @Positive long id
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return movieService.getMovie(id);
    }

    @GetMapping(value = "/search", produces = "application/json")
    @Operation(summary = "Get a movie by title")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    ResponseEntity<?> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        var resultado = movieService.searchMovies(title,category);
        if(resultado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Create a new movie")
    @ResponseStatus(HttpStatus.CREATED)
    MovieDTO createMovie(
            @Valid @RequestBody MovieCreateTDO movieCreate
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return movieService.createMovie(movieCreate);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Movie")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMovie(
            @Valid @PathVariable @Positive long id
    ) throws MethodArgumentNotValidException{
        movieService.deleteMovie(id);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a Movie")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    ResponseEntity<MovieDTO> putMovie(
            @Valid @PathVariable @Positive long id,
            @Valid @RequestBody MovieCreateTDO movieCreate
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return ResponseEntity.of(movieService.updateMovie(id, movieCreate));
    }

    @PostMapping(value = "/{id}/rate")
    @Operation(summary = "Rate a Movie")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    ResponseEntity<MovieRatingDTO> rateMovie(
            @Valid @PathVariable @Positive long id,
            @Valid @RequestBody MovieRatingDTO movieRate
    ) throws MethodArgumentNotValidException, ItemNotFoundException {
        return ResponseEntity.of(movieService.rateMovie(id, movieRate));
    }

}

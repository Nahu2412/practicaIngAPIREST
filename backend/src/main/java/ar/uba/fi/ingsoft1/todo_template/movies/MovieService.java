package ar.uba.fi.ingsoft1.todo_template.movies;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.todo_template.projects.ProjectDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
@Transactional
class MovieService {
    private final MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    Page<MovieDTO> getMovies(Pageable pageable){
        return movieRepository.findAll(pageable).map(MovieDTO::new);
    }

    MovieDTO createMovie(MovieCreateTDO movieCreate) throws ItemNotFoundException {
        return new MovieDTO(movieRepository.save(movieCreate.asMovie()));
    }

    MovieDTO getMovie(long id) throws ItemNotFoundException {
        var movie = movieRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("movie",id));
        return new MovieDTO(movie);
    }

    /*
    List<MovieDTO> getMovieByTitle(String title){
        return movieRepository.findByTitle(title)
                .stream()
                .map(MovieDTO::new)
                .toList();
    }
    */

    List<MovieDTO> searchMovies(String title, String category){
        List<Movie> movies;

        if(title != null && category != null){
            movies = movieRepository.findByTitleAndCategory(title,category);
        }else if(title != null){
            movies = movieRepository.findByTitle(title);
        }else if(category != null){
            movies = movieRepository.findByCategory(category);
        }else{
            movies = movieRepository.findAll();
        }
        return movies.stream()
                .map(MovieDTO::new)
                .toList();
    }

    void deleteMovie(long id){movieRepository.deleteById(id);}

    Optional<MovieDTO> updateMovie(long id, MovieCreateTDO movieCreate) throws ItemNotFoundException {
        if (!movieRepository.existsById(id)) {
            return Optional.empty();
        }
        var saved = movieRepository.save(movieCreate.asMovie(id));
        return Optional.of(new MovieDTO(saved));
    }

    Optional<MovieRatingDTO> rateMovie(@Valid @Positive long id, @Valid MovieCreateTDO movieCreate) {
    }

    /*
    List<MovieDTO> getMovieByCategory(String category){
        return movieRepository.findByCategory(category)
                .stream()
                .map(MovieDTO::new)
                .toList();
    }*/
}

package ar.uba.fi.ingsoft1.todo_template.movies;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MoviesService {
    private final MoviesRepository moviesRepository;

    @Autowired
    MoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    Page<MoviesDTO> getMovies(Pageable pageable) {
        return moviesRepository.findAll(pageable).map(MoviesDTO::new);
    }

    Optional<MoviesDTO> getProject(String title) {
        return moviesRepository.findByTitle(title).map(MoviesDTO::new);
    }

    void deleteMovie(long id){moviesRepository.deleteById(id);}

    Optional<MoviesDTO> updateTask(long id, MoviesCreateDTO MoviesCreate) throws ItemNotFoundException {
        if (!moviesRepository.existsById(id)) {
            return Optional.empty();
        }
        var saved = moviesRepository.save(moviesCreate.asMovie(id));
        return Optional.of(new MoviesDTO(saved));
    }
}

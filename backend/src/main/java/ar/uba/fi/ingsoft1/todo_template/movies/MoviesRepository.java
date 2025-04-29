package ar.uba.fi.ingsoft1.todo_template.movies;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoviesRepository extends JpaRepository<Movies,Long>{
    Optional<Movies> findByTitle(String title);
}

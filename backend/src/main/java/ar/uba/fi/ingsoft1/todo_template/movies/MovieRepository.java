package ar.uba.fi.ingsoft1.todo_template.movies;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByTitle(String title);
    List<Movie> findByTitleAndCategory(String title, String category);
    List<Movie> findByCategory(String category);
}

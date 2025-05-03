package ar.uba.fi.ingsoft1.todo_template.movies;

public record MovieDTO(
        long id,
        String title,
        String category
) {
    MovieDTO(Movie movie){ this(movie.getId(), movie.getTitle(), movie.getCategory());}
}

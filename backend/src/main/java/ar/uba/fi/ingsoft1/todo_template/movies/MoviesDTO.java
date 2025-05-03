package ar.uba.fi.ingsoft1.todo_template.movies;


public record MoviesDTO(long id, String title, String category, long year) {
    public MoviesDTO(Movies movies) {
        this(movies.getId(), movies.getTitle(), movies.getCategory(), movies.getMovieYear());
    }
}

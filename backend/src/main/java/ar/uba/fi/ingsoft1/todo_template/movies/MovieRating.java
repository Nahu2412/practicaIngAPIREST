package ar.uba.fi.ingsoft1.todo_template.movies;

import ar.uba.fi.ingsoft1.todo_template.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class MovieRating {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long rating;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    MovieRating(){}

    MovieRating(Long id, Movie movie, User user, Long rating){
        this.id = id;
        this.movie = movie;
        this.user = user;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public String getMovieTitle() {
        return this.movie.getTitle();
    }

    public String getUsername() {
        return this.user.getUsername();
    }
    public Long getRating() {
        return this.rating;
    }


}
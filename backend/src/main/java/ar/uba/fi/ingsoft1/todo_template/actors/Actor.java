package ar.uba.fi.ingsoft1.todo_template.actors;

import ar.uba.fi.ingsoft1.todo_template.movies.Movie;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Actor {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "actors")
    private List<Movie> movies;

    Actor() {}

    Actor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

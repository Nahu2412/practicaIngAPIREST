package ar.uba.fi.ingsoft1.todo_template.movies;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity(name = "movies")
public class Movies {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private long movie_year;

    @Column(nullable = false)
    private String category;

    public Movies() {}

    public Movies(String name, long year,String category) {
        this.title = name;
        this.movie_year = year;
        this.category = category;
    }

    public Long getId(){return this.id;}
    public String getTitle(){
        return this.title;
    }


    public long getMovieYear(){
        return this.movie_year;
    }

    public String getCategory(){
        return this.category;
    }
}

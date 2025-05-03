package ar.uba.fi.ingsoft1.todo_template.movies;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MovieCreateTDO(
        @NotBlank @Size(min=1,max=70) String title,
        @Size(min=1,max=40) String category
) {
    public Movie asMovie() { return this.asMovie(null); }

    public Movie asMovie(Long id){ return new Movie(id,this.title,this.category); }
}

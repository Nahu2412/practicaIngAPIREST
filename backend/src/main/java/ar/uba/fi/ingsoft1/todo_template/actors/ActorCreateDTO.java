package ar.uba.fi.ingsoft1.todo_template.actors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActorCreateDTO(
        @NotBlank @Size(min = 1, max = 100) String name
){
    public Actor asActor() { return this.asActor(null); }

    public Actor asActor(Long id) { return new Actor(id, this.name); }
}


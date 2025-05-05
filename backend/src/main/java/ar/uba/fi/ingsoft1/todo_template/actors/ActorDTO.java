package ar.uba.fi.ingsoft1.todo_template.actors;

public record ActorDTO(
        long id,
        String name
) {
    public ActorDTO(Actor actor){ this(actor.getId(), actor.getName());}
}

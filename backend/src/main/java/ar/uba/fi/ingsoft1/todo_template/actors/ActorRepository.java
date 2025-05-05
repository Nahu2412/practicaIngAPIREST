package ar.uba.fi.ingsoft1.todo_template.actors;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByName(String name);

    Actor getActorById(Long id);
}

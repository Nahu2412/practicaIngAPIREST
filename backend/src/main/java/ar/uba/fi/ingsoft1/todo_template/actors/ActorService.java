package ar.uba.fi.ingsoft1.todo_template.actors;

import ar.uba.fi.ingsoft1.todo_template.projects.ProjectCreateDTO;
import ar.uba.fi.ingsoft1.todo_template.projects.ProjectDTO;
import ar.uba.fi.ingsoft1.todo_template.projects.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ActorService {
    private final ActorRepository actorRepository;

    @Autowired
    ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    Page<ActorDTO> getActors(Pageable pageable) {
        return actorRepository.findAll(pageable).map(ActorDTO::new);
    }

    Optional<ActorDTO> getActor(long id) {
        return actorRepository.findById(id).map(ActorDTO::new);
    }

    ActorDTO createActor(ActorCreateDTO actorCreate) {
        return new ActorDTO(actorRepository.save(actorCreate.asActor()));
    }

    Optional<ActorDTO> updateActor(long id, ActorCreateDTO actorCreate) {
        if (!actorRepository.existsById(id)) {
            return Optional.empty();
        }
        var saved = actorRepository.save(actorCreate.asActor(id));
        return Optional.of(new ActorDTO(saved));
    }

    void deleteActor(long id) {
        actorRepository.deleteById(id);
    }
}

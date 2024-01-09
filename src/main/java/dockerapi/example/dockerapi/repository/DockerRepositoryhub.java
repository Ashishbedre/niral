package dockerapi.example.dockerapi.repository;

import dockerapi.example.dockerapi.entity.DockerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DockerRepositoryhub extends JpaRepository<DockerRepository,Long> {
    Optional<DockerRepository> findByNameAndNamespace(String name,String namespace);


}

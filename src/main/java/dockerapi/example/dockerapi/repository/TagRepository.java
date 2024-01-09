package dockerapi.example.dockerapi.repository;

import dockerapi.example.dockerapi.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<RepositoryEntity,Long> {


}

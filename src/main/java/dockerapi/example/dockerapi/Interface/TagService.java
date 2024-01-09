package dockerapi.example.dockerapi.Interface;

import dockerapi.example.dockerapi.dto.DockerImageResult;
import dockerapi.example.dockerapi.dto.ResponceFormate;
import dockerapi.example.dockerapi.entity.DockerRepository;
import dockerapi.example.dockerapi.entity.RepositoryEntity;

import java.util.List;

public interface TagService {
    public List<ResponceFormate> getAllDockerRepository();
    public List<ResponceFormate> formatTheEntity(List<RepositoryEntity> repositoryEntityReturn);
    public  String buildDockerHubApiUrl(String namespace, String repository);
    public List<DockerImageResult> fetchAndSaveTags(String apiUrl);
}

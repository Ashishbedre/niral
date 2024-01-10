package dockerapi.example.dockerapi.Interface;

import dockerapi.example.dockerapi.dto.DockerImageResult;
import dockerapi.example.dockerapi.dto.DockerRepository;

import java.util.List;

public interface DockerRepositoryService {
    public String login();
    public String gettoken();
    public List<DockerRepository> fetchAndSaveRepositories();
    public String buildDockerHubApiUrl();
}

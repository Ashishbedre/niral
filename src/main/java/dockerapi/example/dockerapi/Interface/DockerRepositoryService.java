package dockerapi.example.dockerapi.Interface;

public interface DockerRepositoryService {
    public String login();
    public String gettoken();
    public void fetchAndSaveRepositories();
    public String buildDockerHubApiUrl();
}

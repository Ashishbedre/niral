package dockerapi.example.dockerapi.dto;


import java.util.List;

public class DockerHubApiResponse {
    private List<RepositoryEntity> results;

    // Getters and setters

    public List<RepositoryEntity> getResults() {
        return results;
    }

    public void setResults(List<RepositoryEntity> results) {
        this.results = results;
    }
}
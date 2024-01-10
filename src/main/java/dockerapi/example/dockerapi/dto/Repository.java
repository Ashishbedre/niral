package dockerapi.example.dockerapi.dto;

import java.util.List;

public class Repository {
    private List<Tag> results;

    // Getters and setters

    public List<Tag> getResults() {
        return results;
    }

    public void setResults(List<Tag> results) {
        this.results = results;
    }


}

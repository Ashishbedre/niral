package dockerapi.example.dockerapi.dto;

import dockerapi.example.dockerapi.entity.TagEntity;

import java.util.List;

public class ResponceFormate {

    private  String repository ;
    private List<String> tags;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

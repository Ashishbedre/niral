package dockerapi.example.dockerapi.dto;

import java.util.List;

public  class RepositoryEntity {
    private  String repository ;
    private List<ResponceFormate.TagEntity> tags;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public List<ResponceFormate.TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<ResponceFormate.TagEntity> tags) {
        this.tags = tags;
    }
}

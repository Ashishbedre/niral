package dockerapi.example.dockerapi.dto;

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

    public static class TagEntity {


    //    private Long id;

        private String name;

        // Getters and setters

    //    public Long getId() {
    //        return id;
    //    }

        public TagEntity(String name) {
            this.name = name;
        }

    //    public void setId(Long id) {
    //        this.id = id;
    //    }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

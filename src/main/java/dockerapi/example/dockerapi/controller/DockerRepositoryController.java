package dockerapi.example.dockerapi.controller;

import dockerapi.example.dockerapi.dto.ResponceFormate;
import dockerapi.example.dockerapi.entity.DockerRepository;
import dockerapi.example.dockerapi.entity.RepositoryEntity;
import dockerapi.example.dockerapi.entity.TagEntity;
import dockerapi.example.dockerapi.service.DockerRepositoryServiceImp;
import dockerapi.example.dockerapi.service.TagServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class DockerRepositoryController {

    @Autowired
    private DockerRepositoryServiceImp repositoryService;

    @Autowired
    private TagServiceImp tagServiceImp;

    @PostMapping("/fetchAndSaveRepositories")
    public List<ResponceFormate> fetchAndSaveRepositories() {
        return tagServiceImp.getAllDockerRepository();
    }
}

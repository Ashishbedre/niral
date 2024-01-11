package dockerapi.example.dockerapi.controller;

import dockerapi.example.dockerapi.dto.ResponceFormate;
import dockerapi.example.dockerapi.service.DockerRepositoryServiceImp;
import dockerapi.example.dockerapi.service.TagServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class DockerRepositoryController {

    @Autowired
    private DockerRepositoryServiceImp repositoryService;

    @Autowired
    private TagServiceImp tagServiceImp;

    @PostMapping("/fetchRepositories")
    public List<ResponceFormate> fetchAndSaveRepositories() {
        return tagServiceImp.getAllDockerRepository();
    }

    @GetMapping("/repositoriesDetail/{repository}")
    public ResponseEntity<List<String>> repositoriesDetail(@PathVariable String repository){
        List<String> repositoriesDetail = tagServiceImp.filterTheRepository(repository);
        return new ResponseEntity<>(repositoriesDetail , HttpStatus.OK);
    }

}

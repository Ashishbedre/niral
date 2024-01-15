package dockerapi.example.dockerapi.controller;

import dockerapi.example.dockerapi.Interface.DeleteRepository;
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

    @Autowired
    private DeleteRepository deleteRepository;

    @PostMapping("/fetchRepositories")
    public List<ResponceFormate> fetchAndSaveRepositories() {
        return tagServiceImp.getAllDockerRepository();
    }

    @GetMapping("/repositoriesDetail/{repository}")
    public ResponseEntity<List<String>> repositoriesDetail(@PathVariable String repository){
        List<String> repositoriesDetail = tagServiceImp.filterTheRepository(repository);
        return new ResponseEntity<>(repositoriesDetail , HttpStatus.OK);
    }

    @DeleteMapping("/deleteTag/{repository}/{tag}")
    public ResponseEntity<String>  deleteTag(@PathVariable String repository , @PathVariable String tag){
        return new ResponseEntity<>(deleteRepository.deleteTag(repository,tag), HttpStatus.OK);
    }

    @DeleteMapping("/deleteRepository/{repository}")
    public ResponseEntity<String>  deleteTag(@PathVariable String repository ){
        return new ResponseEntity<>(deleteRepository.deleteRepo(repository), HttpStatus.OK);
    }


}

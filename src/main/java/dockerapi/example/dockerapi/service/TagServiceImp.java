package dockerapi.example.dockerapi.service;

import dockerapi.example.dockerapi.Interface.TagService;
import dockerapi.example.dockerapi.dto.DockerImageInfo;
import dockerapi.example.dockerapi.dto.DockerImageResult;
import dockerapi.example.dockerapi.dto.ResponceFormate;
import dockerapi.example.dockerapi.entity.*;
import dockerapi.example.dockerapi.repository.DockerRepositoryhub;
import dockerapi.example.dockerapi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Service
public class TagServiceImp implements TagService {

    @Autowired
    private DockerRepositoryhub repository;
    @Autowired
    private TagRepository  tagRepository;

    @Autowired
    private DockerRepositoryServiceImp dockerRepositoryServiceImp;

//    @Value("${myapp.api.url}")
//    private String apiUrl;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

//    String apiUrl = "https://hub.docker.com/v2/namespaces/ashishbedre/repositories/ashish/tags";

    @Override
//    @Scheduled(fixedDelay = 9000000)
    public List<ResponceFormate> getAllDockerRepository() {
        List<DockerRepository> find_all = repository.findAll();
        List<RepositoryEntity> repositoryEntityReturn = new ArrayList<>();
        for (DockerRepository repositoryNameAndNamespace : find_all) {
            String dockerHubApiUrl = buildDockerHubApiUrl(repositoryNameAndNamespace.getNamespace(), repositoryNameAndNamespace.getName());
            List<DockerImageResult> apiResponse = fetchAndSaveTags(dockerHubApiUrl);
            RepositoryEntity repositoryEntity = new RepositoryEntity();
            List<TagEntity> tags = new ArrayList<>();
            for (DockerImageResult repository1 : apiResponse) {
                tags.add(new TagEntity(repository1.getName()));
            }
            repositoryEntity.setRepository(repositoryNameAndNamespace.getName());
            repositoryEntity.setTags(tags);
            System.out.println(repositoryNameAndNamespace.getName());
            for(TagEntity ex : tags){
                System.out.println(ex.getName());
            }
            repositoryEntityReturn.add(repositoryEntity);
//            tagRepository.save(repositoryEntity);
        }

        //for repositoryEntityReturn to TagResponceFormate
//        for(RepositoryEntity format :repositoryEntityReturn ){
//            String repo = format.getRepository();
//            List<TagEntity> tag = format.getTags();
//            ResponceFormate responce = new ResponceFormate();
//            responce.setRepository(repo);
//            List<String> tagEntity = new ArrayList<>();
//            for(TagEntity copy :  tag){
//                String copyTag = copy.getName();
//                tagEntity.add(copyTag);
//            }
//            responce.setTags(tagEntity);
//
//            ResponceFormates.add(responce);
//        }

        return formatTheEntity(repositoryEntityReturn);
    }

    @Override
    public List<ResponceFormate> formatTheEntity(List<RepositoryEntity> repositoryEntityReturn) {
        //for repositoryEntityReturn to TagResponceFormate
        List<ResponceFormate> ResponceFormates = new ArrayList<>();
        for(RepositoryEntity format :repositoryEntityReturn ){
            String repo = format.getRepository();
            List<TagEntity> tag = format.getTags();
            ResponceFormate responce = new ResponceFormate();
            responce.setRepository(repo);
            List<String> tagEntity = new ArrayList<>();
            for(TagEntity copy :  tag){
                String copyTag = copy.getName();
                tagEntity.add(copyTag);
            }
            responce.setTags(tagEntity);

            ResponceFormates.add(responce);
        }
        return ResponceFormates;
    }

    @Override
    public String buildDockerHubApiUrl(String namespace, String repository) {
        return "https://hub.docker.com/v2/namespaces/" + namespace + "/repositories/" + repository + "/tags";
    }

    @Override
    public List<DockerImageResult> fetchAndSaveTags(String apiUrl) {
        headers.set("Authorization", "Bearer " +dockerRepositoryServiceImp.gettoken() );
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the API call
        ResponseEntity<DockerImageInfo> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                DockerImageInfo.class
        );
        List<DockerImageResult> repositories = response.getBody().getResults();
        return  repositories;


    }


}

package dockerapi.example.dockerapi.service;

import dockerapi.example.dockerapi.Interface.TagService;
import dockerapi.example.dockerapi.config.Config;
import dockerapi.example.dockerapi.dto.*;
//import dockerapi.example.dockerapi.repository.DockerRepositoryhub;
//import dockerapi.example.dockerapi.repository.TagRepository;
import dockerapi.example.dockerapi.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Service
public class TagServiceImp implements TagService {


    @Autowired
    private AppConfig appConfig;
    @Autowired
    private Config config;
    @Autowired
    private DockerRepositoryServiceImp dockerRepositoryServiceImp;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Override
    public List<ResponceFormate> getAllDockerRepository() {
        List<DockerRepository> find_all =dockerRepositoryServiceImp.fetchAndSaveRepositories();
        List<ResponceFormate> responceFormates = new ArrayList<>();
        for (DockerRepository repositoryNameAndNamespace : find_all) {
            String dockerHubApiUrl = buildDockerHubApiUrl(repositoryNameAndNamespace.getNamespace(), repositoryNameAndNamespace.getName());
            List<DockerImageResult> apiResponse = fetchAndSaveTags(dockerHubApiUrl);
            ResponceFormate repositoryEntity = new ResponceFormate();
            List<String> tags = new ArrayList<>();
            for (DockerImageResult repository1 : apiResponse) {
                tags.add(repository1.getName());
            }
            repositoryEntity.setRepository(repositoryNameAndNamespace.getName());
            repositoryEntity.setTags(tags);
            System.out.println(repositoryNameAndNamespace.getName());
            for(String ex : tags){
                System.out.println(ex);
            }
            responceFormates.add(repositoryEntity);
        }
        return responceFormates;
    }

    @Override
    public String buildDockerHubApiUrl(String namespace, String repository) {
        return "https://hub.docker.com/v2/namespaces/" + namespace + "/repositories/" + repository + "/tags";
    }

    @Override
    public List<DockerImageResult> fetchAndSaveTags(String apiUrl) {
        WebClient webClient = WebClient.create();
        String authorizationHeader = "Bearer " + appConfig.getGlobalVariable();
        DockerImageInfo response = webClient.get()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(DockerImageInfo.class)
                .block();
        List<DockerImageResult> repositories = response.getResults();
        return  repositories;


    }


}

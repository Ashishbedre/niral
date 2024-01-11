package dockerapi.example.dockerapi.service;

import dockerapi.example.dockerapi.Interface.TagService;
import dockerapi.example.dockerapi.config.Config;
import dockerapi.example.dockerapi.dto.*;
//import dockerapi.example.dockerapi.repository.DockerRepositoryhub;
//import dockerapi.example.dockerapi.repository.TagRepository;
import dockerapi.example.dockerapi.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${tag.api.url}")
    private String tagUrl;

    @Override
    public List<ResponceFormate> getAllDockerRepository() {
        List<DockerRepository> find_all =dockerRepositoryServiceImp.fetchAndSaveRepositories();
        List<ResponceFormate> responceFormates = new ArrayList<>();
        for (DockerRepository repositoryNameAndNamespace : find_all) {
            String dockerHubApiUrl = buildDockerHubApiUrl(repositoryNameAndNamespace.getNamespace(), repositoryNameAndNamespace.getName());
            List<DockerImageResult> apiResponse = fetchAndSaveTags(dockerHubApiUrl);
            ResponceFormate repositoryEntity = new ResponceFormate();
            List<String> tags = new ArrayList<>();
            for (DockerImageResult repository : apiResponse) {
                tags.add(repository.getName());
            }
            repositoryEntity.setRepository(repositoryNameAndNamespace.getName());
            repositoryEntity.setTags(tags);
            responceFormates.add(repositoryEntity);
        }
        return responceFormates;
    }

    @Override
    public String buildDockerHubApiUrl(String namespace, String repository) {
        return tagUrl + namespace + "/repositories/" + repository + "/tags";
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

    @Override
    public List<String> filterTheRepository(String repository) {
        List<String> repositoryDetail = new ArrayList<>();
        List<ResponceFormate> getDetail = getAllDockerRepository();
        try{
            for(ResponceFormate detail : getDetail) {
                if (detail.getRepository().equals(repository)) {
                    for (String tag : detail.getTags()) {
                        repositoryDetail.add(tag);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  repositoryDetail;
    }


}

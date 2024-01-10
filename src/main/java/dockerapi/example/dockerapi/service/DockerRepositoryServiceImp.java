package dockerapi.example.dockerapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dockerapi.example.dockerapi.Interface.DockerRepositoryService;
import dockerapi.example.dockerapi.config.Config;
import dockerapi.example.dockerapi.dto.DockerApiResponse;
import dockerapi.example.dockerapi.dto.DockerImageInfo;
import dockerapi.example.dockerapi.dto.DockerRepository;
import dockerapi.example.dockerapi.dto.DockerRepositoryResponse;
import dockerapi.example.dockerapi.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Service
public class DockerRepositoryServiceImp implements DockerRepositoryService {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private Config config;
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @Value("${login.api.url}")
    private String apiUrllogin;
    @Value("${user}")
    private String username;
    @Value("${password}")
    private String password;



    public String login(){
        WebClient webClient = WebClient.create();
        String jsonBody = "{\"username\": \"" +username+ "\", \"password\": \"" +password+ "\"}";
        String responseBody = webClient.post()
                .uri(apiUrllogin)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        String bearerToken1="";
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            bearerToken1 = rootNode.get("token").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bearerToken1;
    }

    public String gettoken(){
        String token = login();
        appConfig.setGlobalVariable(token);
        return token;
    }

    public List<DockerRepository> fetchAndSaveRepositories() {
        WebClient webClient = WebClient.create();
        String authorizationHeader = "Bearer " + gettoken();
        DockerApiResponse response = webClient.get()
                .uri(buildDockerHubApiUrl())
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(DockerApiResponse.class)
                .block();


        List<DockerRepositoryResponse> repositories =null;
        List<DockerRepository> existingRepository = new ArrayList<>();
        if (repositories == null || repositories.isEmpty()) {
            System.out.println("No Docker repositories found or the list is empty.");
        } else {
            for (DockerRepositoryResponse repository1 : repositories) {
                String name = repository1.getName();
                String namespace = repository1.getNamespace();
                DockerRepository dockerRepository = new DockerRepository();
                dockerRepository.setName(name);
                dockerRepository.setNamespace(namespace);
                existingRepository.add(dockerRepository);
            }
        }
        return  existingRepository;
    }

    @Override
    public String buildDockerHubApiUrl() {
        return "https://hub.docker.com/v2/namespaces/"+username+"/repositories";
    }
}

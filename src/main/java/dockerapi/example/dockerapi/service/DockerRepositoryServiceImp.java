package dockerapi.example.dockerapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dockerapi.example.dockerapi.Interface.DockerRepositoryService;
import dockerapi.example.dockerapi.dto.DockerApiResponse;
import dockerapi.example.dockerapi.dto.DockerImageResult;
import dockerapi.example.dockerapi.dto.DockerRepository;
import dockerapi.example.dockerapi.dto.DockerRepositoryResponse;
import dockerapi.example.dockerapi.entity.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Service
public class DockerRepositoryServiceImp implements DockerRepositoryService {

    @Autowired
    private AppConfig appConfig;
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @Value("${login.api.url}")
    private String apiUrllogin;
    @Value("${user}")
    private String username;
    @Value("${password}")
    private String password;



    public String login(){
//        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrllogin,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String responseBody = responseEntity.getBody();
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
        headers.set("Authorization", "Bearer " + gettoken());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DockerApiResponse> response = restTemplate.exchange(
                buildDockerHubApiUrl(),
                HttpMethod.GET,
                entity,
                DockerApiResponse.class
        );
        List<DockerRepositoryResponse> repositories = response.getBody().getResults();
        List<DockerRepository> existingRepository = new ArrayList<>();
        for (DockerRepositoryResponse repository1 : repositories) {
            String name = repository1.getName();
            String namespace = repository1.getNamespace();
            DockerRepository dockerRepository = new DockerRepository();
            dockerRepository.setName(name);
            dockerRepository.setNamespace(namespace);
            existingRepository.add(dockerRepository);
        }
        return  existingRepository;
    }

    @Override
    public String buildDockerHubApiUrl() {
        return "https://hub.docker.com/v2/namespaces/"+username+"/repositories";
    }
}

package dockerapi.example.dockerapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dockerapi.example.dockerapi.Interface.DockerRepositoryService;
import dockerapi.example.dockerapi.dto.DockerApiResponse;
import dockerapi.example.dockerapi.entity.DockerRepository;
import dockerapi.example.dockerapi.dto.DockerRepositoryResponse;
import dockerapi.example.dockerapi.repository.DockerRepositoryhub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@EnableScheduling
@Service
public class DockerRepositoryServiceImp implements DockerRepositoryService {

    @Autowired
    private DockerRepositoryhub repository;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    String apiUrl1 = "https://hub.docker.com/v2/users/login";

    String apiUrl = "https://hub.docker.com/v2/namespaces/ashishbedre/repositories";

    String token = "";
    public String login(){
//        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonBody = "{\"username\": \"ashishbedre\", \"password\": \"123456789\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl1,
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

//    @Scheduled(cron = "0 0 */2 * * *")
    public void gettoken(){
        token=login();
    }

//    @Scheduled(fixedDelay = 1000)
    public void fetchAndSaveRepositories() {
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the API call
        ResponseEntity<DockerApiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                DockerApiResponse.class
        );
        List<DockerRepositoryResponse> repositories = response.getBody().getResults();
        for (DockerRepositoryResponse repository1 : repositories) {
            String name = repository1.getName();
            String namespace = repository1.getNamespace();
            Optional<DockerRepository> existingRepository = repository.findByNameAndNamespace(name, namespace);

            if (existingRepository.isEmpty()) {
                DockerRepository dockerRepository = new DockerRepository();
                dockerRepository.setName(name);
                dockerRepository.setNamespace(namespace);
                repository.save(dockerRepository);
            }
        }
    }
}

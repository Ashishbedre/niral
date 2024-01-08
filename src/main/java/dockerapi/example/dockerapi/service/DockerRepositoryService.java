package dockerapi.example.dockerapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dockerapi.example.dockerapi.entity.DockerApiResponse;
import dockerapi.example.dockerapi.entity.DockerRepository;
import dockerapi.example.dockerapi.entity.DockerRepositoryResponse;
import dockerapi.example.dockerapi.repository.DockerRepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DockerRepositoryService {

    @Autowired
    private DockerRepositoryRepository repository;

    public void fetchAndSaveRepositories(String apiUrl, String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

//ashish


        // Set the Docker Hub API URL
        String apiUrl1 = "https://hub.docker.com/v2/users/login";

        // Create a HttpHeaders object and set the content type to JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the JSON body as a string
        String jsonBody = "{\"username\": \"ashishbedre\", \"password\": \"ACMSbedre@1999\"}";

        // Create an HttpEntity with the JSON body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        // Create a RestTemplate
//        RestTemplate restTemplate = new RestTemplate();

        // Make a POST request using exchange method
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl1,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Get the response body as a string
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);
        // Parse the JSON response using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        String bearerToken1="";
        try {
            // Read the root node
            JsonNode rootNode = objectMapper.readTree(responseBody);
            bearerToken1 = rootNode.get("token").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }
//ashish



        // Set the Authorization header with the Bearer token
//        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken1);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the API call
        ResponseEntity<DockerApiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                DockerApiResponse.class
        );

        // Save the repositories in the database
        List<DockerRepositoryResponse> repositories = response.getBody().getResults();
        for (DockerRepositoryResponse repository1 : repositories) {
//            DockerRepository dockerRepository = new DockerRepository();
//            dockerRepository.setName(repository1.getName());
//            dockerRepository.setNamespace(repository1.getNamespace());
//            repository.save(dockerRepository);
            String name = repository1.getName();
            String namespace = repository1.getNamespace();

            // Check if the entry already exists in the database
            Optional<DockerRepository> existingRepository = repository.findByNameAndNamespace(name, namespace);

            if (existingRepository.isEmpty()) {
                // Save the repository in the database
                DockerRepository dockerRepository = new DockerRepository();
                dockerRepository.setName(name);
                dockerRepository.setNamespace(namespace);
                repository.save(dockerRepository);
            }
        }
    }
}

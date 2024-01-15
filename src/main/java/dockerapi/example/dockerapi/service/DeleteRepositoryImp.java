package dockerapi.example.dockerapi.service;

import dockerapi.example.dockerapi.Interface.DeleteRepository;
import dockerapi.example.dockerapi.config.AppConfig;
import dockerapi.example.dockerapi.config.Config;
import dockerapi.example.dockerapi.dto.DockerApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DeleteRepositoryImp implements DeleteRepository {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private Config config;
    @Value("${delete.api.url}")
    private String apiUrldelete;
    @Override
    public String deleteTag(String repository, String tag) {
        WebClient webClient = WebClient.create();
        String authorizationHeader = "Bearer " + appConfig.getGlobalVariable();
        webClient.delete()
                .uri(buildDockerHubDeleteTagApiUrl(repository,tag))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(void.class)
                .block();
        return repository+" "+tag+" has been deleted";
    }

    @Override
    public String deleteRepo(String repository) {
        WebClient webClient = WebClient.create();
        String authorizationHeader = "Bearer " + appConfig.getGlobalVariable();
        webClient.delete()
                .uri(buildDockerHubDeleteRepoApiUrl(repository))
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(void.class)
                .block();
        return repository+" has been deleted";
    }

    @Override
    public String buildDockerHubDeleteTagApiUrl(String repository, String tag) {
        return apiUrldelete+config.getUsername()+"/"+repository+"/tags/"+tag;
    }

    @Override
    public String buildDockerHubDeleteRepoApiUrl(String repository) {
        return apiUrldelete+config.getUsername()+"/"+repository;
    }


}

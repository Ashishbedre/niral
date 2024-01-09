package dockerapi.example.dockerapi.service;

import dockerapi.example.dockerapi.Interface.TagService;
import dockerapi.example.dockerapi.dto.DockerImageInfo;
import dockerapi.example.dockerapi.dto.DockerImageResult;
import dockerapi.example.dockerapi.dto.ResponceFormate;
import dockerapi.example.dockerapi.entity.*;
import dockerapi.example.dockerapi.repository.DockerRepositoryhub;
import dockerapi.example.dockerapi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    String apiUrl = "https://hub.docker.com/v2/namespaces/ashishbedre/repositories/ashish/tags";

    @Override
//    @Scheduled(fixedDelay = 9000000)
    public List<ResponceFormate> getAllDockerRepository() {
        List<DockerRepository> find_all = repository.findAll();
        List<RepositoryEntity> repositoryEntityReturn = new ArrayList<>();
        List<ResponceFormate> ResponceFormates = new ArrayList<>();
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
        headers.set("Authorization", "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1YyI6WyJNSUlDK1RDQ0FwK2dBd0lCQWdJQkFEQUtCZ2dxaGtqT1BRUURBakJHTVVRd1FnWURWUVFERXp0U1RVbEdPbEZNUmpRNlEwZFFNenBSTWtWYU9sRklSRUk2VkVkRlZUcFZTRlZNT2taTVZqUTZSMGRXV2pwQk5WUkhPbFJMTkZNNlVVeElTVEFlRncweU16QXhNRFl3TkRJM05EUmFGdzB5TkRBeE1qWXdOREkzTkRSYU1FWXhSREJDQmdOVkJBTVRPME5EVlVZNlNqVkhOanBGUTFORU9rTldSRWM2VkRkTU1qcEtXa1pST2xOTk0wUTZXRmxQTkRwV04wTkhPa2RHVjBJNldsbzFOam8wVlVSRE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBek4wYjBqN1V5XC9hc2pZWFdoMmczcW82SmhPa0FqWFdBVUJjc0h1NmhZWlJGTDl2ZTgxM1RCNGNsOFFreENJNGNVZ0dHbkdXWFZ4SDJ1NXZFdHhTT3FXQnJ4U05yaFNNalwvVk8rNlwvaVkrOG1GRmEwR2J5czF3VDVjNlY5cWROaERiVGNwQXVYSjFSNGJLdSt1VGpVS0VIYXlqSFI5TFBEeUdnUFwvbm1BWnZOT1hHbXJTU0pGSTZ4RTZmN0FcLys5Wm1xaDJWVFpCVzRxd25KcXRyc0kzY294M1BzMzBLYythSHdXdVl2TlF0U0F3K2pVeENVUWhFZkZrSUpLOHo5V2xcL1FjdE9EcEdUeXNtVHBjNzZaVEdKWWtnaGhGTFJEMmJQTlFEOEU1ZWdKa2RQOXhpaW5sVGx3MjBxWlhVRmlqdWFBcndOR0xJbUJEWE0wWlI1YzVtU3Z3SURBUUFCbzRHeU1JR3ZNQTRHQTFVZER3RUJcL3dRRUF3SUhnREFQQmdOVkhTVUVDREFHQmdSVkhTVUFNRVFHQTFVZERnUTlCRHREUTFWR09rbzFSelk2UlVOVFJEcERWa1JIT2xRM1RESTZTbHBHVVRwVFRUTkVPbGhaVHpRNlZqZERSenBIUmxkQ09scGFOVFk2TkZWRVF6QkdCZ05WSFNNRVB6QTlnRHRTVFVsR09sRk1SalE2UTBkUU16cFJNa1ZhT2xGSVJFSTZWRWRGVlRwVlNGVk1Pa1pNVmpRNlIwZFdXanBCTlZSSE9sUkxORk02VVV4SVNUQUtCZ2dxaGtqT1BRUURBZ05JQURCRkFpRUFtUTR4bEF2V1ZQK09cL2E2WENTTmlhQVhFTUFvVFBUVFhFYlgySzZFVThlMENJSDRBMDBJWG1Sd2N0a0Q4eVg3N2RNWjIrSkRjUUZ0MXFGS0xkVHlKdXNPVSJdfQ.eyJzb3VyY2UiOnsiaWQiOiJkODc5ZTI3YS03YjIxLTRkOGQtODA1OS1iNjY4OTZhNDg4NGQiLCJ0eXBlIjoicHdkIn0sImh0dHBzOlwvXC9odWIuZG9ja2VyLmNvbSI6eyJzb3VyY2UiOiJkb2NrZXJfcHdkfGQ4NzllMjdhLTdiMjEtNGQ4ZC04MDU5LWI2Njg5NmE0ODg0ZCIsInV1aWQiOiJkODc5ZTI3YS03YjIxLTRkOGQtODA1OS1iNjY4OTZhNDg4NGQiLCJzZXNzaW9uX2lkIjoiNjQ3YjE4N2MtOGMwNS00NWNlLTg4YjYtNWEwZWQxYTQxYTM0IiwidXNlcm5hbWUiOiJhc2hpc2hiZWRyZSIsInJvbGVzIjpbXSwiZW1haWwiOiJhc2hpc2hiZWRyZTA3QGdtYWlsLmNvbSJ9LCJzdWIiOiJkODc5ZTI3YTdiMjE0ZDhkODA1OWI2Njg5NmE0ODg0ZCIsImlzcyI6Imh0dHBzOlwvXC9hcGkuZG9ja2VyLmNvbVwvIiwiYXVkIjpbImh0dHBzOlwvXC9odWIuZG9ja2VyLmNvbSJdLCJqdGkiOiI2NDdiMTg3Yy04YzA1LTQ1Y2UtODhiNi01YTBlZDFhNDFhMzQiLCJ1c2VyX2lkIjoiZDg3OWUyN2E3YjIxNGQ4ZDgwNTliNjY4OTZhNDg4NGQiLCJzZXNzaW9uX2lkIjoiNjQ3YjE4N2MtOGMwNS00NWNlLTg4YjYtNWEwZWQxYTQxYTM0IiwiaWF0IjoxNzA0Nzg1MjE3LCJleHAiOjE3MDczNzcyMTcsInVzZXJuYW1lIjoiYXNoaXNoYmVkcmUifQ.RpQhFZex57Br3x9XMl1uYO7PTHHwvl2_p02xF_N2kEbdweN9aLhOoMrYsrBVhrxic-KbN_dAQO1ML0zSWw5tySz_Ykw4oNL0v2YNu8DiHPPeZJBM9sx6yTDXwtdMOJcSIMk7avjJ2-QVeO_jkAkaGHHJfML13OCd2Uwbmu3YRBY9H3DGysAmWLTw_6JCfd_Dfq87LbRHaPDZpsboKKtwHrJfA5brWNt8k2FIsM5aIxsZbjz4PKUgNMBu9tUWFleGlbvTvWWO_B7bZS36gbRnZLLMaHgv3G1GXd9jIaFndG9Rs3BkLXe7h1awtQ2h55EHWOuA6brsJhwbaolsMTXp3g");
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

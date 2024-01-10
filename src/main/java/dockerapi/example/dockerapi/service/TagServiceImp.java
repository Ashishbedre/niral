package dockerapi.example.dockerapi.service;

import dockerapi.example.dockerapi.Interface.TagService;
import dockerapi.example.dockerapi.dto.*;
//import dockerapi.example.dockerapi.repository.DockerRepositoryhub;
//import dockerapi.example.dockerapi.repository.TagRepository;
import dockerapi.example.dockerapi.entity.config.AppConfig;
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
    private AppConfig appConfig;
    @Autowired
    private DockerRepositoryServiceImp dockerRepositoryServiceImp;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Override
    public List<ResponceFormate> getAllDockerRepository() {
//        List<DockerRepository> find_all =dockerRepositoryServiceImp.fetchAndSaveRepositories();
//        List<RepositoryEntity> repositoryEntityReturn = new ArrayList<>();
//        for (DockerRepository repositoryNameAndNamespace : find_all) {
//            String dockerHubApiUrl = buildDockerHubApiUrl(repositoryNameAndNamespace.getNamespace(), repositoryNameAndNamespace.getName());
//            List<DockerImageResult> apiResponse = fetchAndSaveTags(dockerHubApiUrl);
//            RepositoryEntity repositoryEntity = new RepositoryEntity();
//            List<ResponceFormate.TagEntity> tags = new ArrayList<>();
//            for (DockerImageResult repository1 : apiResponse) {
//                tags.add(new ResponceFormate.TagEntity(repository1.getName()));
//            }
//            repositoryEntity.setRepository(repositoryNameAndNamespace.getName());
//            repositoryEntity.setTags(tags);
//            System.out.println(repositoryNameAndNamespace.getName());
//            for(ResponceFormate.TagEntity ex : tags){
//                System.out.println(ex.getName());
//            }
//            repositoryEntityReturn.add(repositoryEntity);
//        }
        List<DockerRepository> find_all =dockerRepositoryServiceImp.fetchAndSaveRepositories();
        List<ResponceFormate> ResponceFormates = new ArrayList<>();
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
            ResponceFormates.add(repositoryEntity);
        }
        return ResponceFormates;
    }
//
//    @Override
//    public List<ResponceFormate> formatTheEntity(List<RepositoryEntity> repositoryEntityReturn) {
//        //ashish
////        List<DockerRepository> find_all =dockerRepositoryServiceImp.fetchAndSaveRepositories();
////        List<ResponceFormate> ResponceFormates1 = new ArrayList<>();
////        for (DockerRepository repositoryNameAndNamespace : find_all) {
////            String dockerHubApiUrl = buildDockerHubApiUrl(repositoryNameAndNamespace.getNamespace(), repositoryNameAndNamespace.getName());
////            List<DockerImageResult> apiResponse = fetchAndSaveTags(dockerHubApiUrl);
////            ResponceFormate repositoryEntity = new ResponceFormate();
////            List<String> tags = new ArrayList<>();
////            for (DockerImageResult repository1 : apiResponse) {
////                tags.add(repository1.getName());
////            }
////            repositoryEntity.setRepository(repositoryNameAndNamespace.getName());
////            repositoryEntity.setTags(tags);
////            System.out.println(repositoryNameAndNamespace.getName());
////            for(String ex : tags){
////                System.out.println(ex);
////            }
////            ResponceFormates1.add(repositoryEntity);
////        }
//
//        ///
//        List<ResponceFormate> ResponceFormates = new ArrayList<>();
//        for(RepositoryEntity format :repositoryEntityReturn ){
//            String repo = format.getRepository();
//            List<ResponceFormate.TagEntity> tag = format.getTags();
//            ResponceFormate responce = new ResponceFormate();
//            responce.setRepository(repo);
//            List<String> tagEntity = new ArrayList<>();
//            for(ResponceFormate.TagEntity copy :  tag){
//                String copyTag = copy.getName();
//                tagEntity.add(copyTag);
//            }
//            responce.setTags(tagEntity);
//
//            ResponceFormates.add(responce);
//        }
//        return ResponceFormates;
//    }

    @Override
    public String buildDockerHubApiUrl(String namespace, String repository) {
        return "https://hub.docker.com/v2/namespaces/" + namespace + "/repositories/" + repository + "/tags";
    }

    @Override
    public List<DockerImageResult> fetchAndSaveTags(String apiUrl) {
        headers.set("Authorization", "Bearer " +appConfig.getGlobalVariable() );
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

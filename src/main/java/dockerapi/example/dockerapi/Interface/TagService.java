package dockerapi.example.dockerapi.Interface;

import dockerapi.example.dockerapi.dto.DockerImageResult;
import dockerapi.example.dockerapi.dto.ResponceFormate;

import java.util.List;

public interface TagService {
    public List<ResponceFormate> getAllDockerRepository();
//    public List<ResponceFormate> formatTheEntity(List<RepositoryEntity> repositoryEntityReturn);
    public  String buildDockerHubApiUrl(String namespace, String repository);
    public List<DockerImageResult> fetchAndSaveTags(String apiUrl);
    public  List<String> filterTheRepository(String repository);

    public boolean findTheTag(String repository, String tag);
}

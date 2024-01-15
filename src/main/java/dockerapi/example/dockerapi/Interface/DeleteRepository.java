package dockerapi.example.dockerapi.Interface;

import org.springframework.web.bind.annotation.PathVariable;

public interface DeleteRepository {
    public  String deleteTag(String repository ,String tag);
    public  String deleteRepo(String repository);
    public String buildDockerHubDeleteTagApiUrl(String repository, String tag);

    public String buildDockerHubDeleteRepoApiUrl(String repository);
}

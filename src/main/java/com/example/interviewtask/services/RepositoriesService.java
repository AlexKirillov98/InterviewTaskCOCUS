package com.example.interviewtask.services;

import com.example.interviewtask.mappers.RepositoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.kohsuke.github.GitHubBuilder;
import org.openapitools.model.GetRepositoriesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RepositoriesService {

    private final RepositoryMapper repositoryMapper;

    @Value("${github.token}")
    private String githubToken;
    @Value("${github.uri}")
    private String githubUri;

    @SneakyThrows
    public Mono<GetRepositoriesResponse> getRepositories(String username) {
        var response = new GetRepositoriesResponse();
        var github = new GitHubBuilder().withEndpoint(githubUri).withOAuthToken(githubToken).build();

        var user = github.getUser(username);
        var repositories = user.getRepositories();

        var filteredRepositories = repositories.values().stream().filter(r -> !r.isFork()).toList();

        for (var r : filteredRepositories) {
            var branches = r.getBranches().values();
            response.addRepositriesItem(repositoryMapper.map(r, branches));
        }

        return Mono.just(response);
    }
}
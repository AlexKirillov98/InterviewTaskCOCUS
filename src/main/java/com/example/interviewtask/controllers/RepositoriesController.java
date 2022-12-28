package com.example.interviewtask.controllers;

import com.example.interviewtask.services.RepositoriesService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.RepositoriesApi;
import org.openapitools.model.GetRepositoriesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class RepositoriesController implements RepositoriesApi {

    private final RepositoriesService repositoriesService;

    @Override
    public Mono<ResponseEntity<GetRepositoriesResponse>> getRepositories(String username, final ServerWebExchange exchange) {
        return repositoriesService.getRepositories(username).map(ResponseEntity::ok);
    }

}

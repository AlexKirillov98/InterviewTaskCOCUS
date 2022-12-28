package com.example.interviewtask;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.GetRepositoriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000000")
public class RepositoriesIT {

    @Autowired
    WebTestClient webTestClient;

    @Rule
    public WireMockRule wm = new WireMockRule(options().port(8081));

    @Test
    public void testGetRepositories() {
        stubFor(any(urlEqualTo("/user")).willReturn(ok()));
        stubFor(any(urlEqualTo("/users/test1")).willReturn(ok().withBodyFile("UsersTest1Response.json")));

        stubFor(any(urlEqualTo("/users/test1/repos?per_page=100")).willReturn(ok().withBodyFile("RepoTest1Response.json")));

        stubFor(any(urlEqualTo("/repos/test1/Hello-World1/branches")).willReturn(ok().withBodyFile("BranchesTest1Response.json")));

        var response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repositories")
                        .queryParam("username", "test1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GetRepositoriesResponse.class).getResponseBody().blockFirst();

        Assert.assertNotNull(response);
        Assert.assertEquals(1, response.getRepositries().size());
        Assert.assertEquals(2, response.getRepositries().get(0).getBranches().size());
    }

    @Test
    public void testGetRepositoriesNotFound() {
        stubFor(any(urlEqualTo("/user")).willReturn(ok()));
        stubFor(any(urlEqualTo("/users/test2")).willReturn(notFound()));

        var response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repositories")
                        .queryParam("username", "test2")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .returnResult(ErrorResponse.class).getResponseBody().blockFirst();

        Assert.assertNotNull(response);
        Assert.assertEquals("404", response.getStatus());
    }

    @Test
    public void testGetRepositoriesWrongMediaType() {
        var response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repositories")
                        .queryParam("username", "test2")
                        .build())
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(ErrorResponse.class).getResponseBody().blockFirst();

        Assert.assertNotNull(response);
        Assert.assertEquals("406", response.getStatus());
    }
}

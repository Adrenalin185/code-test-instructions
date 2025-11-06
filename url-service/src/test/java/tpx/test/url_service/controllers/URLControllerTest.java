package tpx.test.url_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import tpx.test.url_service.dtos.URLShortenRequest;
import tpx.test.url_service.entities.Url;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(URLController.class)
class URLControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private URLController controllerMock;

    @Test
    public void shouldReturnListOfURLs() throws Exception {
        List<Url> urls = List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/1", "HTTPS://www.web.com/short1", "abc123"),
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/2", "HTTPS://www.web.com/short2", "cba321")
        );

        given(controllerMock.getAllURLs()).willReturn(ResponseEntity.ok(urls));

        mockMvc.perform(get("/url/urls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originalUrl").value("HTTPS://www.web.com/very/long/url/link/for/reference/1"))
                .andExpect(jsonPath("$[0].shortenedUrl").value("HTTPS://www.web.com/short1"))
                .andExpect(jsonPath("$[0].alias").value("abc123"))
                .andExpect(jsonPath("$[1].originalUrl").value("HTTPS://www.web.com/very/long/url/link/for/reference/2"))
                .andExpect(jsonPath("$[1].shortenedUrl").value("HTTPS://www.web.com/short2"))
                .andExpect(jsonPath("$[1].alias").value("cba321"));
    }

    @Test
    public void shouldSaveAndShortenURL() throws Exception {
        URLShortenRequest requestJson = new URLShortenRequest("HTTPS://www.web.com/very/long/url/link/for/reference", "cba321");

        String shortenedUrl = "{\"shortenedUrl\":\"HTTPS://www.web.com/cba321\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                UriComponentsBuilder.fromPath("/url/shortenedUrl").buildAndExpand(shortenedUrl).toUri()
        );
        ResponseEntity<String> response = new ResponseEntity<>(shortenedUrl, headers, HttpStatus.CREATED);

        given(controllerMock.addNewURL(any(URLShortenRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/url/shorten")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestJson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortenedUrl").value("HTTPS://www.web.com/cba321"));
    }

    @Test
    public void shouldReturn400NotValidForInvalidAlias() throws Exception {
        URLShortenRequest request = new URLShortenRequest("HTTPS://www.web.com/very/long/url/link/for/reference", "cba321");

        given(controllerMock.addNewURL(any(URLShortenRequest.class))).willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input or alias already taken"));

        mockMvc.perform(post("/url/shorten")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid input or alias already taken"));
    }

    @Test
    public void shouldReturn400NotValidForInvalidInput() throws Exception {
        URLShortenRequest request = new URLShortenRequest();

        given(controllerMock.addNewURL(any(URLShortenRequest.class))).willReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input or alias already taken"));

        mockMvc.perform(post("/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid input or alias already taken"));
    }

    @Test
    public void shouldReturnUrlFromAlias() throws Exception {

        String originalUrl = "{\"originalUrl\":\"HTTPS://www.web.com/very/long/url/link/for/reference\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                UriComponentsBuilder.fromPath("/url/abc123").buildAndExpand(originalUrl).toUri()
        );
        ResponseEntity<String> response = new ResponseEntity<>(originalUrl, headers, HttpStatus.FOUND);

        given(controllerMock.getURLFromAlias("abc123")).willReturn(response);

        mockMvc.perform(get("/url/abc123"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.originalUrl").value("HTTPS://www.web.com/very/long/url/link/for/reference"));
    }

    @Test
    public void shouldReturn404NotFoundForGetInvalidAlias() throws Exception {
        given(controllerMock.getURLFromAlias("abc123")).willReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alias Not Found"));

        mockMvc.perform(get("/url/abc123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Alias Not Found"));
    }

    @Test
    public void shouldDeleteURLFromAlias() throws Exception {
        given(controllerMock.deleteUrlFromAlias("abc123")).willReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted"));

        mockMvc.perform(delete("/url/abc123"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("Successfully deleted"));
    }

    @Test
    public void shouldReturn404NotFoundForDeleteInvalidAlias() throws Exception {
        given(controllerMock.deleteUrlFromAlias("abc123")).willReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alias Not Found"));

        mockMvc.perform(delete("/url/abc123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Alias Not Found"));
    }

}
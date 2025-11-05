package tpx.test.url_service.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tpx.test.url_service.entities.Url;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(URLController.class)
class URLControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

}
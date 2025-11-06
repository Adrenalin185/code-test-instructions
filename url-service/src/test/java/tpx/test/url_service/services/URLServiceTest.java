package tpx.test.url_service.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tpx.test.url_service.dtos.URLShortenRequest;
import tpx.test.url_service.entities.Url;
import tpx.test.url_service.repositories.URLRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;


class URLServiceTest {

    @Mock
    private URLRepository repository;

    @InjectMocks
    private URLService service;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldReturnListOfURLs() {
        List<Url> urls = List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/1", "HTTPS://www.web.com/short1", "abc123"),
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/2", "HTTPS://www.web.com/short2", "cba321")
        );

        given(repository.findAll()).willReturn(urls);

        ResponseEntity<List<Url>> response = service.getAllURLS();

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().size()).isEqualTo(urls.size());
        assertThat(response.getBody().get(0)).isEqualTo(urls.get(0));
        assertThat(response.getBody().get(1)).isEqualTo(urls.get(1));
    }

    @Test
    void shouldSaveAndReturnShortenedURL() {
        URLShortenRequest urlShortenRequest = new URLShortenRequest("HTTPS://www.web.com/very/long/url/link/for/reference/", "abc123");

        ResponseEntity<String> response = service.addNewUrl(urlShortenRequest);

        ArgumentCaptor<Url> captor = ArgumentCaptor.forClass(Url.class);
        verify(repository, times(1)).save(captor.capture());

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("HTTPS://www.web.com/abc123");
    }

    @Test
    void shouldReturn400NotValidForInvalidAlias() {
        URLShortenRequest urlShortenRequest = new URLShortenRequest("HTTPS://www.web.com/very/long/url/link/for/reference/", "abc123");

        given(repository.findAll()).willReturn(List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/", "HTTPS://www.web.com/short", "abc123")
        ));

        ResponseEntity<String> response = service.addNewUrl(urlShortenRequest);

        ArgumentCaptor<Url> captor = ArgumentCaptor.forClass(Url.class);
        verify(repository, times(0)).save(captor.capture());

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Invalid input or alias already taken");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn400NotValidForInvalidInput() {
        URLShortenRequest urlShortenRequest = new URLShortenRequest();

        ResponseEntity<String> response = service.addNewUrl(urlShortenRequest);

        verify(repository, times(0)).findAll();
        ArgumentCaptor<Url> captor = ArgumentCaptor.forClass(Url.class);
        verify(repository, times(0)).save(captor.capture());

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Invalid input or alias already taken");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnUrlFromAlias() {
        given(repository.findAll()).willReturn(List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/", "HTTPS://www.web.com/short", "abc123")
        ));

        ResponseEntity<String> response = service.getURLFromAlias("abc123");

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("HTTPS://www.web.com/very/long/url/link/for/reference/");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.FOUND.value());
    }

    @Test
    void shouldReturn404ForInvalidAlias() {
        ResponseEntity<String> response = service.getURLFromAlias("abc123");

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Alias Not Found");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn404ForInvalidAliasWithOtherAliases() {
        given(repository.findAll()).willReturn(List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/", "HTTPS://www.web.com/short", "123abc")
        ));

        ResponseEntity<String> response = service.getURLFromAlias("abc123");

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Alias Not Found");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldDeleteURLFromAlias() {
        given(repository.findAll()).willReturn(List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/", "HTTPS://www.web.com/short", "abc123")
        ));

        ResponseEntity<String> response = service.deleteURLFromAlias("abc123");

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Successfully deleted");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldReturn404NotFoundForDeleteInvalidAlias() {
        ResponseEntity<String> response = service.deleteURLFromAlias("abc123");

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Alias Not Found");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn404NotFoundForDeleteInvalidAliasWithOtherAliases() {
        given(repository.findAll()).willReturn(List.of(
                new Url("HTTPS://www.web.com/very/long/url/link/for/reference/", "HTTPS://www.web.com/short", "123abc")
        ));

        ResponseEntity<String> response = service.deleteURLFromAlias("abc123");

        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo("Alias Not Found");
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
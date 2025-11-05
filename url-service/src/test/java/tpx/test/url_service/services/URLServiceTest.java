package tpx.test.url_service.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import tpx.test.url_service.entities.Url;
import tpx.test.url_service.repositories.URLRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


class URLServiceTest {

    @Mock
    private URLRepository repository;

    @InjectMocks
    private URLService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}
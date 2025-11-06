package tpx.test.url_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import tpx.test.url_service.dtos.URLShortenRequest;
import tpx.test.url_service.entities.Url;
import tpx.test.url_service.repositories.URLRepository;


import java.util.List;

@Service
public class URLService {

    @Autowired
    private URLRepository repository;

    public ResponseEntity<List<Url>> getAllURLS() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<String> addNewUrl(URLShortenRequest urlShortenRequest) {

        if (urlShortenRequest.getAlias() == null || urlShortenRequest.getAlias().isEmpty()
                || urlShortenRequest.getUrl() == null || urlShortenRequest.getUrl().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input or alias already taken");
        }

        List<Url> urlAliases = repository.findAll();
        for (Url urlAlias : urlAliases) {
            if (urlAlias.getAlias().equals(urlShortenRequest.getAlias())) {
                return ResponseEntity.badRequest().body("Invalid input or alias already taken");
            }
        }

        String shortURLAddress = "HTTPS://www.web.com/" + urlShortenRequest.getAlias();
        Url url = new Url(urlShortenRequest.getUrl(), shortURLAddress, urlShortenRequest.getAlias());

        repository.save(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                UriComponentsBuilder.fromPath("/url/shorten").buildAndExpand(shortURLAddress).toUri()
        );

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(shortURLAddress);
    }
}

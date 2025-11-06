package tpx.test.url_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tpx.test.url_service.dtos.URLShortenRequest;
import tpx.test.url_service.entities.Url;
import tpx.test.url_service.services.URLService;

import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/url")
public class URLController {

    @Autowired
    private URLService service;

    @GetMapping("/urls")
    public ResponseEntity<List<Url>> getAllURLs() {
        return service.getAllURLS();
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> addNewURL(@RequestBody URLShortenRequest urlShortenRequest) {
        return service.addNewUrl(urlShortenRequest);
    }

    @GetMapping("/{alias}")
    public Object getURLFromAlias(@PathVariable String alias) {
        return service.getURLFromAlias(alias);
    }

    @DeleteMapping("/{alias}")
    public Object deleteUrlFromAlias(@PathVariable String alias) {
        return service.deleteURLFromAlias(alias);
    }
}

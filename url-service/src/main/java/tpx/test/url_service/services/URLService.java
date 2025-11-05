package tpx.test.url_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
}

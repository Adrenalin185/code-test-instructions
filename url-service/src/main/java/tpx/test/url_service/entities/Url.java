package tpx.test.url_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "url", schema = "TEST")
public class Url {
    @Id
    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "shortened_url")
    private String shortenedUrl;

    @Column(name = "alias")
    private String alias;

    public Url(String originalUrl, String shortenedUrl, String alias) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.alias = alias;
    }

    public Url() {

    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
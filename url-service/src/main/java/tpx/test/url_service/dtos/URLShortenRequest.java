package tpx.test.url_service.dtos;

import lombok.Getter;

public class URLShortenRequest {

    @Getter
    private String url;

    @Getter
    private String alias;

    public URLShortenRequest(String url, String alias) {
        this.url = url;
        this.alias = alias;
    }

    public URLShortenRequest() {
    }
}

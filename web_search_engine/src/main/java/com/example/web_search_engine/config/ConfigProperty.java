package com.example.web_search_engine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "config")
@Setter
@Getter
public class ConfigProperty {

    private String prefix;

    private String userAgent;

    private String adminLogin;

    private String adminPassword;

    private String webInterface;

    private List<Site> sites;

    @Getter
    @Setter
    public static class Site {
        String url;
        String name;
    }
}

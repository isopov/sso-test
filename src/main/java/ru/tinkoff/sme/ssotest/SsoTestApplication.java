package ru.tinkoff.sme.ssotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
//@EnableOAuth2Client
public class SsoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoTestApplication.class, args);
    }

    @RestController
    @RequestMapping("/hello")
    public static class HelloController {
        @GetMapping
        public String hello(Principal principal) {
            return "Hello " + principal.getName();
        }
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }


    @RestController
    @RequestMapping("/repos")
    public static class ReposController {

        private final OAuth2RestTemplate oAuth2RestTemplate;

        public ReposController(OAuth2RestTemplate oAuth2RestTemplate) {
            this.oAuth2RestTemplate = oAuth2RestTemplate;
        }

        @GetMapping
        public String repos() {
            return oAuth2RestTemplate.getForObject("https://api.github.com/user/repos", String.class);
        }
    }

    @RestController
    @RequestMapping("/token")
    public static class TokenController {

        private final OAuth2RestTemplate oAuth2RestTemplate;

        public TokenController(OAuth2RestTemplate oAuth2RestTemplate) {
            this.oAuth2RestTemplate = oAuth2RestTemplate;
        }

        @GetMapping
        public String token() {
            return oAuth2RestTemplate.getAccessToken().getValue();
        }
    }

}

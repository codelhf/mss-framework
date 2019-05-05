package com.mss.framework.base.user.oauth;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuthApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OAuthApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
    }

}

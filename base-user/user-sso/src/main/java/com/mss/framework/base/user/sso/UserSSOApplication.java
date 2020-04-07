package com.mss.framework.base.user.sso;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserSSOApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(UserSSOApplication.class);
		application.setBannerMode(Banner.Mode.CONSOLE);
		application.run(args);
	}
}

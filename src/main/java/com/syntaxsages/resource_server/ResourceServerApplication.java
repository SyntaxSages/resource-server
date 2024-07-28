package com.syntaxsages.resource_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@EnableMethodSecurity
@SpringBootApplication
public class ResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}

	@Service
	class GreetingsService {

		@PreAuthorize("hasAuthority('SCOPE_user.read')")
		public Map<String,  String> greet() {
			var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return Map.of("message", jwt.getSubject());
		}

    }

	@Controller
	@ResponseBody
	class GreetingsController {
			private final GreetingsService greetingsService;

        GreetingsController(GreetingsService greetingsService) {
            this.greetingsService = greetingsService;
        }

        @GetMapping("/hello")
			Map<String, String> hello() {
				return greetingsService.greet();
			}
	}

}

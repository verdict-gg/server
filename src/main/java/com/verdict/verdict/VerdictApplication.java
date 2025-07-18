package com.verdict.verdict;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableJpaAuditing
public class VerdictApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("src/main/resources/")
				.filename("envs.env")
				.ignoreIfMissing()
				.load();
		Map<String, Object> envMap = dotenv.entries().stream()
				.collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue));

		SpringApplication app = new SpringApplication(VerdictApplication.class);
		app.addInitializers(ctx -> ctx.getEnvironment()
                .getPropertySources()
                .addFirst(new MapPropertySource("dotenv", envMap)));
		app.run(args);
	}
}

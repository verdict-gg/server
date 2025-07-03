package com.verdict.verdict.config;

import com.verdict.verdict.util.LocalDateFormatter;
import com.verdict.verdict.util.LocalDateTimeFormatter;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AppConfig{

//    private final S3Config s3Config;
    private final Environment env;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @PostConstruct
    public void loadDotenv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .filename("local.env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        Map<String, Object> envMap = dotenv.entries().stream()
                .collect(Collectors.toMap(
                        DotenvEntry::getKey,
                        DotenvEntry::getValue
                ));

        configurableEnvironment.getPropertySources()
                .addFirst(new MapPropertySource("localDotenv", envMap));
    }

//    @Bean
//    public AesBytesEncryptor aesBytesEncryptor() {
//        return new AesBytesEncryptor(
//                env.getProperty("github.encryptSecretKey"),
//                env.getProperty("github.salt"));
//    }

    @Bean
    public LocalDateFormatter localDateFormatter() {
        return new LocalDateFormatter();
    }

    @Bean
    public LocalDateTimeFormatter localDateTimeFormatter() {
        return new LocalDateTimeFormatter();
    }
}
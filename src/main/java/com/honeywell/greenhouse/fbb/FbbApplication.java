package com.honeywell.greenhouse.fbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p><code>export MAVEN_OPTS="-Dfile.encoding=UTF-8"</code> is quite useful
 * when using maven tools, which solves almost all encoding issues, especially
 * running junit test via `Win cmd` or `Git bash` to show the Chinese output
 * correctly.
 */
@SpringBootApplication
@ComponentScan("com.honeywell.greenhouse.fbb")
@EnableConfigurationProperties
public class FbbApplication {

    public static void main(String[] args) {
        SpringApplication.run(FbbApplication.class, args);
    }

}

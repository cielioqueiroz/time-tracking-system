package com.company.timetracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * Application entry point.
 *
 * <p>The codebase follows a Hexagonal / Clean Architecture layout:
 * <ul>
 *   <li>{@code domain}         — framework-agnostic core (entities, value objects, ports).</li>
 *   <li>{@code application}    — use cases orchestrating the domain.</li>
 *   <li>{@code infrastructure} — outbound adapters (JPA, security, config).</li>
 *   <li>{@code presentation}   — inbound adapters (REST controllers).</li>
 * </ul>
 * Dependencies always point inward; the domain depends on nothing.
 */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class TimeTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackingApplication.class, args);
    }
}

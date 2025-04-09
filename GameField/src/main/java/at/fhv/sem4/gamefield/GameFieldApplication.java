package at.fhv.sem4.gamefield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.context.annotation.Bean;


import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.client.circuitbreaker.Customizer;

import java.time.Duration;

@SpringBootApplication
public class GameFieldApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameFieldApplication.class, args);
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.custom()
						.slowCallDurationThreshold(Duration.ofSeconds(3))
						.slowCallRateThreshold(50)
						.minimumNumberOfCalls(3)
						.waitDurationInOpenState(Duration.ofSeconds(20))
						.build())
				.timeLimiterConfig(TimeLimiterConfig.custom()
						.timeoutDuration(Duration.ofSeconds(12))
						.build())
				.build());
	}

}

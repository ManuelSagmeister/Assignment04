package at.fhv.sem4.gamefield.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue playerQueue() {
        return new Queue("player.queue", false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("player.exchange");
    }

    @Bean
    public Binding binding(Queue playerQueue, DirectExchange exchange) {
        return BindingBuilder.bind(playerQueue).to(exchange).with("player.created");
    }
}


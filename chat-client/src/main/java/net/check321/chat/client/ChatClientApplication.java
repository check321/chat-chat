package net.check321.chat.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.check321.chat.core","net.check321.chat.client"})
public class ChatClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatClientApplication.class, args);
    }

}

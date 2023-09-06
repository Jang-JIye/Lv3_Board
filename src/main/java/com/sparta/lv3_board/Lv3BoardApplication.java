package com.sparta.lv3_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Lv3BoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lv3BoardApplication.class, args);
    }

}

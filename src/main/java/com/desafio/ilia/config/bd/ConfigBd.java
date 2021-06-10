package com.desafio.ilia.config.bd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

@Configuration
public class ConfigBd {


    @Autowired
    DbInitialize dbInitialize;

    @Bean
    public void intastianteDatabase() {
        dbInitialize.instantieateTestDatabase();
    }
}

package com.tinqinacademy.hotel.persistence;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Component
@NoArgsConstructor
public class DBConnection {
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;
    @Getter
    private static Connection connection;

    @PostConstruct
    public void init() {
        log.info("Creating DB connection");
        try {
            //Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            log.info("Created DB connection");
        } catch (SQLException e) {
            log.error("Error creating DB connection, URL={}", url);
            throw new RuntimeException(e);
        }
    }

}

package com.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author alex@aguzun.com
 * @date 13-05-2016.
 */
@SpringBootApplication
@RestController
public class App {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
    }

    @RequestMapping("/")
    public ResponseEntity<TestResponse> test() {
        try {
            return ResponseEntity.ok(
                    new TestResponse("Hello World!!", InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getCanonicalHostName())
            );
        } catch (UnknownHostException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TestResponse("KABOOM" + e.getMessage()));
        }
    }

    public static class TestResponse {
        public final String message;
        public final String ip;
        public final String hostname;

        public TestResponse(String message, String ip, String hostname) {
            this.message = message;
            this.ip = ip;
            this.hostname = hostname;
        }

        public TestResponse(String message) {
            this.message = message;
            this.ip = null;
            this.hostname = null;
        }
    }
}

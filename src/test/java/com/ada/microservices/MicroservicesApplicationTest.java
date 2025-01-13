package com.ada.microservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.config.location=classpath:application-test.properties",
    "server.port=8081"
})
public class MicroservicesApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void applicationStarts() {
        MicroservicesApplication.main(new String[] {});
        assertThat(true).isTrue();
    }
}

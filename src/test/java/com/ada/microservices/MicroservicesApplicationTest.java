package com.ada.microservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MicroservicesApplicationTest {

    @Test
    void applicationStarts() {
        MicroservicesApplication.main(new String[] {});
        assertThat(true).isTrue();
    }
}

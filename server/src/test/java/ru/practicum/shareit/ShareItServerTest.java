package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShareItServerTest {

    @Test
    void contextLoads() {
    }

    @Test
    void testMain() {
        ShareItServer.main(new String[]{});
    }
}

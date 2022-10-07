package com.example.demo;

import com.example.demo.controller.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DemoApplicationTests {


    @Test
    void contextLoads() {
        assertEquals(1+1, 2);
    }

}

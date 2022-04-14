package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@Disabled
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class TestBase {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;


}

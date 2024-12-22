package dev.biddan.springbootbatchexample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class BatchApiControllerTest {

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @DisplayName("매핑된 유저를 찾은 경우, 배치 작업을 실행한다")
    @Test
    void success() throws Exception {
        String requestJson = """
                {
                    "username": "biddan606"
                }
                """;

        MockHttpServletRequestBuilder requestBuilder = post("/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);

        mockMvcBuilder.build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @DisplayName("매핑된 유저를 찾지 못한 경우, 작업을 아지 않고 응답한다")
    @Test
    void failed() throws Exception {
        // username이 없는 JSON
        String requestJson = """
            {
              "username": ""
            }
            """;

        MockHttpServletRequestBuilder requestBuilder = post("/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);

        mockMvcBuilder.build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }
}

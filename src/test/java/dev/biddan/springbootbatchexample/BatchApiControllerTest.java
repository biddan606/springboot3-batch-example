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

    @Autowired
    private UserRepoMappingRepository userRepoMappingRepository;

    @DisplayName("매핑된 유저를 찾은 경우, 배치 작업을 실행한다")
    @Test
    void success() throws Exception {
        UserRepoMapping userRepoMapping1 = saveUserRepoMapping("biddan606", "coding-tests");
        UserRepoMapping userRepoMapping2 = saveUserRepoMapping("Sonseongoh", "Algorithm");

        MockHttpServletRequestBuilder requestBuilder1 = generateRequestBuilder(userRepoMapping1.username());
        mockMvcBuilder.build()
                .perform(requestBuilder1)
                .andExpect(status().isOk());

        MockHttpServletRequestBuilder requestBuilder2 = generateRequestBuilder(userRepoMapping2.username());
        mockMvcBuilder.build()
                .perform(requestBuilder2)
                .andExpect(status().isOk());
    }

    @DisplayName("매핑된 유저를 찾지 못한 경우, 작업을 아지 않고 응답한다")
    @Test
    void failed() throws Exception {
        // username이 없는 JSON
        MockHttpServletRequestBuilder requestBuilder = generateRequestBuilder("");

        mockMvcBuilder.build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    private static MockHttpServletRequestBuilder generateRequestBuilder(String username) {
        String requestJson = String.format("""
                        {
                            "username": "%s"
                        }
                        """, username);

        return post("/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);
    }

    private UserRepoMapping saveUserRepoMapping(String username, String repositoryName) {
        UserRepoMapping userRepoMapping1 = UserRepoMapping.builder()
                .username(username)
                .repositoryName(repositoryName)
                .build();

        userRepoMappingRepository.save(userRepoMapping1);
        return userRepoMapping1;
    }
}

package com.danebrown.json.inttime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LocalTimeTest.TimeController.class)
public class TimeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
//    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试 GET /api/time 接口
     * 预期返回 {"time": "23:59:59"}
     */
    @Test
    public void testGetTime() throws Exception {
        this.mockMvc.perform(get("/api/time"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time").value("23:59:59"));
    }

    /**
     * 测试 POST /api/time 接口
     * 输入 {"time": "01:00:00"}，预期返回 {"time": "01:00:00"}
     */
    @Test
    public void testPostTimeStringToInteger() throws Exception {
        // 构造输入 DTO
        LocalTimeTest.TimeDto inputDto = new LocalTimeTest.TimeDto();
        inputDto.setTime(10000L); // 逻辑上等价于 "01:00:00"

        String jsonInput = "{\"time\": \"01:00:00\"}";

        this.mockMvc.perform(post("/api/time")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time").value("01:00:00"));
    }
}

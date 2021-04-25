package com.reloadly.security;

import com.reloadly.security.model.HelloResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloControllerTests extends AbstractIntegrationTest {

    @Test
    public void should_get_hello_message_with_mock_security() throws Exception {
        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/hello")
                .header("X-Mock-UID", testUid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        HelloResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HelloResponse.class);
        assertThat(response.getMessage()).isEqualTo("Hello World!");
    }

    @Test
    public void should_not_get_hello_message_without_mock_security_header() throws Exception {
        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        // Act and Assert
        mockMvc.perform(get("/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

//    @Test
    public void should_get_hello_message() throws Exception {

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/hello")
                .header("Authorization", getBearerTestToken(true))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        HelloResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HelloResponse.class);
        assertThat(response.getMessage()).isEqualTo("Hello World!");
    }

//    @Test
    public void should_get_hello_message_with_api_key() throws Exception {
        String apiKey = "d3fe6f0d-120e-4161-a134-8c2342e36ca6";
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/hello")
                .header("RELOADLY-API-KEY", apiKey)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        HelloResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HelloResponse.class);
        assertThat(response.getMessage()).isEqualTo("Hello World!");
    }

    //    @Test
    public void should_not_get_hello_message() throws Exception {

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/hello")
                .header("Authorization", getBearerTestToken(false))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private String getBearerTestToken(boolean validToken) {
        // This should be an expired token
        String badIdToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFkZTgwNjdhODI5OGE0ZTMzNDRiNGRiZGVkMjVmMmZiNGY0MGYzY2UiLCJ0eXAiOiJKV1QifQ.eyJhcHBsaWNhdGlvbl9yb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9tZWVsdS1hcHAiLCJhdWQiOiJtZWVsdS1hcHAiLCJhdXRoX3RpbWUiOjE2MTg1OTg5ODQsInVzZXJfaWQiOiJTdWhVSkhRRTF6ZlNWR3NoZHZyTmx2T2Y3NHoxIiwic3ViIjoiU3VoVUpIUUUxemZTVkdzaGR2ck5sdk9mNzR6MSIsImlhdCI6MTYxODU5ODk4NCwiZXhwIjoxNjE4NjAyNTg0LCJwaG9uZV9udW1iZXIiOiIrOTE5OTk5OTk5OTk5IiwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJwaG9uZSI6WyIrOTE5OTk5OTk5OTk5Il19LCJzaWduX2luX3Byb3ZpZGVyIjoicGhvbmUifX0.V_j60NTs2Z5MPJyJavGTQI_5WXi3QmLAbOu5ESJ0J9zURh5rg6--Y6r6-EuhVHIUkq88RnfX9st66DTf_Nioe8ODGRR0f4h0Fg8OFj2WRH7JRsJ3nBzPqyIsDg2tfbWAvXPh8QgZXdIXk1-71idp37ZJ-YAcp4OTiOFDj_DR95p49hp9i9FqdTJvoawpt7CWYNs56ogYc2Yk5sa9d0UmrdKc8dPykrFE2QCG3fPwKTqZPQzjJjqOuCd_AZKclO5ig6LOvrTmSCL7lAOQ6tvWmn3CbBLKS7ip3XDYU8dsu4YcphPA0JqqfXcP7e3EKck9JyGxnJjPLWy9178lqJUewA";
        // This should be a valid token
        String goodIdToken = "eyJhbGciOiJIUzM4NCJ9.eyJhdWQiOiJyZWxvYWRseS1wbGF0Zm9ybSIsInVpZCI6ImMxZmU2ZjBkLTQyMGUtNDE2MS1hMTM0LTljMjM0MmUzNmM5NSIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwiaXNzIjoiUmVsb2FkbHkgQXV0aGVudGljYXRpb24gU2VydmljZSIsInN1YiI6ImMxZmU2ZjBkLTQyMGUtNDE2MS1hMTM0LTljMjM0MmUzNmM5NSIsImlhdCI6MTYxOTM3OTc1NCwiZXhwIjoxNjE5Mzk3NzU0fQ.fTaacvBXDbWYptegdC9V8dWXsdg1t-XojiW3An3esb3O94qay55okM7d0kQVYQfF";
        return "Bearer ".concat(validToken ? goodIdToken : badIdToken);
    }
}

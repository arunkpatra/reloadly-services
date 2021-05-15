/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.security.controller;

import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.AbstractIntegrationTest;
import com.reloadly.security.model.HelloResponse;
import com.reloadly.security.service.ReloadlyAuth;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloControllerTests extends AbstractIntegrationTest {

    @Autowired
    private ReloadlyAuth reloadlyAuth;

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
    public void should_not_get_hello_message_without_any_security_header() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void should_not_get_hello_message() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        ReloadlyAuthToken mockAuthToken = new ReloadlyAuthToken(claims);
        Mockito.when(reloadlyAuth.verifyToken(getBearerTestToken(false))).thenReturn(mockAuthToken);

        // Setup and Act
        mockMvc.perform(get("/hello")
                .header("Authorization", getBearerTestToken(false).substring(7))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void should_get_hello_message_with_api_key() throws Exception {
        String apiKey = "d3fe6f0d-120e-4161-a134-8c2342e36ca6";
        ReloadlyApiKeyIdentity mockIdentity = new ReloadlyApiKeyIdentity();
        mockIdentity.setUid("test-uid");
        mockIdentity.setRoles(Collections.singletonList("ROLE_USER"));

        Mockito.when(reloadlyAuth.verifyApiKey(eq(apiKey), eq("test-client-id"))).thenReturn(mockIdentity);

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/hello")
                .header("RELOADLY-API-KEY", apiKey)
                .header("RELOADLY-CLIENT-ID", "test-client-id")
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
    public void should_get_hello_message_with_jwt_token() throws Exception {
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("sub", uid);
        claims.put("iss", "Reloadly Mock Authentication Service");
        claims.put("aud", "reloadly-platform");
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        claims.put("roles", roles);
        claims.put("claims", claims);

        ReloadlyAuthToken mockAuthToken = new ReloadlyAuthToken(claims);
        Mockito.when(reloadlyAuth.verifyToken(eq(getBearerTestToken(true).substring(7)))).thenReturn(mockAuthToken);

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

    private String getBearerTestToken(boolean validToken) {
        // This should be an expired token
        String badIdToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFkZTgwNjdhODI5OGE0ZTMzNDRiNGRiZGVkMjVmMmZiNGY0MGYzY2UiLCJ0eXAiOiJKV1QifQ.eyJhcHBsaWNhdGlvbl9yb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9tZWVsdS1hcHAiLCJhdWQiOiJtZWVsdS1hcHAiLCJhdXRoX3RpbWUiOjE2MTg1OTg5ODQsInVzZXJfaWQiOiJTdWhVSkhRRTF6ZlNWR3NoZHZyTmx2T2Y3NHoxIiwic3ViIjoiU3VoVUpIUUUxemZTVkdzaGR2ck5sdk9mNzR6MSIsImlhdCI6MTYxODU5ODk4NCwiZXhwIjoxNjE4NjAyNTg0LCJwaG9uZV9udW1iZXIiOiIrOTE5OTk5OTk5OTk5IiwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJwaG9uZSI6WyIrOTE5OTk5OTk5OTk5Il19LCJzaWduX2luX3Byb3ZpZGVyIjoicGhvbmUifX0.V_j60NTs2Z5MPJyJavGTQI_5WXi3QmLAbOu5ESJ0J9zURh5rg6--Y6r6-EuhVHIUkq88RnfX9st66DTf_Nioe8ODGRR0f4h0Fg8OFj2WRH7JRsJ3nBzPqyIsDg2tfbWAvXPh8QgZXdIXk1-71idp37ZJ-YAcp4OTiOFDj_DR95p49hp9i9FqdTJvoawpt7CWYNs56ogYc2Yk5sa9d0UmrdKc8dPykrFE2QCG3fPwKTqZPQzjJjqOuCd_AZKclO5ig6LOvrTmSCL7lAOQ6tvWmn3CbBLKS7ip3XDYU8dsu4YcphPA0JqqfXcP7e3EKck9JyGxnJjPLWy9178lqJUewA";
        // This should be a valid token
        String goodIdToken = "eyJhbGciOiJIUzM4NCJ9.eyJhdWQiOiJyZWxvYWRseS1wbGF0Zm9ybSIsInVpZCI6ImMxZmU2ZjBkLTQyMGUtNDE2MS1hMTM0LTljMjM0MmUzNmM5NSIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwiaXNzIjoiUmVsb2FkbHkgQXV0aGVudGljYXRpb24gU2VydmljZSIsInN1YiI6ImMxZmU2ZjBkLTQyMGUtNDE2MS1hMTM0LTljMjM0MmUzNmM5NSIsImlhdCI6MTYxOTM3OTc1NCwiZXhwIjoxNjE5Mzk3NzU0fQ.fTaacvBXDbWYptegdC9V8dWXsdg1t-XojiW3An3esb3O94qay55okM7d0kQVYQfF";
        return "Bearer ".concat(validToken ? goodIdToken : badIdToken);
    }
}

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

package com.reloadly.security.service;

import com.reloadly.security.AbstractIntegrationTest;
import com.reloadly.security.model.ReloadlyUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class ReloadlyUserDetailsServiceTests extends AbstractIntegrationTest {

    @Test
    public void should_get_user_details() {
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        ReloadlyUserDetails mockUserDetails = new ReloadlyUserDetails();
        mockUserDetails.setUid("test-uid");
        mockUserDetails.setIssuer("test-iss");
        mockUserDetails.setAudience("test-aud");
        mockUserDetails.setCredentialsNonExpired(true);
        mockUserDetails.setEnabled(true);

        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(mockUserDetails);

        UserDetails userDetails = userDetailsService.loadUserByUsername("test");

        ReloadlyUserDetails reloadlyUserDetails = (ReloadlyUserDetails) userDetails;

        assertThat(reloadlyUserDetails.getUid()).isEqualTo("test-uid");
        assertThat(reloadlyUserDetails.getAudience()).isEqualTo("test-aud");
        assertThat(reloadlyUserDetails.getIssuer()).isEqualTo("test-iss");
        assertThat(reloadlyUserDetails.getPassword()).isEqualTo("[REDACTED]");
        assertThat(reloadlyUserDetails.isAccountNonExpired()).isFalse();
        assertThat(reloadlyUserDetails.isAccountNonLocked()).isTrue();
        assertThat(reloadlyUserDetails.isCredentialsNonExpired()).isTrue();
        assertThat(reloadlyUserDetails.isEnabled()).isTrue();
        assertThat(reloadlyUserDetails.getClaims()).isEmpty();

    }
}

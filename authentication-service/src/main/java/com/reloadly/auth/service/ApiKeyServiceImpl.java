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

package com.reloadly.auth.service;

import com.reloadly.auth.entity.ApiKeyEntity;
import com.reloadly.auth.entity.ClientEntity;
import com.reloadly.auth.exception.ApiKeyException;
import com.reloadly.auth.model.ApiKeyCreationRequest;
import com.reloadly.auth.model.ApiKeyCreationResponse;
import com.reloadly.auth.repository.ApiKeyRepository;
import com.reloadly.auth.repository.ClientRepository;
import com.reloadly.commons.model.user.UserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyServiceImpl  implements ApiKeyService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(UserService userService,
                             PasswordEncoder passwordEncoder,
                             ClientRepository clientRepository,
                             ApiKeyRepository apiKeyRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * Create a new API Key.
     * <p>
     * APi Keys can only be create by human users who have signed in using a standard process like a username
     * password based method. A client ID will be created if it does not exist.
     * </p>
     *
     * @param headers The HTTP headers containing authentication information.
     * @return ApiKeyCreationResponse The APi key creation response.
     * @throws ApiKeyException If an error occurs.
     */
    @Override
    @Transactional
    public ApiKeyCreationResponse createApiKey(HttpHeaders headers, ApiKeyCreationRequest request) throws ApiKeyException {

        try {
            UserInfo userInfo = userService.getUserInfo(headers);

            String uid = userInfo.getUid();
            Optional<ClientEntity> clientEntity = clientRepository.findByUid(uid);
            ClientEntity ce;
            if (clientEntity.isPresent()) {
                ce = clientEntity.get();
            } else {
                ce = new ClientEntity();
                ce.setClientId(UUID.randomUUID().toString());
                ce.setUid(uid);
                clientRepository.save(ce);
            }

            ApiKeyEntity ake = new ApiKeyEntity();
            ake.setClientId(ce.getClientId());
            String rawApiKey = UUID.randomUUID().toString();
            ake.setApiKey(passwordEncoder.encode(rawApiKey));
            ake.setApiKeyDescription(request.getDescription());
            ake.setActive(true);
            ake = apiKeyRepository.save(ake);
            // The only time, the un-encrypted key is handed over to the user.
            return new ApiKeyCreationResponse(rawApiKey, ce.getClientId(), ake.getApiKeyDescription());
        } catch (Exception e) {
            throw new ApiKeyException("An error occurred. Root cause: " + e.getMessage(), e);
        }
    }
}

package com.reloadly.security.auth.service;

import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.exception.ReloadlyAuthException;

public interface ReloadlyAuth {

    ReloadlyAuthToken verifyToken(String token) throws ReloadlyAuthException;
}

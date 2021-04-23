package com.reloadly.auth.service;

import com.reloadly.auth.entity.AuthorityEntity;
import com.reloadly.auth.entity.UserEntity;
import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import com.reloadly.auth.exception.InvalidPasswordFormatException;
import com.reloadly.auth.exception.UsernameAlreadyTakenException;
import com.reloadly.auth.repository.AuthorityRepository;
import com.reloadly.auth.repository.UserRepository;
import com.reloadly.auth.repository.UsernamePasswordCredentialsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.usernamePasswordCredentialsRepository = usernamePasswordCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Create a new user. A default authority named ROLE_USER is added for every newly created user.
     *
     * @param username The username
     * @param password The password. It will be stored in an encrypted format.
     * @return The UID. Its a unique identifier across the authentication system.
     * @throws UsernameAlreadyTakenException  If the username exists already.
     * @throws InvalidPasswordFormatException If the password format is not valid. A rudimentary 8 character
     *                                        check is enforced.
     */
    @Override
    @Transactional
    public String createUserForUsernamePassword(String username, String password) throws UsernameAlreadyTakenException,
            InvalidPasswordFormatException {

        Assert.notNull(username, "Username can not be null");
        Assert.notNull(password, "Password can not be null");

        if (password.length() < 8) {
            throw new InvalidPasswordFormatException();
        }

        UserEntity ue;
        // Create a user first
        try {
            ue = new UserEntity();
            ue.setUid(UUID.randomUUID().toString());
            ue.setActive(true);
            ue = userRepository.save(ue);
        } catch (Exception e) {
            throw new UsernameAlreadyTakenException();
        }

        // Add a ROLE_USER role.
        addUserRole(ue);

        // Now create username password(encrypted) record
        try {
            Assert.notNull(ue, "UserEntity can not be null");
            Assert.notNull(ue.getUid(), "UID can not be null");
            String encodedPassword = passwordEncoder.encode(password);
            UsernamePasswordCredentialsEntity upce = new UsernamePasswordCredentialsEntity();
            upce.setUid(ue.getUid());
            upce.setUsername(username);
            upce.setPassword(encodedPassword);

            usernamePasswordCredentialsRepository.save(upce);
        } catch (Exception e) {
            throw new UsernameAlreadyTakenException(String.format("Failed to create UID. Original exception is %s",
                    e.getMessage()), e);
        }
        return ue.getUid();
    }

    private void addUserRole(UserEntity ue) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority("ROLE_USER");
        authorityEntity.setUid(ue.getUid());

        ue.getAuthorityEntities().add(authorityEntity);

        userRepository.save(ue);
    }
}

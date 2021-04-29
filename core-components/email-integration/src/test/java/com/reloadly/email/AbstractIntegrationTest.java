package com.reloadly.email;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test", "mock-ses-client"})
public abstract class AbstractIntegrationTest {

}

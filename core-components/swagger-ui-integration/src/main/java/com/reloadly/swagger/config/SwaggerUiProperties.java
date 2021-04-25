package com.reloadly.swagger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(SwaggerUiProperties.PREFIX)
public class SwaggerUiProperties {

    public static final String PREFIX = "reloadly.integration.swagger";

    /**
     * (Optional) Is Swagger UI enabled?
     */
    private boolean enabled = true;
    /**
     * Do we need to use security headers?
     */
    private boolean secured = false;
    /**
     * The API base URL
     */
    private String baseUrl = "/";
    /**
     * The API Host
     */
    private String host = "localhost:8080";
    /**
     * The base package to scan for REST APIs.
     */
    private String basePackage = "com.reloadly";
    /**
     * The API description.
     */
    private String apiDescription = "API Description";
    /**
     * The API version.
     */
    private String apiVersion = "1.0.0";
    /**
     * The API title.
     */
    private String apiTitle = "API Title";
    /**
     * The API terms of service URL.
     */
    private String apiTermsOfServiceUrl = "https://api.com/termsofservice";
    /**
     * License verbiage.
     */
    private String license = "Copyright api.com";
    /**
     * License URL.
     */
    private String licenseUrl = "https://api.com/license";
    /**
     * The contact properties. See {@link SwaggerContactProperties}
     */
    @NestedConfigurationProperty
    private SwaggerContactProperties contact;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiTitle() {
        return apiTitle;
    }

    public void setApiTitle(String apiTitle) {
        this.apiTitle = apiTitle;
    }

    public String getApiTermsOfServiceUrl() {
        return apiTermsOfServiceUrl;
    }

    public void setApiTermsOfServiceUrl(String apiTermsOfServiceUrl) {
        this.apiTermsOfServiceUrl = apiTermsOfServiceUrl;
    }

    public SwaggerContactProperties getContact() {
        return contact;
    }

    public void setContact(SwaggerContactProperties contact) {
        this.contact = contact;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }
}

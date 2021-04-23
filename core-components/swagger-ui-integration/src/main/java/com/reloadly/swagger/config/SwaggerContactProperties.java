package com.reloadly.swagger.config;

public class SwaggerContactProperties {

    /**
     * The contact name.
     */
    private String name = "Contact Name";
    /**
     * The API home page URL.
     */
    private String url = "https://api.com";
    /**
     * The email contact.
     */
    private String email = "info@api.com";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

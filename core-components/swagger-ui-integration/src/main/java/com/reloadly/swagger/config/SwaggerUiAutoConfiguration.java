package com.reloadly.swagger.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Swagger UI auto-configuration.
 *
 * @author Arun Patra
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SwaggerUiProperties.class)
@ConditionalOnProperty(name = "reloadly.integration.swagger.enabled", matchIfMissing = true)
public class SwaggerUiAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(SwaggerUiProperties.class)
    @ConditionalOnProperty(name = "reloadly.integration.swagger.enabled", matchIfMissing = true)
    public static class SwaggerConfig implements WebMvcConfigurer {

        private final SwaggerUiProperties properties;

        public SwaggerConfig(SwaggerUiProperties properties) {
            this.properties = properties;
        }

        @Bean
        public Docket api() {
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .host(this.properties.getHost())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(this.properties.getBasePackage()))
                    .paths(PathSelectors.any())
                    .build();
            if (properties.isSecured()) {
                docket = docket
                        .securitySchemes(new ArrayList<>(Collections.singletonList(apiKey())))
                        .securityContexts(new ArrayList<>(Collections.singletonList(securityContext())));
            }
            return docket.apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
            return new ApiInfo(
                    this.properties.getApiTitle(),
                    this.properties.getApiDescription(),
                    this.properties.getApiVersion(),
                    this.properties.getApiTermsOfServiceUrl(),
                    new Contact(this.properties.getContact().getName(), this.properties.getContact().getUrl(),
                            this.properties.getContact().getEmail()),
                    this.properties.getLicense(), this.properties.getLicenseUrl(), Collections.emptyList());
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String baseUrl = StringUtils.trimTrailingCharacter(this.properties.getBaseUrl(), '/');
            registry.
                    addResourceHandler(baseUrl + "/swagger-ui/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                    .resourceChain(false);
        }

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController(this.properties.getBaseUrl() + "/swagger-ui/")
                    .setViewName("forward:" + this.properties.getBaseUrl() + "/swagger-ui/index.html");
        }

        private ApiKey apiKey() {
            return new ApiKey("Access Token", "Authorization", "header");
        }

        private SecurityContext securityContext() {
            return SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .forPaths(PathSelectors.regex("/api.*"))
                    .build();
        }

        private List<SecurityReference> defaultAuth() {
            AuthorizationScope authorizationScope
                    = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            return new ArrayList<>(
                    Collections.singletonList(new SecurityReference("Access Token", authorizationScopes)));
        }
    }

}

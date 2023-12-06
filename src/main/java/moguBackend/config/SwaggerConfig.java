package moguBackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger springdoc-ui 구성 파일
 */
@OpenAPIDefinition(
        info = @Info(title = "MoguMogu",
                description = "MoguMogu api명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi group1() {
        return GroupedOpenApi.builder()
                .group("USER")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group2() {
        return GroupedOpenApi.builder()
                .group("ADMIN")
                .pathsToMatch("/admin/**","/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group3() {
        return GroupedOpenApi.builder()
                .group("ALL")
                .pathsToMatch("/**")
                .build();
    }



    @Bean
    public OpenAPI openAPI() {

        io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
                .version("v1.0.0")
                .title("PROJECT MOGUMOGU API Document");

        // SecuritySecheme명
        String jwtSchemeName = "JWT Token";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }




}
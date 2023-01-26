package uz.tm.tashman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.repository.RoleRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableSwagger2
public class TashmanApplication {

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(TashmanApplication.class, args);
    }


    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("uz.tm.tashman")).build();
    }

    public void initializeUserRoles() {
        if (roleRepository.findAll().isEmpty()) {
            ERole[] roles = ERole.values();
            for (int i = 0; i < ERole.values().length; i++) {
                Role role = new Role();
                role.setName(roles[i]);
                roleRepository.save(role);
            }
        }
    }

    @PostConstruct
    public void initializeDatabaseTables() {
        initializeUserRoles();
    }
}
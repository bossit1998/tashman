package uz.tm.tashman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uz.tm.tashman.entity.ProductCategory;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.EProductCategory;
import uz.tm.tashman.repository.ProductCategoryRepository;
import uz.tm.tashman.repository.RoleRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableSwagger2
public class TashmanApplication {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

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

    public void initializeProductCategories() {
        if (productCategoryRepository.findAll().isEmpty()) {
            EProductCategory[] categories = EProductCategory.values();
            for (int i = 0; i < EProductCategory.values().length; i++) {
                ProductCategory category = new ProductCategory();
                category.setSlug(categories[i].code);
                category.setNameEn(categories[i].englishName);
                category.setNameRu(categories[i].russianName);
                category.setNameUz(categories[i].uzbekName);
                category.setCreatedBy(0L);
                category.setCreatedDate(LocalDateTime.now());

                productCategoryRepository.save(category);
            }
        }
    }

    @PostConstruct
    public void initializeDatabaseTables() {
        initializeUserRoles();
        initializeProductCategories();
    }
}
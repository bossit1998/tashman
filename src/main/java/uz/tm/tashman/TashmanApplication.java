package uz.tm.tashman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.repository.RoleRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TashmanApplication {

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(TashmanApplication.class, args);
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
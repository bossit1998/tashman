package uz.tm.tashman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.tm.tashman.dao.RoleRepository;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.enums.ERole;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TashmanApplication {

    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(TashmanApplication.class, args);
    }

    @PostConstruct
    public void initializeDatabaseTables() {
        //initialize user roles
        if (roleRepository.findAll().isEmpty()) {
            ERole[] roles = ERole.values();
            for (int i = 0; i < ERole.values().length; i++) {
                Role role = new Role();
                role.setName(roles[i]);
                roleRepository.save(role);
            }
        }
    }
}

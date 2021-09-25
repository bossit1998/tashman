package uz.tm.tashman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.tm.tashman.dao.RoleRepository;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.enums.ERole;

import javax.annotation.PostConstruct;

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
            for (int i = 0; i < ERole.values().length; i++) {
                Role role = new Role();
                role.setId(ERole.getById(i).getId());
                role.setName(ERole.getById(i).getCode());
                roleRepository.save(role);
            }
        }

    }
}

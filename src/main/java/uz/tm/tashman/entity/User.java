package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.tm.tashman.enums.ERole;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User implements UserDetails {

    private static final long serialVersionUID = -486368026849312703L;
    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<UserAgent> userAgents;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "otp")
    private String otp;
    private String profileImage;
    @Column(name = "pin_code")
    private Integer pinCode;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Client client;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean hasRole(ERole role) {
        if (role != null) {
            List<String> roles = getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

			return roles.contains(role.name());
        }

        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roleAuthorities = new ArrayList<>();
        for (Role role : roles) {
            roleAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return roleAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package uz.tm.tashman.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.Language;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String encodedId;

    @Column(nullable = false, unique = true)
    private String username;

    private String maskedPhoneNumber;

    private String password;

    private LocalDateTime createdDate;

    private String profileImageUrl;

    private String otpForDeletion;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    private Long deletedBy;
    private String blockMessage;

    @Column(columnDefinition = "boolean default false")
    private Boolean isBlocked=false;

    private LocalDateTime deletedDate;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive = true;

    private String pinCode;
    private Integer pinCodeTrials = 0;

    @Enumerated(EnumType.STRING)
    private Language language = Language.RU;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserAgent> userAgents;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Address> address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Client client;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Admin admin;

    public User(String username, String password, String maskedPhoneNumber) {
        this.username = username;
        this.password = password;
        this.maskedPhoneNumber = maskedPhoneNumber;
    }

    public boolean hasRole(ERole role) {
        if (role != null) {
            List<String> roles = getAuthorities().stream().map(item -> item.getAuthority())
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
    public String getUsername() {
        return username;
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
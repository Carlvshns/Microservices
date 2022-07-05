package devdojo.academy.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationUser implements AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotNull(message = "The field 'username' is mandatory")
    @Column(nullable = false)
    private String username;

    @NotNull(message = "The field 'password' is mandatory")
    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @NotNull(message = "The field 'role' is mandatory")
    @Column(nullable = false)
    private String role = "USER";

    public ApplicationUser(Long id, @NotNull(message = "The field 'username' is mandatory") String username,
            @NotNull(message = "The field 'password' is mandatory") String password,
            @NotNull(message = "The field 'role' is mandatory") String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ApplicationUser(@NotNull ApplicationUser applicationUser) {
        this.id = applicationUser.getId();
        this.username = applicationUser.getUsername();
        this.password = applicationUser.getPassword();
        this.role = applicationUser.getRole();
    }

    public ApplicationUser() {
    }

    @Override
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }   
}


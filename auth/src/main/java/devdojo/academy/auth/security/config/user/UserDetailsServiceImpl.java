package devdojo.academy.auth.security.config.user;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import devdojo.academy.core.model.ApplicationUser;
import devdojo.academy.core.repository.ApplicationUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final ApplicationUserRepository applicationUserRepository;

    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository){
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("Searching in the DB the user by username {"+username+"}"); //Or log.info
        
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        System.out.println("Application found {"+applicationUser+"}"); //Or log.info

        if(applicationUser == null){
            throw new UsernameNotFoundException(String.format("Application user '%s not found", username));
        }
        return new CustomUserDetails(applicationUser);
    }

    private static final class CustomUserDetails extends ApplicationUser implements UserDetails {

        public CustomUserDetails(@NotNull ApplicationUser applicationUser) {
            super(applicationUser);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+this.getRole());
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
}

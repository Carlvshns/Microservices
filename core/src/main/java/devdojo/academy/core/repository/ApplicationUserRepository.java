package devdojo.academy.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import devdojo.academy.core.model.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long>{
    
    public ApplicationUser findByUsername(String username);
}

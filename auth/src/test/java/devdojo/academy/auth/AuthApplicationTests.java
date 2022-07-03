package devdojo.academy.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class AuthApplicationTests {
    
    @Test
	void contextLoads() {
	}

    @Test
    public void test(){
        System.out.println(new BCryptPasswordEncoder().encode("devdojo"));
    }
}

/**********************************
*              @2023              *
**********************************/

// GENERAL TODO's
// Email verification.
// Rename project.
// Proper context configuration piece by piece (Instead of using auto configure everywhere).
// Local Database configuration. (Done)
// Dev environment.
// Dev Database configuration.
// Set session timeout. (Done)

// (Rename Project) TODO
// Rename 'Index.html' to 'home.html'.
// Rename 'CreateUser.html' to 'signup.html'
// Rename 'Login.html' to 'signin.html'
// Rename 'Failure.html' to 'servererror.html'

package simplelogin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "simplelogin" })
public class Start {


	public static void main(String[] args) {
		SpringApplication.run(Start.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}
}

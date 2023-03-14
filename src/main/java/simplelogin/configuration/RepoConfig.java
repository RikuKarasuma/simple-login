/**********************************
*              @2023              *
**********************************/
package simplelogin.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("simplelogin.repository")
public class RepoConfig {}

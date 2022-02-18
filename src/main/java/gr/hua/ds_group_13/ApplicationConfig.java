package gr.hua.ds_group_13;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedirectHandler redirectHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register**").permitAll()
                .antMatchers("/").permitAll() // This will be your home screen URL
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/professor/**").hasRole("PROFESSOR")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(redirectHandler)
                .usernameParameter("email")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true).permitAll();
    }
}

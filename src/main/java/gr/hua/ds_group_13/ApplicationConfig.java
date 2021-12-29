package gr.hua.ds_group_13;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class ApplicationConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http)throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register**").permitAll()
                .antMatchers("/").permitAll() // This will be your home screen URL
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/dashboard")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout() .invalidateHttpSession(true)
                .clearAuthentication(true) .permitAll();
    }
}

package com.codebrewers.server.config;

import com.codebrewers.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
//
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/auth/**"
                ).permitAll()
                .anyRequest().authenticated().and().httpBasic();
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .logout().permitAll()
//                .and()
//                .exceptionHandling().accessDeniedPage("/403")
        ;
    }
}

// Roles - "COLLEGE_ADMIN", "COLLEGE_SPOC", "COMPANY_ADMIN", "COMPANY_SPOC"

//                .antMatchers(HttpMethod.GET,"/api/**").hasAnyAuthority("SUPER_ADMIN")
//                .antMatchers("/new").hasAnyAuthority("ADMIN", "CREATOR")
//                .antMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
//                .antMatchers("/delete/**").hasAuthority("ADMIN")

/*
 * COLLEGE_ADMIN
 * (Auth) /api/user/** GET, POST, PUT, DELETE
 * /api/colleges GET, POST, PUT, DELETE
 * /api/companies GET
 * /api/jobposts GET,
 * /api/location GET
 * /api/qualifications GET
 * /api/skillsets GET
 * /api/create/user/** GET, POST, PUT, DELETE
 * /api/apply/jobposts GET, POST, DELETE
 */

/*
 * COMPANY_ADMIN
 * (Auth) /api/user/** GET, POST, PUT, DELETE
 * /api/companies GET, POST, PUT, DELETE
 * /api/colleges GET
 * /api/jobposts GET, POST, PUT, DELETE
 * /api/location GET
 * /api/qualifications GET
 * /api/skillsets GET
 * /api/create/user/** GET, POST, PUT, DELETE
 */

/*
* COLLEGE_SPOC
* (Auth) /api/user/** GET, POST, PUT, DELETE
* /api/colleges GET,
* /api/companies GET
* /api/jobposts GET,
* /api/location GET
* /api/qualifications GET
* /api/skillsets GET
* /api/apply/jobposts GET, POST, DELETE
 */

/*
* COMPANY_SPOC
* (Auth) /api/user/** GET, POST, PUT, DELETE
* /api/colleges GET
* /api/companies GET
* /api/jobposts GET, POST, PUT, DELETE
* /api/location GET,
* /api/qualifications GET,
* /api/skillsets GET
*/






























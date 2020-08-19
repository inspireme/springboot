package jp.whisper.securitydb.config;

import jp.whisper.securitydb.service.UserSercice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author whisper
 * @Date 2020/08/19 15:54
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserSercice userSercice;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSercice);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/dba/**").hasRole("dba")
                .antMatchers("/user/**").hasRole("user")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_dba > ROLE_admin \n ROLE_admin > ROLE_user"; //改行で区切る
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}

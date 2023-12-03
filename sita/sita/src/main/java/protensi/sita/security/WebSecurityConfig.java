package protensi.sita.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.model.EnumRole;
import protensi.sita.model.UserModel;

import java.util.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // to encode the password
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // to integrate with spring data jpa hibernate
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    // to pass authetication method to the config method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Set<EnumRole> koordinator = new HashSet<>();
        // koordinator.add(EnumRole.KOORDINATOR);
        // Set<EnumRole> dosenboth1 = new HashSet<>();
        // koordinator.add(EnumRole.PEMBIMBING);
        // koordinator.add(EnumRole.PENGUJI);
        // Set<EnumRole> dosenboth2 = new HashSet<>();
        // koordinator.add(EnumRole.PENGUJI);
        // koordinator.add(EnumRole.PEMBIMBING);
        http.csrf().disable()
                .authorizeRequests().antMatchers(
                "/js/**",
                "/css/**",
                "/logo/**",
                "/images/**",
                "/create-dummy").permitAll()
                .antMatchers("/ugb/viewall").hasAnyAuthority("ROLE_[KOORDINATOR]","ROLE_[PEMBIMBING, PENGUJI]","ROLE_[PENGUJI, PEMBIMBING]")
                .antMatchers("/ugb/approve/**").hasAnyAuthority("ROLE_[KOORDINATOR]","ROLE_[PEMBIMBING, PENGUJI]","ROLE_[PENGUJI, PEMBIMBING]")
                .antMatchers("/ugb/deny/**").hasAnyAuthority("ROLE_[KOORDINATOR]","ROLE_[PEMBIMBING, PENGUJI]","ROLE_[PENGUJI, PEMBIMBING]")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();

    }

    // .csrf().disable()
    // .authorizeRequests()
    // .antMatchers("/css/**").permitAll().antMatchers("/js/**").permitAll()
    // .antMatchers("/api/**").permitAll()
    // .antMatchers("/create-dummy").permitAll()
    // // .antMatchers("/obat/viewall").hasAnyRole( "ADMIN", "APOTEKER")
    // // .antMatchers("/resep/viewall").hasAnyRole( "ADMIN", "APOTEKER")
    // // .antMatchers("/appointment/viewall").not().hasRole("APOTEKER")
    // // .antMatchers("/obat/update").hasRole("APOTEKER")
    // // .antMatchers("/appointment/viewall").not().hasRole("APOTEKER")
    // // .antMatchers("/mahasiswa/**").hasRole("ADMIN")
    // // .antMatchers("/pasien/**").hasRole("ADMIN")
    // // .antMatchers("/dokter/**").hasRole("ADMIN")
    // // .antMatchers("/whitelist/**").hasRole("ADMIN")
    // .anyRequest().authenticated()
    // .and()
    // .formLogin()
    // .loginPage("/login").permitAll()
    // .and()
    // .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    // .logoutSuccessUrl("/login?logout").permitAll()
    // .permitAll();
}

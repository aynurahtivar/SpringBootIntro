package com.tpe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//security katmanini kendimiz configure ediyoruz
@Configuration  // bu class configurasyon classi
@EnableWebSecurity  // web securityi aktif et
@EnableGlobalMethodSecurity(prePostEnabled = true)//metod bazli yetkilendirmeye izin vermek için
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // parametre olarak HttpSecurity objesi aliyor,
        // biz burada security katmani olusturuyoruz ancak hangi kisimlara uyelik ,yetkilendirme gereksin vs diyoruz
        http.csrf().disable().//restAPI:session yok  //csrf() : saldirma yontemi
                // ornek: internet bankaciligina girildiginde yan sekmede farkli bir site acik,
                // reklam amacli vs gelen linke tiklandiginda diger sekmede acik olan banka bilgilerine ulasabiliyorlar
                // spring securityde otomatik geliyor, restApi olusturdugumuz icin iptal ettik cunku session yok
                authorizeHttpRequests().
                antMatchers("/", "index.html", "/css/*", "/images/*", "/register", "/login").permitAll()
                .anyRequest()   //bunun disindaki her rrequesti authenticate et
                .authenticated().and()
                .httpBasic(); //basic auth
    }

    @Bean   //PasswordEncoder'in 1 tane objesini olusturup ihtiyac olan her yerde kullan
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);//zorluk seviyesi 4-34
            // passwordun sifrelenerek DB'ye kaydedilmesini saglar, baska yontemler de var
        // DaoAuthenticationProvider'e verip kullanicaz
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Override//auth manager configurasyonu
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());

    }
}

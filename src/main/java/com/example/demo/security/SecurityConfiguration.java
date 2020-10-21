package com.example.demo.security;

//import com.example.wl.datamodel.dao.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许prauthorize
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LibraryUserDetailsService libraryUserDetailsService;

    @Autowired
    private AjaxAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AjaxLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private MyAuthenctiationSuccessHandler myAuthenctiationSuccessHandler;

    @Autowired
    private MyAuthenctiationFailureHandler myAuthenctiationFailureHandler;

    @Autowired
    private AjaxAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);

        http.authorizeRequests()
                  .antMatchers("/admin/discussNum","/admin/registerNum","/admin/userList","/admin/discussList").hasAnyRole("JUNIOR","SENIOR")
                .antMatchers("/admin/state","/audit/list","/audit/process").hasAnyRole("SENIOR")
                .antMatchers("/audit/one").hasAnyRole("JUNIOR")
                .antMatchers(HttpMethod.POST,"/comment/one","/discuss/one","/reply/one").hasAnyRole("FREE")//被禁言了就不可以
                .antMatchers("/user/**").permitAll()//无需认证，随意访问
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()//其他的那些只要登录认证就可以。
        ;

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
        ;
        http.formLogin()
                .successHandler(myAuthenctiationSuccessHandler)//要用这个才可以返回详细信息（或者？可以把详细信息提取出来再包装）
                .failureHandler(myAuthenctiationFailureHandler)
                .permitAll();
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
        ;

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http.cors().and().csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(libraryUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}

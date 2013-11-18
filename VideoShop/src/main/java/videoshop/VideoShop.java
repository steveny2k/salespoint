package videoshop;

import java.util.List;

import org.salespointframework.Salespoint;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.salespointframework.spring.converter.JpaEntityConverter;
import org.salespointframework.spring.converter.StringToRoleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import videoshop.VideoShop.WebConfiguration;
import videoshop.VideoShop.WebSecurityConfiguration;

@Configuration
@EnableAutoConfiguration
@Import({ Salespoint.class, WebConfiguration.class, WebSecurityConfiguration.class })
@ComponentScan
public class VideoShop {

	public static void main(String[] args) {
		SpringApplication.run(VideoShop.class, args);
	}

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Configuration
	static class WebConfiguration extends WebMvcConfigurerAdapter {

		@Autowired JpaEntityConverter entityConverter;
		@Autowired UserAccountManager userAccountManager;
		@Autowired List<HandlerMethodArgumentResolver> argumentResolvers;

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.addAll(this.argumentResolvers);
		}

		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(entityConverter);
			registry.addConverter(new StringToRoleConverter());
		}
	}

	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired AuthenticationProvider authenticationProvider;

		@Override
		protected void registerAuthentication(AuthenticationManagerBuilder amBuilder) {
			amBuilder.authenticationProvider(authenticationProvider);
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().formLogin().loginProcessingUrl("/login").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}

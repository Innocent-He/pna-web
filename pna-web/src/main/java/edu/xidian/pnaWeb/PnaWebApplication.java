package edu.xidian.pnaWeb;

import edu.xidian.pnaWeb.petri.alg.PetriNetAlg;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class PnaWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(PnaWebApplication.class, args);
	}
}

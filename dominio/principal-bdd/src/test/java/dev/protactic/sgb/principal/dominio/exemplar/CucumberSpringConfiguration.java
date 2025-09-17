package dev.protactic.sgb.principal.dominio.exemplar;

import dev.protactic.sgb.principal.ProTacticApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = ProTacticApplication.class)
public class CucumberSpringConfiguration {
}

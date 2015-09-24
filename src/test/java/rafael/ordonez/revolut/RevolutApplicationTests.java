package rafael.ordonez.revolut;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
public class RevolutApplicationTests {

}

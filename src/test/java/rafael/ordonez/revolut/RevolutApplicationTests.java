package rafael.ordonez.revolut;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
public class RevolutApplicationTests {

}

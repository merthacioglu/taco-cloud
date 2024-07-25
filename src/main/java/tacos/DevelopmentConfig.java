package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

  @Bean
  public CommandLineRunner dataLoader(IngredientRepository repo,
                                      UserRepository userRepo,
                                      TacoRepository tacoRepo,
                                      OrderRepository orderRepo,
                                      PasswordEncoder encoder) { // user repo for ease of testing with a built-in user
    return args -> {
      repo.deleteAll();
      userRepo.deleteAll();
      tacoRepo.deleteAll();
      orderRepo.deleteAll();

      Ingredient flto = new Ingredient("FLTO", "Flour Tortilla", Type.WRAP);
      repo.save(flto);
      Ingredient coto = new Ingredient("COTO", "Corn Tortilla", Type.WRAP);
      repo.save(coto);
      Ingredient gbrf = new Ingredient("GRBF", "Ground Beef", Type.PROTEIN);
      repo.save(gbrf);
      Ingredient carn = new Ingredient("CARN", "Carnitas", Type.PROTEIN);
      repo.save(carn);
      Ingredient tmto = new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
      repo.save(tmto);
      Ingredient letc = new Ingredient("LETC", "Lettuce", Type.VEGGIES);
      repo.save(letc);
      Ingredient ched = new Ingredient("CHED", "Cheddar", Type.CHEESE);
      repo.save(ched);
      Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Type.CHEESE);
      repo.save(jack);
      Ingredient slsa = new Ingredient("SLSA", "Salsa", Type.SAUCE);
      repo.save(slsa);
      Ingredient srcr = new Ingredient("SRCR", "Sour Cream", Type.SAUCE);
      repo.save(srcr);

      Taco taco1 = new Taco();
      taco1.setName("taco1");
      taco1.setIngredients(Arrays.asList(flto, coto));
   ;
      Taco taco2 = new Taco();
      taco2.setName("taco2");
      taco2.setIngredients(List.of(coto, carn, ched, srcr, letc));
      tacoRepo.save(taco1);
      tacoRepo.save(taco2);


        User mert = new User("habuma", encoder.encode("password"),
                "Mert Hacioglu", "123 North Street", "Cross Roads", "TX",
                "76227", "123-123-1234");
        userRepo.save(mert);


        TacoOrder order1 = new TacoOrder();
        order1.setDeliveryName("Mert Hacioglu");
        order1.setUser(mert);
        order1.setDeliveryCity("Austin");
        order1.setDeliveryState("TX");
        order1.setDeliveryZip("76227");
        order1.setDeliveryStreet("123 North Street");
        order1.setTacos(List.of(taco1, taco2));
        order1.setCcCVV("554");
        order1.setCcExpiration("09/25");
        order1.setCcNumber("4539148803436467");
        orderRepo.save(order1);

    };
  }
  
}

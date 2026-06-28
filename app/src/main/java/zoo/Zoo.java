package zoo;

import static java.util.logging.Level.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import zoo.Enclosure.Enclosure;
import zoo.animal.Animal;
import zoo.animal.Mammal;

public class Zoo {
  private final Logger logger;
  private final List<Enclosure<? extends Animal>> enclosures;

  public Zoo() {
    this.logger = Logger.getLogger(Zoo.class.getName());
    logger.log(INFO, "params()");
    this.enclosures = new ArrayList<>();
  }

  // Why return: represents the result of adding the item to the enclosure
  public boolean addEnclosure(Enclosure<? extends Animal> enclosure) {
    logger.log(INFO, "params({0})", enclosure);
    if (enclosure == null) {
      logger.log(SEVERE, "Attempted to add null enclosure!");
      return false;
    }
    boolean added = enclosures.add(enclosure);
    logger.log(FINE, "Zoo now has {0} enclosures", enclosures.size());
    return added;
  }

  // Why return: Gives all the Enclosures
  public List<Enclosure<? extends Animal>> getEnclosures() {
    logger.log(INFO, "params()");
    logger.log(FINE, "Zoo has {0} enclosures", enclosures.size());
    return List.copyOf(enclosures);
  }

  // Why return: Optional avoids null and forces caller to handle "not found"
  public Optional<Enclosure<? extends Animal>> findEnclosureByName(String name) {
    logger.log(INFO, "params({0})", name);
    // 1. We use stream to go through all enclosures
    // 2. We check if the enclosure fits the provided name, else we return null
    Optional<Enclosure<? extends Animal>> result =
        this.enclosures.stream().filter(e -> e.getName().equals(name)).findFirst();
    if (result.isEmpty()) {
      logger.log(WARNING, "Enclosure {0} not found", new Object[] {name});
    } else {
      logger.log(
          FINE,
          "Found enclosure {0} with {1} animals",
          new Object[] {name, result.get().getInhabitants().size()});
    }
    return result;
  }

  // why return: it is a list of animals we want
  public List<? extends Animal> getAllAnimals() {
    logger.log(INFO, "params()");
    // 1. We use stream to go through all enclosures and get all inhabitants
    // 2. Use flatMap to turn all the Animal Lists into a single one
    // 3. We then collect the result as a List and return
    List<? extends Animal> animals =
        this.enclosures.stream().flatMap(e -> e.getInhabitants().stream()).toList();
    logger.log(FINE, "Found {0} animals", animals.size());
    return animals;
  }

  //  why return: it is a list of Mammals instead of all animals
  public List<Mammal> getAllMammals() {
    logger.log(INFO, "params()");
    // 1. We use stream to go through all enclosures
    // 2. Use flatMap to turn all the Animal Lists into a single one
    // 3. We check for Mammal-types in the List
    // 4. We get the Mammals and put them into a list of m
    List<Mammal> mammals =
        this.enclosures.stream()
            .flatMap(e -> e.getInhabitants().stream())
            .filter(Mammal.class::isInstance)
            .map(Mammal.class::cast)
            .toList();

    logger.log(FINE, "Found {0} mammals", mammals.size());
    return mammals;
  }

  // why return: gives a flexible List of animals
  public List<? extends Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
    logger.log(INFO, "params({0})", predicate);
    // 1. We use stream to go through all enclosures
    // 2. Use flatMap to turn all the Animal Lists into a single one
    // 3. We filter vor the chosen Predicate
    // 4. collect the matches and return
    List<? extends Animal> animals =
        this.enclosures.stream()
            .flatMap(e -> e.getInhabitants().stream())
            .filter(predicate)
            .toList();
    logger.log(FINE, "Predicate matched {0} animals", animals.size());
    return animals;
  }

  // why return: The map contains the animal type and their count
  public Map<Class<? extends Animal>, Long> countAnimalsByType() {
    logger.log(INFO, "params()");
    // 1. We use stream to go through all enclosures
    // 2. We put all the inhabitants into a single list
    // 3. we get the classes and the count
    Map<Class<? extends Animal>, Long> counts =
        this.enclosures.stream()
            .flatMap(e -> e.getInhabitants().stream())
            .collect(Collectors.groupingBy(Animal::getClass, Collectors.counting()));
    logger.log(FINE, "Found {0} animal types", counts.size());
    return counts;
  }

  // why return: We want the overcrowded Enclosures so we return a list of Enclosures
  public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int max) {
    logger.log(INFO, "params({0})", max);
    // 1. We use stream to go through all enclosures
    // 2. We filter for all enclosures where the inhabitants-count is over the provided max
    // 3. We collect the matches
    List<Enclosure<? extends Animal>> overcrowded =
        this.enclosures.stream().filter(e -> e.getInhabitants().size() > max).toList();
    logger.log(FINE, "Found {0} overcrowded enclosures", overcrowded.size());
    return overcrowded;
  }

  // We want a String which can be printed
  public String summary() {
    logger.log(INFO, "params()");
    long enclosureCount = this.enclosures.size();
    Map<Class<? extends Animal>, Long> counts = countAnimalsByType();
    long animalCount = counts.values().stream().mapToLong(Long::longValue).sum();
    // 1. we go through the map
    // 2. convert it to a string
    // 3. join the parts
    String animalSummary =
        counts.entrySet().stream()
            .map(e -> e.getValue() + " " + e.getKey().getSimpleName())
            .collect(Collectors.joining(", "));

    logger.log(
        FINE, "Summary: {0} enclosures, {1} animals", new Object[] {enclosureCount, animalCount});
    return String.format(
        "Zoo mit %d Gehegen und %d Tieren: %s", enclosureCount, animalCount, animalSummary);
  }
}

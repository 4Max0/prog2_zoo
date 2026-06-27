package zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import zoo.Enclosure.Enclosure;
import zoo.animal.Animal;
import zoo.animal.Mammal;

public class Zoo {
  private final List<Enclosure<? extends Animal>> enclosures;

  public Zoo() {
    this.enclosures = new ArrayList<>();
  }

  // Why return: represents the result of adding the item to the enclosure
  public boolean addEnclosure(Enclosure<? extends Animal> enclosure) {
    return enclosures.add(enclosure);
  }

  // Why return: Gives all the Enclosures
  public List<Enclosure<? extends Animal>> getEnclosures() {
    return List.copyOf(enclosures);
  }

  // Why return: Optional avoids null and forces caller to handle "not found"
  public Optional<Enclosure<? extends Animal>> findEnclosureByName(String name) {
    return this.enclosures.stream()
        // 1. We use stream to go through all enclosures
        // 2. We check if the enclosure fits the provided name, else we return null
        .filter(e -> e.getName().equals(name))
        .findFirst();
  }

  // why return: it is a list of animals we want
  public List<? extends Animal> getAllAnimals() {
    // 1. We use stream to go through all enclosures and get all inhabitants
    // 2. Use flatMap to turn all the Animal Lists into a single one
    // 3. We then collect the result as a List and return
    return this.enclosures.stream().flatMap(e -> e.getInhabitants().stream()).toList();
  }

  //  why return: it is a list of Mammals instead of all animals
  public List<Mammal> getAllMammals() {
    // 1. We use stream to go through all enclosures
    // 2. Use flatMap to turn all the Animal Lists into a single one
    // 3. We check for Mammal-types in the List
    // 4. We get the Mammals and put them into a list of mammals
    // 5. We collect the Mammals-List
    return this.enclosures.stream()
        .flatMap(e -> e.getInhabitants().stream())
        .filter(Mammal.class::isInstance)
        .map(Mammal.class::cast)
        .toList();
  }

  // why return: gives a flexible List of animals
  public List<? extends Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
    // 1. We use stream to go through all enclosures
    // 2. Use flatMap to turn all the Animal Lists into a single one
    // 3. We filter vor the chosen Predicate
    // 4. collect the matches and return
    return this.enclosures.stream()
        .flatMap(e -> e.getInhabitants().stream())
        .filter(predicate)
        .toList();
  }

  // why return: The map contains the animal type and their count
  public Map<Class<? extends Animal>, Long> countAnimalsByType() {
    // 1. We use stream to go through all enclosures
    // 2. We put all the inhabitants into a single list
    // 3. we get the classes and the count
    return this.enclosures.stream()
        .flatMap(e -> e.getInhabitants().stream())
        .collect(Collectors.groupingBy(Animal::getClass, Collectors.counting()));
  }

  // why return: We want the overcrowded Enclosures so we return a list of Enclosures
  public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int max) {
    // 1. We use stream to go through all enclosures
    // 2. We filter for all enclosures where the inhabitants-count is over the provided max
    // 3. We collect the matches
    return this.enclosures.stream().filter(e -> e.getInhabitants().size() > max).toList();
  }

  // We want a String which can be printed
  public String summary() {
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
    return String.format(
        "Zoo mit %d Gehegen und %d Tieren: %s", enclosureCount, animalCount, animalSummary);
  }
}

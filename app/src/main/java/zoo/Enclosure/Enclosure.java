package zoo.Enclosure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import zoo.animal.Animal;

public class Enclosure<T extends Animal> {
  private final String name;
  // NOTE: Instead of a List I decided to create a HashSet, so that objects get filtered
  // automatically, if they're already inside (and it's theoretically faster, but it doesn't matter)
  private final Set<T> inhabitants;

  public Enclosure(String name) {
    this.name = name;
    this.inhabitants = new HashSet<>();
  }

  public String getName() {
    return this.name;
  }

  public boolean add(T animal) {
    return inhabitants.add(animal);
  }

  public boolean remove(T animal) {
    return inhabitants.remove(animal);
  }

  public List<T> getInhabitants() {
    return List.copyOf(inhabitants);
  }
}

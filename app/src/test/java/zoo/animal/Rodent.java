package zoo.animal;

public sealed interface Rodent extends Mammal permits Rat, Mouse {}

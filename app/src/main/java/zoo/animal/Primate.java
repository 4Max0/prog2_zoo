package zoo.animal;

public sealed interface Primate extends Mammal permits Lemur, Gorilla {}

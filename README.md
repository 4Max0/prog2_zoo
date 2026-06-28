# prog2_zoo
HSBI Prog2 2026 Task 7

Entry Point: `app/src/main/java/zoo/Main.java`
## Aufgabe 3.
### 3.1: Generics
- Wo helfen Ihnen die Generics im Zoo-Szenario, Fehler bereits zur Compile-Zeit zu vermeiden?
  - Durch Generics können wir sicherstellen, dass die Tiere nicht in falsche gehege kommen können
    - Jedes gehege kann nur Tiere von eigenem Typ nehmen und dessen Kinder
      - z.B. kann ein Fisch nicht im Katzenhaus sein
    - Methoden wie Enclosure.add() oder Enclosure.getInhabitants() sind typsicher, d.h. liefern sie automatisch den korrekten Tier-Typ zurück
    - Es werden ClassCastExceptions vermieden, weil Casting zur Laufzeit nicht nötig ist und solche Fehler beim Kompilieren erkannt wird
  - Allgemein: Generics erhöhen die Typsicherheit und machen den Code klarer, weil direkt erkennbar welche Typen in einem Gehege erlaubt sind
- Nennen Sie ein Beispiel aus Ihrer Implementierung, bei dem falsche Tier-Gehege-Kombinationen durch den Typ-Checker verhindert werden.
  - Zoo.getAllMammals(): `.flatMap(e -> e.getInhabitants().stream()).filter(Mammal.class::isInstance).map(Mammal.class::cast)`
    - Die Enclosure selbst hat bereits definiert welche Typen in der Liste sein können
    - Die Enclosure hat bereits den korrekten Obertypen und man muss nur noch filtern
  - Enclosure.add() ist typsicher, durch den Generic, wodurch unter Enclosure<T> nur T objekte hineinkommen können
    - oder z.B. in Enclosure<Fish> können nur Animals von typ Fisch rein
  - Das falsche hinzufügen wurde einen Compile-Time-Error erzeugen
  ```java
    Aquarium aquarium1 = new Aquarium("Aquarium 1");
    aquarium1.add(new Ragdoll("Raggi"));
  ```
### 3.2: Logging
- Warum ist systematisches Logging mit einem Logger und Log-Leveln für ein Zoo-Management-System sinnvoller als Ausgaben mit IO.println?
  - Der Logger ist ein bereits existierendes und complexes system
    - mann muss also nicht selber einen logger implementieren
  - Der Logger enthält eingebaute Level für das logging
    - Der Informationsflow kann dadurch kontrolliert werden nach Belieben
  - Der Logger gibt zusätzliche informationen wie datum und uhrzeit her
  - Der Logger sagt in welcher Funktion und Klasse man sich befindet
  - Man kann das Logging gezielt anpassen
  - Man kann den Output des Loggers direkt speichern
- In welchen Situationen würden Sie in Ihrem System die Log-Level `INFO`, `WARNING` und ggf. `SEVERE` verwenden?
  - `INFO`
 - Allgemeine Status Meldungen
   -  Bei Funktionscalls
   - Eine Operation z.B. wurde erfolgreich abgeschlossen
  - `WARNING`
    - Wenn eine Funktion kein gewolltes Ergebnis finden kann (die nicht kritisch ist) z.B.
      - eine Funktion kein Objekt in einer Liste nicht finden
      - eine Operation nicht abgeschlossen werden kann, weil sonst constraints zerstört werden
 - `SEVERE`
    - Wenn ein Zustand betreten wird, der negative folgen haben kann z.B.
      - Eine Operation mit einem nullptr / null wird versucht
      - Ein Objekt kann nicht erstellt werden
      - Ein anderes Teilsystem nicht erreichbar ist (z.B. Datenbank oder anderer Service)
### 3.3: Streams
Vorteile:
- Streams haben geholfen code Zeilen zu sparen (weniger boilerplate)
- Es konnten for-schleifen weggelassen / ersetzt werden
- Es gibt feste arten gewisse Operationen zu machen z.B.
  - Filtering
  - Sorting
  - Mapping
Nachteile:
- Es kann schnell unübersichtlich werden, vor allem, wenn man 5 oder 6 Operationen machen muss
  - z.B. getAnimalsByPredicate ist auf den ersten Blick verwirrend
- Debugging ist schwerer als bei normalen schleifen

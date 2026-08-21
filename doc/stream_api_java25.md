# Java 25 Stream API & Grundlagen – Dokumentation

Diese Dokumentation bietet eine umfassende Übersicht über die **Stream API in Java 25** sowie die damit eng verbundenen Kernkonzepte: `Stream`, `Optional`, `Collectors` und `Comparator`. Sie dient als Vorbereitung auf die Übungsaufgaben und das Tutoratsgespräch.

---

## 1. Das Stream Interface (`java.util.stream.Stream`)

### 1.1 Konzept & Eigenschaften
Ein **Stream** in Java ist keine Datenstruktur, die Elemente speichert. Stattdessen ist ein Stream eine **Sequenz von Elementen**, die aus einer Datenquelle (z. B. `Collection`, `Array`, I/O-Kanal) fließt und durch eine Pipeline von Operationen transformiert oder verarbeitet werden kann.

Wichtige Eigenschaften:
* **Keine Speicherung:** Ein Stream hält selbst keine Daten vor.
* **Functional in Nature:** Operationen verändern die ursprüngliche Datenquelle nicht, sondern erzeugen ein neues Ergebnis.
* **Lazy Evaluation (Träge Ausführung):** Zwischenoperationen (*Intermediate Operations*) werden erst ausgeführt, wenn eine Terminal-Operation (*Terminal Operation*) aufgerufen wird.
* **Einweg-Verwendung (Single-Use):** Ein Stream kann nur genau einmal konsumiert werden. Nach dem Aufruf einer Terminal-Operation ist der Stream geschlossen.

### 1.2 Intermediate vs. Terminal Operations
Eine Stream-Pipeline besteht aus:
1. **Quelle** (z. B. `list.stream()`, `IntStream.range(1, 10)`)
2. **Intermediate Operations** (geben wieder einen `Stream` zurück und sind lazy):
   * `filter(Predicate<T>)`: Filtert Elemente basierend auf einer Bedingung.
   * `map(Function<T, R>)`: Transformiert jedes Element von Typ `T` zu Typ `R`.
   * `flatMap(Function<T, Stream<R>>)`: Flacht verschachtelte Streams/Strukturen in einen einzelnen Stream ab.
   * `distinct()`: Entfernt Duplikate (basierend auf `equals()`).
   * `sorted()` / `sorted(Comparator<T>)`: Sortiert die Elemente.
   * `takeWhile(Predicate<T>)` / `dropWhile(Predicate<T>)`: Nimmt/verwirft Elemente, solange die Bedingung gilt.
   * `gather(Gatherer)` *(Neu/Finalisiert in Java 24/25, JEP 485)*: Erlaubt benutzerdefinierte Zwischenoperationen (z. B. Windowing, Fixed-size groups, Stateful Transformations).
3. **Terminal Operations** (leiten die Ausführung ein und beenden den Stream):
   * `forEach(Consumer<T>)`: Führt eine Aktion für jedes Element aus.
   * `collect(Collector)`: Sammelt Elemente in eine Zielstruktur (z. B. `List`, `Set`, `Map`).
   * `toList()`: Ab Java 16 direkt am `Stream`-Interface verfügbar (gibt eine unmodifizierbare `List` zurück).
   * `reduce(...)`: Aggregiert Elemente zu einem einzelnen Wert.
   * `count()`, `min(Comparator)`, `max(Comparator)`.
   * `findFirst()`, `findAny()` (geben ein `Optional<T>` zurück).
   * `anyMatch()`, `allMatch()`, `noneMatch()`.

### 1.3 Primitiv-Streams (`IntStream`, `LongStream`, `DoubleStream`)
Um **Autoboxing/Unboxing** zwischen primitiven Datentypen (z. B. `int`) und ihren Wrapper-Klassen (z. B. `Integer`) zu vermeiden, stellt Java spezialisierte Streams bereit: `IntStream`, `LongStream` und `DoubleStream`.

**Vorteile & Besonderheiten:**
* **Performance:** Vermeidet den Speicher- und CPU-Overhead von Wrapper-Objekten.
* **Spezielle Methoden:** `sum()`, `average()`, `min()`, `max()`, `summaryStatistics()`.
* **Erzeugung:** `IntStream.range(1, 5)` (1 bis 4), `IntStream.rangeClosed(1, 5)` (1 bis 5), `IntStream.of(1, 2, 3)`.
* **Konvertierung:** 
  * `mapToObj(i -> ...)` wandelt einen `IntStream` in ein `Stream<T>` um.
  * `stream.mapToInt(Person::getAge)` wandelt ein `Stream<Person>` in ein `IntStream` um.

---

## 2. Die Optional Klasse (`java.util.Optional<T>`)

### 2.1 Konzept
`Optional<T>` ist ein Container-Objekt, das entweder genau einen Wert vom Typ `T` enthält oder **leer** (`empty`) ist. Es wurde eingeführt, um Rückgabewerte explizit als "kann abwesend sein" zu kennzeichnen und `NullPointerException` (NPE) abzuwenden.

### 2.2 Erzeugung
```java
Optional<String> empty = Optional.empty();
Optional<String> value = Optional.of("Hallo"); // Wirft NPE, falls Argument null ist!
Optional<String> nullable = Optional.ofNullable(maybeNullValue); // Sicher bei null
```

### 2.3 Werte abfragen und verarbeiten
* **Prüfen:** `isPresent()`, `isEmpty()`
* **Sicherer Zugriff / Fallbacks:**
  * `orElse(T defaultVal)`: Liefert den Wert oder den Default-Wert. **Achtung:** `defaultVal` wird *immer* ausgewertet.
  * `orElseGet(Supplier<T> supplier)`: Liefert den Wert oder führt den `Supplier` träge aus (bevorzugt bei teuren Berechnungen).
  * `orElseThrow()`: Liefert den Wert oder wirft `NoSuchElementException`.
  * `orElseThrow(Supplier<Exception>)`: Wirft eine benutzerdefinierte Exception.
* **Aktionen bei Anwesenheit:**
  * `ifPresent(Consumer<T>)`
  * `ifPresentOrElse(Consumer<T> action, Runnable emptyAction)`
* **Transformieren & Filtern:**
  * `map(Function)` / `flatMap(Function)`
  * `filter(Predicate)`
  * `stream()`: Wandelt ein `Optional<T>` in ein `Stream<T>` mit 0 oder 1 Element um (sehr nützlich zum Herausfiltern von leeren Optionals in Streams via `flatMap(Optional::stream)`).

---

## 3. Die Collectors Klasse (`java.util.stream.Collectors`)

### 3.1 Konzept
`Collectors` ist eine Utility-Klasse, die viele vorgefertigte Implementierungen des `Collector`-Interfaces bereitstellt. Sie wird zusammen mit der Terminal-Operation `stream.collect(...)` genutzt.

### 3.2 Wichtigste Collector-Methoden

#### 3.2.1 Aufsammlen in Sammlungen
* `Collectors.toList()`: Sammelt in eine `List` (Modifizierbarkeit nicht garantiert).
* `Collectors.toSet()`: Sammelt in ein `Set`.
* `Collectors.toCollection(TreeSet::new)`: Sammelt in eine spezifische `Collection`.
* `Collectors.toUnmodifiableList()`: Sammelt in eine unveränderliche Liste.
* *Hinweis zu Java 16+:* Für eine unveränderliche Liste kann stattdessen direkt `stream.toList()` verwendet werden.

#### 3.2.2 Erzeugen von Maps
```java
Map<Integer, String> map = stream.collect(
    Collectors.toMap(Person::getId, Person::getName)
);
```

#### 3.2.3 Reduktion und Aggregation
* `Collectors.joining(", ")`: Verbindet Strings mit einem Trennzeichen.
* `Collectors.counting()`: Zählt Elemente.
* `Collectors.summingInt(ToIntFunction)`, `Collectors.averagingDouble(ToDoubleFunction)`.
* `Collectors.summarizingInt(...)`: Erzeugt `IntSummaryStatistics` (Count, Min, Max, Sum, Average auf einmal).

#### 3.2.4 Gruppierung & Partitionierung
* **`groupingBy`:** Gruppiert Elemente nach einem Schlüssel in eine `Map<K, List<V>>`.
  ```java
  Map<City, List<Person>> peopleByCity = people.stream()
      .collect(Collectors.groupingBy(Person::getCity));
  ```
  Mit Downstream-Collector (z. B. Anzahl pro Stadt):
  ```java
  Map<City, Long> countByCity = people.stream()
      .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
  ```
* **`partitioningBy`:** Spezialfall von `groupingBy`, teilt Elemente anhand eines `Predicate` in eine `Map<Boolean, List<V>>` auf (`true` / `false`).
  ```java
  Map<Boolean, List<Person>> adultsAndMinors = people.stream()
      .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));
  ```

---

## 4. Das Comparator Interface (`java.util.Comparator<T>`)

### 4.1 Konzept
`Comparator<T>` ist ein funktionales Interface mit der Hauptmethode `int compare(T o1, T o2)`. Es definiert eine benutzerdefinierte Sortierordnung für Objekte.

* `compare(a, b) < 0`: `a` ist kleiner als `b`.
* `compare(a, b) == 0`: `a` ist gleich `b`.
* `compare(a, b) > 0`: `a` ist größer als `b`.

### 4.2 Erzeugung & Verkettung (Fluent API)
Seit Java 8 bietet `Comparator` statische und Default-Methoden für eine lesbare Deklaration:

```java
// Natürliche Ordnung
Comparator<String> nat = Comparator.naturalOrder();
Comparator<String> rev = Comparator.reverseOrder();

// Nach einer Eigenschaft vergleichen
Comparator<Person> byName = Comparator.comparing(Person::getName);

// Nach Primitivtypen vergleichen (ohne Autoboxing)
Comparator<Person> byAge = Comparator.comparingInt(Person::getAge);

// Mehrere Kriterien verketten (thenComparing)
Comparator<Person> complex = Comparator.comparing(Person::getLastName)
    .thenComparing(Person::getFirstName)
    .thenComparingInt(Person::getAge);

// Umgang mit null-Werten
Comparator<String> safeNullsFirst = Comparator.nullsFirst(Comparator.naturalOrder());
Comparator<String> safeNullsLast  = Comparator.nullsLast(Comparator.naturalOrder());
```

### 4.3 Verwendung im Stream
```java
List<Person> sortedPeople = people.stream()
    .sorted(Comparator.comparing(Person::getAge).reversed())
    .toList();
```

---

## 5. Tutor-Fragen & Antworten (3 Fragen pro Themenbereich)

### 🔹 Themenbereich 1: Stream Interface

**Frage 1: Was unterscheidet Intermediate-Operations von Terminal-Operations in der Stream API?**
> **Antwort:** Intermediate-Operations (z. B. `filter`, `map`, `sorted`) geben einen neuen Stream zurück und werden **lazy** (träge) ausgewertet – das bedeutet, sie führen erst dann Berechnungen aus, wenn eine Terminal-Operation aufgerufen wird. Terminal-Operations (z. B. `collect`, `forEach`, `reduce`, `count`) leiten die tatsächliche Verarbeitung der Pipeline ein, produzieren ein Ergebnis (oder einen Seiteneffekt) und schließen den Stream.

**Frage 2: Was ist ein `IntStream` und warum nutzt man ihn anstelle von `Stream<Integer>`?**
> **Antwort:** Ein `IntStream` ist eine spezialisierte Form von `Stream` für den primitiven Datentyp `int`. Man verwendet ihn, um den Speicher- und Laufzeit-Overhead von **Autoboxing und Unboxing** (Umwandlung zwischen `int` und `Integer`) zu vermeiden. Zudem stellt `IntStream` zusätzliche mathematische Aggregationsmethoden wie `sum()`, `average()`, `range()` und `summaryStatistics()` bereit.

**Frage 3: Was bedeutet "Lazy Evaluation" im Kontext von Streams und welchen Vorteil bietet sie?**
> **Antwort:** "Lazy Evaluation" bedeutet, dass Elemente erst dann durch die Stream-Pipeline fließen, wenn die Terminal-Operation dies anfordert. Der Vorteil ist Optimierung und Effizienz: Operationen können zusammengefasst (*Loop Fusion*) oder vorzeitig abgebrochen werden (*Short-circuiting*, z. B. bei `findFirst()` oder `anyMatch()`), sodass nicht unnötig alle Elemente verarbeitet werden müssen.

---

### 🔹 Themenbereich 2: Optional Klasse

**Frage 1: Wann sollte man `Optional.of()` und wann `Optional.ofNullable()` verwenden?**
> **Antwort:** `Optional.of(val)` verwendet man nur, wenn man absolut sicher ist, dass `val` niemals `null` ist (falls `val` doch `null` ist, wird sofort eine `NullPointerException` geworfen). `Optional.ofNullable(val)` verwendet man, wenn der Wert `null` sein könnte; falls `val == null` ist, wird ein leeres `Optional.empty()` zurückgegeben.

**Frage 2: Was ist der Unterschied zwischen `orElse()` und `orElseGet()`?**
> **Antwort:** 
> * `orElse(defaultVal)` wertet den Ausdruck `defaultVal` **immer** sofort aus, unabhängig davon, ob das Optional gefüllt ist oder nicht.
> * `orElseGet(Supplier)` wertet den `Supplier` **lazy** nur dann aus, wenn das Optional tatsächlich leer ist. `orElseGet` ist daher performanter und sicherer bei aufwendigen Berechnungen oder Methodenaufrufen.

**Frage 3: Wie wandelt man ein `Optional` in einen `Stream` um und wozu ist das nützlich?**
> **Antwort:** Über die Methode `optional.stream()`. Sie gibt einen Stream mit 1 Element zurück (wenn das Optional präsent ist) oder einen leeren Stream (wenn es leer ist). Das ist besonders hilfreich beim Verarbeiten einer Liste von Optionals in einer Stream-Pipeline: Mit `streamOfOptionals.flatMap(Optional::stream)` lassen sich leere Optionals elegant herausfiltern und entpacken.

---

### 🔹 Themenbereich 3: Collectors Klasse

**Frage 1: Was ist der Unterschied zwischen `Collectors.toList()` und der Methode `Stream.toList()` (seit Java 16)?**
> **Antwort:** `Collectors.toList()` gibt eine Liste zurück, bei der weder die Veräußerlichkeit (Modifizierbarkeit) noch der konkrete Typ (z. B. `ArrayList`) garantiert ist. `Stream.toList()` ist eine bequeme Methode direkt am Stream-Interface, die eine explizit **unmodifizierbare** `List` zurückgibt und performanter ist, da sie weniger Overhead hat.

**Frage 2: Wie funktioniert `Collectors.groupingBy()` und wie setzt man einen Downstream-Collector ein?**
> **Antwort:** `Collectors.groupingBy(classifier)` gruppiert die Elemente eines Streams anhand einer Klassifizierungsfunktion in eine `Map<K, List<V>>`. Wenn man als zweiten Parameter einen sogenannten **Downstream-Collector** übergibt (z. B. `Collectors.groupingBy(Person::getCity, Collectors.counting())`), werden die in den Gruppen enthaltenen Elemente weiterverarbeitet (in diesem Beispiel wird die Anzahl der Personen pro Stadt gezählt statt eine Liste von Personen zu erstellen).

**Frage 3: Was bewirkt `Collectors.partitioningBy()`?**
> **Antwort:** `Collectors.partitioningBy(predicate)` teilt die Elemente des Streams in zwei Gruppen auf basierend auf einer Wahrheitsbedingung (`Predicate`). Das Ergebnis ist stets eine `Map<Boolean, List<V>>` mit genau zwei Schlüssel-Einträgen (`true` und `false`).

---

### 🔹 Themenbereich 4: Comparator Interface

**Frage 1: Wie vergleicht man Objekte nach mehreren Kriterien nacheinander (z. B. Nachname, dann Vorname)?**
> **Antwort:** Man verknüpft Comparatoren fluent mit der Methode `thenComparing()`. 
> Beispiel: `Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName)`. Wenn der Vergleich nach Nachname 0 ergibt (Gleichheit), wird nach Vorname verglichen.

**Frage 2: Wie geht man sicher mit `null`-Werten um, wenn man Objekte vergleicht?**
> **Antwort:** Man nutzt `Comparator.nullsFirst(...)` oder `Comparator.nullsLast(...)`. 
> Beispiel: `Comparator.nullsFirst(Comparator.naturalOrder())` sortiert `null`-Werte an den Anfang der Liste, ohne dass eine `NullPointerException` geworfen wird.

**Frage 3: Warum sollte man `Comparator.comparingInt(...)` statt `Comparator.comparing(...)` für `int`-Eigenschaften nutzen?**
> **Antwort:** `Comparator.comparingInt(Person::getAge)` arbeitet direkt mit dem primitiven Typ `int` (Funktion `ToIntFunction`). `Comparator.comparing(Person::getAge)` hingegen verlangt eine `Function<T, U>`, was dazu führt, dass der `int`-Wert per **Autoboxing** in ein `Integer`-Objekt umgewandelt wird. `comparingInt` spart somit Speicher und Rechenzeit.

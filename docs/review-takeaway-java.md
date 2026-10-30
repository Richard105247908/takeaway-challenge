# Takeaway Challenge: Code Review Checklist (Java version)

The Makers takeaway rubric translated for Java 21, JUnit 5 and Mockito. Save this as `docs/review.md` and tick things off as you go. If you are reviewing someone else, go through it together.

## Step 0: Checkout and run the tests
- [ ] `mvn test` passes from a clean checkout
- [ ] `mvn verify` produces a coverage report (`target/site/jacoco/index.html`)
- [ ] Try it by hand in `jshell --class-path target/classes`. How readable is the structure? Did you need any cognitive leaps to understand it?

## Step 1: Structure and supporting files

### Dependencies are declared
- [ ] Every library you use (for example the Twilio SDK) is in `pom.xml` under `<dependencies>`, with a pinned version. Never commit `.jar` files. Anyone should be able to run `mvn test` on a fresh machine.

### README is updated
- [ ] Explains your approach, and where you got to if unfinished
- [ ] Shows the full user story from the user's side, for example a `jshell` session:

```
jshell> var menu = new Menu(Map.of("spring roll", 99, "pork dumpling", 299))
jshell> var takeaway = new Takeaway(menu, smsSender, Clock.systemDefaultZone())
jshell> takeaway.order("spring roll", 4)
jshell> takeaway.basketSummary()
$4 ==> "spring roll x4 = £3.96"
jshell> takeaway.total()
$5 ==> "Total: £3.96"
```

Keep prices as whole pence (`int`) in the code and format them as pounds only when displaying.

## Step 2: Tests

### Test real behaviour, not stubs
In Ruby the vacuous test stubs a method on the subject and then calls it. The Java version is a Mockito `spy` on the class under test:

```java
// Vacuous: this only proves that Mockito works
Takeaway takeaway = spy(new Takeaway(menu, smsSender, clock));
doNothing().when(takeaway).sendText(any());
takeaway.sendText("hello");
verify(takeaway).sendText("hello");
```

- [ ] Never stub or spy on the object under test
- [ ] Stub only the two legitimate things: **randomness** and **third-party services**
- [ ] You can check a test is vacuous by deleting it and seeing whether coverage changes

### Stub Twilio by injecting your own interface
Wrap Twilio in your own `SmsSender` interface, inject it, and mock the interface. The real `TwilioSmsSender` is the only class that touches the SDK.

```java
@ExtendWith(MockitoExtension.class)
class TakeawayTest {

    @Mock SmsSender smsSender;

    @Test
    void sendsConfirmationAfterAnOrderIsPlaced() {
        Clock clock = Clock.fixed(Instant.parse("2026-10-30T17:52:00Z"), ZoneId.of("UTC"));
        // ... build the takeaway with the mock and the fixed clock, place a correct order ...

        verify(smsSender).send(
            "Thank you! Your order was placed and will be delivered before 18:52");
    }
}
```

- [ ] Text sending is mocked, so no real text is ever sent by a test
- [ ] The delivery time uses an injected `Clock`, never `LocalTime.now()` directly
- [ ] When the total is wrong, `verify(smsSender, never()).send(any())` proves no text was sent
- [ ] Use `ArgumentCaptor<String>` if you need to inspect the message in detail

### Unit tests vs integration (feature) tests
At Makers they recommend the **London style** for unit tests: mock every collaborator so you test one class at a time. Using real collaborators is the **Chicago style**, which counts as an integration test.

- [ ] Unit tests mock collaborators (`Menu`, `SmsSender`, `Clock`)
- [ ] Integration tests use real objects and mock only the outside world (`SmsSender`)
- [ ] Keep them separate: unit tests in `AirportTest`-style classes beside the code, feature tests in their own class (for example `TakeawayFeatureTest`) or package

### Test every public method; keep the public interface small
- [ ] Every public method has an explicit test
- [ ] Helpers used only inside the class are `private` (or package-private), so they don't need their own tests
- [ ] Fewer public methods and fewer parameters means looser coupling. Aim for a small public interface.
- [ ] If you find yourself wanting to test a private method, it is usually a sign it belongs in its own class

## Step 3: Application code

### Prefer composition and injection over inheritance and mixins
Java has no mixins. The nearest thing is inheritance or interface default methods, and either can break single responsibility if you hide extra duties in a base class.

- [ ] No base class that sends texts. Inject an `SmsSender` instead.

### Law of Demeter
Don't reach through several objects. Method chaining on one object (a builder, a stream) is fine. Chains across different objects are the warning sign.

```java
// Smell: the test knows how the menu stores dishes
assertEquals(2, restaurant.menu().dishes().size());

// Better: ask the object that owns the information
assertEquals(2, menu.dishCount());
```

- [ ] No `a.b().c().d()` chains that cross object boundaries, in code or in tests

### Appropriate dependency injection
Java has no default arguments, so use constructor overloads for optional defaults.

```java
public Takeaway(Menu menu, SmsSender smsSender, Clock clock) {
    this.menu = menu;
    this.smsSender = smsSender;
    this.clock = clock;
}

public Takeaway(Menu menu, SmsSender smsSender) {
    this(menu, smsSender, Clock.systemDefaultZone());
}
```

- [ ] `SmsSender`, `Clock` and `Menu` are passed in, never created inside the class with `new`
- [ ] Fields are `private final`

### Separation of concerns
Business logic should return values, not print them.

```java
// Bad: business class talks to the screen
public void displayMenu() { System.out.println("the menu is empty"); }

// Good: returns data; presentation happens elsewhere (or in toString)
@Override
public String toString() { return "the menu is empty"; }
```

- [ ] No `System.out.println` inside `Menu`, `Order` or `Takeaway`
- [ ] Any console interaction sits in a thin outer layer you don't unit test

### Single Responsibility: look for repeated nouns
If `addToOrder`, `orderTotal` and `finalizeOrder` all share the word "order", you need an `Order` class.

- [ ] `Menu` knows the dishes and prices
- [ ] `Order` holds the chosen dishes, quantities and the total
- [ ] `Takeaway` coordinates placing an order and sending the text
- [ ] `SmsSender` is the only thing that knows about Twilio

### Keep personal details out of GitHub
- [ ] Phone numbers and the Twilio account SID and token are read with `System.getenv("TWILIO_AUTH_TOKEN")`, never written in code
- [ ] Set them in IntelliJ's run configuration or your operating system. Java doesn't read a `.env` file by default.
- [ ] `.env` is listed in `.gitignore`
- [ ] If a secret was ever committed, rotate it in Twilio. Deleting it later doesn't remove it from Git history.

### Use what the language gives you
Counting dishes without checking for a missing key (Ruby's `Hash.new(0)`):

```java
// Instead of: if (!items.containsKey(dish)) items.put(dish, 0); items.put(dish, items.get(dish) + qty);
items.merge(dish, quantity, Integer::sum);
```

Totalling with a stream instead of a manual loop:

```java
int total = items.entrySet().stream()
        .mapToInt(e -> menu.priceOf(e.getKey()) * e.getValue())
        .sum();
```

- [ ] `Map.merge` or `getOrDefault` instead of manual key checks
- [ ] A stream (or a clear loop) for totals
- [ ] `List.copyOf` or `Map.copyOf` when returning collections, so callers can't change your internals

### Open/Closed Principle
The menu should not be hard-coded inside `Takeaway`.

```java
// Closed to extension: changing the menu means editing Takeaway
this.menu = new Menu(Map.of("pizza", 1000, "coke", 70));

// Open to extension: pass any menu in
public Takeaway(Menu menu, ...) { this.menu = menu; }
```

- [ ] Dishes and prices come in through the constructor or a file, not inside the class
- [ ] Optional: make `Menu` an interface so you could have an in-memory menu and a file-based one

### Consistent style
- [ ] `PascalCase` classes, `camelCase` methods, `UPPER_SNAKE_CASE` constants
- [ ] Four-space indentation. In IntelliJ press Ctrl+Alt+L to format the file.
- [ ] No commented-out code, no unused imports or fields
- [ ] Optional: add Checkstyle if you'd like a linter, the Java version of Rubocop

## Before you submit
- [ ] Tests green, coverage above 95%
- [ ] No phone number or API key anywhere in the repo or its history
- [ ] README written for a stranger, with a usage example
- [ ] Every class has one clear responsibility
- [ ] Partial work is still committed

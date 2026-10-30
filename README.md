# Takeaway Challenge (Java)

![CI](https://github.com/YOUR-USERNAME/takeaway-challenge/actions/workflows/ci.yml/badge.svg)

A Java 21 / JUnit 5 / Mockito port of the Makers [takeaway-challenge](https://github.com/makersacademy/takeaway-challenge), built test-first.

## Stack
- Java 21, Maven
- JUnit 5, Mockito
- JaCoCo (coverage), GitHub Actions CI

## What it does
_Write 2-3 sentences in your own words once it works._

## User stories
```
As a customer
So that I can check if I want to order something
I would like to see a list of dishes with prices

As a customer
So that I can order the meal I want
I would like to be able to select some number of several available dishes

As a customer
So that I can verify that my order is correct
I would like to check that the total I have been given matches the sum of the various dishes in my order

As a customer
So that I am reassured that my order will be delivered on time
I would like to receive a text such as "Thank you! Your order was placed and will be delivered before 18:52" after I have ordered
```

## Java translation notes
| Ruby / RSpec | Java |
|---|---|
| `double` for the Twilio client | Mockito mock of your own `SmsSender` interface |
| `expect(client).to receive(:send)` | `verify(smsSender).send(...)`, with `ArgumentCaptor` for the message text |
| Stubbing the current time | Inject a `java.time.Clock`; use `Clock.fixed(...)` in tests |
| `raise_error` | `assertThrows`, and check the message |
| twilio-ruby gem | Twilio Java SDK, wrapped by `TwilioSmsSender` (add it last) |
| Keeping secrets out of the repo | `System.getenv("TWILIO_AUTH_TOKEN")`, never committed |
| SimpleCov | JaCoCo |

Prices are whole pence (`int`), not `double`, to avoid rounding errors.

## Edge cases to cover
- Ordering a dish that is not on the menu
- A quantity of zero or less
- An empty order
- A total that does not equal the sum of the dishes: no order is placed and **no text is sent**
- Delivery time is one hour from now, formatted `HH:mm`, including across midnight (23:30 becomes 00:30)

## Running it
```bash
mvn test      # run the tests
mvn verify    # tests + coverage report at target/site/jacoco/index.html
```

## Suggested test order
1. Menu returns the price of a dish (written for you, RED)
2. Menu lists its dishes
3. Order accepts a dish and quantity
4. Order total is the sum of dishes times quantities
5. Unknown dish is rejected (check the message)
6. Placing an order with the wrong total is rejected and sends no text
7. Placing a correct order sends a text through `SmsSender` (mock it)
8. The text says delivery is one hour from now (inject a fixed `Clock`)
9. Feature test: order several dishes and place the order with real objects and a mocked `SmsSender`
10. Last: `TwilioSmsSender`, with credentials read from environment variables

## Progress (update as you go)
- [ ] See the menu with prices
- [ ] Select dishes and quantities
- [ ] Total checked against the sum
- [ ] Text sent after ordering (mocked in tests)
- [ ] Delivery time one hour ahead
- [ ] Edge cases
- [ ] Coverage above 95%
- [ ] Real Twilio sender, with secrets in environment variables
- [ ] Advanced (optional): ordering by text needs a web endpoint, so leave it until week 3

## Architecture notes
_Fill in as you build: why the classes are split the way they are, how the text sender and clock are injected, and what you would improve._

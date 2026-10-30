# Grocery Utility Functions

Java 17 utility functions for grocery data: sorting, filtering, conversion between products and
`name,category,price,quantity` records, and inventory totals. The solution is in
[`GroceryUtils.java`](src/main/java/com/company/grocery/GroceryUtils.java). `Product.java`,
`GroceryApplication.java` and `GroceryUtilsTest.java` are unchanged from the candidate pack.

## Project layout

```
grocery-utility/
├── README.md
├── pom.xml
├── build.gradle
├── settings.gradle
└── src
    ├── main/java/com/company/grocery
    │   ├── Product.java
    │   ├── GroceryUtils.java
    │   └── GroceryApplication.java
    └── test/java/com/company/grocery
        └── GroceryUtilsTest.java
```

## Build and run

Requires JDK 17 or newer. Gradle's toolchain setting expects a JDK 17 installation.

| Purpose         | Maven                                                | Gradle                                                  |
|-----------------|------------------------------------------------------|---------------------------------------------------------|
| Compile         | `mvn clean compile`                                  | `gradle clean compileJava`                              |
| Run all tests   | `mvn clean test`                                     | `gradle clean test`                                     |
| Run test class  | `mvn -Dtest=GroceryUtilsTest test`                   | `gradle test --tests GroceryUtilsTest`                  |
| Run one method  | `mvn -Dtest=GroceryUtilsTest#sortProductsByName test` | `gradle test --tests "GroceryUtilsTest.sortProductsByName"` |
| HTML report     | `target/surefire-reports`                            | `build/reports/tests/test/index.html`                   |

The full suite has 62 tests, and all of them pass under both Maven and Gradle.

## Design notes

**Validation happens before any work.** Every public method checks all of its input first and
throws `IllegalArgumentException` with a message that names the bad argument and its index, for
example `products[1].name must not contain a comma`. So a failed `sort` never leaves an array
half-sorted, and a failed filter never returns a partial result. The same product rules apply
everywhere a `Product` is accepted:

- the array and its elements are non-null
- name and category are non-blank and contain no commas, so every record can be parsed back
- the price is finite and non-negative, and the quantity is non-negative

**Overloading.** `sort(Product[])` delegates to `sort(Product[], Comparator)` with a
case-insensitive name comparator, and `getProductInfo(Product[])` reuses `productsToStringArray`.
This keeps each rule in a single place.

**Ordering.** Product sorts use `Arrays.sort(T[], Comparator)` (TimSort). It is stable, so
products with equal keys keep their input order. `sort(String[])` uses natural order, because the
contract makes only product-name sorting case-insensitive. `sort(double[])` uses the primitive
dual-pivot quicksort.

**Filters do not change their input.** All four filters go through one private
`filter(products, predicate)` helper. It makes one pass in source order and returns a new array,
even when every product matches. Category and keyword criteria are trimmed. Matching is
case-insensitive and uses `Locale.ROOT`, so the result does not depend on the JVM's default
locale.

**Parsing is strict.**

- `split(",", -1)` keeps trailing empty fields, so `"Apple,Fruits,5.0,20,"` is rejected as a
  five-field record instead of quietly passing as four.
- Every field is trimmed.
- A `NumberFormatException` is re-thrown as an `IllegalArgumentException` that names the field.
- `NaN` and `Infinity` parse as doubles, so they are rejected by the same price check.

**Copies and totals.** `pricesToDoubleArray` and `quantitiesToIntArray` validate their input and
return `clone()`d arrays. `getTotalPrice` sums `price * quantity`. `getTotalQuantity` uses
`Math.addExact`. The contract assumes the total fits in an `int`, and `addExact` throws if it
does not, so an overflow can't produce a silently wrong total.

## Complexity

| Operation                              | Time         | Extra space                    |
|----------------------------------------|--------------|--------------------------------|
| `sort` (all overloads)                 | O(n log n)   | O(n) for TimSort, O(log n) for primitives |
| `filterBy*`                            | O(n)         | O(k) for k matches             |
| `productsToStringArray`, `stringsToProducts` | O(n)   | O(n)                           |
| `pricesToDoubleArray`, `quantitiesToIntArray` | O(n)  | O(n)                           |
| `getProductInfo(Product[])`            | O(total characters) | O(total characters)     |
| `getTotalPrice`, `getTotalQuantity`    | O(n)         | O(1)                           |

Validation is a single O(n) pass, so it doesn't change any of these bounds.

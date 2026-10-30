# Grocery Utility Functions: Java Candidate Pack

This file is the original assessment brief, transcribed in full. It is kept here for reference.
The completed solution is in [`GroceryUtils.java`](src/main/java/com/company/grocery/GroceryUtils.java),
and [`README.md`](README.md) explains it.

| Attribute             | Detail                                                        |
|-----------------------|---------------------------------------------------------------|
| Difficulty            | Medium                                                        |
| Recommended duration  | 75–90 minutes                                                 |
| Language              | Java 17                                                       |
| Testing               | JUnit 4.13.2                                                  |
| Build tools           | Maven and Gradle                                              |
| Primary skills        | Overloading, arrays, sorting, filtering, conversion, validation |
| Candidate deliverable | Complete `GroceryUtils.java` and make the supplied tests pass |

> **Assessment note:** The utility implementation is intentionally incomplete. Candidates must not
> delete, disable, rename, or weaken supplied tests or change the public API.

## Document guide

| Part | Contents                                                                      |
|------|-------------------------------------------------------------------------------|
| I    | Problem statement, requirements, contracts, and candidate instructions        |
| II   | Project structure, Product model, and GroceryUtils starter code               |
| III  | Internally consistent JUnit 4 test suite                                      |
| IV   | Maven and Gradle configuration, build/run instructions                        |
| V    | Evaluation rubric, timing guidance, and submission checklist                  |

## Candidate quick start

- Read the complete problem statement and validation contract before coding.
- Run the full JUnit 4 suite once to establish the baseline.
- Implement only the TODO methods in `GroceryUtils.java`.
- Preserve all public method signatures and supplied tests.
- Use either Maven or Gradle and ensure the full suite passes before submission.

## Recommended project layout

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

---

# Part I: Candidate Brief

## 1. Problem statement

You are developing reusable utility functions for grocery data in a retail application. The
utilities sort product and primitive data, filter products by business criteria, convert between
products and explicit string/array formats, and calculate inventory information. Implement the
functions efficiently while demonstrating method overloading, defensive validation, predictable
ordering, and non-destructive filtering.

## 2. Required functions

### 2.1 Sorting

- `public static void sort(Product[] products)`
- `public static void sort(Product[] products, Comparator<Product> comparator)`
- `public static void sort(String[] names)`
- `public static void sort(double[] prices)`

### 2.2 Filtering

- `public static Product[] filterByCategory(Product[] products, String category)`
- `public static Product[] filterByPrice(Product[] products, double minPrice, double maxPrice)`
- `public static Product[] filterByName(Product[] products, String keyword)`
- `public static Product[] filterByQuantity(Product[] products, int minQuantity)`

### 2.3 Conversion

- `public static String[] productsToStringArray(Product[] products)`
- `public static Product[] stringsToProducts(String[] data)`
- `public static double[] pricesToDoubleArray(double[] prices)`
- `public static int[] quantitiesToIntArray(int[] quantities)`

### 2.4 Product operations

- `public static String getProductInfo(Product product)`
- `public static String getProductInfo(Product[] products)`
- `public static double getTotalPrice(Product[] products)`
- `public static int getTotalQuantity(Product[] products)`

## 3. Functional and validation contract

1. Null arguments and arrays containing null elements throw `IllegalArgumentException`.
2. Empty arrays are valid and return empty arrays, an empty string, or zero as appropriate.
3. Product and string sorting modifies the supplied array. Filters do not modify the source array.
4. Filters preserve input order and return a new array.
5. Product-name sorting and category/name filtering are case-insensitive.
6. Category and keyword criteria are trimmed; null, empty, or blank criteria are invalid.
7. Prices must be finite and non-negative. Quantities and minimum quantities must be non-negative.
8. Price boundaries are inclusive, finite, non-negative, and `minPrice` must not exceed `maxPrice`.
9. Product string format is `name,category,price,quantity`. Fields are trimmed.
10. Names and categories must be nonblank and cannot contain commas.
11. Primitive conversion methods return independent copies.
12. `getProductInfo(Product[])` uses one product per platform line separator with no trailing separator.
13. `getTotalPrice` sums price multiplied by quantity; total quantity is assumed to fit in `int`.

## 4. Candidate instructions

- Implement all TODO methods in `GroceryUtils.java`.
- Do not modify `Product.java` or `GroceryUtilsTest.java`.
- Do not change package names, method signatures, return types, or visibility.
- Do not remove, ignore, or weaken assertions.
- Use clear, maintainable Java; streams are optional.
- Be prepared to explain validation, complexity, and key design choices.

---

# Part II: Starter Project

## 5. Product.java

Unchanged in this repository: [`Product.java`](src/main/java/com/company/grocery/Product.java).

## 6. GroceryUtils.java (starter code)

This is the starter code as it was handed out. The completed version replaces it in
[`GroceryUtils.java`](src/main/java/com/company/grocery/GroceryUtils.java).

```java
package com.company.grocery;

import java.util.Comparator;

public final class GroceryUtils {
    private GroceryUtils() { }

    public static void sort(Product[] products) {
        // TODO: validate and sort by name, ignoring case
    }

    public static void sort(Product[] products,
                            Comparator<Product> comparator) {
        // TODO: validate and sort using comparator
    }

    public static void sort(String[] names) {
        // TODO: validate and sort alphabetically
    }

    public static void sort(double[] prices) {
        // TODO: validate prices and sort ascending
    }

    public static Product[] filterByCategory(Product[] products,
                                             String category) {
        // TODO: trim criterion, match ignoring case, preserve order
        return new Product[0];
    }

    public static Product[] filterByPrice(Product[] products,
                                          double minPrice,
                                          double maxPrice) {
        // TODO: validate inclusive range and preserve order
        return new Product[0];
    }

    public static Product[] filterByName(Product[] products,
                                         String keyword) {
        // TODO: trim keyword, contains match ignoring case
        return new Product[0];
    }

    public static Product[] filterByQuantity(Product[] products,
                                             int minQuantity) {
        // TODO: return products with quantity >= minimum
        return new Product[0];
    }

    public static String[] productsToStringArray(Product[] products) {
        // TODO: convert using name,category,price,quantity
        return new String[0];
    }

    public static Product[] stringsToProducts(String[] data) {
        // TODO: parse exactly four trimmed fields per record
        return new Product[0];
    }

    public static double[] pricesToDoubleArray(double[] prices) {
        // TODO: validate and return an independent copy
        return new double[0];
    }

    public static int[] quantitiesToIntArray(int[] quantities) {
        // TODO: validate and return an independent copy
        return new int[0];
    }

    public static String getProductInfo(Product product) {
        // TODO: return standard product representation
        return "";
    }

    public static String getProductInfo(Product[] products) {
        // TODO: one product per line, no trailing separator
        return "";
    }

    public static double getTotalPrice(Product[] products) {
        // TODO: sum price * quantity
        return 0.0;
    }

    public static int getTotalQuantity(Product[] products) {
        // TODO: sum quantities
        return 0;
    }
}
```

## 7. GroceryApplication.java

Unchanged in this repository:
[`GroceryApplication.java`](src/main/java/com/company/grocery/GroceryApplication.java).

---

# Part III: JUnit 4 Test Suite

The test suite is internally consistent with the contract in Part I. It covers:

- positive behavior
- null and invalid inputs
- empty and boundary cases
- source immutability
- order preservation
- defensive-copy semantics

It is unchanged in this repository and has 62 tests:
[`GroceryUtilsTest.java`](src/test/java/com/company/grocery/GroceryUtilsTest.java).

---

# Part IV: Build and Test Configuration

## 8. Maven: pom.xml

See [`pom.xml`](pom.xml).

## 9. Gradle: build.gradle

See [`build.gradle`](build.gradle).

## 10. Gradle: settings.gradle

See [`settings.gradle`](settings.gradle).

## 11. Build and run commands

| Purpose         | Maven                                                 | Gradle                                                     |
|-----------------|-------------------------------------------------------|------------------------------------------------------------|
| Compile         | `mvn clean compile`                                   | `gradle clean compileJava`                                 |
| Run all tests   | `mvn clean test`                                      | `gradle clean test`                                        |
| Run test class  | `mvn -Dtest=GroceryUtilsTest test`                    | `gradle test --tests GroceryUtilsTest`                     |
| Run one method  | `mvn -Dtest=GroceryUtilsTest#sortProductsByName test` | `gradle test --tests "GroceryUtilsTest.sortProductsByName"` |
| HTML report     | `target/surefire-reports`                             | `build/reports/tests/test/index.html`                      |

---

# Part V: Evaluation

## 12. Expected completion-time guidelines

| Activity             | Guideline     | Expected focus                                              |
|----------------------|---------------|-------------------------------------------------------------|
| Read and plan        | 10 minutes    | Understand signatures, contracts, and source-order rules    |
| Sorting functions    | 10–15 minutes | Overloads, comparators, data validation                     |
| Filtering functions  | 20 minutes    | Case-insensitive criteria, inclusive ranges, new arrays     |
| Conversion functions | 15–20 minutes | Parsing, formatting, defensive copies                       |
| Product operations   | 10 minutes    | Information formatting and aggregation                      |
| Test/fix/review      | 15 minutes    | Run full suite, diagnose failures, final cleanup            |
| **Total**            | 75–90 minutes | Complete implementation with all tests passing              |

## 13. Evaluation rubric

| Area                      | Weight | What good looks like                                                                 |
|---------------------------|--------|--------------------------------------------------------------------------------------|
| Functional correctness    | 45%    | All required behavior is implemented and all supplied tests pass.                    |
| Validation and edge cases | 20%    | Null, blank, malformed, numeric, boundary, and empty inputs follow the contract.     |
| Code quality              | 15%    | Readable names, focused methods, minimal duplication, maintainable logic.            |
| API and test discipline   | 10%    | Public signatures are preserved; supplied tests are not weakened or removed.         |
| Efficiency and reasoning  | 10%    | Sorting is O(n log n); linear operations are O(n); choices are explained.            |

## 14. Candidate submission checklist

- [ ] All TODO methods in `GroceryUtils.java` are implemented.
- [ ] The Maven project passes `mvn clean test`.
- [ ] The Gradle project passes `gradle clean test`.
- [ ] No public API, package name, or supplied assertion was changed.
- [ ] Filters preserve order, do not mutate source arrays, and return new arrays.
- [ ] Conversion and information formats match the specified contract.
- [ ] Null, blank, malformed, numeric, and boundary cases are handled.
- [ ] Code is formatted and free from debugging output.
- [ ] Candidate can explain time/space complexity and validation choices.

The status of each item for this solution is in the [README](README.md#submission-checklist).

## 15. Interviewer scoring guide

| Score    | Interpretation                                                         |
|----------|------------------------------------------------------------------------|
| 90–100   | Complete, correct, clean, and well-explained solution.                 |
| 75–89    | Core solution correct with minor edge-case or readability issues.      |
| 60–74    | Most core behavior works; several validation or conversion gaps remain. |
| 40–59    | Partial implementation; significant functional gaps or test failures.  |
| Below 40 | Limited progress or major API/test-discipline violations.              |

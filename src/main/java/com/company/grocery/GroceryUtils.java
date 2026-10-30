package com.company.grocery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * Stateless helpers for sorting, filtering, converting and totalling grocery data.
 *
 * <p>Every public method validates all of its input before doing any work and
 * throws {@link IllegalArgumentException} for null arguments, null elements and
 * values that break the product contract (blank or comma-containing names and
 * categories, negative or non-finite prices, negative quantities). Sorting
 * reorders the supplied array; every other method leaves its input untouched.
 */
public final class GroceryUtils {
    private static final String SEPARATOR = ",";
    private static final int FIELD_COUNT = 4;
    private static final Comparator<Product> BY_NAME_IGNORING_CASE =
            Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);

    private GroceryUtils() { }

    // Sorting

    /** Sorts products in place by name, ignoring case. Stable, O(n log n). */
    public static void sort(Product[] products) {
        sort(products, BY_NAME_IGNORING_CASE);
    }

    /** Sorts products in place using the comparator. Stable, O(n log n). */
    public static void sort(Product[] products,
                            Comparator<Product> comparator) {
        requireValidProducts(products);
        requireNonNull(comparator, "comparator");
        Arrays.sort(products, comparator);
    }

    /** Sorts names in place in natural (alphabetical) order. O(n log n). */
    public static void sort(String[] names) {
        requireNonNull(names, "names");
        for (int i = 0; i < names.length; i++) {
            requireText(names[i], "names[" + i + "]");
        }
        Arrays.sort(names);
    }

    /** Sorts prices in place, ascending. O(n log n). */
    public static void sort(double[] prices) {
        requireValidPrices(prices);
        Arrays.sort(prices);
    }

    // Filtering

    /** Returns products whose category equals the trimmed criterion, ignoring case. */
    public static Product[] filterByCategory(Product[] products,
                                             String category) {
        String criterion = requireCriterion(category, "category");
        return filter(products, p -> p.getCategory().equalsIgnoreCase(criterion));
    }

    /** Returns products priced within the inclusive range [minPrice, maxPrice]. */
    public static Product[] filterByPrice(Product[] products,
                                          double minPrice,
                                          double maxPrice) {
        requireValidPrice(minPrice, "minPrice");
        requireValidPrice(maxPrice, "maxPrice");
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice (" + minPrice
                    + ") must not exceed maxPrice (" + maxPrice + ")");
        }
        return filter(products, p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice);
    }

    /** Returns products whose name contains the trimmed keyword, ignoring case. */
    public static Product[] filterByName(Product[] products,
                                         String keyword) {
        // Locale.ROOT keeps case folding stable regardless of the JVM's default locale.
        String needle = requireCriterion(keyword, "keyword").toLowerCase(Locale.ROOT);
        return filter(products, p -> p.getName().toLowerCase(Locale.ROOT).contains(needle));
    }

    /** Returns products whose quantity is at least minQuantity. */
    public static Product[] filterByQuantity(Product[] products,
                                             int minQuantity) {
        requireValidQuantity(minQuantity, "minQuantity");
        return filter(products, p -> p.getQuantity() >= minQuantity);
    }

    // Conversion

    /** Converts each product to a name,category,price,quantity record. */
    public static String[] productsToStringArray(Product[] products) {
        requireValidProducts(products);
        String[] records = new String[products.length];
        for (int i = 0; i < products.length; i++) {
            records[i] = toRecord(products[i]);
        }
        return records;
    }

    /** Parses name,category,price,quantity records; fields are trimmed. */
    public static Product[] stringsToProducts(String[] data) {
        requireNonNull(data, "data");
        Product[] products = new Product[data.length];
        for (int i = 0; i < data.length; i++) {
            products[i] = parseRecord(data[i], "data[" + i + "]");
        }
        return products;
    }

    /** Returns an independent copy of the validated prices. */
    public static double[] pricesToDoubleArray(double[] prices) {
        requireValidPrices(prices);
        return prices.clone();
    }

    /** Returns an independent copy of the validated quantities. */
    public static int[] quantitiesToIntArray(int[] quantities) {
        requireNonNull(quantities, "quantities");
        for (int i = 0; i < quantities.length; i++) {
            requireValidQuantity(quantities[i], "quantities[" + i + "]");
        }
        return quantities.clone();
    }

    // Product operations

    /** Returns the standard name,category,price,quantity representation. */
    public static String getProductInfo(Product product) {
        requireValidProduct(product, "product");
        return toRecord(product);
    }

    /** Returns one record per line, joined by the platform line separator. */
    public static String getProductInfo(Product[] products) {
        return String.join(System.lineSeparator(), productsToStringArray(products));
    }

    /** Returns the inventory value: the sum of price * quantity. */
    public static double getTotalPrice(Product[] products) {
        requireValidProducts(products);
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    /** Returns the sum of all quantities. */
    public static int getTotalQuantity(Product[] products) {
        requireValidProducts(products);
        int total = 0;
        for (Product product : products) {
            // The contract assumes the total fits in an int; addExact fails loudly if not.
            total = Math.addExact(total, product.getQuantity());
        }
        return total;
    }

    // Internal helpers

    private static Product[] filter(Product[] products, Predicate<Product> predicate) {
        requireValidProducts(products);
        List<Product> matches = new ArrayList<>();
        for (Product product : products) {
            if (predicate.test(product)) {
                matches.add(product);
            }
        }
        return matches.toArray(new Product[0]);
    }

    private static String toRecord(Product product) {
        return product.getName() + SEPARATOR + product.getCategory() + SEPARATOR
                + product.getPrice() + SEPARATOR + product.getQuantity();
    }

    private static Product parseRecord(String record, String label) {
        requireNonNull(record, label);
        // A limit of -1 keeps trailing empty fields, so "a,b,1,2," is five fields, not four.
        String[] fields = record.split(SEPARATOR, -1);
        if (fields.length != FIELD_COUNT) {
            throw new IllegalArgumentException(label + " must have exactly " + FIELD_COUNT
                    + " fields (name,category,price,quantity): \"" + record + "\"");
        }
        String name = fields[0].trim();
        String category = fields[1].trim();
        requireText(name, label + ".name");
        requireText(category, label + ".category");
        double price = parsePrice(fields[2].trim(), label + ".price");
        int quantity = parseQuantity(fields[3].trim(), label + ".quantity");
        return new Product(name, category, price, quantity);
    }

    private static double parsePrice(String value, String label) {
        double price;
        try {
            price = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " is not a number: \"" + value + "\"", e);
        }
        requireValidPrice(price, label);
        return price;
    }

    private static int parseQuantity(String value, String label) {
        int quantity;
        try {
            quantity = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " is not an integer: \"" + value + "\"", e);
        }
        requireValidQuantity(quantity, label);
        return quantity;
    }

    // Validation

    private static void requireNonNull(Object value, String label) {
        if (value == null) {
            throw new IllegalArgumentException(label + " must not be null");
        }
    }

    private static void requireValidProducts(Product[] products) {
        requireNonNull(products, "products");
        for (int i = 0; i < products.length; i++) {
            requireValidProduct(products[i], "products[" + i + "]");
        }
    }

    private static void requireValidProduct(Product product, String label) {
        requireNonNull(product, label);
        requireText(product.getName(), label + ".name");
        requireText(product.getCategory(), label + ".category");
        requireValidPrice(product.getPrice(), label + ".price");
        requireValidQuantity(product.getQuantity(), label + ".quantity");
    }

    private static void requireValidPrices(double[] prices) {
        requireNonNull(prices, "prices");
        for (int i = 0; i < prices.length; i++) {
            requireValidPrice(prices[i], "prices[" + i + "]");
        }
    }

    /** Names and categories must be non-blank and comma-free so records stay parseable. */
    private static void requireText(String value, String label) {
        requireNonNull(value, label);
        if (value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        if (value.contains(SEPARATOR)) {
            throw new IllegalArgumentException(label + " must not contain a comma: \"" + value + "\"");
        }
    }

    /** Search criteria are trimmed; null or blank criteria are rejected. */
    private static String requireCriterion(String value, String label) {
        requireNonNull(value, label);
        if (value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value.trim();
    }

    private static void requireValidPrice(double price, String label) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException(label + " must be finite and non-negative: " + price);
        }
    }

    private static void requireValidQuantity(int quantity, String label) {
        if (quantity < 0) {
            throw new IllegalArgumentException(label + " must be non-negative: " + quantity);
        }
    }
}

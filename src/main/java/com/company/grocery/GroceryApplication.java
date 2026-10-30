package com.company.grocery;

import java.util.Arrays;
import java.util.Comparator;

public class GroceryApplication {
    public static void main(String[] args) {
        Product[] products = {
            new Product("Orange", "Fruits", 6.50, 10),
            new Product("Milk", "Dairy", 4.25, 5),
            new Product("Apple", "Fruits", 5.00, 20),
            new Product("Bread", "Bakery", 3.75, 8),
            new Product("Banana", "Fruits", 2.50, 15)
        };

        GroceryUtils.sort(products);
        System.out.println(Arrays.toString(products));
        GroceryUtils.sort(products,
                Comparator.comparingDouble(Product::getPrice));
        System.out.println(Arrays.toString(products));
    }
}

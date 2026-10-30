package com.company.grocery;

import java.util.Objects;

public class Product {
    private String name;
    private String category;
    private double price;
    private int quantity;

    public Product(String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return name + "," + category + "," + price + "," + quantity;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        Product p = (Product) other;
        return Double.compare(p.price, price) == 0
                && quantity == p.quantity
                && Objects.equals(name, p.name)
                && Objects.equals(category, p.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, price, quantity);
    }
}

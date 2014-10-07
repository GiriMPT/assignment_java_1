package com.bskyb.onlinestore.model;

import com.bskyb.onlinestore.model.Product;

import java.util.Set;
import java.util.TreeSet;

public class Basket {

    private Set<Product> products;

    public void addProduct(Product product) {
        if (products == null) {
            products = new TreeSet<>();
        }
        products.add(product);
    }

    public Set<Product> getProducts() {
        return products;
    }

}

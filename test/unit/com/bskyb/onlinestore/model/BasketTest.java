package com.bskyb.onlinestore.model;

import com.bskyb.onlinestore.model.Basket;
import com.bskyb.onlinestore.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BasketTest {

    private Basket basket;

    @Before
    public void init() {
        basket = new Basket();
    }

    @Test
    public void shouldAddProduct() throws Exception {
        Set<Product> expectedResult = Sets.newSet(Product.MOVIES_1, Product.NEWS);

        basket.addProduct(Product.MOVIES_1);
        basket.addProduct(Product.NEWS);

        assertThat(basket.getProducts(), is(expectedResult));
    }


}
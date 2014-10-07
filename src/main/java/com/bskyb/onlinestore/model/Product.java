package com.bskyb.onlinestore.model;

/**
 * Enum to represent the available set of products.
 */
public enum Product {
    SPORTS("SPORTS", Addon.SPORTS_3D_ADD_ON),
    KIDS("KIDS", null),
    VARIETY("KIDS", null),
    NEWS("NEWS", Addon.NEWS_3D_ADD_ON),
    MOVIES_1("MOVIES_1", Addon.MOVIES_3D_ADD_ON),
    MOVIES_2("MOVIES_2", Addon.MOVIES_3D_ADD_ON);

    private final String productCode;
    private final Addon addon;

    Product(String productCode, Addon addon) {
        this.productCode = productCode;
        this.addon = addon;
    }

    public boolean is3DCompatible() {
        return addon != null;
    }

    public Addon getAddon() {
        return addon;
    }

}
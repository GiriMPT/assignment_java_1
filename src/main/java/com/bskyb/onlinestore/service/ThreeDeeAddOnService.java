package com.bskyb.onlinestore.service;

import com.bskyb.onlinestore.model.Basket;
import com.bskyb.onlinestore.model.ThreeDeeResult;

/**
 * 3D AddOn Service. Checks for applicable 3D Addons.
 */
public interface ThreeDeeAddOnService {

    /**
     * Method to check the applicable 3D Addon packages
     *
     * @param basket   Contains the selected products
     * @param postCode Postcode for which the 3D service availability has to be checked
     * @return ThreeDeeResult @see ThreeDeeResult
     */
    public ThreeDeeResult checkFor3DAddOnProducts(Basket basket, String postCode);

}

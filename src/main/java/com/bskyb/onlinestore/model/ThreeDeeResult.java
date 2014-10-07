package com.bskyb.onlinestore.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Result class for the ThreeDeeAddOnService.
 */
public class ThreeDeeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<Addon> availableAddons;
    private boolean isValidPostCode;

    public ThreeDeeResult(Set<Addon> availableAddons, boolean isValidPostCode) {
        this.availableAddons = availableAddons;
        this.isValidPostCode = isValidPostCode;
    }

    /**
     * Returns the available 3D addons applicable.
     *
     * @return Set of Addons
     */
    public Set<Addon> getAvailableAddons() {
        return availableAddons;
    }

    public void setAvailableAddons(Set<Addon> availableAddons) {
        this.availableAddons = availableAddons;
    }

    /**
     * Was request PostCode a valid one.
     *
     * @return isValidPostCode
     */
    public boolean isValidPostCode() {
        return isValidPostCode;
    }

    public void setValidPostCode(boolean isValidPostCode) {
        this.isValidPostCode = isValidPostCode;
    }

}

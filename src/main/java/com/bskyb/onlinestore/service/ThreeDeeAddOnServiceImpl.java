package com.bskyb.onlinestore.service;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;
import com.bskyb.onlinestore.model.Basket;
import com.bskyb.onlinestore.model.Addon;
import com.bskyb.onlinestore.model.AvailabilityCheckerStatus;
import com.bskyb.onlinestore.model.ThreeDeeResult;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class ThreeDeeAddOnServiceImpl implements ThreeDeeAddOnService {

    private AvailabilityChecker availabilityChecker;
    private static Logger logger = Logger.getLogger(ThreeDeeAddOnServiceImpl.class);

    public ThreeDeeAddOnServiceImpl(AvailabilityChecker availabilityChecker) {
        this.availabilityChecker = availabilityChecker;
    }

    @Override
    public ThreeDeeResult checkFor3DAddOnProducts(Basket basket, String postCode) {
        validateParams(basket, postCode);
        Set<Addon> addons = Collections.emptySet();
        boolean isValidPostCode = true;

        String availabilityResponse;

        try {
            availabilityResponse = availabilityChecker.isPostCodeIn3DTVServiceArea(postCode);
        } catch (TechnicalFailureException tfe) {
            logger.warn("TechnicalFailureException when using availability checker service. ", tfe);
            availabilityResponse = AvailabilityCheckerStatus.SERVICE_UNAVAILABLE.name();
        } catch (Exception e) {
            logger.error("Unknown Exception when using availability checker service. ", e);
            availabilityResponse = AvailabilityCheckerStatus.SERVICE_UNAVAILABLE.name();
        }

        AvailabilityCheckerStatus availabilityCheckerStatus = AvailabilityCheckerStatus.valueOf(availabilityResponse);

        switch (availabilityCheckerStatus) {
            case SERVICE_AVAILABLE:
                addons = processBasket(basket);
                break;
            case POSTCODE_INVALID:
                isValidPostCode = false;
            case SERVICE_UNAVAILABLE:
            case SERVICE_PLANNED:
            default:
                addons = Collections.emptySet();
                break;
        }

        return new ThreeDeeResult(addons, isValidPostCode);
    }

    private Set<Addon> processBasket(Basket basket) {
        Set<Addon> addons = new TreeSet<>();
        basket.getProducts().stream().filter(product -> product.is3DCompatible()).forEach(product -> addons.add(product.getAddon()));
        return addons;
    }

    private void validateParams(Basket basket, String postCode) {
        if (basket == null) {
            throw new IllegalArgumentException("Basket may not be null");
        }

        if (postCode == null || postCode.isEmpty()) {
            throw new IllegalArgumentException("PostCode may not be empty");
        }
    }

}

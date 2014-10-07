package com.bskyb.onlinestore.model;

import com.acme.serviceavailability.AvailabilityChecker;

/**
 * Enum to represent the possible return values for AvailabilityChecker.isPostCodeIn3DTVServiceArea
 *
 * @see AvailabilityChecker
 */
public enum AvailabilityCheckerStatus {
    SERVICE_AVAILABLE, SERVICE_UNAVAILABLE, SERVICE_PLANNED, POSTCODE_INVALID;
}
package com.bskyb.onlinestore.service;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;
import com.bskyb.onlinestore.model.Addon;
import com.bskyb.onlinestore.model.Basket;
import com.bskyb.onlinestore.model.Product;
import com.bskyb.onlinestore.model.ThreeDeeResult;
import com.bskyb.onlinestore.service.ThreeDeeAddOnServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ThreeDeeAddOnServiceTest {

    private static final String VALID_POSTCODE = "SOME_POSTCODE";
    @Mock
    private AvailabilityChecker availabilityChecker;

    private Basket basket;

    //Possible AvailabilityChecker statuses
    private static final String SERVICE_AVAILABLE = "SERVICE_AVAILABLE";
    private static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
    private static final String SERVICE_PLANNED = "SERVICE_PLANNED";
    private static final String POSTCODE_INVALID = "POSTCODE_INVALID";
    private ThreeDeeAddOnServiceImpl threeDeeAddOnService;


    @Before
    public void setUp() throws Exception {
        threeDeeAddOnService = new ThreeDeeAddOnServiceImpl(availabilityChecker);
        basket = new Basket();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkFor3DAddOnProductsShouldThrowExceptionIfBasketIsNull() {
        threeDeeAddOnService.checkFor3DAddOnProducts(null, VALID_POSTCODE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkFor3DAddOnProductsShouldThrowExceptionIfPostcodeIsNull() {
        threeDeeAddOnService.checkFor3DAddOnProducts(basket, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkFor3DAddOnProductsShouldThrowExceptionIfPostcodeIsEmpty() {
        threeDeeAddOnService.checkFor3DAddOnProducts(basket, "");
    }

    @Test
    public void testThreeDeeAddonServiceAvailability() {
        assertNotNull(basket);
        assertNotNull(threeDeeAddOnService);
    }


    @Test
    public void shouldReturnSports3DAddonProductForBasketWithSportsProductAnd3DPostCode() throws TechnicalFailureException {
        basket.addProduct(Product.SPORTS);
        Set<Addon> expectedResultList = new TreeSet<>();
        expectedResultList.add(Addon.SPORTS_3D_ADD_ON);

        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_AVAILABLE);

        Set<Addon> actualResultList = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE).getAvailableAddons();
        assertEquals(expectedResultList, actualResultList);
    }

    @Test
    public void shouldReturnMultiple3DAddonProductForBasketWithMultipleProductAnd3DPostCode() throws TechnicalFailureException {
        basket.addProduct(Product.SPORTS);
        basket.addProduct(Product.NEWS);
        basket.addProduct(Product.MOVIES_1);
        basket.addProduct(Product.MOVIES_2);
        Set<Addon> expectedResultList = new TreeSet<>();
        expectedResultList.add(Addon.SPORTS_3D_ADD_ON);
        expectedResultList.add(Addon.NEWS_3D_ADD_ON);
        expectedResultList.add(Addon.MOVIES_3D_ADD_ON);

        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_AVAILABLE);
        Set<Addon> actualResultList = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE).getAvailableAddons();

        assertEquals(expectedResultList, actualResultList);
    }

    @Test
    public void shouldNotReturnSports3DAddonProductForBasketWithSportsProductAndNon3DPostCode() throws TechnicalFailureException {
        basket.addProduct(Product.SPORTS);

        Set<Addon> expectedResultList = Collections.emptySet();
        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_UNAVAILABLE);

        Set<Addon> actualResultList = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE).getAvailableAddons();
        assertEquals(expectedResultList, actualResultList);
    }

    @Test
    public void shouldNotReturnSports3DAddonProductForBasketWithSportsProductAndPlanned3DPostCode() throws TechnicalFailureException {
        basket.addProduct(Product.SPORTS);
        Set<Addon> expectedResultSet = Collections.emptySet();

        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_PLANNED);

        Set<Addon> actualResultSet = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE).getAvailableAddons();
        assertEquals(expectedResultSet, actualResultSet);
    }


    @Test
    public void shouldReturnInvalidPostcodeIndicatorToIndicateInvalidPostCodeStatusfromAvailabilityChecker() throws TechnicalFailureException {
        basket.addProduct(Product.SPORTS);

        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(POSTCODE_INVALID);

        ThreeDeeResult actualResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);

        //then
        assertEquals(false, actualResult.isValidPostCode());
    }

    @Test
    public void shouldReturnEmptyAddonsToIndicateTechnicalExceptionFromAvailabilityChecker() throws TechnicalFailureException {
        basket.addProduct(Product.SPORTS);
        Set<Addon> expectedResultSet = Collections.emptySet();

        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenThrow(new TechnicalFailureException());

        ThreeDeeResult actualResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
        assertEquals(expectedResultSet, actualResult.getAvailableAddons());
    }

    @Test
    public void shouldReturnEmptyAddonsToIndicateExceptionFromAvailabilityChecker() throws Exception {
        basket.addProduct(Product.SPORTS);
        Set<Addon> expectedResultSet = Collections.emptySet();

        doThrow(Exception.class).when(availabilityChecker).isPostCodeIn3DTVServiceArea(anyString());

        ThreeDeeResult actualResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
        assertEquals(expectedResultSet, actualResult.getAvailableAddons());
    }

}

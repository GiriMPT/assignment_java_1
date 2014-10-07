package com.bskyb.onlinestore;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;
import com.bskyb.onlinestore.model.Addon;
import com.bskyb.onlinestore.model.Basket;
import com.bskyb.onlinestore.model.Product;
import com.bskyb.onlinestore.model.ThreeDeeResult;
import com.bskyb.onlinestore.service.ThreeDeeAddOnService;
import com.bskyb.onlinestore.service.ThreeDeeAddOnServiceImpl;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@Story(classpath = "/stories/threeDeeSuggest.story")
public class ThreeDeeSuggestSteps extends StoryWithSteps {

    private static final String SERVICE_AVAILABLE = "SERVICE_AVAILABLE";
    private static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
    private static final String POSTCODE_INVALID = "POSTCODE_INVALID";

    private ThreeDeeAddOnService threeDeeAddOnService;

    @Mock
    private AvailabilityChecker availabilityChecker;
    private Basket basket;
    private ThreeDeeResult threeDeeResult = null;
    private static final String VALID_POSTCODE = "SOME_POSTCODE";

    @BeforeStory
    public void setUpStory() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeScenario
    public void setUpScenario() {
        threeDeeAddOnService = new ThreeDeeAddOnServiceImpl(availabilityChecker);
        basket = new Basket();
        threeDeeResult = null;
    }

    @Given("a postcode along with following products in the basket: $productsTable1")
    public void usePostCode1(ExamplesTable productsTable1) {
        for (Map<String, String> row : productsTable1.getRows()) {
            basket.addProduct(Product.valueOf(row.get("products")));
        }
    }


    @When("the postcode is in 3D area and basket has 3D products")
    public void postCodeIsIn3DAreaAnd3DProductsAreInBasket() throws TechnicalFailureException {
        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_AVAILABLE);
        threeDeeResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
    }

    @Then("return following addons $addons")
    public void verifyAddOns1(ExamplesTable addonsTable) {
        Set<Addon> expectedResultSet = addonsTable.getRows().stream().map(row -> Addon.valueOf(row.get("addon"))).collect(Collectors.toCollection(TreeSet::new));
        assertEquals(expectedResultSet, threeDeeResult.getAvailableAddons());
    }


    @When("the postcode is in non-3D area and basket has 3D products")
    public void postCodeIsInNon3DAreaAnd3DProductsAreInBasket() throws TechnicalFailureException {
        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_UNAVAILABLE);
        threeDeeResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
    }

    @Then("return zero addons")
    public void verifyAddOns3() {
        Set<Addon> expectedResultSet = emptySet();
        assertEquals(expectedResultSet, threeDeeResult.getAvailableAddons());
    }

    @When("the postcode is in 3D area and basket has non 3D products")
    public void postCodeIsIn3DAreaAndNon3DProductsAreInBasket() throws TechnicalFailureException {
        //threeDeeAddOnService.setAvailabilityChecker(availabilityChecker);
        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(SERVICE_AVAILABLE);
        threeDeeResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
    }

    @Then("no addons should be returned")
    public void verifyAddOns2() {
        Set<Addon> expectedResultSet = emptySet();
        assertEquals(expectedResultSet, threeDeeResult.getAvailableAddons());
    }

    @When("the postcode is invalid")
    public void invalidPostCode() throws TechnicalFailureException {
        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenReturn(POSTCODE_INVALID);
        threeDeeResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
    }

    @Then("addons should not be returned")
    public void verifyAddOns5() {
        Set<Addon> expectedResultSet = emptySet();
        assertEquals(expectedResultSet, threeDeeResult.getAvailableAddons());
        assertFalse(threeDeeResult.isValidPostCode());
    }

    @When("there is technical failure")
    public void postCodeLookUpTechnicalFailure() throws TechnicalFailureException {
        when(availabilityChecker.isPostCodeIn3DTVServiceArea(anyString())).thenThrow(new TechnicalFailureException());
        threeDeeResult = threeDeeAddOnService.checkFor3DAddOnProducts(basket, VALID_POSTCODE);
    }

    @Then("empty addons should be returned")
    public void verifyAddOns4() {
        Set<Addon> expectedResultSet = emptySet();
        assertEquals(expectedResultSet, threeDeeResult.getAvailableAddons());
    }

}

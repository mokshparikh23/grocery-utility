package com.company.grocery;

import static org.junit.Assert.*;
import java.util.Comparator;
import org.junit.Before;
import org.junit.Test;

public class GroceryUtilsTest {
    private static final double DELTA = 0.0001;
    private Product apple, banana, bread, milk, orange;
    private Product[] products;

    @Before
    public void setUp() {
        apple = new Product("Apple", "Fruits", 5.00, 20);
        banana = new Product("Banana", "Fruits", 2.50, 15);
        bread = new Product("Bread", "Bakery", 3.75, 8);
        milk = new Product("Milk", "Dairy", 4.25, 5);
        orange = new Product("Orange", "Fruits", 6.50, 10);
        products = new Product[] { orange, milk, apple, bread, banana };
    }

    // Sorting: Product[]
    @Test public void sortProductsByName() {
        GroceryUtils.sort(products);
        assertArrayEquals(new Product[]{apple,banana,bread,milk,orange}, products);
    }

    @Test public void sortProductsIgnoresCase() {
        Product a = new Product("apple", "Fruits", 1, 1);
        Product b = new Product("Banana", "Fruits", 1, 1);
        Product[] input = { b, a };
        GroceryUtils.sort(input);
        assertSame(a, input[0]);
        assertSame(b, input[1]);
    }

    @Test public void sortProductsUsingComparator() {
        GroceryUtils.sort(products,
                Comparator.comparingDouble(Product::getPrice));
        assertArrayEquals(new Product[]{banana,bread,milk,apple,orange}, products);
    }

    @Test public void sortProductsUsingDescendingQuantity() {
        GroceryUtils.sort(products,
                Comparator.comparingInt(Product::getQuantity).reversed());
        assertArrayEquals(new Product[]{apple,banana,orange,bread,milk}, products);
    }

    @Test public void sortEmptyProductArray() {
        Product[] input = {};
        GroceryUtils.sort(input);
        assertEquals(0, input.length);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNullProductArrayRejected() {
        GroceryUtils.sort((Product[]) null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNullComparatorRejected() {
        GroceryUtils.sort(products, (Comparator<Product>) null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNullProductElementRejected() {
        GroceryUtils.sort(new Product[]{apple,null});
    }

    // Sorting: String[] and double[]
    @Test public void sortNamesAlphabetically() {
        String[] names = {"Orange","Apple","Milk","Banana"};
        GroceryUtils.sort(names);
        assertArrayEquals(new String[]{"Apple","Banana","Milk","Orange"}, names);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNullNamesRejected() {
        GroceryUtils.sort((String[]) null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNullNameElementRejected() {
        GroceryUtils.sort(new String[]{"Apple",null});
    }

    @Test public void sortPricesAscending() {
        double[] input = {6.5,2.5,5.0,3.75,4.25};
        GroceryUtils.sort(input);
        assertArrayEquals(new double[]{2.5,3.75,4.25,5.0,6.5}, input, DELTA);
    }

    @Test public void sortDuplicateAndZeroPrices() {
        double[] input = {5.0,0.0,2.5,5.0};
        GroceryUtils.sort(input);
        assertArrayEquals(new double[]{0.0,2.5,5.0,5.0}, input, DELTA);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNegativePriceRejected() {
        GroceryUtils.sort(new double[]{2.5,-1.0});
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortNaNPriceRejected() {
        GroceryUtils.sort(new double[]{2.5,Double.NaN});
    }

    @Test(expected=IllegalArgumentException.class)
    public void sortInfinitePriceRejected() {
        GroceryUtils.sort(new double[]{2.5,Double.POSITIVE_INFINITY});
    }

    // Category filtering
    @Test public void filterCategoryMatchesIgnoringCaseAndPreservesOrder() {
        Product[] result = GroceryUtils.filterByCategory(products, "fRuItS");
        assertArrayEquals(new Product[]{orange,apple,banana}, result);
    }

    @Test public void filterCategoryTrimsCriterion() {
        assertArrayEquals(new Product[]{orange,apple,banana},
                GroceryUtils.filterByCategory(products, "  Fruits  "));
    }

    @Test public void filterCategoryNoMatchReturnsEmpty() {
        assertEquals(0,
                GroceryUtils.filterByCategory(products,"Vegetables").length);
    }

    @Test public void filterCategoryReturnsNewArrayAndDoesNotMutate() {
        Product[] original = products.clone();
        Product[] result = GroceryUtils.filterByCategory(products,"Fruits");
        assertNotSame(products, result);
        assertArrayEquals(original, products);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullCategoryRejected() {
        GroceryUtils.filterByCategory(products,null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void blankCategoryRejected() {
        GroceryUtils.filterByCategory(products,"   ");
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullProductsForCategoryRejected() {
        GroceryUtils.filterByCategory(null,"Fruits");
    }

    // Price filtering
    @Test public void filterPriceUsesInclusiveBounds() {
        assertArrayEquals(new Product[]{orange,apple},
                GroceryUtils.filterByPrice(products,5.0,6.5));
    }

    @Test public void filterPriceEqualBoundsFindsExactPrice() {
        assertArrayEquals(new Product[]{apple},
                GroceryUtils.filterByPrice(products,5.0,5.0));
    }

    @Test public void filterPriceWideFiniteRangeReturnsAllAsCopy() {
        Product[] result = GroceryUtils.filterByPrice(products,0,Double.MAX_VALUE);
        assertArrayEquals(products,result);
        assertNotSame(products,result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void reversedPriceRangeRejected() {
        GroceryUtils.filterByPrice(products,10,5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativePriceBoundaryRejected() {
        GroceryUtils.filterByPrice(products,-1,5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nanPriceBoundaryRejected() {
        GroceryUtils.filterByPrice(products,0,Double.NaN);
    }

    @Test(expected=IllegalArgumentException.class)
    public void infinitePriceBoundaryRejected() {
        GroceryUtils.filterByPrice(products,0,Double.POSITIVE_INFINITY);
    }

    // Name and quantity filtering
    @Test public void filterNameContainsIgnoringCase() {
        assertArrayEquals(new Product[]{orange,banana},
                GroceryUtils.filterByName(products,"AN"));
    }

    @Test public void filterNameTrimsKeyword() {
        assertArrayEquals(new Product[]{apple},
                GroceryUtils.filterByName(products,"  apple  "));
    }

    @Test(expected=IllegalArgumentException.class)
    public void blankKeywordRejected() {
        GroceryUtils.filterByName(products,"  ");
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullKeywordRejected() {
        GroceryUtils.filterByName(products,null);
    }

    @Test public void filterQuantityIncludesMinimum() {
        assertArrayEquals(new Product[]{apple,banana},
                GroceryUtils.filterByQuantity(products,15));
    }

    @Test public void zeroMinimumReturnsAllAsCopy() {
        Product[] result = GroceryUtils.filterByQuantity(products,0);
        assertArrayEquals(products,result);
        assertNotSame(products,result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativeMinimumQuantityRejected() {
        GroceryUtils.filterByQuantity(products,-1);
    }

    // Product conversion
    @Test public void productsConvertToStrings() {
        assertArrayEquals(new String[]{"Apple,Fruits,5.0,20","Milk,Dairy,4.25,5"},
                GroceryUtils.productsToStringArray(new Product[]{apple,milk}));
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullProductArrayConversionRejected() {
        GroceryUtils.productsToStringArray(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullProductElementConversionRejected() {
        GroceryUtils.productsToStringArray(new Product[]{apple,null});
    }

    @Test(expected=IllegalArgumentException.class)
    public void commaInProductNameRejected() {
        Product p = new Product("Tomatoes, Canned","Goods",3.5,4);
        GroceryUtils.productsToStringArray(new Product[]{p});
    }

    @Test public void stringsConvertToProductsAndTrimFields() {
        Product[] result = GroceryUtils.stringsToProducts(
                new String[]{" Apple , Fruits , 5.0 , 20 "});
        assertArrayEquals(new Product[]{apple},result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void malformedProductRecordRejected() {
        GroceryUtils.stringsToProducts(new String[]{"Apple,Fruits,5.0"});
    }

    @Test(expected=IllegalArgumentException.class)
    public void extraProductFieldRejected() {
        GroceryUtils.stringsToProducts(
                new String[]{"Apple,Fruits,5.0,20,Extra"});
    }

    @Test(expected=IllegalArgumentException.class)
    public void blankProductNameRejected() {
        GroceryUtils.stringsToProducts(new String[]{" ,Fruits,5.0,20"});
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidProductPriceRejected() {
        GroceryUtils.stringsToProducts(new String[]{"Apple,Fruits,wrong,20"});
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativeProductPriceRejected() {
        GroceryUtils.stringsToProducts(new String[]{"Apple,Fruits,-1,20"});
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidProductQuantityRejected() {
        GroceryUtils.stringsToProducts(new String[]{"Apple,Fruits,5.0,wrong"});
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativeProductQuantityRejected() {
        GroceryUtils.stringsToProducts(new String[]{"Apple,Fruits,5.0,-1"});
    }

    @Test public void productConversionRoundTripPreservesData() {
        assertArrayEquals(products, GroceryUtils.stringsToProducts(
                GroceryUtils.productsToStringArray(products)));
    }

    // Primitive copies
    @Test public void priceConversionReturnsIndependentCopy() {
        double[] input = {2.5,5.0};
        double[] result = GroceryUtils.pricesToDoubleArray(input);
        assertNotSame(input,result);
        result[0]=100;
        assertEquals(2.5,input[0],DELTA);
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidPriceCopyInputRejected() {
        GroceryUtils.pricesToDoubleArray(new double[]{2.5,-1});
    }

    @Test public void quantityConversionReturnsIndependentCopy() {
        int[] input = {5,10};
        int[] result = GroceryUtils.quantitiesToIntArray(input);
        assertNotSame(input,result);
        result[0]=100;
        assertEquals(5,input[0]);
    }

    @Test(expected=IllegalArgumentException.class)
    public void negativeQuantityCopyInputRejected() {
        GroceryUtils.quantitiesToIntArray(new int[]{5,-1});
    }

    // Product information and totals
    @Test public void singleProductInfoUsesStandardFormat() {
        assertEquals("Apple,Fruits,5.0,20",
                GroceryUtils.getProductInfo(apple));
    }

    @Test public void multipleProductInfoUsesLinesWithoutTrailingSeparator() {
        String result = GroceryUtils.getProductInfo(new Product[]{apple,milk});
        assertEquals("Apple,Fruits,5.0,20" + System.lineSeparator()
                + "Milk,Dairy,4.25,5", result);
        assertFalse(result.endsWith(System.lineSeparator()));
    }

    @Test public void emptyProductInfoIsEmptyString() {
        assertEquals("", GroceryUtils.getProductInfo(new Product[0]));
    }

    @Test public void totalPriceUsesPriceTimesQuantity() {
        assertEquals(253.75,GroceryUtils.getTotalPrice(products),DELTA);
    }

    @Test public void emptyTotalPriceIsZero() {
        assertEquals(0,GroceryUtils.getTotalPrice(new Product[0]),DELTA);
    }

    @Test public void totalQuantitySumsQuantities() {
        assertEquals(58,GroceryUtils.getTotalQuantity(products));
    }

    @Test public void emptyTotalQuantityIsZero() {
        assertEquals(0,GroceryUtils.getTotalQuantity(new Product[0]));
    }

    @Test(expected=IllegalArgumentException.class)
    public void nullElementForTotalRejected() {
        GroceryUtils.getTotalPrice(new Product[]{apple,null});
    }
}

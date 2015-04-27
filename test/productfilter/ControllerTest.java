/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collection;
import static productfilter.Controller.TAG_CONTROLLER;

/**
 *
 * @author Simon
 */
public class ControllerTest {

    /**
     * Test of select method, of class Controller.
     */
    @Test
    public void testSelect() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);
        Controller controller = new Controller(input, output, logger);
        
        Product product1 = new Product(1, "dress", Color.RED, new BigDecimal(200));
        Product product2 = new Product(1, "skirt", Color.BLUE, new BigDecimal(100));
        Collection<Product> allProducts = new ArrayList<>();
        allProducts.add(product1);
        allProducts.add(product2);
        
        when(input.obtainProducts()).thenReturn(allProducts);
        
        Filter filter = mock(Filter.class);
        
        when(filter.passes(product1)).thenReturn(Boolean.TRUE);
        when(filter.passes(product2)).thenReturn(Boolean.FALSE);
        
        controller.select(filter);
        
        Collection<Product> selectedProducts = new ArrayList<>();
        selectedProducts.add(product1);
        
        verify(output).postSelectedProducts(selectedProducts);
    }
    
    @Test
    public void testLogOnSuccess() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);
        Controller controller = new Controller(input, output, logger);

        Product product1 = new Product(1, "dress", Color.RED, new BigDecimal(200));
        Product product2 = new Product(1, "skirt", Color.BLUE, new BigDecimal(100));
        Collection<Product> allProducts = new ArrayList<>();
        allProducts.add(product1);
        allProducts.add(product2);

        when(input.obtainProducts()).thenReturn(allProducts);

        Filter filter = mock(Filter.class);

        when(filter.passes(product1)).thenReturn(Boolean.TRUE);
        when(filter.passes(product2)).thenReturn(Boolean.FALSE);

        controller.select(filter);

        Collection<Product> selectedProducts = new ArrayList<>();
        selectedProducts.add(product1);

        verify(logger).setLevel("INFO");
        verify(logger).log(TAG_CONTROLLER,
                "Successfully selected " + selectedProducts.size()
                + " out of " + allProducts.size() + " available products.");
    }
    
    @Test
    public void testIfExceptionLoggedIfThrownWhenObtainingProductData() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);
        Controller controller = new Controller(input, output, logger);

        ObtainFailedException obtainFailedException = new ObtainFailedException();
        when(input.obtainProducts()).thenThrow(obtainFailedException);
        controller.select(null);
        
        verify(logger).setLevel("ERROR");
        verify(logger).log(TAG_CONTROLLER, "Filter procedure failed with exception: " + obtainFailedException);
    }
    
    @Test
    public void testNothingPassedToOutputIfExceptionThrownWhenObtainingProductData() throws ObtainFailedException {
        Input input = mock(Input.class);
        Output output = mock(Output.class);
        Logger logger = mock(Logger.class);
        Controller controller = new Controller(input, output, logger);

        ObtainFailedException obtainFailedException = new ObtainFailedException();
        when(input.obtainProducts()).thenThrow(obtainFailedException);
        controller.select(null);

        verify(output, times(0)).postSelectedProducts(Matchers.anyCollectionOf(Product.class));
    }
}

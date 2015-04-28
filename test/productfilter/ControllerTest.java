/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import java.util.ArrayList;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
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
        
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        
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

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
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
        Input input = new InputDummy();
        OutputSpy output = new OutputSpy();
        Logger logger = new LoggerDummy();
        Controller controller = new Controller(input, output, logger);

        controller.select(null);

        assertEquals(0, output.postSelectedProductsCallCount);
        
    }
    
    private class InputDummy implements Input {

        @Override
        public Collection<Product> obtainProducts() throws ObtainFailedException {
            throw new ObtainFailedException();
        }
        
    }
    
    private class OutputSpy implements Output {
        private int postSelectedProductsCallCount = 0;
        
        @Override
        public void postSelectedProducts(Collection<Product> products) {
            postSelectedProductsCallCount++;
        }
        
        public int getpostSelectedProductsCallCount() {
            return postSelectedProductsCallCount;
        }
    }
    
    private class LoggerDummy implements Logger {

        @Override
        public void setLevel(String level) {
        }

        @Override
        public void log(String tag, String message) {
        }
        
    }
}

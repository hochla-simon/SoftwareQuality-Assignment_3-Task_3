/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package productfilter;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author xorsag2
 */
public class AtLeastNOfFilterTest {
    
    public AtLeastNOfFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of passes method, of class AtLeastNOfFilter.
     */
    
    @Test
    public void testPassesNumberOfAllFilters() {
        Product product = mock(Product.class);
        Filter firstChildPassFilter = mock(Filter.class);
        Filter secondChildPassFilter = mock(Filter.class);
        when(firstChildPassFilter.passes(product)).thenReturn(true);
        when(secondChildPassFilter.passes(product)).thenReturn(true);
        int n = 2;        
        AtLeastNOfFilter parentFilter = new AtLeastNOfFilter(n, secondChildPassFilter, firstChildPassFilter);
        assertTrue(parentFilter.passes(product));
    }
    
    @Test
    public void testPassesNumberOfFiltersGreatherThanN() {
        Product product = mock(Product.class);
        Filter firstChildPassFilter = mock(Filter.class);
        Filter secondChildPassFilter = mock(Filter.class);
        when(firstChildPassFilter.passes(product)).thenReturn(true);
        when(secondChildPassFilter.passes(product)).thenReturn(true);
        int n = 1;        
        AtLeastNOfFilter parentFilter = new AtLeastNOfFilter(n, secondChildPassFilter, firstChildPassFilter);
        assertTrue(parentFilter.passes(product));
    }
    
    @Test
    public void testPassesNumberOfFiltersGreatherThanNWithFail() {
        Product product = mock(Product.class);
        Filter childFailFilter = mock(Filter.class);
        Filter childPassFilter = mock(Filter.class);
        
        when(childFailFilter.passes(product)).thenReturn(false);
        when(childPassFilter.passes(product)).thenReturn(true);
        int n = 1;        
        AtLeastNOfFilter parentFilter = new AtLeastNOfFilter(n, childFailFilter, childPassFilter);
        assertTrue(parentFilter.passes(product));
    }
    
    @Test
    public void testPassesFail() {
        Product product = mock(Product.class);
        Filter childFailFilter = mock(Filter.class);
        Filter childPassFilter = mock(Filter.class);
        
        when(childFailFilter.passes(product)).thenReturn(false);
        when(childPassFilter.passes(product)).thenReturn(true);
        int n = 2;        
        AtLeastNOfFilter parentFilter = new AtLeastNOfFilter(n, childFailFilter, childPassFilter);
        assertFalse(parentFilter.passes(product));
        
    }
    
    
    @Test(expected = IllegalArgumentException.class) 
    public void testConstructorIllegalArgumentException() {
        Filter filter1 = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        int n = 0;        
        AtLeastNOfFilter filter = new AtLeastNOfFilter(n, filter1, filter2);
    }
    
    @Test(expected = FilterNeverSucceeds.class)  
    public void testConstructorFilterNeverSucceeds() {        
        Filter filter1 = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        int n = 3;        
        AtLeastNOfFilter filter = new AtLeastNOfFilter(n, filter1, filter2);
    }
    
}

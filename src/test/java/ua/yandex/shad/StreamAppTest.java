
package ua.yandex.shad;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import ua.yandex.shad.stream.*;

public class StreamAppTest {
    
    private IntStream intStream;
    private IntStream intStreamEmpty;
    private IntStream intStreamNotMonotone;
    
    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
        int[] intArrEmpty = {};
        intStreamEmpty = AsIntStream.of(intArrEmpty);
        int[] intArrNotMonotone = {0, -1, 1, 2, 3};
        intStreamNotMonotone = AsIntStream.of(intArrNotMonotone);
    }
    
    @Test
    public void testStreamConstructor() {
        System.out.println("streamConstructor");
        StreamApp streamApp = new StreamApp();      
    }
    
    @Test
    public void testStreamOperations() {
        System.out.println("streamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testStreamToArray() {
        System.out.println("streamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);
        assertArrayEquals(expResult, result);        
    }

    @Test
    public void testStreamForEach() {
        System.out.println("streamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void testStreamAverage() {
        System.out.println("streamAverage");
        Double expResult = 1.0;
        Double result = intStream.average();
        assertEquals(expResult, result, 0.00001);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testStreamAverageWhenValArrayEmpty() {
        System.out.println("streamAverageEmpty");
        intStreamEmpty.average();
    }
    
    @Test
    public void testStreamMax() {
        System.out.println("streamMax");
        Integer expResult = 3;
        Integer result = intStreamNotMonotone.max();
        assertEquals(expResult, result);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testStreamMaxEmpty() {
        System.out.println("streamMaxEmpty");
        intStreamEmpty.max();
    }
    
    @Test
    public void testStreamMin() {
        System.out.println("streamMin");
        Integer expResult = -1;
        Integer result = intStreamNotMonotone.min();
        assertEquals(expResult, result);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testStreamMinEmpty() {
        System.out.println("streamMinEmpty");
        intStreamEmpty.min();
    }
    
    @Test
    public void testStreamSum() {
        System.out.println("streamSum");
        Integer expResult = 5;
        Integer result = intStream.sum();
        assertEquals(expResult, result);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testStreamSumEmpty() {
        System.out.println("streamSumEmpty");
        intStreamEmpty.sum();
    }
    
    @Test
    public void testStreamGetOperationNumberByName() {
        System.out.println("streamGetOperationNumberByName");
        int expResult = 1;
        int result = ((AsIntStream) intStreamEmpty)
                                        .getOperationNumberByName("MAP");
        assertEquals(expResult, result);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testStreamGetOperationNumberByNotExistingName() {
        System.out.println("streamGetOperationNumberByNameThatDoesNotExist");
        ((AsIntStream) intStreamEmpty).getOperationNumberByName("YO");
    }
    
    @Test
    public void testStreamGetOperationNames() {
        System.out.println("streamGetOperationNames");
        String[] expResult = {"FILTER", "MAP", "FLATMAP"};
        String[] result = ((AsIntStream) intStreamEmpty).getOperationNames();
        Assert.assertArrayEquals(expResult, result);
    }
    
}

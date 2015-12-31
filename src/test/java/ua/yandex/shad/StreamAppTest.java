
package ua.yandex.shad;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import ua.yandex.shad.stream.*;

public class StreamAppTest {
    
    private IntStream intStream;
    private IntStream intStreamEmpty;
    private IntStream intStreamNotMonotone;
    private DynamicArray<Integer> dynamicArr;
    
    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
        int[] intArrEmpty = {};
        intStreamEmpty = AsIntStream.of(intArrEmpty);
        int[] intArrNotMonotone = {0, -1, 1, 2, 3};
        intStreamNotMonotone = AsIntStream.of(intArrNotMonotone);
        dynamicArr = new DynamicArray<>(1, 2, 3);
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
    
    @Test (expected = NoSuchElementException.class)
    public void testDynamicArrayIteratorNextWhenNotHasNext() {
        Iterator<Integer> iter = dynamicArr.iterator();
        iter.next();
        iter.next();
        iter.next();
        iter.next();
    }
    
    @Test
    public void testDynamicArrayConstructorOfCopy() {
        DynamicArray<Integer> dynamicArrDiff = new DynamicArray<>(dynamicArr);
        Integer[] valDiff = dynamicArrDiff.toArray(new Integer[1]);
        Integer[] val = dynamicArr.toArray(new Integer[1]);
        assertArrayEquals(val, valDiff);
    }
    
    @Test
    public void testDynamicArrayAddOtherDynArrayWhenNullInput() {
        DynamicArray<Integer> dynArr = null;
        boolean expResult = false;
        boolean result = dynamicArr.add(dynArr);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayAddOneElementWhenNullInput() {
        Integer elem = null;
        boolean expResult = false;
        boolean result = dynamicArr.add(elem);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayAddElemArrayWhenNullInput() {
        Integer[] arr = null;
        boolean expResult = false;
        boolean result = dynamicArr.add(arr);
        assertEquals(expResult, result);
    }
    
    class A {
        int val;
        
        A(int i) {
            val = i;
        }
        
        public int getVal() {
            return val;
        }
    }
    
    class B extends A {

        public B(int i) {
            super(i);
        }
        
    }
    
    @Test
    public void testDynamicArrayAddAll() {
        DynamicArray<A> dynArray = new DynamicArray<>();
        B[] val = new B[2];
        val[0] = new B(1);
        val[1] = new B(5);
        DynamicArray<B> yo = new DynamicArray<>(val);
        dynArray.addAll(yo);
        
        int expResultOne = 1;
        int expResultTwo = 5;
        
        int resultOne = dynArray.getAt(0).getVal();
        int resultTwo = dynArray.getAt(1).getVal();
        
        assertEquals(expResultOne, resultOne);
        assertEquals(expResultTwo, resultTwo);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testDynamicArrayChangeAtWhenBadIndex() {
        dynamicArr.changeAt(100, 1);
    }
    
    @Test
    public void testDynamicArrayContainsWhenContains() {
        boolean expResult = true;
        boolean result = dynamicArr.contains(1);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayContainsWhenDoesNotContain() {
        boolean expResult = false;
        boolean result = dynamicArr.contains(10);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayContainsAllWhenContains() {
        boolean expResult = true;
        boolean result = dynamicArr.containsAll(
                                        new DynamicArray<Integer>(1, 2, 2));
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayContainsAllWhenDoesNotContain() {
        boolean expResult = false;
        boolean result = dynamicArr.containsAll(
                                        new DynamicArray<Integer>(1, 4, 2));
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayCopyValuesDueConstructorWhenAddOneElement() {
        DynamicArray<Integer> dynArr = new DynamicArray<>(1);
        int expResult = 1;
        int result = dynArr.getAt(0) * dynArr.size();
        assertEquals(expResult, result);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testDynamicArrayGetAtWhenBadIndex() {
        dynamicArr.getAt(100);
    }
    
    @Test
    public void testDynamicArrayRemoveWhenExists() {
        boolean expResult = true;
        boolean result = dynamicArr.remove(2);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayRemoveWhenDoesNotExist() {
        boolean expResult = false;
        boolean result = dynamicArr.remove(0);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDynamicArrayRemoveAllWhenExists() {
        dynamicArr.removeAll(new DynamicArray(3, 4, 1));
        int expResult = 1;
        int result = (dynamicArr.getAt(0) - 1) * dynamicArr.size();
        assertEquals(expResult, result);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testDynamicArrayRemoveAtWhenBadIndex() {
        dynamicArr.removeAt(100);
    }
    
    @Test
    public void testDynamicArrayRetainAll() {
        DynamicArray<Integer> coll = new DynamicArray<>(1, 3, 6, 5);
        dynamicArr.retainAll(coll);
        int expResult = 2;
        int result = dynamicArr.size();
        assertEquals(expResult, result);
    }
    
}

package ua.yandex.shad.stream;

import ua.yandex.shad.function.IntUnaryOperator;
import ua.yandex.shad.function.IntToIntStreamFunction;
import ua.yandex.shad.function.IntPredicate;
import ua.yandex.shad.function.IntConsumer;
import ua.yandex.shad.function.IntBinaryOperator;

import ua.yandex.shad.DynamicArray;
import java.util.Arrays;
import ua.yandex.shad.function.IntStreamOperation;

public class AsIntStream implements IntStream {

    private static final String NO_VALUES_IN_STREAM
                                    = "There are no values in stream";
    
    private static enum OperationName {
        FILTER, 
        MAP, 
        FLATMAP
    }
    
    private DynamicArray<Integer> valuesArray = new DynamicArray<>();
    private DynamicArray<IntStreamOperation> operations = new DynamicArray<>();
    private DynamicArray<OperationName> operationNames = new DynamicArray<>();
    
    private AsIntStream() {
        // To Do
    }

    public static IntStream of(int... values) {
        AsIntStream stream = new AsIntStream();
        
        Integer[] val = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            val[i] = values[i];
        }
        stream.valuesArray.add(val);
        
        return stream;
    }

    @Override
    public Double average() throws IllegalArgumentException {
        if (valuesArray.isEmpty()) {
            throw new IllegalArgumentException(NO_VALUES_IN_STREAM);
        }
        doOldOperations();
        
        return (double) sum() / count();
    }

    @Override
    public Integer max() throws IllegalArgumentException {
        if (valuesArray.isEmpty()) {
            throw new IllegalArgumentException(NO_VALUES_IN_STREAM);
        }
        doOldOperations();
        
        Integer maxValue = Integer.MIN_VALUE;
        for (Integer val: valuesArray) {
            if (val > maxValue) {
                maxValue = val;
            }
        }
        return maxValue;
    }

    @Override
    public Integer min() throws IllegalArgumentException {
        if (valuesArray.isEmpty()) {
            throw new IllegalArgumentException(NO_VALUES_IN_STREAM);
        }
        doOldOperations();
        
        Integer minValue = Integer.MAX_VALUE;
        for (Integer val: valuesArray) {
            if (val > minValue) {
                minValue = val;
            }
        }
        return minValue;
    }

    @Override
    public long count() {
        doOldOperations();
        return valuesArray.size();
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        operations.add(predicate);
        operationNames.add(OperationName.FILTER);
        return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        doOldOperations();
        for (Integer val: valuesArray) {
            action.accept(val.intValue());
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        operations.add(mapper);
        operationNames.add(OperationName.MAP);
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        doOldOperations();
        
        int result = identity;
        for (Integer val: valuesArray) {
            result = op.apply(result, val);
        }
        
        return result;
    }

    @Override
    public Integer sum() throws IllegalArgumentException {
        if (valuesArray.isEmpty()) {
            throw new IllegalArgumentException(NO_VALUES_IN_STREAM);
        }
        //doOldOperations() will be called in reduce;
        return reduce(0, (sum, val) -> sum += val);
    }

    @Override
    public int[] toArray() {
        doOldOperations();
        int size = valuesArray.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = valuesArray.getAt(i);
        }
        return array;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        operations.add(func);
        operationNames.add(OperationName.FLATMAP);
        return this;
    }
    
    private void doFilter(IntPredicate predicate) {
        int valuesSize = valuesArray.size();
        DynamicArray<Integer> newValArray = new DynamicArray<>();
        for (int i = 0; i < valuesSize; i++) {
            if (predicate.test(valuesArray.getAt(i))) {
                newValArray.add(valuesArray.getAt(i));
            }
        }
        valuesArray = newValArray;
    }
    
    private void doMap(IntUnaryOperator mapper) {
        int valuesSize = valuesArray.size();
        for (int i = 0; i < valuesSize; i++) {
            valuesArray.changeAt(i, mapper.apply(valuesArray.getAt(i)));
        }
    }
    
    private void doFlatMap(IntToIntStreamFunction func) {
        DynamicArray<Integer> resultArray = new DynamicArray<>();

        int valuesSize = valuesArray.size();
        for (int i = 0; i < valuesSize; i++) {
            AsIntStream oneStream 
                    = (AsIntStream) func.applyAsIntStream(valuesArray.getAt(i));
            resultArray.add(oneStream.valuesArray);
        }

        valuesArray = resultArray;
    }
    
    private void doOldOperations() {
        int numberOfOper = operationNames.size();
        for (int i = 0; i < numberOfOper; i++) {
            OperationName operName = operationNames.getAt(i);
            if (operName == OperationName.FILTER) {
                doFilter((IntPredicate) operations.getAt(i));
                continue;
            }
            if (operName == OperationName.MAP) {
                doMap((IntUnaryOperator) operations.getAt(i));
                continue;
            }
            if (operName == OperationName.FLATMAP) {
                doFlatMap((IntToIntStreamFunction) operations.getAt(i));
            }
        }
        operations.clear();
        operationNames.clear();
    }

}

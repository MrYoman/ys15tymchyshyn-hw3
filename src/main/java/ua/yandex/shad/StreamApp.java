package ua.yandex.shad;

import ua.yandex.shad.stream.IntStream;
import ua.yandex.shad.stream.AsIntStream;

public class StreamApp {

    public static int streamOperations(IntStream intStream) {
        int res = intStream
                .filter(x -> x > 0)
                .map(x -> x * x)
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1))
                .reduce(0, (sum, x) -> sum += x);
        return res;
    }

    public static int[] streamToArray(IntStream intStream) {        
        int[] intArr = intStream.toArray();
        return intArr;
    }

    public static String streamForEach(IntStream intStream) {        
        StringBuilder str = new StringBuilder();
        intStream.forEach(x -> str.append(x));
        return str.toString();
    }
}

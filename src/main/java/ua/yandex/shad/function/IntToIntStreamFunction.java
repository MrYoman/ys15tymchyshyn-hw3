package ua.yandex.shad.function;

import ua.yandex.shad.stream.IntStream;

public interface IntToIntStreamFunction extends IntStreamOperation {
     IntStream applyAsIntStream(int value);
}

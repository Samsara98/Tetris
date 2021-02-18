import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
    public static int add(int a, int b) {
        // TODO
        return a+b;
    }

    public static void main(String[] args) {
        assert 2 == add(1, 1);
        assert 3 == add(1, 2);
        assert 0 == add(-1, 1); // 测试负数
        assert 1 == add(0, 1); // 测试0
    }
}

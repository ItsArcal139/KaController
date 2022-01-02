package com.kakaouo.mods.kacontroller.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public enum KakaUtils {
    ;

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> mapCollector() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}

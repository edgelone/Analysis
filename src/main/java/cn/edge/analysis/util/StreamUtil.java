package cn.edge.analysis.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamUtil {

  /**
   * Open stream of list.
   *
   * @param list may be {@literal null}.
   * @return Stream<T>
   */
  public static <T> Stream<T> open(List<T> list) {
    return Optional.ofNullable(list)
        .map(Collection::stream)
        .orElse(Stream.empty());
  }

  /**
   * Concat several streams into one stream.
   *
   * @param streams
   * @param <T>
   * @return Stream<T>
   */
  public static <T> Stream<T> concat(Stream<T>... streams) {
    return Stream.of(streams)
        .filter(Objects::nonNull)
        .flatMap(Function.identity());
  }

  /**
   * Concat several lists into one stream.
   *
   * @param lists
   * @param <T>
   * @return Stream<T>
   */
  public static <T> Stream<T> concat(List<T>... lists) {
    return Arrays.stream(lists)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream);
  }

  private StreamUtil() {
  }


  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
  }
}

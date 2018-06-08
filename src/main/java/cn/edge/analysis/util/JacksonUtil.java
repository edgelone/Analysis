package cn.edge.analysis.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 */
public class JacksonUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

  }

  private JacksonUtil() {
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  /**
   * 获取泛型的Collection Type
   *
   * @param collectionClass 泛型的Collection
   * @param elementClasses  元素类
   * @return JavaType Java类型
   * @since 1.0
   */
  public static JavaType getCollectionType(Class<?> collectionClass,
                                           Class<?>... elementClasses) {
    return getObjectMapper().getTypeFactory()
        .constructParametricType(collectionClass, elementClasses);
  }

  public static String writeValue(Object data) {
    try {
      if (data != null) {
        return objectMapper.writeValueAsString(data);
      }
    } catch (IOException e) {
      LoggerFactory.getLogger(JacksonUtil.class)
          .error("jackson write value error ", e);
    }
    return null;
  }

  public static <T> T readValue(String json, Class<T> valueType) {
    return readValue(json, valueType, false);
  }

  public static <T> T readValue(
      String json, Class<T> valueType, boolean noErrorLog) {

    try {
      if (!StringUtils.isEmpty(json)) {
        return objectMapper.readValue(json, valueType);
      }
    } catch (IOException e) {
      if (!noErrorLog) {
        LoggerFactory.getLogger(JacksonUtil.class)
            .error("jackson read value error ", e);
      }
    }
    return null;
  }

  public static Map readValue(String json) {
    return readValue(json, Map.class);
  }

  public static <T> List<T> readValueList(String json, Class<T> valueType) {
    return readValueList(json, valueType, false);
  }

  public static <T> List<T> readValueList(
      String json, Class<T> valueType, boolean noErrorLog) {
    try {
      if (!StringUtils.isEmpty(json)) {
        return objectMapper.readValue(
            json,
            objectMapper.getTypeFactory().constructCollectionType(
                List.class, valueType));
      }
    } catch (IOException e) {
      if (!noErrorLog) {
        LoggerFactory.getLogger(JacksonUtil.class)
            .error("jackson read value error ", e);
      }
    }
    return null;
  }


  public static List<Map> readValueList(String json) {
    return readValueList(json, Map.class);
  }

  public static <K, V> Map<K, V> readValueMap(String json, Class<K> key,
                                              Class<V> value) {
    try {
      if (!StringUtils.isEmpty(json)) {
        return objectMapper.readValue(
            json,
            objectMapper.getTypeFactory().constructMapType(
                Map.class, key, value));
      }
    } catch (IOException e) {
      LoggerFactory.getLogger(JacksonUtil.class)
          .error("jackson read value error ", e);
    }
    return null;
  }

  public static <T> T readValueForBean(Object obj, Class<T> clazs) {
    if (obj instanceof String) {
      return readValue(obj.toString(), clazs);
    }
    return readValue(writeValue(obj), clazs);

  }

  public static <T> List<T> readValueForBeanList(Object obj, Class<T> clazs) {
    return readValueList(writeValue(obj), clazs);
  }

  public static <K, V> Map<K, List<V>> readValueListMap(String json,
                                                        Class<K> key, Class<V> value) {
    try {
      if (!StringUtils.isEmpty(json)) {
        return objectMapper.readValue(json,
            objectMapper.getTypeFactory().constructMapType(Map.class,
                objectMapper.getTypeFactory().constructType(key),
                objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, value)));
      }
    } catch (IOException e) {
      LoggerFactory.getLogger(JacksonUtil.class)
          .error("jackson read value error ", e);
    }
    return null;
  }

}

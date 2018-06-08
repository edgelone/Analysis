package cn.edge.analysis.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class XmlUtils {
  
  private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);
  

  private static XmlMapper xmlMapper = new XmlMapper();
  static {
    xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
  }
  
  
  public static <T> T fromXml(String xml, Class<T> claz) {
    try {
      if (xml == null || xml.isEmpty()) {return null;}
      return xmlMapper.readValue(xml,claz);
      
    } catch (Exception e) {
      logger.error("fromXml.fail", e);
      return null;
    }
  }
  public static <T> T fromXml(File xml, Class<T> claz) {
    try {
      if (xml == null || !xml.canRead()) {return null;}
      return xmlMapper.readValue(xml,claz);

    } catch (Exception e) {
      logger.error("fromXml.fail", e);
      return null;
    }
  }
  
}

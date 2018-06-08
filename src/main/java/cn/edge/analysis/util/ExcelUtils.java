package cn.edge.analysis.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.EntrySetMapIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lk on 2017/3/30.
 * Excel工具类
 */
@Slf4j
public class ExcelUtils {


  /**
   * 类SettingProperty的唯一实例
   */
  private static ExcelUtils excelUtils = null;

  /**
   * 获取类ConfigFile的实例
   *
   * @return 类ConfigFile的实例
   */
  public static synchronized ExcelUtils getInstance() {
    if (excelUtils == null) {
      excelUtils = new ExcelUtils();
    }
    return excelUtils;
  }

  public List<JSONObject> readExcelByResource(String resourcePath, Integer page, String key) {
    try {

      InputStream in = ExcelUtils.class.getResourceAsStream(resourcePath);
      if (in == null) {
        throw new RuntimeException("resource  not exists");
      }
      return readExcel(in, page, key);
    } catch (Exception e) {
      log.error("read excel error ", e);
    }
    return null;
  }


  public List<JSONObject> readExcel(String path, Integer page, String key) throws Exception {
    if (path == null || "".equals(path)) {
      return null;
    }
    File file = new File(path);
    if (!file.exists()) {
      return null;
    }

    /*读取整个文件*/
    InputStream in = new FileInputStream((path));
    return readExcel(in, page, key);
  }

  /**
   * xlsx读取方法
   *
   * @param key  不为空的字段
   * @param in   文件流
   * @param page 读取文件的活动页 默认为第一页 page=1
   * @return
   * @throws Exception
   */
  public List<JSONObject> readExcel(InputStream in, Integer page, String key) throws Exception {


    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);

    //获取当前页 默认第一页
    if (page == null) {
      page = 1;
    }
    XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(page - 1);
    if (xssfSheet == null) {
      return null;
    }

    List<JSONObject> resultList = new ArrayList<>();

    XSSFRow head = xssfSheet.getRow(0);

    Map<String, Integer> colRecordMap = new HashMap<>(head.getLastCellNum());

    /*处理表头*/
    for (int col = head.getFirstCellNum(); col < head.getLastCellNum(); col++) {
      XSSFCell cell = head.getCell(col);
      //过滤空列
      if (cell == null || "".equals(cell.toString())) {
        continue;
      }
      colRecordMap.put(cell.toString(), col);
    }

    /*开始处理每行数据*/

    for (int row = 1; row <= xssfSheet.getLastRowNum(); row++) {
      XSSFRow xssfRow = xssfSheet.getRow(row);
      if (xssfRow == null) {
        continue;
      }

      JSONObject obj = new JSONObject();

      if (!StringUtils.isEmpty(key)) {
        XSSFCell keyCell = xssfRow.getCell(colRecordMap.get(key));
        if (keyCell == null || "".equals(keyCell.toString())) {
          continue;
        }
      }

      EntrySetMapIterator<String, Integer> ite = new EntrySetMapIterator(colRecordMap);

      while (ite.hasNext()) {

        String k = ite.next();
        String v = String.valueOf(xssfRow.getCell(ite.getValue()));

        if (StringUtils.isEmpty(v)) {
          continue;
        }

        obj.put(k, v);

      }

      resultList.add(obj);
    }
    in.close();

    if (CollectionUtils.isEmpty(resultList)) {
      throw new RuntimeException("Excel is empty or wrong");
    }

    return resultList;
  }


}
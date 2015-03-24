package com.nissan.tests.utils;

import java.util.Arrays;
import java.util.List;

import com.nissan.tests.framework.Log;

public class StringUtil {

  public List<String> stringListConverter(String list) {

    List<String> listItems = null;
    try {
      listItems = Arrays.asList(list.split("\\s*,\\s*"));
    }
    catch (Exception e) {
      Log.messageRed("Invalid string. Cannot convert to List<String>");
    }

    return listItems;
  }

}

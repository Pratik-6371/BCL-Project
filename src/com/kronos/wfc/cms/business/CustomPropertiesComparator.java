package com.kronos.wfc.cms.business;

import java.text.Collator;
import java.util.Comparator;

public class CustomPropertiesComparator implements Comparator
{
  protected Collator collator;
  
  public CustomPropertiesComparator(Collator collator2)
  {
    collator = collator2;
  }
  
  public int compare(Object strValue, Object strTarget)
  {
    if ((strTarget == null) || (strValue == null)) {
      return evaluateNulls(strValue, strTarget);
    }
    return compare((CustomProperties)strValue, (CustomProperties)strTarget);
  }
  
  private int compare(CustomProperties prop1, CustomProperties prop2)
  {
    return collator.compare(prop1.getPropertyName(), prop2.getPropertyName());
  }
  
  public int evaluateNulls(Object value, Object target)
  {
    boolean targetIsNull = target == null;
    return evaluateNulls(value, targetIsNull);
  }
  
  private int evaluateNulls(Object value, boolean targetIsNull)
  {
    int result = 0;
    if ((targetIsNull) && (value == null)) {
      result = 0;
    }
    else if (value == null) {
      result = -1;
    } else
      result = 1;
    return result;
  }
}

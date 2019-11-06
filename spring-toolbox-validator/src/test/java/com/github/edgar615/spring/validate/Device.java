package com.github.edgar615.spring.validate;

/**
 * 测试用
 *
 * @author Edgar
 * @create 2018-09-12 12:48
 **/
public class Device {

  private String macAddress;

  private String barcode;

  private int type;

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}

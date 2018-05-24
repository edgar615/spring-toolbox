package com.github.edgar615.util.spring.operatelog;

import com.google.common.base.MoreObjects;

public class OperateLog {

  private Integer type;

  /**
   * 业务类型
   */
  private String businessMoudle;

  /**
   * 请求地址
   */
  private String requestUrl;

  /**
   * 调用方IP
   */
  private String clientIp;

  /**
   * 操作人
   */
  private String operator;

  /**
   * 操作人ID
   */
  private Integer operatorId;

  /**
   * 公司编码
   */
  private String companyCode;

  /**
   * 动作
   */
  private String operatorAction;

  /**
   * 类
   */
  private String className;

  /**
   * 方法
   */
  private String methodName;

  /**
   * 请求参数
   */
  private String requestParam;

  /**
   * 请求时间
   */
  private Integer logTime;

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getBusinessMoudle() {
    return businessMoudle;
  }

  public void setBusinessMoudle(String businessMoudle) {
    this.businessMoudle = businessMoudle;
  }

  public String getRequestUrl() {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public Integer getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Integer operatorId) {
    this.operatorId = operatorId;
  }

  public String getOperatorAction() {
    return operatorAction;
  }

  public void setOperatorAction(String operatorAction) {
    this.operatorAction = operatorAction;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getRequestParam() {
    return requestParam;
  }

  public void setRequestParam(String requestParam) {
    this.requestParam = requestParam;
  }

  public Integer getLogTime() {
    return logTime;
  }

  public void setLogTime(Integer logTime) {
    this.logTime = logTime;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("OperateLog")
            .add("type", type)
            .add("businessMoudle", businessMoudle)
            .add("requestUrl", requestUrl)
            .add("clientIp", clientIp)
            .add("operator", operator)
            .add("operatorId", operatorId)
            .add("companyCode", companyCode)
            .add("operatorAction", operatorAction)
            .add("className", className)
            .add("methodName", methodName)
            .add("requestParam", requestParam)
            .add("logTime", logTime)
            .toString();
  }

}
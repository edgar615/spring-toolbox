package com.github.edgar615.spring.web;

import java.util.List;

public class WebLogConfig {

  private boolean showTrace;

  private boolean showReqBody;

  private boolean showErrorStackTrace;

  private List<String> ignoreLogPath;

  public boolean isShowTrace() {
    return showTrace;
  }

  public void setShowTrace(boolean showTrace) {
    this.showTrace = showTrace;
  }

  public List<String> getIgnoreLogPath() {
    return ignoreLogPath;
  }

  public void setIgnoreLogPath(List<String> ignoreLogPath) {
    this.ignoreLogPath = ignoreLogPath;
  }

  public boolean isShowReqBody() {
    return showReqBody;
  }

  public void setShowReqBody(boolean showReqBody) {
    this.showReqBody = showReqBody;
  }

  public boolean isShowErrorStackTrace() {
    return showErrorStackTrace;
  }

  public void setShowErrorStackTrace(boolean showErrorStackTrace) {
    this.showErrorStackTrace = showErrorStackTrace;
  }
}

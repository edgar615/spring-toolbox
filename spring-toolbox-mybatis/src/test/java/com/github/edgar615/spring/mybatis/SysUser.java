package com.github.edgar615.spring.mybatis;

import com.github.edgar615.util.db.Persistent;
import com.github.edgar615.util.db.PrimaryKey;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class SysUser implements Persistent<Long> {

    private static final long serialVersionUID = 1L;

    /**
    * Column : sys_user_id
    * remarks: 用户id
    * default:
    * isNullable: false
    * isAutoInc: true
    * isPrimary: true
    * type: -5
    * size: 19
    */
    @PrimaryKey
    private Long sysUserId;

    /**
    * Column : company_id
    * remarks: 公司id
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -5
    * size: 19
    */
    private Long companyId;

    /**
    * Column : company_code
    * remarks: 公司编码
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 24
    */
    private String companyCode;

    /**
    * Column : username
    * remarks: 用户名
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 60
    */
    private String username;

    /**
    * Column : password
    * remarks: 密码
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 64
    */
    private String password;

    /**
    * Column : tel
    * remarks: 联系电话
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 1
    * size: 11
    */
    private String tel;

    /**
    * Column : mail
    * remarks: 邮箱
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 60
    */
    private String mail;

    /**
    * Column : fullname
    * remarks: 姓名
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 32
    */
    private String fullname;

    /**
    * Column : salt
    * remarks: 盐值
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 1
    * size: 128
    */
    private String salt;

    /**
    * Column : state
    * remarks: 状态
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -6
    * size: 3
    */
    private Integer state;

    /**
    * Column : sorted
    * remarks: 顺序
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer sorted;

    /**
    * Column : must_change_passwd
    * remarks: 首次登录必须修改密码
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -7
    * size: 1
    */
    private Boolean mustChangePasswd;

    /**
    * Column : passwd_changed
    * remarks: 密码已修改
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -7
    * size: 1
    */
    private Boolean passwdChanged;

    /**
    * Column : last_login_on
    * remarks: 最近登录时间
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer lastLoginOn;

    /**
    * Column : add_on
    * remarks: 添加时间
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer addOn;

    /**
    * Column : language
    * remarks: 语言
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer language;

    /**
    * Column : time_zone
    * remarks: 时区
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer timeZone;

    /**
    * Column : ext
    * remarks: 扩展字段
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -1
    * size: 1073741824
    */
    private String ext;

    /**
    * Column : internal
    * remarks: 内部的
    * default:
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -7
    * size: 1
    */
    private Boolean internal;

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSorted() {
        return sorted;
    }

    public void setSorted(Integer sorted) {
        this.sorted = sorted;
    }

    public Boolean getMustChangePasswd() {
        return mustChangePasswd;
    }

    public void setMustChangePasswd(Boolean mustChangePasswd) {
        this.mustChangePasswd = mustChangePasswd;
    }

    public Boolean getPasswdChanged() {
        return passwdChanged;
    }

    public void setPasswdChanged(Boolean passwdChanged) {
        this.passwdChanged = passwdChanged;
    }

    public Integer getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(Integer lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public Integer getAddOn() {
        return addOn;
    }

    public void setAddOn(Integer addOn) {
        this.addOn = addOn;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SysUser")
            .add("sysUserId",  sysUserId)
            .add("companyId",  companyId)
            .add("companyCode",  companyCode)
            .add("username",  username)
            .add("password",  password)
            .add("tel",  tel)
            .add("mail",  mail)
            .add("fullname",  fullname)
            .add("salt",  salt)
            .add("state",  state)
            .add("sorted",  sorted)
            .add("mustChangePasswd",  mustChangePasswd)
            .add("passwdChanged",  passwdChanged)
            .add("lastLoginOn",  lastLoginOn)
            .add("addOn",  addOn)
            .add("language",  language)
            .add("timeZone",  timeZone)
            .add("ext",  ext)
            .add("internal",  internal)
           .toString();
    }


    @Override
    public Long id () {
    return sysUserId;
    }

    @Override
    public void setId(Long id) {
        this.sysUserId = id;
    }

    @Override
    public void setGeneratedKey(Number key) {

        this.sysUserId = key.longValue();

    }

   /* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/
	/* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/


}

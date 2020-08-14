package com.mashirro.web.pojo;

public class User {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户账号
     */
    private String loginAccount;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 是否锁定
     */
    private Integer isLocked;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 盐
     */
    private String salt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", loginAccount='" + loginAccount + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", isLocked=" + isLocked +
                ", isDeleted=" + isDeleted +
                ", salt='" + salt + '\'' +
                '}';
    }
}

package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.TobStaff;

/**
 * 工作人员扩展Vo（关联sys_user获取手机号）
 */
public class TobStaffVo extends TobStaff {

    private static final long serialVersionUID = 1L;

    /** 手机号码（从sys_user关联） */
    private String phonenumber;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
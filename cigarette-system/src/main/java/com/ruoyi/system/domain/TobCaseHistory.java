package com.ruoyi.system.domain;
import java.io.Serializable;
import lombok.Data;

/**
 * 历史案件车辆信息表
 */
@Data
public class TobCaseHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 历史案件车牌
     */
    private String hisCarPlate;

    /**
     * 历史案件车型
     */
    private String hisCarType;

    /**
     * 历史案件品牌
     */
    private String hisCarBrand;

}
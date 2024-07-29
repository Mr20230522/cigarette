package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
public class TobTreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TobTreeSelect> children;

    public TobTreeSelect() {

    }

    public TobTreeSelect(TobDistrict district) {
        this.id = district.getDistrictId();
        this.label = district.getDistrictName();
        this.children = district.getChildren().stream().map(TobTreeSelect::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TobTreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TobTreeSelect> children) {
        this.children = children;
    }
}

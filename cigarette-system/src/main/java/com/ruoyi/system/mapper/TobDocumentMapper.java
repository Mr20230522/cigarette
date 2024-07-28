package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobDocument;

/**
 * 文档Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface TobDocumentMapper 
{
    /**
     * 查询文档
     * 
     * @param id 文档主键
     * @return 文档
     */
    public TobDocument selectTobDocumentById(Long id);

    /**
     * 查询文档列表
     * 
     * @param tobDocument 文档
     * @return 文档集合
     */
    public List<TobDocument> selectTobDocumentList(TobDocument tobDocument);

    /**
     * 新增文档
     * 
     * @param tobDocument 文档
     * @return 结果
     */
    public int insertTobDocument(TobDocument tobDocument);

    /**
     * 修改文档
     * 
     * @param tobDocument 文档
     * @return 结果
     */
    public int updateTobDocument(TobDocument tobDocument);

    /**
     * 删除文档
     * 
     * @param id 文档主键
     * @return 结果
     */
    public int deleteTobDocumentById(Long id);

    /**
     * 批量删除文档
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobDocumentByIds(Long[] ids);
}

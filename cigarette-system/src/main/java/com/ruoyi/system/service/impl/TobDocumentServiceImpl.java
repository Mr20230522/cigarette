package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobDocumentMapper;
import com.ruoyi.system.domain.TobDocument;
import com.ruoyi.system.service.ITobDocumentService;

/**
 * 文档Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobDocumentServiceImpl implements ITobDocumentService 
{
    @Autowired
    private TobDocumentMapper tobDocumentMapper;

    /**
     * 查询文档
     * 
     * @param id 文档主键
     * @return 文档
     */
    @Override
    public TobDocument selectTobDocumentById(Long id)
    {
        return tobDocumentMapper.selectTobDocumentById(id);
    }

    /**
     * 查询文档列表
     * 
     * @param tobDocument 文档
     * @return 文档
     */
    @Override
    public List<TobDocument> selectTobDocumentList(TobDocument tobDocument)
    {
        return tobDocumentMapper.selectTobDocumentList(tobDocument);
    }

    /**
     * 新增文档
     * 
     * @param tobDocument 文档
     * @return 结果
     */
    @Override
    public int insertTobDocument(TobDocument tobDocument)
    {
        tobDocument.setCreateTime(DateUtils.getNowDate());
        return tobDocumentMapper.insertTobDocument(tobDocument);
    }

    /**
     * 修改文档
     * 
     * @param tobDocument 文档
     * @return 结果
     */
    @Override
    public int updateTobDocument(TobDocument tobDocument)
    {
        tobDocument.setUpdateTime(DateUtils.getNowDate());
        return tobDocumentMapper.updateTobDocument(tobDocument);
    }

    /**
     * 批量删除文档
     * 
     * @param ids 需要删除的文档主键
     * @return 结果
     */
    @Override
    public int deleteTobDocumentByIds(Long[] ids)
    {
        return tobDocumentMapper.deleteTobDocumentByIds(ids);
    }

    /**
     * 删除文档信息
     * 
     * @param id 文档主键
     * @return 结果
     */
    @Override
    public int deleteTobDocumentById(Long id)
    {
        return tobDocumentMapper.deleteTobDocumentById(id);
    }
}

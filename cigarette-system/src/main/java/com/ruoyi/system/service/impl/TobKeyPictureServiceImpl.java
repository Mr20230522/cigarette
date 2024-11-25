package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobKeyPictureMapper;
import com.ruoyi.system.domain.TobKeyPicture;
import com.ruoyi.system.service.ITobKeyPictureService;

/**
 * 关键帧图片Service业务层处理
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
@Service
public class TobKeyPictureServiceImpl implements ITobKeyPictureService 
{
    @Autowired
    private TobKeyPictureMapper tobKeyPictureMapper;

    /**
     * 查询关键帧图片
     * 
     * @param keyPictureId 关键帧图片主键
     * @return 关键帧图片
     */
    @Override
    public TobKeyPicture selectTobKeyPictureByKeyPictureId(Long keyPictureId)
    {
        return tobKeyPictureMapper.selectTobKeyPictureByKeyPictureId(keyPictureId);
    }

    /**
     * 查询关键帧图片列表
     * 
     * @param tobKeyPicture 关键帧图片
     * @return 关键帧图片
     */
    @Override
    public List<TobKeyPicture> selectTobKeyPictureList(TobKeyPicture tobKeyPicture)
    {
        return tobKeyPictureMapper.selectTobKeyPictureList(tobKeyPicture);
    }

    /**
     * 新增关键帧图片
     * 
     * @param tobKeyPicture 关键帧图片
     * @return 结果
     */
    @Override
    public int insertTobKeyPicture(TobKeyPicture tobKeyPicture)
    {
        tobKeyPicture.setCreateTime(DateUtils.getNowDate());
        return tobKeyPictureMapper.insertTobKeyPicture(tobKeyPicture);
    }

    /**
     * 修改关键帧图片
     * 
     * @param tobKeyPicture 关键帧图片
     * @return 结果
     */
    @Override
    public int updateTobKeyPicture(TobKeyPicture tobKeyPicture)
    {
        return tobKeyPictureMapper.updateTobKeyPicture(tobKeyPicture);
    }

    /**
     * 批量删除关键帧图片
     * 
     * @param keyPictureIds 需要删除的关键帧图片主键
     * @return 结果
     */
    @Override
    public int deleteTobKeyPictureByKeyPictureIds(Long[] keyPictureIds)
    {
        return tobKeyPictureMapper.deleteTobKeyPictureByKeyPictureIds(keyPictureIds);
    }

    /**
     * 删除关键帧图片信息
     * 
     * @param keyPictureId 关键帧图片主键
     * @return 结果
     */
    @Override
    public int deleteTobKeyPictureByKeyPictureId(Long keyPictureId)
    {
        return tobKeyPictureMapper.deleteTobKeyPictureByKeyPictureId(keyPictureId);
    }
    /**
     * 根据行为ID查询关键帧图片列表
     *
     * @param behaviorId 关键帧图片
     * @return 关键帧图片
     */
    @Override
    public List<TobKeyPicture> listPictureByBehaviorId(Long behaviorId)
    {
        return tobKeyPictureMapper.listPictureByBehaviorId(behaviorId);
    }
}

package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobKeyPicture;

/**
 * 关键帧图片Mapper接口
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
public interface TobKeyPictureMapper 
{
    /**
     * 查询关键帧图片
     * 
     * @param keyPictureId 关键帧图片主键
     * @return 关键帧图片
     */
    public TobKeyPicture selectTobKeyPictureByKeyPictureId(Long keyPictureId);

    /**
     * 查询关键帧图片列表
     * 
     * @param tobKeyPicture 关键帧图片
     * @return 关键帧图片集合
     */
    public List<TobKeyPicture> selectTobKeyPictureList(TobKeyPicture tobKeyPicture);

    /**
     * 新增关键帧图片
     * 
     * @param tobKeyPicture 关键帧图片
     * @return 结果
     */
    public int insertTobKeyPicture(TobKeyPicture tobKeyPicture);

    /**
     * 修改关键帧图片
     * 
     * @param tobKeyPicture 关键帧图片
     * @return 结果
     */
    public int updateTobKeyPicture(TobKeyPicture tobKeyPicture);

    /**
     * 删除关键帧图片
     * 
     * @param keyPictureId 关键帧图片主键
     * @return 结果
     */
    public int deleteTobKeyPictureByKeyPictureId(Long keyPictureId);

    /**
     * 批量删除关键帧图片
     * 
     * @param keyPictureIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobKeyPictureByKeyPictureIds(Long[] keyPictureIds);
}

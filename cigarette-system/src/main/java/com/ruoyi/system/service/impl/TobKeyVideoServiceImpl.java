package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.TobKeyPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobKeyVideoMapper;
import com.ruoyi.system.domain.TobKeyVideo;
import com.ruoyi.system.service.ITobKeyVideoService;

/**
 * 关键帧视频Service业务层处理
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
@Service
public class TobKeyVideoServiceImpl implements ITobKeyVideoService 
{
    @Autowired
    private TobKeyVideoMapper tobKeyVideoMapper;

    /**
     * 查询关键帧视频
     * 
     * @param videoId 关键帧视频主键
     * @return 关键帧视频
     */
    @Override
    public TobKeyVideo selectTobKeyVideoByVideoId(Long videoId)
    {
        return tobKeyVideoMapper.selectTobKeyVideoByVideoId(videoId);
    }

    /**
     * 查询关键帧视频列表
     * 
     * @param tobKeyVideo 关键帧视频
     * @return 关键帧视频
     */
    @Override
    public List<TobKeyVideo> selectTobKeyVideoList(TobKeyVideo tobKeyVideo)
    {
        return tobKeyVideoMapper.selectTobKeyVideoList(tobKeyVideo);
    }

    /**
     * 新增关键帧视频
     * 
     * @param tobKeyVideo 关键帧视频
     * @return 结果
     */
    @Override
    public int insertTobKeyVideo(TobKeyVideo tobKeyVideo)
    {
        tobKeyVideo.setCreateTime(DateUtils.getNowDate());
        return tobKeyVideoMapper.insertTobKeyVideo(tobKeyVideo);
    }

    /**
     * 修改关键帧视频
     * 
     * @param tobKeyVideo 关键帧视频
     * @return 结果
     */
    @Override
    public int updateTobKeyVideo(TobKeyVideo tobKeyVideo)
    {
        return tobKeyVideoMapper.updateTobKeyVideo(tobKeyVideo);
    }

    /**
     * 批量删除关键帧视频
     * 
     * @param videoIds 需要删除的关键帧视频主键
     * @return 结果
     */
    @Override
    public int deleteTobKeyVideoByVideoIds(Long[] videoIds)
    {
        return tobKeyVideoMapper.deleteTobKeyVideoByVideoIds(videoIds);
    }

    /**
     * 删除关键帧视频信息
     * 
     * @param videoId 关键帧视频主键
     * @return 结果
     */
    @Override
    public int deleteTobKeyVideoByVideoId(Long videoId)
    {
        return tobKeyVideoMapper.deleteTobKeyVideoByVideoId(videoId);
    }

    /**
     * 根据行为ID查询关键帧视频列表
     *
     * @param behaviorId 行为1iID
     * @return 关键帧视频
     */
    @Override
    public List<TobKeyVideo> listVideoByBehaviorId(Long behaviorId)
    {
        return tobKeyVideoMapper.listVideoByBehaviorId(behaviorId);
    }
}

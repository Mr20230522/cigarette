package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobKeyVideo;

/**
 * 关键帧视频Service接口
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
public interface ITobKeyVideoService 
{
    /**
     * 查询关键帧视频
     * 
     * @param videoId 关键帧视频主键
     * @return 关键帧视频
     */
    public TobKeyVideo selectTobKeyVideoByVideoId(Long videoId);

    /**
     * 查询关键帧视频列表
     * 
     * @param tobKeyVideo 关键帧视频
     * @return 关键帧视频集合
     */
    public List<TobKeyVideo> selectTobKeyVideoList(TobKeyVideo tobKeyVideo);

    /**
     * 新增关键帧视频
     * 
     * @param tobKeyVideo 关键帧视频
     * @return 结果
     */
    public int insertTobKeyVideo(TobKeyVideo tobKeyVideo);

    /**
     * 修改关键帧视频
     * 
     * @param tobKeyVideo 关键帧视频
     * @return 结果
     */
    public int updateTobKeyVideo(TobKeyVideo tobKeyVideo);

    /**
     * 批量删除关键帧视频
     * 
     * @param videoIds 需要删除的关键帧视频主键集合
     * @return 结果
     */
    public int deleteTobKeyVideoByVideoIds(Long[] videoIds);

    /**
     * 删除关键帧视频信息
     * 
     * @param videoId 关键帧视频主键
     * @return 结果
     */
    public int deleteTobKeyVideoByVideoId(Long videoId);
}

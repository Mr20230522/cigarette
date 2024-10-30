package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobTimeVideoMapper;
import com.ruoyi.system.domain.TobTimeVideo;
import com.ruoyi.system.service.ITobTimeVideoService;

/**
 * 片段化视频Service业务层处理
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
@Service
public class TobTimeVideoServiceImpl implements ITobTimeVideoService 
{
    @Autowired
    private TobTimeVideoMapper tobTimeVideoMapper;

    /**
     * 查询片段化视频
     * 
     * @param videoId 片段化视频主键
     * @return 片段化视频
     */
    @Override
    public TobTimeVideo selectTobTimeVideoByVideoId(Long videoId)
    {
        return tobTimeVideoMapper.selectTobTimeVideoByVideoId(videoId);
    }

    /**
     * 查询片段化视频列表
     * 
     * @param tobTimeVideo 片段化视频
     * @return 片段化视频
     */
    @Override
    public List<TobTimeVideo> selectTobTimeVideoList(TobTimeVideo tobTimeVideo)
    {
        return tobTimeVideoMapper.selectTobTimeVideoList(tobTimeVideo);
    }

    /**
     * 新增片段化视频
     * 
     * @param tobTimeVideo 片段化视频
     * @return 结果
     */
    @Override
    public int insertTobTimeVideo(TobTimeVideo tobTimeVideo)
    {
        tobTimeVideo.setCreateTime(DateUtils.getNowDate());
        return tobTimeVideoMapper.insertTobTimeVideo(tobTimeVideo);
    }

    /**
     * 修改片段化视频
     * 
     * @param tobTimeVideo 片段化视频
     * @return 结果
     */
    @Override
    public int updateTobTimeVideo(TobTimeVideo tobTimeVideo)
    {
        return tobTimeVideoMapper.updateTobTimeVideo(tobTimeVideo);
    }

    /**
     * 批量删除片段化视频
     * 
     * @param videoIds 需要删除的片段化视频主键
     * @return 结果
     */
    @Override
    public int deleteTobTimeVideoByVideoIds(Long[] videoIds)
    {
        return tobTimeVideoMapper.deleteTobTimeVideoByVideoIds(videoIds);
    }

    /**
     * 删除片段化视频信息
     * 
     * @param videoId 片段化视频主键
     * @return 结果
     */
    @Override
    public int deleteTobTimeVideoByVideoId(Long videoId)
    {
        return tobTimeVideoMapper.deleteTobTimeVideoByVideoId(videoId);
    }
}

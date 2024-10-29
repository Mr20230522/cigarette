package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobTimeVideo;

/**
 * 片段化视频Mapper接口
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
public interface TobTimeVideoMapper 
{
    /**
     * 查询片段化视频
     * 
     * @param videoId 片段化视频主键
     * @return 片段化视频
     */
    public TobTimeVideo selectTobTimeVideoByVideoId(Long videoId);

    /**
     * 查询片段化视频列表
     * 
     * @param tobTimeVideo 片段化视频
     * @return 片段化视频集合
     */
    public List<TobTimeVideo> selectTobTimeVideoList(TobTimeVideo tobTimeVideo);

    /**
     * 新增片段化视频
     * 
     * @param tobTimeVideo 片段化视频
     * @return 结果
     */
    public int insertTobTimeVideo(TobTimeVideo tobTimeVideo);

    /**
     * 修改片段化视频
     * 
     * @param tobTimeVideo 片段化视频
     * @return 结果
     */
    public int updateTobTimeVideo(TobTimeVideo tobTimeVideo);

    /**
     * 删除片段化视频
     * 
     * @param videoId 片段化视频主键
     * @return 结果
     */
    public int deleteTobTimeVideoByVideoId(Long videoId);

    /**
     * 批量删除片段化视频
     * 
     * @param videoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobTimeVideoByVideoIds(Long[] videoIds);
}

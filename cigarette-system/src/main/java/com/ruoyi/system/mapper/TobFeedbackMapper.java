package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobFeedback;

/**
 * 问题意见反馈Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-25
 */
public interface TobFeedbackMapper
{
    /**
     * 查询问题意见反馈
     *
     * @param id 问题意见反馈主键
     * @return 问题意见反馈
     */
    public TobFeedback selectTobFeedbackById(Long id);

    /**
     * 查询问题意见反馈列表
     *
     * @param tobFeedback 问题意见反馈
     * @return 问题意见反馈集合
     */
    public List<TobFeedback> selectTobFeedbackList(TobFeedback tobFeedback);

    /**
     * 新增问题意见反馈
     *
     * @param tobFeedback 问题意见反馈
     * @return 结果
     */
    public int insertTobFeedback(TobFeedback tobFeedback);

    /**
     * 修改问题意见反馈
     *
     * @param tobFeedback 问题意见反馈
     * @return 结果
     */
    public int updateTobFeedback(TobFeedback tobFeedback);

    /**
     * 删除问题意见反馈
     *
     * @param id 问题意见反馈主键
     * @return 结果
     */
    public int deleteTobFeedbackById(Long id);

    /**
     * 批量删除问题意见反馈
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobFeedbackByIds(Long[] ids);
}

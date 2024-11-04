package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobFeedbackMapper;
import com.ruoyi.system.domain.TobFeedback;
import com.ruoyi.system.service.ITobFeedbackService;

/**
 * 问题意见反馈Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-25
 */
@Service
public class TobFeedbackServiceImpl implements ITobFeedbackService
{
    @Autowired
    private TobFeedbackMapper tobFeedbackMapper;

    /**
     * 查询问题意见反馈
     *
     * @param id 问题意见反馈主键
     * @return 问题意见反馈
     */
    @Override
    public TobFeedback selectTobFeedbackById(Long id)
    {
        return tobFeedbackMapper.selectTobFeedbackById(id);
    }

    /**
     * 查询问题意见反馈列表
     *
     * @param tobFeedback 问题意见反馈
     * @return 问题意见反馈
     */
    @Override
    public List<TobFeedback> selectTobFeedbackList(TobFeedback tobFeedback)
    {
        return tobFeedbackMapper.selectTobFeedbackList(tobFeedback);
    }

    /**
     * 新增问题意见反馈
     *
     * @param tobFeedback 问题意见反馈
     * @return 结果
     */
    @Override
    public int insertTobFeedback(TobFeedback tobFeedback)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        tobFeedback.setCreatorId(user.getUserId());
        tobFeedback.setCreatorNickName(user.getNickName());
        tobFeedback.setCreatorUserName(user.getUserName());
        tobFeedback.setCreateTime(DateUtils.getNowDate());
        tobFeedback.setStatus(1L); // 已提交
        return tobFeedbackMapper.insertTobFeedback(tobFeedback);
    }

    /**
     * 修改问题意见反馈
     *
     * @param tobFeedback 问题意见反馈
     * @return 结果
     */
    @Override
    public int updateTobFeedback(TobFeedback tobFeedback)
    {
        return tobFeedbackMapper.updateTobFeedback(tobFeedback);
    }

    /**
     * 批量删除问题意见反馈
     *
     * @param ids 需要删除的问题意见反馈主键
     * @return 结果
     */
    @Override
    public int deleteTobFeedbackByIds(Long[] ids)
    {
        return tobFeedbackMapper.deleteTobFeedbackByIds(ids);
    }

    /**
     * 删除问题意见反馈信息
     *
     * @param id 问题意见反馈主键
     * @return 结果
     */
    @Override
    public int deleteTobFeedbackById(Long id)
    {
        return tobFeedbackMapper.deleteTobFeedbackById(id);
    }
}

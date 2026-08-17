package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobSmsMoRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TobSmsMoRecordMapper {

    /**
     * 插入单条记录（选择性插入，只插入非空字段）
     */
    int insertSelective(TobSmsMoRecord record);

    /**
     * 批量插入（用于轮询拉取后批量存储）
     * @param list 记录列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<TobSmsMoRecord> list);

    /**
     * 根据主键ID查询
     */
    TobSmsMoRecord selectById(@Param("id") Long id);

    /**
     * 根据运营商流水号（唯一）查询
     */
    TobSmsMoRecord selectByMsgid(@Param("msgid") String msgid);

    /**
     * 根据处理状态查询所有记录（可排序）
     */
    List<TobSmsMoRecord> selectByStatus(@Param("status") Integer status);

    /**
     * 查询待处理记录，按上行时间正序，限制条数（供消费Worker使用）
     * @param limit 最大条数
     */
    List<TobSmsMoRecord> selectPendingList(@Param("limit") Integer limit);

    /**
     * 根据主键ID更新状态
     */
    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据msgid更新状态（备用）
     */
    int updateStatusByMsgid(@Param("msgid") String msgid, @Param("status") Integer status);

    /**
     * 根据主键ID删除记录（慎用，一般只更新状态）
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据状态计数（可选）
     */
    int countByStatus(@Param("status") Integer status);
}
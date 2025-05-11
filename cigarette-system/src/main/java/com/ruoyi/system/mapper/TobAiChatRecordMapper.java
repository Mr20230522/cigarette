package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.TobAiChatRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TobAiChatRecordMapper {
    int insertAiChatRecord(TobAiChatRecord record);
    List<TobAiChatRecord> selectAiChatRecordList(TobAiChatRecord record);
    List<TobAiChatRecord> selectRecentBySessionId(
            @Param("sessionId") String sessionId,
            @Param("limit") int limit
    );}
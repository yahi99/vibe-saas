package com.yongche.yopsaas.db.dao;

import org.apache.ibatis.annotations.Param;
import com.yongche.yopsaas.db.domain.YopsaasOrder;

import java.time.LocalDateTime;

public interface OrderMapper {
    int updateWithOptimisticLocker(@Param("lastUpdateTime") LocalDateTime lastUpdateTime, @Param("order") YopsaasOrder order);
}
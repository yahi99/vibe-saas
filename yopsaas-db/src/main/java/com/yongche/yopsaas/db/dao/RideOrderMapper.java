package com.yongche.yopsaas.db.dao;

import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import org.apache.ibatis.annotations.Param;

public interface RideOrderMapper {
    int updateWithOptimisticLocker(@Param("lastUpdateTime") int lastUpdateTime, @Param("order") YopsaasRideOrder order);
}
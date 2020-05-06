package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.ToftAPI;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.toft.AvailableService;
import com.ridegroup.yop.bean.toft.Estimated;

import java.util.List;
import java.util.Map;

public class ToftService extends BaseService {
    /**
     * city	城市	*	bj
     type	产品类型	*	7
     aircode	机场三字码，接送机必传，接机则传到达机场三字码、送机则传起飞机场三字码接送机必填		PEK
     car_type_id	车型	*	2
     expect_start_longitude	出发地点经度	*	116.458637
     expect_start_latitude	出发地点纬度	*	39.955538
     expect_end_longitude	目的地点经度	*	116.373055
     expect_end_latitude	目的地点纬度	*	39.911093
     time	开始时间	*	2013-04-19 11:22:33
     rent_time	使用时长 单位：小时,时租必填		2
     map_type	地图类型 1-百度 2-火星 3-谷歌 默认 1-百度	*	2
     * @param reqMap 请求参数
     * @return BaseResultT&lt;List&lt;Estimated&gt;&gt;
     */
    public BaseResultT<List<Estimated>> estimatedAll(Map<String, Object> reqMap) {
        String accessToken = this.getProperties().getAccessToken();
        return ToftAPI.estimatedAll(accessToken, reqMap);
    }

    public BaseResultT<Map<String, AvailableService>> getAvailableService() {
        String accessToken = this.getProperties().getAccessToken();
        return ToftAPI.getAvailableService(accessToken);
    }
}

package com.yongche.yopsaas.core;

import com.ridegroup.yop.bean.driver.DriverInfo;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.yop.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class YopTest {
    private final Log logger = LogFactory.getLog(YopTest.class);

    @Autowired
    private OrderService orderService;

    @Test
    public void testGetOrderDriverInfo() {
        DriverInfo driverInfo = orderService.getOrderDriverInfo("6819690328181142745");
        logger.debug(JacksonUtil.toJson(driverInfo));
        int driverId = driverInfo.getDriver_id();
        //{"driver_id":0,"name":"易师傅","score":0,"good_comment_rate":0,"unittime_complete_count":36,"is_served":0,"latitude":0,"longitude":0,"brand":"奥迪 A3","car_type":null,"car_type_id":0,"is_default":0,"photo":null,"gender":"男","driving_years":6,"star_level":2,"vehicle_number":"京U23456","car_setup":"","car_company_name":"","driver_company_name":"北京易快行技术服务有限责任公司","is_default_photo":0,"photo_url":"https://i3.yongche.name/media/g1/M00/01/06/rBAApVUmFBKIfcrKAAE4EwGNvvMAAAbpgGVXMYAATgr708.png","cellphone":"16818862925"}
        logger.info(driverId);
    }
}

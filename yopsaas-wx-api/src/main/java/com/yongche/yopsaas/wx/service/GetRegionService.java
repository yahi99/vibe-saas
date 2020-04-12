package com.yongche.yopsaas.wx.service;

import com.yongche.yopsaas.db.domain.YopsaasRegion;
import com.yongche.yopsaas.db.service.YopsaasRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhy
 * @date 2019-01-17 23:07
 **/
@Component
public class GetRegionService {

	@Autowired
	private YopsaasRegionService regionService;

	private static List<YopsaasRegion> yopsaasRegions;

	protected List<YopsaasRegion> getYopsaasRegions() {
		if(yopsaasRegions==null){
			createRegion();
		}
		return yopsaasRegions;
	}

	private synchronized void createRegion(){
		if (yopsaasRegions == null) {
			yopsaasRegions = regionService.getAll();
		}
	}
}

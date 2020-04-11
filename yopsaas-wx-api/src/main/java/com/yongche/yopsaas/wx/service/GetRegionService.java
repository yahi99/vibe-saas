package com.yongche.yopsaas.wx.service;

import com.yongche.yopsaas.db.domain.LitemallRegion;
import com.yongche.yopsaas.db.service.LitemallRegionService;
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
	private LitemallRegionService regionService;

	private static List<LitemallRegion> yopsaasRegions;

	protected List<LitemallRegion> getLitemallRegions() {
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

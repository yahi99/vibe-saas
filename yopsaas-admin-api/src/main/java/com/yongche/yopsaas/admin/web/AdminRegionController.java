package com.yongche.yopsaas.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.yongche.yopsaas.admin.vo.RegionVo;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.db.domain.YopsaasRegion;
import com.yongche.yopsaas.db.service.YopsaasRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/region")
@Validated
public class AdminRegionController {
    private final Log logger = LogFactory.getLog(AdminRegionController.class);

    @Autowired
    private YopsaasRegionService regionService;

    @GetMapping("/clist")
    public Object clist(@NotNull Integer id) {
        List<YopsaasRegion> regionList = regionService.queryByPid(id);
        return ResponseUtil.okList(regionList);
    }

    @GetMapping("/list")
    public Object list() {
        List<RegionVo> regionVoList = new ArrayList<>();

        List<YopsaasRegion> yopsaasRegions = regionService.getAll();
        Map<Byte, List<YopsaasRegion>> collect = yopsaasRegions.stream().collect(Collectors.groupingBy(YopsaasRegion::getType));
        byte provinceType = 1;
        List<YopsaasRegion> provinceList = collect.get(provinceType);
        byte cityType = 2;
        List<YopsaasRegion> city = collect.get(cityType);
        Map<Integer, List<YopsaasRegion>> cityListMap = city.stream().collect(Collectors.groupingBy(YopsaasRegion::getPid));
        byte areaType = 3;
        List<YopsaasRegion> areas = collect.get(areaType);
        Map<Integer, List<YopsaasRegion>> areaListMap = areas.stream().collect(Collectors.groupingBy(YopsaasRegion::getPid));

        for (YopsaasRegion province : provinceList) {
            RegionVo provinceVO = new RegionVo();
            provinceVO.setId(province.getId());
            provinceVO.setName(province.getName());
            provinceVO.setCode(province.getCode());
            provinceVO.setType(province.getType());

            List<YopsaasRegion> cityList = cityListMap.get(province.getId());
            List<RegionVo> cityVOList = new ArrayList<>();
            for (YopsaasRegion cityVo : cityList) {
                RegionVo cityVO = new RegionVo();
                cityVO.setId(cityVo.getId());
                cityVO.setName(cityVo.getName());
                cityVO.setCode(cityVo.getCode());
                cityVO.setType(cityVo.getType());

                List<YopsaasRegion> areaList = areaListMap.get(cityVo.getId());
                List<RegionVo> areaVOList = new ArrayList<>();
                for (YopsaasRegion area : areaList) {
                    RegionVo areaVO = new RegionVo();
                    areaVO.setId(area.getId());
                    areaVO.setName(area.getName());
                    areaVO.setCode(area.getCode());
                    areaVO.setType(area.getType());
                    areaVOList.add(areaVO);
                }

                cityVO.setChildren(areaVOList);
                cityVOList.add(cityVO);
            }
            provinceVO.setChildren(cityVOList);
            regionVoList.add(provinceVO);
        }

        return ResponseUtil.okList(regionVoList);
    }
}

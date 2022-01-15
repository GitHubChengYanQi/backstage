package cn.atsoft.dasheng.crm.region;

import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRegionServiceImp implements GetRegionService {
    @Autowired
    private CommonAreaService commonAreaService;

    @Override
    public RegionResult getRegion(String area) {
        //判断地址填入是否正确
        List<CommonArea> list = commonAreaService.lambdaQuery().eq(CommonArea::getParentid, area).list();
        if (list.size() > 0) {
            return null;
        }

        if (ToolUtil.isNotEmpty(area)) {
            RegionResult regionResult = new RegionResult();

            CommonArea Area = commonAreaService.lambdaQuery().eq(CommonArea::getId, area).one();

            if (ToolUtil.isNotEmpty(Area)){
                regionResult.setArea(Area.getTitle());
                CommonArea city = commonAreaService.lambdaQuery().eq(CommonArea::getId, Area.getParentid()).one();
                if (ToolUtil.isNotEmpty(city)){
                    regionResult.setCity(city.getTitle());
                    CommonArea province = commonAreaService.lambdaQuery().eq(CommonArea::getId, city.getParentid()).one();
                    if (ToolUtil.isNotEmpty(province)){
                        regionResult.setProvince(province.getTitle());
                        CommonArea countries = commonAreaService.lambdaQuery().eq(CommonArea::getId, province.getParentid()).one();
                        if (ToolUtil.isNotEmpty(countries)){
                            regionResult.setCountries(countries.getTitle());
                        }
                    }

                }
            }
            return regionResult;
        }

        return null;
    }
}

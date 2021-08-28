package cn.atsoft.dasheng.commonArea.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.mapper.CommonAreaMapper;
import cn.atsoft.dasheng.commonArea.model.params.CommonAreaParam;
import cn.atsoft.dasheng.commonArea.model.result.AreaRequest;
import cn.atsoft.dasheng.commonArea.model.result.CityRequest;
import cn.atsoft.dasheng.commonArea.model.result.CommonAreaResult;
import cn.atsoft.dasheng.commonArea.model.result.ProvinceRequest;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 逐渐取代region表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-24
 */
@Service
public class CommonAreaServiceImpl extends ServiceImpl<CommonAreaMapper, CommonArea> implements CommonAreaService {

    @Override
    public void add(CommonAreaParam param) {
        CommonArea entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CommonAreaParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(CommonAreaParam param) {
        CommonArea oldEntity = getOldEntity(param);
        CommonArea newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CommonAreaResult findBySpec(CommonAreaParam param) {
        return null;
    }

    @Override
    public List<CommonAreaResult> findListBySpec(CommonAreaParam param) {
        return null;
    }

    @Override
    public PageInfo<CommonAreaResult> findPageBySpec(CommonAreaParam param) {
        Page<CommonAreaResult> pageContext = getPageContext();
        IPage<CommonAreaResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<CommonAreaResult> getProvince() {

        CommonAreaParam param = new CommonAreaParam();
        param.setParentid(1);
        List<CommonAreaResult> resultList = this.baseMapper.customList(param);

        QueryWrapper<CommonArea> areaQueryWrapper = new QueryWrapper<>();
        areaQueryWrapper.in("parentid", 1);
        List<CommonArea> commonAreas = this.list(areaQueryWrapper);

        List<CommonAreaResult> results = new ArrayList<>();
        ProvinceRequest provinceRequest = new ProvinceRequest();
        for (CommonArea commonArea : commonAreas) {
            CommonAreaResult Province = new CommonAreaResult();
            ToolUtil.copyProperties(commonArea, Province);
            results.add(Province);
        }
        provinceRequest.setBackProvince(results);


        return resultList;
    }

    @Override
    public List<CommonAreaResult> getCity(CommonAreaParam param) {
        QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
        commonAreaQueryWrapper.in("parentid", param.getId());
        List<CommonArea> cityList = this.list(commonAreaQueryWrapper);
        List<CommonAreaResult> commonAreaResults = new ArrayList<>();
        CityRequest cityRequest = new CityRequest();
        for (CommonArea commonArea : cityList) {
            CommonAreaResult commonAreaResult = new CommonAreaResult();
            ToolUtil.copyProperties(commonArea, commonAreaResult);
            commonAreaResults.add(commonAreaResult);
        }
        cityRequest.setBackCity(commonAreaResults);
        return commonAreaResults;
    }

    @Override
    public List<CommonAreaResult> getArea(CommonAreaParam param) {

        QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
        commonAreaQueryWrapper.in("parentid", param.getId());
        List<CommonArea> areaList = this.list(commonAreaQueryWrapper);
        List<CommonAreaResult> commonAreaResults = new ArrayList<>();
        AreaRequest areaRequest = new AreaRequest();
        for (CommonArea commonArea : areaList) {
            CommonAreaResult commonAreaResult = new CommonAreaResult();
            ToolUtil.copyProperties(commonArea, commonAreaResult);
            commonAreaResults.add(commonAreaResult);
        }
        areaRequest.setBackArea(commonAreaResults);
        return commonAreaResults;
    }

    private Serializable getKey(CommonAreaParam param) {
        return param.getId();
    }

    private Page<CommonAreaResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CommonArea getOldEntity(CommonAreaParam param) {
        return this.getById(getKey(param));
    }

    private CommonArea getEntity(CommonAreaParam param) {
        CommonArea entity = new CommonArea();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

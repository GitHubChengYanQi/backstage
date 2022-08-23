package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.model.params.CustomerMap;
;
import cn.atsoft.dasheng.base.log.FreedLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.mapper.AdressMapper;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.region.GetRegionService;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客户地址表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Service
public class AdressServiceImpl extends ServiceImpl<AdressMapper, Adress> implements AdressService {

    @Autowired
    private GetRegionService getRegionService;
    @Autowired
    private CommonAreaService commonAreaService;

    @FreedLog
    @Override
    public Adress add(AdressParam param) {



        if (ToolUtil.isNotEmpty(param.getMap())) {
            param.setLocation(param.getMap().getAddress());
            if (ToolUtil.isNotEmpty(param.getMap().getMap()) && param.getMap().getMap().size() > 0) {
                param.setLongitude(param.getMap().getMap().get(0));
                param.setLatitude(param.getMap().getMap().get(1));
            }
        }
        List<CommonArea> commonAreas = commonAreaService.lambdaQuery().in(CommonArea::getParentid, param.getRegion()).list();
        if (commonAreas.size() > 0) {
            throw new ServiceException(500, "地址请选择区或县");
        }


        Adress entity = getEntity(param);
        this.save(entity);
        return entity;

    }


    @FreedLog
    @Override
    public Adress delete(AdressParam param) {
        param.setDisplay(0);
        this.update(param);
        Adress entity = getEntity(param);
        return entity;
    }

    @FreedLog
    @Override
    public Adress update(AdressParam param) {

        if (ToolUtil.isNotEmpty(param.getMap())) {
            param.setLocation(param.getMap().getAddress());
            param.setLongitude(param.getMap().getMap().get(0));
            param.setLatitude(param.getMap().getMap().get(1));
        }

        List<CommonArea> commonAreas = commonAreaService.lambdaQuery().in(CommonArea::getParentid, param.getRegion()).list();
        if (commonAreas.size() > 0) {
            throw new ServiceException(500, "地址请选择区或县");
        }
        Adress oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Adress newEntity = getEntity(param);
            newEntity.setCustomerId(null);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
            return oldEntity;
        }
    }

    @Override
    public AdressResult findBySpec(AdressParam param) {
        return null;
    }

    @Override
    public List<AdressResult> findListBySpec(AdressParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec( AdressParam param,DataScope dataScope) {
        Page<AdressResult> pageContext = getPageContext();
        IPage<AdressResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);

        for (AdressResult record : page.getRecords()) {
            Double longitude = record.getLongitude();
            Double latitude = record.getLatitude();

            CustomerMap customerMap = new CustomerMap();
            List<Double> list = new ArrayList<>();
            list.add(longitude);
            list.add(latitude);
            customerMap.setAddress(record.getLocation());
            customerMap.setMap(list);
            record.setMap(customerMap);


            if (ToolUtil.isNotEmpty(record.getRegion())) {
                RegionResult region = getRegionService.getRegion(record.getRegion());
                record.setRegionResult(region);
            }
        }


        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<AdressResult> listQuery(List<Long> ids) {
        List<AdressResult> results = this.baseMapper.testQuery(ids);
        return results;
    }

    private Serializable getKey(AdressParam param) {
        return param.getAdressId();
    }

    private Page<AdressResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Adress getOldEntity(AdressParam param) {
        return this.getById(getKey(param));
    }

    private Adress getEntity(AdressParam param) {
        Adress entity = new Adress();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}

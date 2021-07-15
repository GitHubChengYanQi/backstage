package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Place;
import cn.atsoft.dasheng.app.mapper.PlaceMapper;
import cn.atsoft.dasheng.app.model.params.PlaceParam;
import cn.atsoft.dasheng.app.model.result.PlaceResult;
import  cn.atsoft.dasheng.app.service.PlaceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 地点表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class PlaceServiceImpl extends ServiceImpl<PlaceMapper, Place> implements PlaceService {

    @Override
    public void add(PlaceParam param){
        Place entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PlaceParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PlaceParam param){
        Place oldEntity = getOldEntity(param);
        Place newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PlaceResult findBySpec(PlaceParam param){
        return null;
    }

    @Override
    public List<PlaceResult> findListBySpec(PlaceParam param){
        return null;
    }

    @Override
    public PageInfo<PlaceResult> findPageBySpec(PlaceParam param){
        Page<PlaceResult> pageContext = getPageContext();
        IPage<PlaceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PlaceParam param){
        return param.getPalceId();
    }

    private Page<PlaceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Place getOldEntity(PlaceParam param) {
        return this.getById(getKey(param));
    }

    private Place getEntity(PlaceParam param) {
        Place entity = new Place();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

package cn.atsoft.dasheng.common_area.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.common_area.entity.CommonArea;
import cn.atsoft.dasheng.common_area.mapper.CommonAreaMapper;
import cn.atsoft.dasheng.common_area.model.params.CommonAreaParam;
import cn.atsoft.dasheng.common_area.model.result.CommonAreaResult;
import  cn.atsoft.dasheng.common_area.service.CommonAreaService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
    public void add(CommonAreaParam param){
        CommonArea entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CommonAreaParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CommonAreaParam param){
        CommonArea oldEntity = getOldEntity(param);
        CommonArea newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CommonAreaResult findBySpec(CommonAreaParam param){
        return null;
    }

    @Override
    public List<CommonAreaResult> findListBySpec(CommonAreaParam param){
        return null;
    }

    @Override
    public PageInfo<CommonAreaResult> findPageBySpec(CommonAreaParam param){
        Page<CommonAreaResult> pageContext = getPageContext();
        IPage<CommonAreaResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CommonAreaParam param){
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

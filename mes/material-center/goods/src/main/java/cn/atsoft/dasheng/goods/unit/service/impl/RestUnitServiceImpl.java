package cn.atsoft.dasheng.goods.unit.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.mapper.RestUnitMapper;
import cn.atsoft.dasheng.goods.unit.model.params.RestUnitParam;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import cn.atsoft.dasheng.goods.unit.service.RestUnitService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 单位表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
@Service
public class RestUnitServiceImpl extends ServiceImpl<RestUnitMapper, RestUnit> implements RestUnitService {

    @Override
    public void add(RestUnitParam param){
        RestUnit entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RestUnitParam param){
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(RestUnitParam param){
        RestUnit oldEntity = getOldEntity(param);
        RestUnit newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestUnitResult findBySpec(RestUnitParam param){
        return null;
    }

    @Override
    public List<RestUnitResult> findListBySpec(RestUnitParam param){
        return null;
    }

    @Override
    public PageInfo<RestUnitResult> findPageBySpec(RestUnitParam param, DataScope dataScope){
        Page<RestUnitResult> pageContext = getPageContext();
        IPage<RestUnitResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestUnitParam param){
        return param.getUnitId();
    }

    private Page<RestUnitResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestUnit getOldEntity(RestUnitParam param) {
        return this.getById(getKey(param));
    }

    private RestUnit getEntity(RestUnitParam param) {
        RestUnit entity = new RestUnit();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

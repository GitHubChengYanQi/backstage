package cn.atsoft.dasheng.goods.texture.service.impl;



import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.texture.entity.RestTextrue;
import cn.atsoft.dasheng.goods.texture.mapper.RestTextureMapper;
import cn.atsoft.dasheng.goods.texture.model.params.RestTextrueParam;
import cn.atsoft.dasheng.goods.texture.model.result.RestTextrueResult;
import cn.atsoft.dasheng.goods.texture.service.RestTextrueService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 材质 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class RestTextrueServiceImpl extends ServiceImpl<RestTextureMapper, RestTextrue> implements RestTextrueService {

    @Override
    public Long add(RestTextrueParam param) {
        RestTextrue entity = getEntity(param);
        this.save(entity);
        return entity.getMaterialId();
    }

    @Override
    public void delete(RestTextrueParam param) {
        RestTextrue byId = this.getById(param.getMaterialId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "删除目标不存在");
        }
        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(RestTextrueParam param) {
        RestTextrue oldEntity = getOldEntity(param);
        RestTextrue newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public List<RestTextrueResult> details(List<Long> materialIds) {
        if (ToolUtil.isEmpty(materialIds)) {
            return new ArrayList<>();
        }
        List<RestTextrue> restTextrues = this.listByIds(materialIds);
        if (ToolUtil.isEmpty(restTextrues)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(restTextrues, RestTextrueResult.class);
    }

    @Override
    public RestTextrueResult findBySpec(RestTextrueParam param) {
        return null;
    }

    @Override
    public List<RestTextrueResult> findListBySpec(RestTextrueParam param) {
        return null;
    }

    @Override
    public PageInfo<RestTextrueResult> findPageBySpec(RestTextrueParam param, DataScope dataScope) {
        Page<RestTextrueResult> pageContext = getPageContext();
        IPage<RestTextrueResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestTextrueParam param) {
        return param.getMaterialId();
    }

    private Page<RestTextrueResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("name");
        }};
        return PageFactory.defaultPage(fields);
    }

    private RestTextrue getOldEntity(RestTextrueParam param) {
        return this.getById(getKey(param));
    }

    private RestTextrue getEntity(RestTextrueParam param) {
        RestTextrue entity = new RestTextrue();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

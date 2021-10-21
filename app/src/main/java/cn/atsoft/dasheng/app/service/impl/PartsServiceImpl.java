package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.mapper.PartsMapper;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import  cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
@Service
public class PartsServiceImpl extends ServiceImpl<PartsMapper, Parts> implements PartsService {

    @Override
    public void add(PartsParam param){
        Parts entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PartsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PartsParam param){
        Parts oldEntity = getOldEntity(param);
        Parts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PartsResult findBySpec(PartsParam param){
        return null;
    }

    @Override
    public List<PartsResult> findListBySpec(PartsParam param){
        return null;
    }

    @Override
    public PageInfo<PartsResult> findPageBySpec(PartsParam param){
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PartsParam param){
        return param.getPartsId();
    }

    private Page<PartsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Parts getOldEntity(PartsParam param) {
        return this.getById(getKey(param));
    }

    private Parts getEntity(PartsParam param) {
        Parts entity = new Parts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

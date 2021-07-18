package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Lal;
import cn.atsoft.dasheng.app.mapper.LalMapper;
import cn.atsoft.dasheng.app.model.params.LalParam;
import cn.atsoft.dasheng.app.model.result.LalResult;
import  cn.atsoft.dasheng.app.service.LalService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 经纬度表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
@Service
public class LalServiceImpl extends ServiceImpl<LalMapper, Lal> implements LalService {

    @Override
    public void add(LalParam param){
        Lal entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(LalParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(LalParam param){
        Lal oldEntity = getOldEntity(param);
        Lal newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public LalResult findBySpec(LalParam param){
        return null;
    }

    @Override
    public List<LalResult> findListBySpec(LalParam param){
        return null;
    }

    @Override
    public PageInfo<LalResult> findPageBySpec(LalParam param){
        Page<LalResult> pageContext = getPageContext();
        IPage<LalResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(LalParam param){
        return param.getLalId();
    }

    private Page<LalResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Lal getOldEntity(LalParam param) {
        return this.getById(getKey(param));
    }

    private Lal getEntity(LalParam param) {
        Lal entity = new Lal();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

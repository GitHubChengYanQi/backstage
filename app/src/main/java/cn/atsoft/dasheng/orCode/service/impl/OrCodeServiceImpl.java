package cn.atsoft.dasheng.orCode.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.mapper.OrCodeMapper;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import  cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 二维码 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class OrCodeServiceImpl extends ServiceImpl<OrCodeMapper, OrCode> implements OrCodeService {

    @Override
    public void add(OrCodeParam param){
        OrCode entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OrCodeParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrCodeParam param){
        OrCode oldEntity = getOldEntity(param);
        OrCode newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrCodeResult findBySpec(OrCodeParam param){
        return null;
    }

    @Override
    public List<OrCodeResult> findListBySpec(OrCodeParam param){
        return null;
    }

    @Override
    public PageInfo<OrCodeResult> findPageBySpec(OrCodeParam param){
        Page<OrCodeResult> pageContext = getPageContext();
        IPage<OrCodeResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrCodeParam param){
        return param.getOrCodeId();
    }

    private Page<OrCodeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrCode getOldEntity(OrCodeParam param) {
        return this.getById(getKey(param));
    }

    private OrCode getEntity(OrCodeParam param) {
        OrCode entity = new OrCode();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

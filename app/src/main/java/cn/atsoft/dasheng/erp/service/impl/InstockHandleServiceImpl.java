package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockHandle;
import cn.atsoft.dasheng.erp.mapper.InstockHandleMapper;
import cn.atsoft.dasheng.erp.model.params.InstockHandleParam;
import cn.atsoft.dasheng.erp.model.result.InstockHandleResult;
import  cn.atsoft.dasheng.erp.service.InstockHandleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 入库操作结果 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-07-08
 */
@Service
public class InstockHandleServiceImpl extends ServiceImpl<InstockHandleMapper, InstockHandle> implements InstockHandleService {

    @Override
    public void add(InstockHandleParam param){
        InstockHandle entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InstockHandleParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockHandleParam param){
        InstockHandle oldEntity = getOldEntity(param);
        InstockHandle newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockHandleResult findBySpec(InstockHandleParam param){
        return null;
    }

    @Override
    public List<InstockHandleResult> findListBySpec(InstockHandleParam param){
        return null;
    }

    @Override
    public PageInfo<InstockHandleResult> findPageBySpec(InstockHandleParam param){
        Page<InstockHandleResult> pageContext = getPageContext();
        IPage<InstockHandleResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockHandleParam param){
        return param.getInstockHandleId();
    }

    private Page<InstockHandleResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockHandle getOldEntity(InstockHandleParam param) {
        return this.getById(getKey(param));
    }

    private InstockHandle getEntity(InstockHandleParam param) {
        InstockHandle entity = new InstockHandle();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

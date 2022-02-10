package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.DaoxinShipSetp;
import cn.atsoft.dasheng.production.mapper.DaoxinShipSetpMapper;
import cn.atsoft.dasheng.production.model.params.DaoxinShipSetpParam;
import cn.atsoft.dasheng.production.model.result.DaoxinShipSetpResult;
import  cn.atsoft.dasheng.production.service.DaoxinShipSetpService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class DaoxinShipSetpServiceImpl extends ServiceImpl<DaoxinShipSetpMapper, DaoxinShipSetp> implements DaoxinShipSetpService {

    @Override
    public void add(DaoxinShipSetpParam param){
        DaoxinShipSetp entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DaoxinShipSetpParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DaoxinShipSetpParam param){
        DaoxinShipSetp oldEntity = getOldEntity(param);
        DaoxinShipSetp newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DaoxinShipSetpResult findBySpec(DaoxinShipSetpParam param){
        return null;
    }

    @Override
    public List<DaoxinShipSetpResult> findListBySpec(DaoxinShipSetpParam param){
        return null;
    }

    @Override
    public PageInfo<DaoxinShipSetpResult> findPageBySpec(DaoxinShipSetpParam param){
        Page<DaoxinShipSetpResult> pageContext = getPageContext();
        IPage<DaoxinShipSetpResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DaoxinShipSetpParam param){
        return param.getShipSetpId();
    }

    private Page<DaoxinShipSetpResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DaoxinShipSetp getOldEntity(DaoxinShipSetpParam param) {
        return this.getById(getKey(param));
    }

    private DaoxinShipSetp getEntity(DaoxinShipSetpParam param) {
        DaoxinShipSetp entity = new DaoxinShipSetp();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

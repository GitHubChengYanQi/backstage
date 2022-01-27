package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsDeptBind;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsDeptBindMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsDeptBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsDeptBindResult;
import  cn.atsoft.dasheng.erp.service.StorehousePositionsDeptBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 库位权限绑定表 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-01-25
 */
@Service
public class StorehousePositionsDeptBindServiceImpl extends ServiceImpl<StorehousePositionsDeptBindMapper, StorehousePositionsDeptBind> implements StorehousePositionsDeptBindService {

    @Override
    public void add(StorehousePositionsDeptBindParam param){
        StorehousePositionsDeptBind entity = getEntity(param);
        StorehousePositionsDeptBind storehousePositionsDeptBind = this.query().eq("storehouse_positions_id", param.getStorehousePositionsId()).one();
        if (ToolUtil.isNotEmpty(storehousePositionsDeptBind)){
            storehousePositionsDeptBind.setDeptId(param.getDeptId());
            this.updateById(storehousePositionsDeptBind);
        }else {
            this.save(entity);
        }
    }

    @Override
    public void delete(StorehousePositionsDeptBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehousePositionsDeptBindParam param){
        StorehousePositionsDeptBind oldEntity = getOldEntity(param);
        StorehousePositionsDeptBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehousePositionsDeptBindResult findBySpec(StorehousePositionsDeptBindParam param){
        return null;
    }

    @Override
    public List<StorehousePositionsDeptBindResult> findListBySpec(StorehousePositionsDeptBindParam param){
        return null;
    }
    @Override
    public  List<StorehousePositionsDeptBindResult> getBindByPositionIds(List<Long> positionIds){
        List<StorehousePositionsDeptBind> positionsDeptBinds = this.list(new QueryWrapper<StorehousePositionsDeptBind>() {{
            in("storehouse_positions_id", positionIds);
            eq("display", 1);
        }});
        List<StorehousePositionsDeptBindResult> results = new ArrayList<>();
        for (StorehousePositionsDeptBind storehousePositionsDeptBind : positionsDeptBinds) {
            StorehousePositionsDeptBindResult result = new StorehousePositionsDeptBindResult();
            ToolUtil.copyProperties(storehousePositionsDeptBind,result);
            List<Long> deptId = new ArrayList<>();
            if (ToolUtil.isNotEmpty(storehousePositionsDeptBind.getDeptId())){
                deptId  =  Arrays.asList(storehousePositionsDeptBind.getDeptId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            }
            result.setDeptIds(deptId);
            results.add(result);
        }
        return results;
    }
    @Override
    public PageInfo<StorehousePositionsDeptBindResult> findPageBySpec(StorehousePositionsDeptBindParam param){
        Page<StorehousePositionsDeptBindResult> pageContext = getPageContext();
        IPage<StorehousePositionsDeptBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StorehousePositionsDeptBindParam param){
        return param.getBindId();
    }

    private Page<StorehousePositionsDeptBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositionsDeptBind getOldEntity(StorehousePositionsDeptBindParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositionsDeptBind getEntity(StorehousePositionsDeptBindParam param) {
        StorehousePositionsDeptBind entity = new StorehousePositionsDeptBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

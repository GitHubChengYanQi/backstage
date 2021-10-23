package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.mapper.ToolMapper;
import cn.atsoft.dasheng.erp.model.params.ToolParam;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import  cn.atsoft.dasheng.erp.service.ToolService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工具表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
@Service
public class ToolServiceImpl extends ServiceImpl<ToolMapper, Tool> implements ToolService {

    @Override
    public void add(ToolParam param){
        Tool entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ToolParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ToolParam param){
        Tool oldEntity = getOldEntity(param);
        Tool newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ToolResult findBySpec(ToolParam param){
        return null;
    }

    @Override
    public List<ToolResult> findListBySpec(ToolParam param){
        return null;
    }

    @Override
    public PageInfo<ToolResult> findPageBySpec(ToolParam param){
        Page<ToolResult> pageContext = getPageContext();
        IPage<ToolResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ToolParam param){
        return param.getToolId();
    }

    private Page<ToolResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Tool getOldEntity(ToolParam param) {
        return this.getById(getKey(param));
    }

    private Tool getEntity(ToolParam param) {
        Tool entity = new Tool();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

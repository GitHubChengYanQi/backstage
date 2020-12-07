package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Info;
import cn.atsoft.dasheng.app.mapper.InfoMapper;
import cn.atsoft.dasheng.app.model.params.InfoParam;
import cn.atsoft.dasheng.app.model.result.InfoResult;
import  cn.atsoft.dasheng.app.service.InfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据库信息表 服务实现类
 * </p>
 *
 * @author sing
 * @since 2020-12-07
 */
@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {

    @Override
    public void add(InfoParam param){
        Info entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(InfoParam param){
        Info oldEntity = getOldEntity(param);
        Info newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InfoResult findBySpec(InfoParam param){
        return null;
    }

    @Override
    public List<InfoResult> findListBySpec(InfoParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(InfoParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InfoParam param){
        return param.getDbId();
    }

    private Page getPageContext() {
        return PageFactory.defaultPage();
    }

    private Info getOldEntity(InfoParam param) {
        return this.getById(getKey(param));
    }

    private Info getEntity(InfoParam param) {
        Info entity = new Info();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

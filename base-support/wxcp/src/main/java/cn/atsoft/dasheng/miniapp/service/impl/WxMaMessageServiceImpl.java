package cn.atsoft.dasheng.miniapp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.miniapp.entity.WxMaMessage;
import cn.atsoft.dasheng.miniapp.mapper.WxMaMessageMapper;
import cn.atsoft.dasheng.miniapp.model.params.WxMaMessageParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaMessageResult;
import  cn.atsoft.dasheng.miniapp.service.WxMaMessageService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 小程序订阅消息 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-11
 */
@Service
public class WxMaMessageServiceImpl extends ServiceImpl<WxMaMessageMapper, WxMaMessage> implements WxMaMessageService {

    @Override
    public void add(WxMaMessageParam param){
        WxMaMessage entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WxMaMessageParam param){
        this.removeById(getKey(param));
//        WxMaMessage entity = this.getOldEntity(param);
//        entity.setDisplay(0);
//        this.updateById(entity);
    }

    @Override
    public void update(WxMaMessageParam param){
        WxMaMessage oldEntity = getOldEntity(param);
        WxMaMessage newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxMaMessageResult findBySpec(WxMaMessageParam param){
        return null;
    }

    @Override
    public List<WxMaMessageResult> findListBySpec(WxMaMessageParam param){
        return null;
    }

    @Override
    public PageInfo<WxMaMessageResult> findPageBySpec(WxMaMessageParam param){
        Page<WxMaMessageResult> pageContext = getPageContext();
        IPage<WxMaMessageResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<WxMaMessageResult> findPageBySpec(WxMaMessageParam param, DataScope dataScope){
        Page<WxMaMessageResult> pageContext = getPageContext();
        IPage<WxMaMessageResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WxMaMessageParam param){
        return null;
    }

    private Page<WxMaMessageResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxMaMessage getOldEntity(WxMaMessageParam param) {
        return this.getById(getKey(param));
    }

    private WxMaMessage getEntity(WxMaMessageParam param) {
        WxMaMessage entity = new WxMaMessage();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

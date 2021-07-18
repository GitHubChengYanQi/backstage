package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Client;
import cn.atsoft.dasheng.app.mapper.ClientMapper;
import cn.atsoft.dasheng.app.model.params.ClientParam;
import cn.atsoft.dasheng.app.model.result.ClientResult;
import  cn.atsoft.dasheng.app.service.ClientService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 客户管理表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Override
    public void add(ClientParam param){
        Client entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ClientParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ClientParam param){
        Client oldEntity = getOldEntity(param);
        Client newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ClientResult findBySpec(ClientParam param){
        return null;
    }

    @Override
    public List<ClientResult> findListBySpec(ClientParam param){
        return null;
    }

    @Override
    public PageInfo<ClientResult> findPageBySpec(ClientParam param){
        Page<ClientResult> pageContext = getPageContext();
        IPage<ClientResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ClientParam param){
        return param.getClientId();
    }

    private Page<ClientResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Client getOldEntity(ClientParam param) {
        return this.getById(getKey(param));
    }

    private Client getEntity(ClientParam param) {
        Client entity = new Client();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

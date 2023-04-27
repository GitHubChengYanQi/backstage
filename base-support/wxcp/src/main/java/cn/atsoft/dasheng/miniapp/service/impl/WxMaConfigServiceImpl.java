package cn.atsoft.dasheng.miniapp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.miniapp.entity.WxMaConfig;
import cn.atsoft.dasheng.miniapp.mapper.WxMaConfigMapper;
import cn.atsoft.dasheng.miniapp.model.params.WxMaConfigParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaConfigResult;
import cn.atsoft.dasheng.miniapp.service.WxMaConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.TenantService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 微信小程序配置表（对应租户） 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-25
 */
@Service
public class WxMaConfigServiceImpl extends ServiceImpl<WxMaConfigMapper, WxMaConfig> implements WxMaConfigService {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserService userService;

    @Override
    public void add(WxMaConfigParam param) {
        WxMaConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WxMaConfigParam param) {
        WxMaConfig entity = this.getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(WxMaConfigParam param) {
        WxMaConfig oldEntity = getOldEntity(param);
        WxMaConfig newEntity = getEntity(param);
        if (!oldEntity.getTenantId().equals(newEntity.getTenantId())) {
            throw new RuntimeException("租户ID不能修改");
        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxMaConfigResult findBySpec(WxMaConfigParam param) {
        return null;
    }

    @Override
    public List<WxMaConfigResult> findListBySpec(WxMaConfigParam param) {
        List<WxMaConfigResult> resultList = this.baseMapper.customList(param);
        this.format(resultList);
        return resultList;
    }

    @Override
    public PageInfo<WxMaConfigResult> findPageBySpec(WxMaConfigParam param) {
        Page<WxMaConfigResult> pageContext = getPageContext();
        IPage<WxMaConfigResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<WxMaConfigResult> findPageBySpec(WxMaConfigParam param, DataScope dataScope) {
        Page<WxMaConfigResult> pageContext = getPageContext();
        IPage<WxMaConfigResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<WxMaConfigResult> dataList) {
        //取出tenantIds从dataList
        List<Long> tenantIds = dataList.stream().map(WxMaConfigResult::getTenantId).distinct().collect(Collectors.toList());
        //取出createUser
        List<Long> createUserIds = dataList.stream().map(WxMaConfigResult::getCreateUser).distinct().collect(Collectors.toList());
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(createUserIds);
        //将userResultsByIds转换为map
        Map<Long, UserResult> userResultMap = userResultsByIds.stream().collect(Collectors.toMap(UserResult::getUserId, Function.identity()));
        //tenantService.listByIds(tenantIds) 从数据库中取出租户信息
        List<TenantResult> tenantResultsByIds = tenantService.getTenantResultsByIds(tenantIds);
        //将租户信息放入map中
        Map<Long, TenantResult> tenantResultMap = tenantResultsByIds.stream().collect(Collectors.toMap(TenantResult::getTenantId, Function.identity()));
//        dataList匹配tenantResultMap
        dataList.forEach(item -> {
            item.setTenantResult(tenantResultMap.get(item.getTenantId()));
            item.setUserResult(userResultMap.get(item.getCreateUser()));
        });


    }

    private Serializable getKey(WxMaConfigParam param) {
        return param.getWxMaConfigId();
    }

    private Page<WxMaConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxMaConfig getOldEntity(WxMaConfigParam param) {
        return this.getById(getKey(param));
    }

    private WxMaConfig getEntity(WxMaConfigParam param) {
        WxMaConfig entity = new WxMaConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

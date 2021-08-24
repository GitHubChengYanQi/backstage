package cn.atsoft.dasheng.portal.wxUser.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.mapper.WxuserInfoMapper;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.model.result.WxuserInfoResult;
import  cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户 小程序 关联 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
@Service
public class WxuserInfoServiceImpl extends ServiceImpl<WxuserInfoMapper, WxuserInfo> implements WxuserInfoService {

    @Override
    public void add(WxuserInfoParam param){
        WxuserInfo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WxuserInfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WxuserInfoParam param){
        WxuserInfo oldEntity = getOldEntity(param);
        WxuserInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxuserInfoResult findBySpec(WxuserInfoParam param){
        return null;
    }

    @Override
    public List<WxuserInfoResult> findListBySpec(WxuserInfoParam param){
        return null;
    }

    @Override
    public PageInfo<WxuserInfoResult> findPageBySpec(WxuserInfoParam param){
        Page<WxuserInfoResult> pageContext = getPageContext();
        IPage<WxuserInfoResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WxuserInfoParam param){
        return param.getUserInfoId();
    }

    private Page<WxuserInfoResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxuserInfo getOldEntity(WxuserInfoParam param) {
        return this.getById(getKey(param));
    }

    private WxuserInfo getEntity(WxuserInfoParam param) {
        WxuserInfo entity = new WxuserInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

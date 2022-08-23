package cn.atsoft.dasheng.api.uc.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.mapper.OpenUserInfoMapper;
import cn.atsoft.dasheng.api.uc.model.params.OpenUserInfoParam;
import cn.atsoft.dasheng.api.uc.model.result.OpenUserInfoResult;
import  cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-25
 */
@Service
public class OpenUserInfoServiceImpl extends ServiceImpl<OpenUserInfoMapper, OpenUserInfo> implements OpenUserInfoService {

    @Override
    public void add(OpenUserInfoParam param){
        OpenUserInfo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OpenUserInfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OpenUserInfoParam param){
        OpenUserInfo oldEntity = getOldEntity(param);
        OpenUserInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OpenUserInfoResult findBySpec(OpenUserInfoParam param){
        return null;
    }

    @Override
    public List<OpenUserInfoResult> findListBySpec(OpenUserInfoParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(OpenUserInfoParam param){
        Page<OpenUserInfoResult> pageContext = getPageContext();
        IPage<OpenUserInfoResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OpenUserInfoParam param){
        return param.getPrimaryKey();
    }

    private Page<OpenUserInfoResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OpenUserInfo getOldEntity(OpenUserInfoParam param) {
        return this.getById(getKey(param));
    }

    private OpenUserInfo getEntity(OpenUserInfoParam param) {
        OpenUserInfo entity = new OpenUserInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

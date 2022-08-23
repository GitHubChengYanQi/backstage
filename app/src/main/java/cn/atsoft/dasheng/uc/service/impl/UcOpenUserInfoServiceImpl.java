package cn.atsoft.dasheng.uc.service.impl;


import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.mapper.UcOpenUserInfoMapper;
import cn.atsoft.dasheng.uc.model.params.UcOpenUserInfoParam;
import cn.atsoft.dasheng.uc.model.result.UcOpenUserInfoResult;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
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
 * @author Sing
 * @since 2021-03-17
 */
@Service
public class UcOpenUserInfoServiceImpl extends ServiceImpl<UcOpenUserInfoMapper, UcOpenUserInfo> implements UcOpenUserInfoService {

    @Override
    public UcOpenUserInfo add(UcOpenUserInfoParam param){
        UcOpenUserInfo entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(UcOpenUserInfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public UcOpenUserInfo update(UcOpenUserInfoParam param){
        UcOpenUserInfo oldEntity = getOldEntity(param);
        UcOpenUserInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
        return oldEntity;
    }

    @Override
    public UcOpenUserInfoResult findBySpec(UcOpenUserInfoParam param){
        return null;
    }

    @Override
    public List<UcOpenUserInfoResult> findListBySpec(UcOpenUserInfoParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(UcOpenUserInfoParam param){
        Page<UcOpenUserInfoResult> pageContext = getPageContext();
        IPage<UcOpenUserInfoResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }
    @Override
    public UcOpenUserInfoResult findByOne(UcOpenUserInfoParam param){
        return this.baseMapper.customListFindOne(param);
    }

    private Serializable getKey(UcOpenUserInfoParam param){
        return param.getPrimaryKey();
    }

    private Page<UcOpenUserInfoResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private UcOpenUserInfo getOldEntity(UcOpenUserInfoParam param) {
        return this.getById(getKey(param));
    }

    private UcOpenUserInfo getEntity(UcOpenUserInfoParam param) {
        UcOpenUserInfo entity = new UcOpenUserInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

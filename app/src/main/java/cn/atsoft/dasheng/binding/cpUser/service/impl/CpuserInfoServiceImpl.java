package cn.atsoft.dasheng.binding.cpUser.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.cpUser.entity.CpuserInfo;
import cn.atsoft.dasheng.binding.cpUser.mapper.CpuserInfoMapper;
import cn.atsoft.dasheng.binding.cpUser.model.params.CpuserInfoParam;
import cn.atsoft.dasheng.binding.cpUser.model.result.CpuserInfoResult;
import  cn.atsoft.dasheng.binding.cpUser.service.CpuserInfoService;
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
 * @since 2021-10-12
 */
@Service
public class CpuserInfoServiceImpl extends ServiceImpl<CpuserInfoMapper, CpuserInfo> implements CpuserInfoService {

    @Override
    public void add(CpuserInfoParam param){
        CpuserInfo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CpuserInfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CpuserInfoParam param){
        CpuserInfo oldEntity = getOldEntity(param);
        CpuserInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CpuserInfoResult findBySpec(CpuserInfoParam param){
        return null;
    }

    @Override
    public List<CpuserInfoResult> findListBySpec(CpuserInfoParam param){
        return null;
    }

    @Override
    public PageInfo<CpuserInfoResult> findPageBySpec(CpuserInfoParam param){
        Page<CpuserInfoResult> pageContext = getPageContext();
        IPage<CpuserInfoResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CpuserInfoParam param){
        return param.getCpUserId();
    }

    private Page<CpuserInfoResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CpuserInfo getOldEntity(CpuserInfoParam param) {
        return this.getById(getKey(param));
    }

    private CpuserInfo getEntity(CpuserInfoParam param) {
        CpuserInfo entity = new CpuserInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

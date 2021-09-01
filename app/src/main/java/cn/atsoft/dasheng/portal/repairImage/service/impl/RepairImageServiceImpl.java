package cn.atsoft.dasheng.portal.repairImage.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repairImage.entity.RepairImage;
import cn.atsoft.dasheng.portal.repairImage.mapper.RepairImageMapper;
import cn.atsoft.dasheng.portal.repairImage.model.params.RepairImageParam;
import cn.atsoft.dasheng.portal.repairImage.model.result.RepairImageResult;
import  cn.atsoft.dasheng.portal.repairImage.service.RepairImageService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报修图片 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-09-01
 */
@Service
public class RepairImageServiceImpl extends ServiceImpl<RepairImageMapper, RepairImage> implements RepairImageService {

    @Override
    public void add(RepairImageParam param){
        RepairImage entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RepairImageParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RepairImageParam param){
        RepairImage oldEntity = getOldEntity(param);
        RepairImage newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RepairImageResult findBySpec(RepairImageParam param){
        return null;
    }

    @Override
    public List<RepairImageResult> findListBySpec(RepairImageParam param){
        return null;
    }

    @Override
    public PageInfo<RepairImageResult> findPageBySpec(RepairImageParam param){
        Page<RepairImageResult> pageContext = getPageContext();
        IPage<RepairImageResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RepairImageParam param){
        return param.getRepairImageId();
    }

    private Page<RepairImageResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RepairImage getOldEntity(RepairImageParam param) {
        return this.getById(getKey(param));
    }

    private RepairImage getEntity(RepairImageParam param) {
        RepairImage entity = new RepairImage();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

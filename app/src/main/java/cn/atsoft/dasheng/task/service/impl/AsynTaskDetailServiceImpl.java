package cn.atsoft.dasheng.task.service.impl;


import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.task.entity.AsynTaskDetail;
import cn.atsoft.dasheng.task.mapper.AsynTaskDetailMapper;
import cn.atsoft.dasheng.task.model.params.AsynTaskDetailParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskDetailResult;
import cn.atsoft.dasheng.task.service.AsynTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 任务详情表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-26
 */
@Service
public class AsynTaskDetailServiceImpl extends ServiceImpl<AsynTaskDetailMapper, AsynTaskDetail> implements AsynTaskDetailService {

    @Override
    public void add(AsynTaskDetailParam param) {
        AsynTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AsynTaskDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AsynTaskDetailParam param) {
        AsynTaskDetail oldEntity = getOldEntity(param);
        AsynTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }


    /**
     * 获取物料导入数据任务
     *
     * @param taskId
     * @return
     */
    @Override
    public List<AsynTaskDetailResult> getSkuExcelDetail(Long taskId) {

        List<AsynTaskDetail> taskDetails = this.query().eq("task_id", taskId).eq("display", 1).list();
        List<AsynTaskDetailResult> detailResults = BeanUtil.copyToList(taskDetails, AsynTaskDetailResult.class, new CopyOptions());

        for (AsynTaskDetailResult detailResult : detailResults) {
            SkuExcelItem skuExcelItem = JSON.parseObject(detailResult.getContentJson(), SkuExcelItem.class);
            detailResult.setSkuExcelItem(skuExcelItem);
        }
        return detailResults;
    }

    @Override
    public AsynTaskDetailResult findBySpec(AsynTaskDetailParam param) {
        return null;
    }

    @Override
    public List<AsynTaskDetailResult> findListBySpec(AsynTaskDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<AsynTaskDetailResult> findPageBySpec(AsynTaskDetailParam param) {
        Page<AsynTaskDetailResult> pageContext = getPageContext();
        IPage<AsynTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AsynTaskDetailParam param) {
        return param.getDetailId();
    }

    private Page<AsynTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AsynTaskDetail getOldEntity(AsynTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private AsynTaskDetail getEntity(AsynTaskDetailParam param) {
        AsynTaskDetail entity = new AsynTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}

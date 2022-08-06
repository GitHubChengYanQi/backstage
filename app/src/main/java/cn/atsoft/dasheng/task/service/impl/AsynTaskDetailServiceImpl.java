package cn.atsoft.dasheng.task.service.impl;


import cn.atsoft.dasheng.Excel.pojo.PositionBind;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.Excel.pojo.SpuExcel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Integer> getNum(Long taskId) {

        Map<String, Integer> map = new HashMap<>();
        List<AsynTaskDetail> taskDetails = this.query().eq("task_id", taskId).eq("display", 1).list();
        List<AsynTaskDetailResult> detailResults = BeanUtil.copyToList(taskDetails, AsynTaskDetailResult.class, new CopyOptions());
        int successNum = 0;
        int errorNum = 0;
        for (AsynTaskDetailResult detailResult : detailResults) {
            if (detailResult.getStatus() == 99) {
                successNum++;
            }
            if (detailResult.getStatus() == 50) {
                errorNum++;
            }
        }
        map.put("successNum", successNum);
        map.put("errorNum", errorNum);
        return map;
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
        format(page.getRecords());
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

    private void format(List<AsynTaskDetailResult> data) {

        for (AsynTaskDetailResult datum : data) {

            if (datum.getType().equals("物料导入")) {
                SkuExcelItem result = JSON.parseObject(datum.getContentJson(), SkuExcelItem.class);
                datum.setSkuExcelItem(result);
            }

            if (datum.getType().equals("产品导入")) {
                SpuExcel spuExcel = JSON.parseObject(datum.getContentJson(), SpuExcel.class);
                datum.setSpuExcel(spuExcel);
            }

            if (datum.getType().equals("库存导入")) {
                PositionBind positionBind = JSON.parseObject(datum.getContentJson(), PositionBind.class);
                datum.setPositionBind(positionBind);
            }

            if (datum.getType().equals("库位导入")) {
                PositionBind positionBind = JSON.parseObject(datum.getContentJson(), PositionBind.class);
                datum.setPositionBind(positionBind);
            }
            datum.setContentJson(null);
        }
    }
}

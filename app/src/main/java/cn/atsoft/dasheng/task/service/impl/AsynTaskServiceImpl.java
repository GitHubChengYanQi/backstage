package cn.atsoft.dasheng.task.service.impl;


import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.pojo.AnalysisResult;
import cn.atsoft.dasheng.app.pojo.StockStatement;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.mapper.AsynTaskMapper;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import cn.atsoft.dasheng.task.pojo.SkuAnalyse;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 等待任务表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-01
 */
@Service
public class AsynTaskServiceImpl extends ServiceImpl<AsynTaskMapper, AsynTask> implements AsynTaskService {

    @Override
    public void add(AsynTaskParam param) {
        AsynTask entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AsynTaskParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AsynTaskParam param) {
        AsynTask oldEntity = getOldEntity(param);
        AsynTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AsynTaskResult findBySpec(AsynTaskParam param) {
        return null;
    }

    @Override
    public List<AsynTaskResult> findListBySpec(AsynTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<AsynTaskResult> findPageBySpec(AsynTaskParam param) {
        Page<AsynTaskResult> pageContext = getPageContext();
        IPage<AsynTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }


    @Override
    public Object spectaculars() {
        List<AsynTask> asynTasks = this.query().eq("type", "报表物料分析").eq("display", 1).list();
        List<AsynTaskResult> asynTaskResults = BeanUtil.copyToList(asynTasks, AsynTaskResult.class);
        format(asynTaskResults);

        /**
         * 先从异步里 取出被分析的物料
         */
        Map<String, List<AnalysisResult>> map = new HashMap<>();
        for (AsynTaskResult asynTaskResult : asynTaskResults) {
            AllBomResult allBomResult = asynTaskResult.getAllBomResult();
            List<AllBomResult.View> view = allBomResult.getView();
            AllBomResult.View skuView = view.get(0);
            List<AnalysisResult> owe = allBomResult.getOwe();
            map.put(skuView.getName(), owe);
        }
        /**
         * 通过 物料取出分类
         */
        Map<String, List<SkuAnalyse>> skuMap = new HashMap<>();
        for (String skuName : map.keySet()) {
            List<AnalysisResult> analysisResults = map.get(skuName);
            List<Long> skuIds = new ArrayList<>();
            for (AnalysisResult analysisResult : analysisResults) {
                skuIds.add(analysisResult.getSkuId());
            }
            /**
             * 分类叠加 取最小
             */
            List<SkuAnalyse> skuAnalyses = skuAnalyses(skuIds);
            Map<String, Long> spuClassNum = new HashMap<>();

            for (SkuAnalyse skuAnalyse : skuAnalyses) {
                for (AnalysisResult analysisResult : analysisResults) {
                    if (skuAnalyse.getSkuId().equals(analysisResult.getSkuId())) {

                        analysisResult.setProduceMix(analysisResult.getStockNumber() / analysisResult.getDemandNumber());  //可生产数量
                        Long number = spuClassNum.get(skuAnalyse.getClassName());


                        if (ToolUtil.isEmpty(number)) {
                            spuClassNum.put(skuAnalyse.getClassName(), analysisResult.getProduceMix());
                        } else {
                            if (number >= analysisResult.getProduceMix()) {
                                spuClassNum.put(skuAnalyse.getClassName(), analysisResult.getProduceMix());
                            }
                        }
                        break;
                    }
                }
            }


            List<SkuAnalyse> skuAnalyseList = new ArrayList<>();
            for (String s : spuClassNum.keySet()) {
                SkuAnalyse skuAnalyse = new SkuAnalyse();
                skuAnalyse.setClassName(s);
                skuAnalyse.setNumber(spuClassNum.get(s));
                skuAnalyseList.add(skuAnalyse);
            }
            skuMap.put(skuName, skuAnalyseList);
        }
        return skuMap;
    }


    @Override
    public Object stockSpectaculars() {

        List<AsynTask> asynTasks = this.query().eq("type", "库存报表").last("limit 1").list();
        List<AsynTaskResult> asynTaskResults = BeanUtil.copyToList(asynTasks, AsynTaskResult.class);
        format(asynTaskResults);

        if (ToolUtil.isNotEmpty(asynTaskResults)) {
            AsynTaskResult asynTaskResult = asynTaskResults.get(0);
            return asynTaskResult.getStockStatements();
        }
        return null;
    }

    /**
     * 异步 sku查询分类
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuAnalyse> skuAnalyses(List<Long> skuIds) {
        return this.baseMapper.skuAnalyseList(skuIds);
    }

    private Serializable getKey(AsynTaskParam param) {
        return param.getTaskId();
    }

    private Page<AsynTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AsynTask getOldEntity(AsynTaskParam param) {
        return this.getById(getKey(param));
    }

    private AsynTask getEntity(AsynTaskParam param) {
        AsynTask entity = new AsynTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<AsynTaskResult> data) {

        for (AsynTaskResult datum : data) {
            if (ToolUtil.isNotEmpty(datum.getType()) && (datum.getType().equals("物料分析") || datum.getType().equals("报表物料分析"))) {   //物料分析或者报表
                AllBomResult allBomResult = JSON.parseObject(datum.getContent(), AllBomResult.class);
                datum.setAllBomResult(allBomResult);
            }

            if (ToolUtil.isNotEmpty(datum.getType()) && datum.getType().equals("库存报表")) {
                List<StockStatement> stockStatements = JSON.parseArray(datum.getContent(), StockStatement.class);           //库存报表
                datum.setStockStatements(stockStatements);
            }

        }

    }
}

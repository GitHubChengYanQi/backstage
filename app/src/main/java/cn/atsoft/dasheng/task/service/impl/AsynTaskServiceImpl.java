package cn.atsoft.dasheng.task.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.pojo.AnalysisResult;
import cn.atsoft.dasheng.app.pojo.StockStatement;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.mapper.AsynTaskMapper;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import cn.atsoft.dasheng.task.pojo.BomRotation;
import cn.atsoft.dasheng.task.pojo.SkuAnalyse;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService partsDetailService;

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
    public List<AsynTaskResult> BomDetailed() {
        List<AsynTask> asynTasks = this.query().eq("type", "报表物料分析").list();
        List<AsynTaskResult> asynTaskResults = BeanUtil.copyToList(asynTasks, AsynTaskResult.class);


        List<Long> skuIds = new ArrayList<>();
        for (AsynTaskResult asynTask : asynTaskResults) {
            if (ToolUtil.isNotEmpty(asynTask.getContent())) {
                AllBomResult allBomResult = JSON.parseObject(asynTask.getContent(), AllBomResult.class);
                asynTask.setAllBomResult(allBomResult);
                for (AnalysisResult analysisResult : allBomResult.getOwe()) {
                    skuIds.add(analysisResult.getSkuId());
                }
            }

        }

        List<StockDetails> stockDetails = skuIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query()
                .select("sku_id AS skuId ,sum(number) as num ")
                .eq("display", 1)
                .in("sku_id", skuIds)
                .groupBy("sku_id")
                .list();


        for (AsynTaskResult asynTaskResult : asynTaskResults) {
            AllBomResult allBomResult = asynTaskResult.getAllBomResult();
            if (ToolUtil.isNotEmpty(allBomResult) && ToolUtil.isNotEmpty(allBomResult.getOwe())) {
                for (AnalysisResult analysisResult : allBomResult.getOwe()) {
                    for (StockDetails stockDetail : stockDetails) {
                        if (analysisResult.getSkuId().equals(stockDetail.getSkuId())) {
                            analysisResult.setStockNumber(stockDetail.getNum());
                        }
                    }
                }
            }
        }


        return asynTaskResults;
    }


    @Override
    public List<AllBomResult.View> BomDetailedVersion() {
        List<AsynTask> asynTasks = this.query().eq("type", "报表物料分析").list();
        List<AsynTaskResult> asynTaskResults = BeanUtil.copyToList(asynTasks, AsynTaskResult.class);

        List<AllBomResult.View> views = new ArrayList<>();
        for (AsynTaskResult asynTaskResult : asynTaskResults) {
            if (ToolUtil.isNotEmpty(asynTaskResult.getContent())) {
                AllBomResult allBomResult = JSON.parseObject(asynTaskResult.getContent(), AllBomResult.class);
                if (ToolUtil.isNotEmpty(allBomResult) && ToolUtil.isNotEmpty(allBomResult.getView())) {
                    AllBomResult.View view = allBomResult.getView().get(0);
                    views.add(view);
                }
            }
        }

        return views;
    }


    @Override
    public List<ErpPartsDetail> bomResult(Long skuId, int num) {
        Parts parts = partsService.query().eq("sku_id", skuId).eq("status", 99).eq("display", 1).one();
        if (ToolUtil.isEmpty(parts)) {
            return null;
        }
        return loopChild(parts, num);
    }

    private List<ErpPartsDetail> loopChild(Parts parts, int num) {

        List<ErpPartsDetail> back = new ArrayList<>();
        List<ErpPartsDetail> details = partsDetailService.query().eq("parts_id", parts.getPartsId()).eq("display", 1).list();  //所需的物料

        if (ToolUtil.isNotEmpty(details)) {
            for (ErpPartsDetail detail : details) {
                int needNum = (int) (detail.getNumber() * num);          //需求数
                List<ErpPartsDetail> partsDetails = bomResult(detail.getSkuId(), needNum);
                if (ToolUtil.isNotEmpty(partsDetails)) {
                    back.addAll(bomResult(detail.getSkuId(), needNum));
                } else {   //最下级
                    detail.setNumber((double) needNum);
                    back.add(detail);
                }
            }
        }
        return back;
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
            if (ToolUtil.isNotEmpty(allBomResult)) {
                List<AllBomResult.View> view = allBomResult.getView();
                AllBomResult.View skuView = view.get(0);
                List<AnalysisResult> owe = allBomResult.getOwe();
                map.put(skuView.getName(), owe);
            }
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
    public Object spectacularsVersion() {
        List<AsynTask> asynTasks = this.query().eq("type", "报表物料分析").eq("display", 1).list();
        List<AsynTaskResult> asynTaskResults = BeanUtil.copyToList(asynTasks, AsynTaskResult.class);
        format(asynTaskResults);

        /**
         * 先从异步里 取出被分析的物料
         */
        Map<String, List<AnalysisResult>> map = new HashMap<>();
        for (AsynTaskResult asynTaskResult : asynTaskResults) {
            AllBomResult allBomResult = asynTaskResult.getAllBomResult();
            if (ToolUtil.isNotEmpty(allBomResult)) {
                List<AllBomResult.View> view = allBomResult.getView();
                AllBomResult.View skuView = view.get(0);
                List<AnalysisResult> owe = allBomResult.getOwe();
                map.put(skuView.getName(), owe);
            }
        }
        /**
         * 通过 物料取出分类
         */
        List<BomRotation> bomRotations = new ArrayList<>();
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
            BomRotation bomRotation = new BomRotation();
            bomRotation.setSkuName(skuName);
            bomRotation.setSkuAnalyses(skuAnalyseList);
            bomRotations.add(bomRotation);
        }
        return bomRotations;
    }

    @Override
    public Object stockSpectaculars() {

        List<AsynTask> asynTasks = this.query().eq("type", "库存报表").last("limit 1").list();
        List<AsynTaskResult> asynTaskResults = BeanUtil.copyToList(asynTasks, AsynTaskResult.class);
        format(asynTaskResults);

        if (ToolUtil.isNotEmpty(asynTaskResults)) {     //就是一个
            AsynTaskResult asynTaskResult = asynTaskResults.get(0);
            List<StockStatement> stockStatements = asynTaskResult.getStockStatements();

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

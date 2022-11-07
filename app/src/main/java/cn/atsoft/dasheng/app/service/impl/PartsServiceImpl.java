package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.pojo.AllBomParam;
import cn.atsoft.dasheng.app.pojo.AsyncMethod;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.mapper.PartsMapper;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.asyn.entity.AsynTask;
import cn.atsoft.dasheng.asyn.service.AsynTaskService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
@Service
public class PartsServiceImpl extends ServiceImpl<PartsMapper, Parts> implements PartsService {

    @Autowired
    private UserService userService;
    @Autowired
    private ErpPartsDetailService erpPartsDetailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private AsyncMethod asyncMethod;
    @Autowired
    private AsynTaskService asynTaskService;


    @Override
    public Parts add(PartsParam partsParam) {

        judge(partsParam); //防止添加重复数据
        DeadLoopJudge(partsParam); //防止死循环添加

        //如果相同sku已有发布  新创建
        Parts one = this.query().eq("sku_id", partsParam.getSkuId()).eq("display", 1).eq("type", partsParam.getType()).eq("status", 99).one();
        if (ToolUtil.isNotEmpty(one)) {
            one.setStatus(0);
            this.updateById(one);
            Parts parts = new Parts();
            ToolUtil.copyProperties(partsParam, parts);
            parts.setStatus(99);
            parts.setPartsId(null);
            this.save(parts);
            List<ErpPartsDetail> partsDetails = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            for (ErpPartsDetailParam part : partsParam.getParts()) {
                skuIds.add(part.getSkuId());
                part.setPartsId(parts.getPartsId());
                part.setPartsDetailId(null);
                ErpPartsDetail partsDetail = new ErpPartsDetail();
                ToolUtil.copyProperties(part, partsDetail);
                partsDetails.add(partsDetail);
            }
            erpPartsDetailService.saveBatch(partsDetails);
            // 更新当前节点，及下级
            updateStatue(parts, skuIds);  // 更新上级状态
            return parts;
        }
//----------------------------------------------------------------------------------------------------------------------


        Sku sku = new Sku();
        if (ToolUtil.isNotEmpty(partsParam.getSkuId())) {
            sku = skuService.getById(partsParam.getSkuId()); // query().eq("sku_id", partsParam.getItem().getSkuId()).eq("display", 1).count();
            if (ToolUtil.isEmpty(sku)) {
                throw new ServiceException(500, "物料不存在");
            }
        } else {
            if (ToolUtil.isNotEmpty(partsParam.getItem().getSpuId())) {
                Spu spu = spuService.getById(partsParam.getItem().getSpuId());
                SpuParam spuParam = new SpuParam();
                ToolUtil.copyProperties(spu, spuParam);
                SkuParam skuParam = new SkuParam();
                skuParam.setBatch(partsParam.getBatch());
                skuParam.setSkuName(partsParam.getSkuName());
                skuParam.setType(0);
                skuParam.setRemarks(partsParam.getSkuNote());
                skuParam.setStandard(partsParam.getStandard());
                skuParam.setSpecifications(partsParam.getSpecifications());
                skuParam.setSpuId(partsParam.getItem().getSpuId());
                skuParam.setSku(partsParam.getSku());
                skuParam.setSpu(spuParam);
                skuParam.setSpuClass(spuParam.getSpuClassificationId());

                Map<String, Sku> add = skuService.add(skuParam);
                Sku success = add.get("success");

                Long skuId = success.getSkuId();
                sku.setSkuId(skuId);
            } else if (ToolUtil.isNotEmpty(partsParam.getItem().getSkuId())) {
                sku = skuService.getById(partsParam.getItem().getSkuId()); // query().eq("sku_id", partsParam.getItem().getSkuId()).eq("display", 1).count();
                if (ToolUtil.isEmpty(sku)) {
                    throw new ServiceException(500, "物料不存在");
                }
            }
        }
        Parts entity = getEntity(partsParam);
        entity.setSkuId(sku.getSkuId());
        entity.setPartsId(null);
        entity.setStatus(99);
        this.save(entity);

        List<ErpPartsDetail> details = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : partsParam.getParts()) {
            if (ToolUtil.isNotEmpty(part)) {
                ErpPartsDetail detail = new ErpPartsDetail();
                detail.setSkuId(part.getSkuId());
                detail.setNumber(part.getNumber());
                detail.setPartsId(entity.getPartsId());
                detail.setNote(part.getNote());
                details.add(detail);
                skuIds.add(part.getSkuId());
            }
        }
        erpPartsDetailService.saveBatch(details);

//        updateStatue(parts, skuIds);
        updateStatue(entity, skuIds);  // 更新上级状态

        return entity;


    }

    @Transactional
    @Override
    public Parts newAdd(PartsParam partsParam) {
        //如果相同skuBom    发布最新创建的
        if (ToolUtil.isNotEmpty(partsParam.getSkuId())) {
            Parts one = this.query().eq("sku_id", partsParam.getSkuId()).eq("display", 1).eq("status", 99).one();
            if (ToolUtil.isNotEmpty(one)) {
                one.setStatus(0);
                this.updateById(one);
            }
            Parts parts = new Parts();
            if (ToolUtil.isEmpty(partsParam.getName())) {
                throw new ServiceException(500, "请输入版本号");
            }
            ToolUtil.copyProperties(partsParam, parts);
            parts.setStatus(99);
            parts.setPartsId(null);
            this.save(parts);
            List<ErpPartsDetail> partsDetails = new ArrayList<>();
            for (ErpPartsDetailParam part : partsParam.getParts()) {
                part.setPartsId(parts.getPartsId());
                part.setAutoOutstock(part.getAutoOutstock());
                part.setPartsDetailId(null);
                ErpPartsDetail partsDetail = new ErpPartsDetail();
                ToolUtil.copyProperties(part, partsDetail);
                partsDetails.add(partsDetail);
            }
            erpPartsDetailService.saveBatch(partsDetails);
            List<Long> children = getChildren(parts.getPartsId());   //更新childRens
            if (ToolUtil.isNotEmpty(children)) {
                parts.setChildren(JSON.toJSONString(children));
            }
            this.updateById(parts);
            return parts;
        } else if (ToolUtil.isNotEmpty(partsParam.getSpuId())) {
            Spu spu = spuService.getById(partsParam.getItem().getSpuId());
            SpuParam spuParam = new SpuParam();
            ToolUtil.copyProperties(spu, spuParam);
            SkuParam skuParam = new SkuParam();
            skuParam.setBatch(partsParam.getBatch());
            skuParam.setSkuName(partsParam.getSkuName());
            skuParam.setType(0);
            skuParam.setRemarks(partsParam.getSkuNote());
            skuParam.setStandard(partsParam.getStandard());
            skuParam.setSpecifications(partsParam.getSpecifications());
            skuParam.setSpuId(partsParam.getItem().getSpuId());
            skuParam.setSku(partsParam.getSku());
            skuParam.setSpu(spuParam);
            skuParam.setSpuClass(spuParam.getSpuClassificationId());
            Map<String, Sku> add = skuService.add(skuParam);
            Sku success = add.get("success");
            Long skuId = success.getSkuId();

            // 以上新建sku
            partsParam.setSkuId(skuId);
            partsParam.setSpuId(partsParam.getSpuId());
            //以下新建bom
            Parts parts = new Parts();
            if (ToolUtil.isEmpty(partsParam.getName())) {
                throw new ServiceException(500, "请输入版本号");
            }
            ToolUtil.copyProperties(partsParam, parts);
            parts.setStatus(99);
            parts.setPartsId(null);
            this.save(parts);
            List<ErpPartsDetail> partsDetails = new ArrayList<>();
            for (ErpPartsDetailParam part : partsParam.getParts()) {
                part.setPartsId(parts.getPartsId());
                part.setAutoOutstock(part.getAutoOutstock());
                part.setPartsDetailId(null);
                ErpPartsDetail partsDetail = new ErpPartsDetail();
                ToolUtil.copyProperties(part, partsDetail);
                partsDetails.add(partsDetail);
            }
            erpPartsDetailService.saveBatch(partsDetails);
            List<Long> children = getChildren(parts.getPartsId());   //更新childRens
            if (ToolUtil.isNotEmpty(children)) {
                parts.setChildren(JSON.toJSONString(children));
            }
            this.updateById(parts);
            return parts;
        }

        throw new ServiceException(500, "创建失败");
    }


    private List<Long> getChildren(Long partId) {
        List<Long> partIds = new ArrayList<>();
        List<ErpPartsDetail> erpPartsDetails = erpPartsDetailService.query().eq("parts_id", partId).eq("display", 1).list();
        List<Long> skuIds = new ArrayList<>();

        for (ErpPartsDetail erpPartsDetail : erpPartsDetails) {
            skuIds.add(erpPartsDetail.getSkuId());
        }
        if (ToolUtil.isEmpty(skuIds)) {
            return partIds;
        }
        List<Parts> list = this.query().in("sku_id", skuIds).eq("status", 99).eq("display", 1).list();
        for (Parts parts : list) {
            partIds.add(parts.getPartsId());
            parts.setPid(partId);
//            getChildren(parts.getPartsId());
        }
        this.updateBatchById(list);
        return partIds;
    }


    /**
     * 开始分析
     */
    @Override
    @Scheduled(cron = "0 0 2 * * ?")   //每日凌晨两点
    public void startAnalyse() {
        System.err.println("定时任务-------> 物料分析:" + new DateTime());
        QueryWrapper<AsynTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "报表物料分析");
        asynTaskService.remove(queryWrapper);
        //先取最顶级物料
        List<Long> topSkuId = this.baseMapper.getTopSkuId();
        /**
         * 调用异步物料分析
         */
        for (Long skuId : topSkuId) {
            List<AllBomParam.SkuNumberParam> skuNumberParams = new ArrayList<>();
            skuNumberParams.add(new AllBomParam.SkuNumberParam(skuId, 1L, false));
            AllBomParam allBomParam = new AllBomParam(skuNumberParams);
            asyncMethod.task(null, skuNumberParams, allBomParam, "报表物料分析");
        }
    }

    private void judge(PartsParam partsParam) {
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : partsParam.getParts()) {
            skuIds.add(part.getSkuId());
            if (part.getSkuId().equals(partsParam.getSkuId())) {
                throw new ServiceException(500, "子件信息和父件信息不能相同");
            }
        }
        long count = skuIds.stream().distinct().count();
        if (count != partsParam.getParts().size()) {
            throw new ServiceException(500, "子件信息不可重复");
        }
    }

    @Override
    public void updateAdd(PartsParam partsParam) {

        judge(partsParam); //防止添加重复数据
        DeadLoopJudge(partsParam);  //防止死循环添加

        Parts newPart = new Parts();
        ToolUtil.copyProperties(partsParam, newPart);
        newPart.setPid(partsParam.getPartsId());
        newPart.setPartsId(null);
        this.save(newPart);

        List<ErpPartsDetail> details = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam partsDetail : partsParam.getParts()) {
            ErpPartsDetail newDetail = new ErpPartsDetail();
            ToolUtil.copyProperties(partsDetail, newDetail);
            newDetail.setPartsDetailId(null);
            newDetail.setPartsId(newPart.getPartsId());
            skuIds.add(partsDetail.getSkuId());
            details.add(newDetail);
        }

        erpPartsDetailService.saveBatch(details);

        updateStatue(newPart, skuIds);  //更新上级状态
    }


    /**
     * 获取当前上一级
     *
     * @param skuId
     * @return
     */
    @Override
    public List<PartsResult> getParent(Long skuId) {
        List<Parts> parts = this.query().like("children", skuId).eq("status", 99).eq("display", 1).orderByDesc("create_time").list();
        List<PartsResult> partsResults = BeanUtil.copyToList(parts, PartsResult.class);
        format(partsResults);
        return partsResults;
    }

    /**
     * 更新包含它的
     */
    public void updateChildren(Long skuId, String type) {

        List<Parts> partList = this.query().like("children", skuId).eq("display", 1).eq("type", type).eq("status", 99).list();
        for (Parts part : partList) {
            Map<String, List<Long>> childrenMap = getChildrens(part.getSkuId(), type);
            part.setChildren(JSON.toJSONString(childrenMap.get("children")));
            part.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
            // update
            QueryWrapper<Parts> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parts_id", part.getPartsId());
            this.update(part, queryWrapper);

            updateChildren(part.getSkuId(), type);
        }
    }

    /**
     * 递归
     */
    public Map<String, List<Long>> getChildrens(Long id, String type) {
        List<Long> childrensSkuIds = new ArrayList<>();
        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
            {
                put("children", new ArrayList<>());
                put("childrens", new ArrayList<>());
            }
        };
        List<Long> skuIds = new ArrayList<>();
        Parts parts = this.query().eq("sku_id", id).eq("display", 1).eq("type", type).eq("status", 99).one();
        if (ToolUtil.isNotEmpty(parts)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", parts.getPartsId()).eq("display", 1).list();
            for (ErpPartsDetail detail : details) {
                skuIds.add(detail.getSkuId());
                childrensSkuIds.add(detail.getSkuId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getSkuId(), type);
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", skuIds);
            result.put("childrens", childrensSkuIds);

        }
        return result;
    }

    @Override
    public void delete(PartsParam param) {
    }

    /**
     * 防止死循环添加
     *
     * @param param
     */

    private void DeadLoopJudge(PartsParam param) {

        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : param.getParts()) {
            skuIds.add(part.getSkuId());
        }

        List<Parts> parts = this.query().in("sku_id", skuIds).eq("display", 1).eq("status", 99).list();
        List<Long> alongs = new ArrayList<>();
        for (Parts part : parts) {
            JSONArray jsonArray = JSONUtil.parseArray(part.getChildrens());
            List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
            alongs.addAll(longs);
        }
        for (Long along : alongs) {

            if (along.equals(param.getSkuId())) {
                List<SkuResult> results = skuService.formatSkuResult(new ArrayList<Long>() {{
                    add(along);
                }});
                SkuResult result = results.get(0);
                throw new ServiceException(500, "请勿循环添加:" + result.getSpuResult().getName() + "/" + result.getSkuName());
            }


        }
    }

    @Override
    public void update(PartsParam param) {
        judge(param); //防止添加重复数据
        DeadLoopJudge(param);


        judge(param); //防止添加重复数据

        erpPartsDetailService.remove(new QueryWrapper<ErpPartsDetail>() {{
            eq("parts_id", param.getPartsId());
        }});

        Parts oldEntity = getOldEntity(param);
        Parts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

        List<ErpPartsDetail> partsDetails = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : param.getParts()) {
            ErpPartsDetail partsDetail = new ErpPartsDetail();
            ToolUtil.copyProperties(part, partsDetail);
            partsDetail.setPartsId(newEntity.getPartsId());
            partsDetails.add(partsDetail);
            skuIds.add(part.getSkuId());
        }
        erpPartsDetailService.saveBatch(partsDetails);

        updateStatue(newEntity, skuIds);  // 更新上级状态

    }

    /**
     * 发布
     *
     * @param id
     */
    @Override
    public void release(Long id) {
        Parts release = this.getById(id);
        release.setStatus(99);

        Parts parts = this.query().eq("sku_id", release.getSkuId()).eq("type", release.getType()).eq("display", 1).eq("status", 99).one();
        if (ToolUtil.isNotEmpty(parts)) {
            parts.setDisplay(0);
            this.updateById(parts);
            List<ErpPartsDetail> partsDetails = erpPartsDetailService.query().eq("parts_id", parts.getPartsId()).eq("display", 1).list();
            for (ErpPartsDetail partsDetail : partsDetails) {
                partsDetail.setDisplay(0);
            }
            erpPartsDetailService.updateBatchById(partsDetails);
        }

        this.updateById(release);
    }


    @Override
    public PartsResult findBySpec(PartsParam param) {
        return null;
    }

    @Override
    public List<PartsResult> findListBySpec(PartsParam param) {
        return null;
    }

    @Override
    public PageInfo oldFindPageBySpec(PartsParam param) {
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.oldCustomPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo findPageBySpec(PartsParam param) {
        Page<PartsResult> pageContext = getPageContext();
        param.setStatus(99);
        param.setDisplay(1);
        //skuId 取 清单id
        if (ToolUtil.isNotEmpty(param.getChildren())) {
            List<ErpPartsDetail> erpPartsDetails = erpPartsDetailService.query().eq("sku_id", param.getChildren()).eq("display", 1).list();
            if (ToolUtil.isNotEmpty(erpPartsDetails)) {
                List<Long> partsIds = new ArrayList<>();
                for (ErpPartsDetail erpPartsDetail : erpPartsDetails) {
                    partsIds.add(erpPartsDetail.getPartsId());
                }
                param.setPartIds(partsIds);
            } else {
                return new PageInfo();
            }
        }

        IPage<PartsResult> page = this.baseMapper.customPageList(pageContext, param);
        pageFormat(page.getRecords());
        if (ToolUtil.isNotEmpty(param.getChildren())) {  //取配套数量
            formatNumBySku(page.getRecords(), Long.valueOf(param.getChildren()));
        }
        return PageFactory.createPageInfo(page);
    }

    /**
     * 取配套数量
     *
     * @param data
     * @param skuId
     */
    private void formatNumBySku(List<PartsResult> data, Long skuId) {
        List<Long> partIds = new ArrayList<>();
        for (PartsResult datum : data) {
            partIds.add(datum.getPartsId());
        }
        
        //查询子表取配套数量
        List<ErpPartsDetail> erpPartsDetails = partIds.size() == 0 ? new ArrayList<>() : erpPartsDetailService.query().in("parts_id", partIds).eq("sku_id", skuId).list();
        for (PartsResult datum : data) {
            for (ErpPartsDetail erpPartsDetail : erpPartsDetails) {
                if (datum.getPartsId().equals(erpPartsDetail.getPartsId())) {
                    datum.setBomNum(erpPartsDetail.getNumber());
                }
            }
        }


    }


    @Override
    public List<Long> getSkuIdsByBom(Long skuId) {
        List<Long> ids = new ArrayList<>();

        Parts parts = this.query().eq("sku_id", skuId).eq("status", 99).eq("display", 1).one();
        if (ToolUtil.isEmpty(parts)) {
            return ids;
        } else {
            ids.add(parts.getSkuId());
            ids.addAll(getSkuIdsByPart(parts.getPartsId()));
        }
        return ids;
    }

    private List<Long> getSkuIdsByPart(Long partId) {
        List<Long> ids = new ArrayList<>();
        List<ErpPartsDetail> partsDetails = erpPartsDetailService.query().eq("parts_id", partId).eq("display", 1).list();
        for (ErpPartsDetail partsDetail : partsDetails) {
            ids.add(partsDetail.getSkuId());
            ids.addAll(getSkuIdsByBom(partsDetail.getSkuId()));
        }
        return ids;
    }


    @Override
    public PartsResult getBOM(Long partId) {
        Parts parts = this.getById(partId);
        if (ToolUtil.isEmpty(parts)) {
            return null;
        }
        PartsResult partsResult = new PartsResult();
        ToolUtil.copyProperties(parts, partsResult);
        List<ErpPartsDetailResult> detailResults = recursiveDetails(partsResult.getPartsId());
        partsResult.setParts(detailResults);
        List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
            add(partsResult.getSkuId());
        }});
        partsResult.setSkuResult(skuResults.get(0));
        return partsResult;
    }

    /**
     * 详情递归
     *
     * @param partId
     * @return
     */
    @Override
    public List<ErpPartsDetailResult> recursiveDetails(Long partId) {

        List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", partId).list();
        if (ToolUtil.isNotEmpty(details)) {
            List<ErpPartsDetailResult> detailResults = BeanUtil.copyToList(details, ErpPartsDetailResult.class, new CopyOptions());
            for (ErpPartsDetailResult detailResult : detailResults) {
                List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
                    add(detailResult.getSkuId());
                }});
                detailResult.setSkuResult(skuResults.get(0));
                PartsResult partsResult = recursiveParts(detailResult.getSkuId());
                detailResult.setPartsResult(partsResult);
            }
            return detailResults;
        }

        return null;
    }

    /**
     * 主表递归
     *
     * @param skuId
     * @return
     */
    private PartsResult recursiveParts(Long skuId) {
        Parts parts = this.query().eq("sku_id", skuId).eq("display", 1).eq("status", 99).eq("type", 1).one();
        if (ToolUtil.isNotEmpty(parts)) {
            PartsResult partsResult = new PartsResult();
            ToolUtil.copyProperties(parts, partsResult);
            List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
                add(partsResult.getSkuId());
            }});
            partsResult.setSkuResult(skuResults.get(0));
            List<ErpPartsDetailResult> detailResults = recursiveDetails(partsResult.getPartsId());
            partsResult.setParts(detailResults);
            return partsResult;
        }
        return null;
    }


    @Override
    public List<ErpPartsDetailResult> backDetails(Long skuId, Long partsId, String type) {
        if (ToolUtil.isEmpty(skuId)) {
            throw new ServiceException(500, "沒傳入skuId");
        }
        Parts one;
        if (ToolUtil.isNotEmpty(partsId)) {
            one = this.getById(partsId);
        } else {
            one = this.query().eq("sku_id", skuId).eq("display", 1).eq("type", type).eq("status", 99).one();
        }


        if (ToolUtil.isNotEmpty(one)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", one.getPartsId()).eq("display", 1).list();
            List<Long> skuIds = new ArrayList<>();
            for (ErpPartsDetail detail : details) {
                skuIds.add(detail.getSkuId());
            }
            if (ToolUtil.isNotEmpty(skuIds)) {
                List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
                List<ErpPartsDetailResult> detailResults = new ArrayList<>();
                for (ErpPartsDetail detail : details) {
                    ErpPartsDetailResult detailResult = new ErpPartsDetailResult();
                    ToolUtil.copyProperties(detail, detailResult);
                    detailResults.add(detailResult);
                }


                for (ErpPartsDetailResult detailResult : detailResults) {
                    SpuResult spuResult = skuService.backSpu(detailResult.getSkuId());
                    detailResult.setIsNull(true);
                    Parts parts = partsService.query().eq("sku_id", detailResult.getSkuId()).eq("display", 1).eq("status", 99).one();
                    if (ToolUtil.isEmpty(parts)) {
                        detailResult.setIsNull(false);
                    } else {
                        detailResult.setId(parts.getPartsId());
                    }

                    if (ToolUtil.isNotEmpty(detailResult.getSkuId())) {
                        Sku sku = skuService.getById(detailResult.getSkuId());
                        detailResult.setSku(sku);
                    }

                    for (SkuResult skuResult : skuResults) {
                        if (detailResult.getSkuId().equals(skuResult.getSkuId())) {
                            detailResult.setSkuResult(skuResult);
                            break;
                        }
                    }

                    detailResult.setSpuResult(spuResult);
                }
                return detailResults;
            }
        }


        return null;
    }

    @Override
    public List<ErpPartsDetailResult> oldBackDetails(Long skuId, Long partsId) {
        if (ToolUtil.isEmpty(skuId)) {
            throw new ServiceException(500, "沒傳入skuId");
        }
        Parts parts = this.getById(partsId);
        if (ToolUtil.isNotEmpty(parts)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", parts.getPartsId()).list();
            List<Long> skuIds = new ArrayList<>();
            for (ErpPartsDetail detail : details) {
                skuIds.add(detail.getSkuId());
            }
            if (ToolUtil.isNotEmpty(skuIds)) {
                Map<Long, List<BackSku>> sendSku = skuService.sendSku(skuIds);
                List<ErpPartsDetailResult> detailResults = BeanUtil.copyToList(details, ErpPartsDetailResult.class, new CopyOptions());

                for (ErpPartsDetailResult detailResult : detailResults) {
                    if (parts.getPartsId().equals(detailResult.getPartsId())) {
                        List<BackSku> backSkus = sendSku.get(detailResult.getSkuId());
                        SpuResult spuResult = skuService.backSpu(detailResult.getSkuId());
                        detailResult.setIsNull(true);
                        detailResult.setId(parts.getPartsId());
                        detailResult.setBackSkus(backSkus);
                        detailResult.setSpuResult(spuResult);
                    }

                }
                return detailResults;
            }
        }
        return null;
    }

    @Override
    public List<PartsResult> getTreeParts(Long partId) {
        List<Parts> parts = this.query().like("parts_id", partId).list();
        List<Long> partsIds = new ArrayList<>();
        for (Parts part : parts) {
            partsIds.add(part.getPartsId());
        }
//        Parts partsFather = this.getById(partId);
        List<ErpPartsDetail> partsDetails = erpPartsDetailService.query().in("parts_id", partsIds).list();
        List<PartsResult> partsResults = new ArrayList<>();
        for (Parts part : parts) {
            PartsResult partsResult = new PartsResult();
            ToolUtil.copyProperties(part, partsResult);
            partsResults.add(partsResult);
        }

        for (PartsResult partsResult : partsResults) {
            List<ErpPartsDetailResult> detailResults = new ArrayList<>();
            for (ErpPartsDetail partsDetail : partsDetails) {
                if (partsResult.getPartsId().equals(partsDetail.getPartsId())) {
                    ErpPartsDetailResult detailResult = new ErpPartsDetailResult();
                    ToolUtil.copyProperties(partsDetail, detailResult);
                    detailResults.add(detailResult);
                }
            }
            partsResult.setParts(detailResults);
        }

//        partsResults.sort((PartsResult ord1, PartsResult ord2) -> ord2.getChildren().compareTo(ord1.getChildren()));

        partsResults.sort(new Comparator<PartsResult>() {
            public int compare(PartsResult o1, PartsResult o2) {
                if (o1.getChildren().length() > o2.getChildren().length()) {
                    return 1;
                }
                if (o1.getChildren().length() == o2.getChildren().length()) {
                    return 0;
                }
                return -1;
            }
        });
//        partsResults.stream().;
//        partsResults.stream().forEach();
        return partsResults;
    }


    /**
     * 下级发生变化 更新上级
     */
    public void updateStatue(Parts entity, List<Long> skuIds) {

        // 更新当前节点，及下级
        Map<String, List<Long>> childrenMap = getChildrens(entity.getSkuId(), entity.getType());
        entity.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        entity.setChildren(JSON.toJSONString(skuIds));
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.eq("parts_id", entity.getPartsId());
        this.update(entity, partsQueryWrapper);

        updateChildren(entity.getSkuId(), entity.getType());
    }

    @Override
    public List<PartsResult> getdetails(List<Long> partIds) {
        if (ToolUtil.isEmpty(partIds)) {
            return new ArrayList<>();
        }
        List<Parts> partsList = this.query().in("parts_id", partIds).list();
        List<PartsResult> results = BeanUtil.copyToList(partsList, PartsResult.class, new CopyOptions());
        List<Long> ids = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (PartsResult result : results) {
            ids.add(result.getPartsId());
            skuIds.add(result.getSkuId());
        }
        List<ErpPartsDetailResult> details = erpPartsDetailService.getDetails(ids);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        for (PartsResult result : results) {
            List<ErpPartsDetailResult> detailResults = new ArrayList<>();

            for (ErpPartsDetailResult detail : details) {
                if (detail.getPartsId().equals(result.getPartsId())) {
                    detailResults.add(detail);
                }
            }
            result.setParts(detailResults);

            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(result.getSkuId())) {
                    result.setSkuResult(skuResult);
                    break;
                }
            }
        }
        return results;
    }

    private Serializable getKey(PartsParam param) {
        return param.getPartsId();
    }

    private Page<PartsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Parts getOldEntity(PartsParam param) {
        return this.getById(getKey(param));
    }

    private Parts getEntity(PartsParam param) {
        Parts entity = new Parts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void pageFormat(List<PartsResult> data) {


        List<Long> userIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (PartsResult datum : data) {

            userIds.add(datum.getCreateUser());
            skuIds.add(datum.getSkuId());
        }

        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);


        for (PartsResult datum : data) {
            List<ErpPartsDetailResult> detailResults = new ArrayList<>();


            datum.setParts(detailResults);


            List<PartsResult> partsResults = new ArrayList<>();

            datum.setPartsResults(partsResults);
            if (ToolUtil.isNotEmpty(datum.getAttribute())) {
                datum.setPartsAttributes(datum.getAttribute());
            }
            for (User user : users) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }
            for (SkuResult skuResult : skuResults) {
                if (ToolUtil.isNotEmpty(datum.getSkuId()) && datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
        }
    }


    public void format(List<PartsResult> data) {

        List<Long> pids = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (PartsResult datum : data) {
            pids.add(datum.getPartsId());
            userIds.add(datum.getCreateUser());
            skuIds.add(datum.getSkuId());
        }
        List<Parts> parts = pids.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(Parts::getPartsId, pids).in(Parts::getDisplay, 1).list();
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<ErpPartsDetailResult> details = erpPartsDetailService.getDetails(pids);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);


        for (PartsResult datum : data) {
            List<ErpPartsDetailResult> detailResults = new ArrayList<>();

            for (ErpPartsDetailResult detail : details) {
                if (detail.getPartsId().equals(datum.getPartsId())) {
                    detailResults.add(detail);
                }
            }
            datum.setParts(detailResults);

//            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
//                Sku sku = skuService.getById(datum.getSkuId());
//                datum.setSku(sku);
//            }

            List<PartsResult> partsResults = new ArrayList<>();
            for (Parts part : parts) {
                if (datum.getPartsId() != null && datum.getPartsId().equals(part.getPid())) {
                    PartsResult partsResult = new PartsResult();
                    ToolUtil.copyProperties(part, partsResult);
                    partsResults.add(partsResult);
                }
            }
            datum.setPartsResults(partsResults);
            if (ToolUtil.isNotEmpty(datum.getAttribute())) {
                datum.setPartsAttributes(datum.getAttribute());
            }
            for (User user : users) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }
            for (SkuResult skuResult : skuResults) {
                if (ToolUtil.isNotEmpty(datum.getSkuId()) && datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
        }
    }


}

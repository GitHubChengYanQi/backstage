package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.SkuRequest;
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
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Parts add(PartsParam partsParam) {

        Parts one = this.query().eq("sku_id", partsParam.getSkuId()).eq("display", 1).eq("type", partsParam.getType()).eq("status", 99).one();
        if (ToolUtil.isNotEmpty(one)) {
            Parts parts = new Parts();
            ToolUtil.copyProperties(partsParam, parts);
            parts.setStatus(0);
            parts.setPartsId(null);
            this.save(parts);
            List<ErpPartsDetail> partsDetails = new ArrayList<>();

            for (ErpPartsDetailParam part : partsParam.getParts()) {
                part.setPartsId(parts.getPartsId());
                part.setPartsDetailId(null);
                ErpPartsDetail partsDetail = new ErpPartsDetail();
                ToolUtil.copyProperties(part, partsDetail);
                partsDetails.add(partsDetail);
            }
            erpPartsDetailService.saveBatch(partsDetails);
            return null;
        }
//----------------------------------------------------------------------------------------------------------------------

        judge(partsParam); //防止添加重复数据
        DeadLoopJudge(partsParam); //防止死循环添加


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
                Long skuId = skuService.add(skuParam);
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
        this.save(entity);

        List<ErpPartsDetail> details = new ArrayList<>();

        for (ErpPartsDetailParam part : partsParam.getParts()) {
            if (ToolUtil.isNotEmpty(part)) {
                ErpPartsDetail detail = new ErpPartsDetail();
                detail.setSkuId(part.getSkuId());
                detail.setNumber(part.getNumber());
                detail.setPartsId(entity.getPartsId());
                detail.setNote(part.getNote());
                details.add(detail);
            }
        }
        erpPartsDetailService.saveBatch(details);

        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : partsParam.getParts()) {
            skuIds.add(part.getSkuId());
        }
        // 更新当前节点，及下级
        Map<String, List<Long>> childrenMap = getChildrens(entity.getSkuId(), partsParam.getType());
        entity.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        entity.setChildren(JSON.toJSONString(skuIds));
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.eq("parts_id", entity.getPartsId());
        this.update(entity, partsQueryWrapper);

        updateChildren(entity.getSkuId(), partsParam.getType());

        return entity;


    }

    private void judge(PartsParam partsParam) {
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : partsParam.getParts()) {
            skuIds.add(part.getSkuId());
            if (part.getSkuId().equals(partsParam.getSkuId())) {
                throw new ServiceException(500, "请看清在加");
            }
        }
        long count = skuIds.stream().distinct().count();
        if (count != partsParam.getParts().size()) {
            throw new ServiceException(500, "请勿添加重复数据");
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

        // 更新当前节点，及下级
        Map<String, List<Long>> childrenMap = getChildrens(newPart.getSkuId(), partsParam.getType());
        newPart.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        newPart.setChildren(JSON.toJSONString(skuIds));
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.eq("parts_id", newPart.getPartsId());
        this.update(newPart, partsQueryWrapper);

        updateChildren(newPart.getSkuId(), partsParam.getType());
    }


    /**
     * 更新包含它的
     */
    public void updateChildren(Long skuId, String type) {

        List<Parts> partList = this.query().like("children", skuId).eq("display", 1).eq("type", type).list();
        for (Parts part : partList) {
            Map<String, List<Long>> childrenMap = getChildrens(skuId, type);
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
        List<Parts> parts = this.query().eq("sku_id", id).eq("display", 1).eq("type", type).list();
        if (ToolUtil.isNotEmpty(parts)) {
            for (Parts part : parts) {
                List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", part.getPartsId()).eq("display", 1).list();
                for (ErpPartsDetail detail : details) {
                    skuIds.add(detail.getSkuId());
                    childrensSkuIds.add(detail.getSkuId());
                    Map<String, List<Long>> childrenMap = this.getChildrens(detail.getSkuId(), type);
                    childrensSkuIds.addAll(childrenMap.get("childrens"));
                    result.put("children", skuIds);
                    result.put("childrens", childrensSkuIds);
                }
            }
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

        List<Parts> parts = this.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Long> alongs = new ArrayList<>();

        for (Parts part : parts) {
            JSONArray jsonArray = JSONUtil.parseArray(part.getChildrens());
            List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
            alongs.addAll(longs);
        }
        for (Long along : alongs) {
            if (along.equals(param.getSkuId())) {
                throw new ServiceException(500, "请勿循环添加");
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
        }
        erpPartsDetailService.saveBatch(partsDetails);


        // 更新当前节点，及下级
        Map<String, List<Long>> childrenMap = getChildrens(newEntity.getSkuId(), newEntity.getType());
        newEntity.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        newEntity.setChildren(JSON.toJSONString(skuIds));
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.eq("parts_id", newEntity.getPartsId());
        this.update(newEntity, partsQueryWrapper);

        updateChildren(newEntity.getSkuId(), newEntity.getType());
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
    public PageInfo<PartsResult> oldFindPageBySpec(PartsParam param) {
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.oldCustomPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<PartsResult> findPageBySpec(PartsParam param) {
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PartsResult getBOM(Long partId, String type) {
        Parts parts = this.getById(partId);
        if (ToolUtil.isEmpty(parts)) {
            return null;
        }
        PartsResult partsResult = new PartsResult();
        ToolUtil.copyProperties(parts, partsResult);
        List<ErpPartsDetailResult> detailResults = recursiveDetails(partsResult.getPartsId(), type);
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
    private List<ErpPartsDetailResult> recursiveDetails(Long partId, String type) {

        List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", partId).list();
        if (ToolUtil.isNotEmpty(details)) {
            List<ErpPartsDetailResult> detailResults = BeanUtil.copyToList(details, ErpPartsDetailResult.class, new CopyOptions());
            for (ErpPartsDetailResult detailResult : detailResults) {
                List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
                    add(detailResult.getSkuId());
                }});
                detailResult.setSkuResult(skuResults.get(0));
                PartsResult partsResult = recursiveParts(detailResult.getSkuId(), type);
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
    private PartsResult recursiveParts(Long skuId, String type) {
        Parts parts = this.query().eq("sku_id", skuId).eq("type", type).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(parts)) {
            PartsResult partsResult = new PartsResult();
            ToolUtil.copyProperties(parts, partsResult);
            List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
                add(partsResult.getSkuId());
            }});
            partsResult.setSkuResult(skuResults.get(0));
            List<ErpPartsDetailResult> detailResults = recursiveDetails(partsResult.getPartsId(), type);
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
            one = this.query().eq("sku_id", skuId).eq("display", 1).eq("type", type).one();
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
                    Parts parts = partsService.query().in("sku_id", detailResult.getSkuId()).eq("display", 1).one();
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
     * 生产bom 启用
     *
     * @param partId
     */
    public void updateStatue(Long partId) {
        Parts production = this.getById(partId); //生产BOM

        Parts design = this.getById(production.getPid());  //设计Bom

        List<Parts> partsList = this.list(new QueryWrapper<Parts>() {{ //所有清单
            eq("display", 1);
        }});
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

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        for (PartsResult datum : data) {

            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
                Sku sku = skuService.getById(datum.getSkuId());
                datum.setSku(sku);
            }

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

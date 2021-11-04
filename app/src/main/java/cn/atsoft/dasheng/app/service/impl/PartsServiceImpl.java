package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.params.PartRequest;
import cn.atsoft.dasheng.app.model.params.Spu;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.Item;
import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.mapper.PartsMapper;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
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


    @Transactional
    @Override
    public void add(PartsParam partsParam) {
        //判断详情sku合法性
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetailParam part : partsParam.getParts()) {
            skuIds.add(part.getSkuId());
        }
        Integer count = skuService.query().in("sku_id", skuIds).eq("display", 1).count();
        if (partsParam.getParts().size() != count) {
            throw new ServiceException(500, "数据不合法");
        }
        //如果有通过partsId 修改隐藏
        if (ToolUtil.isNotEmpty(partsParam.getPartsId())) {
            Parts oldParts = this.getById(partsParam.getPartsId());
            if (ToolUtil.isNotEmpty(oldParts)) {
                editParts(oldParts.getSkuId());
            }
            Parts parts = new Parts();
            parts.setDisplay(0);
            QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
            partsQueryWrapper.in("parts_id", partsParam.getPartsId());
            this.update(parts, partsQueryWrapper);
        }
        //如果有通过skuId 隐藏
        if (ToolUtil.isNotEmpty(partsParam.getItem().getSkuId())) {
            Parts oldParts = this.query().eq("sku_id", partsParam.getItem().getSkuId()).eq("display", 1).one();
            if (ToolUtil.isNotEmpty(oldParts)) {
                editParts(oldParts.getSkuId());
            }
            Parts parts = new Parts();
            parts.setDisplay(0);
            QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
            partsQueryWrapper.in("sku_id", partsParam.getItem().getSkuId());
            this.update(parts, partsQueryWrapper);
        }

        if (ToolUtil.isNotEmpty(partsParam.getItem().getSpuId())) {
            Sku sku = new Sku();
            partsParam.getSkuRequests().sort(Comparator.comparing(SkuRequest::getAttributeId));
            String json = JSONUtil.toJsonStr(partsParam.getSkuRequests());
            sku.setSkuValue(json);
            sku.setSkuName(partsParam.getSkuName());
            sku.setType(0);
            sku.setRemarks(partsParam.getSkuNote());
            skuService.save(sku);
            partsParam.setPartsId(null);
            partsParam.setSkuId(sku.getSkuId());
        } else if (ToolUtil.isNotEmpty(partsParam.getItem().getSkuId())) {
            Integer number = skuService.query().eq("sku_id", partsParam.getItem().getSkuId()).eq("display", 1).count();
            if (partsParam.getParts().size() != number) {
                throw new ServiceException(500, "数据不合法");
            }
            partsParam.setPartsId(null);
            partsParam.setSkuId(partsParam.getItem().getSkuId());
        }


        
        Parts entity = getEntity(partsParam);
        this.save(entity);

        List<Long> childSkuIds = new ArrayList<>();
        List<ErpPartsDetail> details = new ArrayList<>();
        if (ToolUtil.isNotEmpty(partsParam.getParts())) {
            for (ErpPartsDetailParam part : partsParam.getParts()) {
                if (ToolUtil.isNotEmpty(part)) {
                    childSkuIds.add(part.getSkuId());
                    ErpPartsDetail detail = new ErpPartsDetail();
                    detail.setSkuId(part.getSkuId());
                    detail.setNumber(part.getNumber());
                    detail.setPartsId(entity.getPartsId());
                    if (ToolUtil.isNotEmpty(part.getNote())) {
                        detail.setNote(part.getNote());
                    }
                    details.add(detail);
                }
            }

        }

        erpPartsDetailService.saveBatch(details);

        updateChildren(entity.getSkuId());

    }

    //隐藏老数据
    public void editParts(Long id) {
        Parts parts = this.query().eq("sku_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(parts)) {
            parts.setDisplay(0);
            QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
            partsQueryWrapper.eq("sku_id", id);
            this.update(parts, partsQueryWrapper);

            List<ErpPartsDetail> partsDetails = erpPartsDetailService.query().eq("parts_id", parts.getPartsId()).list();
            for (ErpPartsDetail partsDetail : partsDetails) {
                partsDetail.setDisplay(0);
            }
            erpPartsDetailService.updateBatchById(partsDetails);
        }
    }


    //递归
    public List<Long> updateChildren(Long id) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> childrensSkuIds = new ArrayList<>();
        Parts parts = this.query().eq("sku_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(parts)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().eq("parts_id", parts.getPartsId()).eq("display", 1).list();
            for (ErpPartsDetail detail : details) {
                skuIds.add(detail.getSkuId());
                childrensSkuIds.addAll(this.updateChildren(detail.getSkuId()));
            }
            parts.setChilds(childrensSkuIds.toString());
            parts.setChild(skuIds.toString());
            QueryWrapper<Parts> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parts_id", parts.getPartsId());
            this.update(parts, queryWrapper);
        }
        return skuIds;
    }

    @Override
    public void delete(PartsParam param) {
//        this.removeById(getKey(param));
//        Parts parts = new Parts();
//        parts.setDisplay(0);
//        this.update(parts, new QueryWrapper<Parts>().in("parts_id", param.getPartsId()));
//        QueryWrapper<ErpPartsDetail> erpPartsDetailQueryWrapper = new QueryWrapper<>();
//        erpPartsDetailQueryWrapper.in("parts_id", param.getPartsId());
//        ErpPartsDetail erpPartsDetail = new ErpPartsDetail();
//        erpPartsDetail.setDisplay(0);
////        erpPartsDetailService.remove(erpPartsDetailQueryWrapper);
//        erpPartsDetailService.update(erpPartsDetail, erpPartsDetailQueryWrapper);
    }

    @Transactional
    @Override
    public void update(PartsParam param) {

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
    public List<ErpPartsDetailResult> backDetails(Long skuId, Long partsId) {
        if (ToolUtil.isEmpty(skuId)) {
            throw new ServiceException(500, "沒傳入skuId");
        }
        Parts one = this.query().in("sku_id", skuId).in("display", 1).one();

        if (ToolUtil.isNotEmpty(one)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().in("parts_id", one.getPartsId()).in("display", 1).list();
            List<Long> skuIds = new ArrayList<>();
            for (ErpPartsDetail detail : details) {
                skuIds.add(detail.getSkuId());
            }
            if (ToolUtil.isNotEmpty(skuIds)) {
                Map<Long, List<BackSku>> sendSku = skuService.sendSku(skuIds);
                List<ErpPartsDetailResult> detailResults = new ArrayList<>();
                for (ErpPartsDetail detail : details) {
                    ErpPartsDetailResult detailResult = new ErpPartsDetailResult();
                    ToolUtil.copyProperties(detail, detailResult);
                    detailResults.add(detailResult);
                }


                for (ErpPartsDetailResult detailResult : detailResults) {
                    List<BackSku> backSkus = sendSku.get(detailResult.getSkuId());
                    SpuResult spuResult = skuService.backSpu(detailResult.getSkuId());

                    if (ToolUtil.isNotEmpty(detailResult.getSkuId())) {
                        Sku sku = skuService.getById(detailResult.getSkuId());
                        detailResult.setSku(sku);
                    }

                    detailResult.setBackSkus(backSkus);
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
        Parts parts = this.query().in("sku_id", skuId).in("display", 0).eq("parts_id", partsId).one();

        if (ToolUtil.isNotEmpty(parts)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().in("parts_id", parts.getPartsId()).in("display", 0).list();
            List<Long> skuIds = new ArrayList<>();
            for (ErpPartsDetail detail : details) {
                skuIds.add(detail.getSkuId());
            }
            if (ToolUtil.isNotEmpty(skuIds)) {
                Map<Long, List<BackSku>> sendSku = skuService.sendSku(skuIds);
                List<ErpPartsDetailResult> detailResults = new ArrayList<>();
                for (ErpPartsDetail detail : details) {
                    ErpPartsDetailResult detailResult = new ErpPartsDetailResult();
                    ToolUtil.copyProperties(detail, detailResult);
                    detailResults.add(detailResult);
                }

                for (ErpPartsDetailResult detailResult : detailResults) {
                    if (parts.getPartsId().equals(detailResult.getPartsId())) {
                        List<BackSku> backSkus = sendSku.get(detailResult.getSkuId());
                        SpuResult spuResult = skuService.backSpu(detailResult.getSkuId());
                        detailResult.setBackSkus(backSkus);
                        detailResult.setSpuResult(spuResult);
                    }

                }
                return detailResults;
            }
        }


        return null;
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
        List<Long> userIds = new ArrayList();
        List<Long> skuIds = new ArrayList<>();
        for (PartsResult datum : data) {
            pids.add(datum.getPartsId());
            userIds.add(datum.getCreateUser());
            skuIds.add(datum.getSkuId());
        }
        List<Parts> parts = pids.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(Parts::getPartsId, pids).in(Parts::getDisplay, 1).list();
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.query().in("user_id", userIds).list();
        Map<Long, List<BackSku>> sendSku = null;
        if (ToolUtil.isNotEmpty(skuIds)) {
            sendSku = skuService.sendSku(skuIds);
        }
        for (PartsResult datum : data) {

            if (ToolUtil.isNotEmpty(datum.getSkuId())) {
                Sku sku = skuService.getById(datum.getSkuId());
                datum.setSku(sku);
            }
            if (sendSku != null && ToolUtil.isNotEmpty(datum.getSkuId())) {
                List<BackSku> backSkus = sendSku.get(datum.getSkuId());
                datum.setBackSkus(backSkus);
                SpuResult spuResult = skuService.backSpu(datum.getSkuId());
                datum.setSpuResult(spuResult);
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
        }
    }
}

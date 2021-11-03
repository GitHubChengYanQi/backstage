package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.params.PartRequest;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.Item;
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
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String s = partsParam.getPartName().replace(" ", "");
        partsParam.setPartName(s);
        Integer count = this.query().in("part_name", s).in("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "错误");
        }
        long l = partsParam.getParts().stream().distinct().count();
        if (l > partsParam.getParts().size()) {
            throw new ServiceException(500, "错误");
        }
        List<Parts> partsList = this.query().in("display", 1).list();
        for (ErpPartsDetailParam part : partsParam.getParts()) {
            if (ToolUtil.isNotEmpty(part) && ToolUtil.isNotEmpty(partsParam.getPSkuId())) {
                if (part.getSkuId().equals(partsParam.getPSkuId())) {
                    throw new ServiceException(500, "错误");
                }
            }
            if (ToolUtil.isNotEmpty(partsParam.getItem().getSkuId())) {
                if (part.getSkuId().equals(partsParam.getItem().getSkuId())) {
                    throw new ServiceException(500, "错误");
                }
            }
            for (Parts parts : partsList) {
                if (parts.getSkuId().equals(part.getSkuId())) {
                    throw new ServiceException(500, "错误");
                }

            }
        }

        if (ToolUtil.isNotEmpty(partsParam.getPSkuId())) {
            Parts parts = this.query().in("sku_id", partsParam.getPSkuId()).in("display", 1).one();
            if (ToolUtil.isNotEmpty(parts)) {
                parts.setDisplay(0);
                LoginUser user = LoginContextHolder.getContext().getUser();
                parts.setUpdateUser(user.getId());
                this.updateById(parts);
                ErpPartsDetail erpPartsDetail = new ErpPartsDetail();
                erpPartsDetail.setDisplay(0);
                erpPartsDetail.setUpdateUser(user.getId());
                QueryWrapper<ErpPartsDetail> detailQueryWrapper = new QueryWrapper<>();
                detailQueryWrapper.in("parts_id", parts.getPartsId());
                erpPartsDetailService.update(erpPartsDetail, detailQueryWrapper);
            }
            partsParam.setSkuId(partsParam.getPSkuId());
        } else {
            if (ToolUtil.isNotEmpty(partsParam.getItem().getSkuId())) {
                Parts parts = this.query().in("sku_id", partsParam.getItem().getSkuId()).in("display", 1).one();
                if (ToolUtil.isNotEmpty(parts)) {
                    parts.setDisplay(0);
                    LoginUser user = LoginContextHolder.getContext().getUser();
                    parts.setUpdateUser(user.getId());
                    this.updateById(parts);
                    ErpPartsDetail erpPartsDetail = new ErpPartsDetail();
                    erpPartsDetail.setDisplay(0);
                    erpPartsDetail.setUpdateUser(user.getId());
                    QueryWrapper<ErpPartsDetail> detailQueryWrapper = new QueryWrapper<>();
                    detailQueryWrapper.in("parts_id", parts.getPartsId());
                    erpPartsDetailService.update(erpPartsDetail, detailQueryWrapper);
                }
                partsParam.setSkuId(partsParam.getItem().getSkuId());
            }

        }


//以上全是判断------------------------------------------------------------------------------------------------------------------------------

        Parts entity = getEntity(partsParam);
//        this.save(entity);
        this.saveOrUpdate(entity);
        List<ErpPartsDetail> details = new ArrayList<>();
        if (ToolUtil.isNotEmpty(partsParam.getParts())) {
            for (ErpPartsDetailParam part : partsParam.getParts()) {
                if (ToolUtil.isNotEmpty(part)) {
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
    }

    @Override
    public void delete(PartsParam param) {
//        this.removeById(getKey(param));
        Parts parts = new Parts();
        parts.setDisplay(0);
        this.update(parts, new QueryWrapper<Parts>().in("parts_id", param.getPartsId()));
        QueryWrapper<ErpPartsDetail> erpPartsDetailQueryWrapper = new QueryWrapper<>();
        erpPartsDetailQueryWrapper.in("parts_id", param.getPartsId());
        ErpPartsDetail erpPartsDetail = new ErpPartsDetail();
        erpPartsDetail.setDisplay(0);
//        erpPartsDetailService.remove(erpPartsDetailQueryWrapper);
        erpPartsDetailService.update(erpPartsDetail, erpPartsDetailQueryWrapper);
    }

    @Transactional
    @Override
    public void update(PartsParam param) {
        if (ToolUtil.isEmpty(param.getPartsId())) {
            throw new ServiceException(500, "错误");
        }
        Parts parts = this.getById(param.getPartsId());
        if (!parts.getPartName().equals(param.getPartName())) {
            Integer count = this.query().in("part_name", param.getPartName()).count();
            if (count > 0) {
                throw new ServiceException(500, "错误");
            }
        }
        long l = param.getParts().stream().distinct().count();
        if (l > param.getParts().size()) {
            throw new ServiceException(500, "错误");
        }
        List<Parts> partsList = this.query().in("display", 1).list();
        for (ErpPartsDetailParam part : param.getParts()) {
            if (ToolUtil.isNotEmpty(part) && ToolUtil.isNotEmpty(param.getPSkuId())) {
                if (part.getSkuId().equals(param.getPSkuId())) {
                    throw new ServiceException(500, "错误");
                }
            }
            if (ToolUtil.isNotEmpty(param.getItem().getSkuId())) {
                if (part.getSkuId().equals(param.getItem().getSkuId())) {
                    throw new ServiceException(500, "错误");
                }
            }
            for (Parts partList : partsList) {
                if (partList.getSkuId().equals(part.getSkuId())) {
                    throw new ServiceException(500, "错误");
                }
            }
        }
//------------------------------------------------------------------------------------
        if (ToolUtil.isNotEmpty(param.getPSkuId())) {
            param.setSkuId(param.getPSkuId());

        } else if (param.getItem().getSkuId() != null) {
            param.setSkuId(param.getItem().getSkuId());
        }

        Parts oldEntity = getOldEntity(param);
        Parts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);


        List<ErpPartsDetail> details = new ArrayList<>();
        for (ErpPartsDetailParam part : param.getParts()) {
            ErpPartsDetail erpPartsDetail = new ErpPartsDetail();
            ToolUtil.copyProperties(part, erpPartsDetail);
            erpPartsDetail.setPartsId(param.getPartsId());
            details.add(erpPartsDetail);
        }
        erpPartsDetailService.saveOrUpdateBatch(details);

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

//    Map<String, String> map = new HashMap<>();

    @Override
    public List<ErpPartsDetailResult> backDetails(Long skuId, Long partsId) {
        if (ToolUtil.isEmpty(skuId)) {
            throw new ServiceException(500, "沒傳入skuId");
        }
        Parts one = this.query().in("sku_id", skuId).in("display", 1).one();
//        map.put("part" + partsId + "skuId" + one.getSkuId(), "真");
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
        Parts one = this.query().in("sku_id", skuId).in("display", 0).one();
//        map.put("part" + partsId + "skuId" + one.getSkuId(), "真");
        if (ToolUtil.isNotEmpty(one)) {
            List<ErpPartsDetail> details = erpPartsDetailService.query().in("parts_id", one.getPartsId()).in("display", 0).list();
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
                    detailResult.setBackSkus(backSkus);
                    detailResult.setSpuResult(spuResult);
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
        List<Parts> parts = pids.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(Parts::getPartsId, pids).list();
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.query().in("user_id", userIds).list();
        Map<Long, List<BackSku>> sendSku = null;
        if (ToolUtil.isNotEmpty(skuIds)) {
            sendSku = skuService.sendSku(skuIds);
        }
        for (PartsResult datum : data) {

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

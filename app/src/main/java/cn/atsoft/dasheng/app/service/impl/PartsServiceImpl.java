package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.params.PartRequest;
import cn.atsoft.dasheng.app.model.result.Item;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.mapper.PartsMapper;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
import java.util.List;

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


    @Transactional
    @Override
    public void add(PartsParam partsParam) {
        String s = partsParam.getPartName().replace(" ", "");
        partsParam.setPartName(s);
        Integer count = this.query().in("part_name", s).count();
        if (count > 0) {
            throw new ServiceException(500, "已有重複名");
        }
        if (ToolUtil.isNotEmpty(partsParam.getPSkuId())) {
            partsParam.setSkuId(partsParam.getPSkuId());
        }

        Parts entity = getEntity(partsParam);
        this.save(entity);
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
        this.removeById(getKey(param));
        QueryWrapper<ErpPartsDetail> erpPartsDetailQueryWrapper = new QueryWrapper<>();
        erpPartsDetailQueryWrapper.in("parts_id", param.getPartsId());
        erpPartsDetailService.remove(erpPartsDetailQueryWrapper);
    }

    @Transactional
    @Override
    public void update(PartsParam param) {

        if (ToolUtil.isNotEmpty(param.getSkuIds())) {
            String skus = "";
            for (int i = 0; i < param.getSkuIds().size(); i++) {
                if (i == param.getSkuIds().size() - 1) {
                    skus = skus + param.getSkuIds().get(i);
                } else {
                    skus = skus + param.getSkuIds().get(i) + ",";
                }

            }
            param.setSkus(skus);
        }

        if (ToolUtil.isNotEmpty(param.getParts())) {
            List<Long> ids = new ArrayList<>();
            for (ErpPartsDetailParam part : param.getParts()) {
                ids.add(part.getSpuId());
            }
            long count = ids.stream().distinct().count();
            if (param.getParts().size() > count) {
                throw new ServiceException(500, "请勿填入重复商品");
            }

            QueryWrapper<ErpPartsDetail> erpPartsDetailQueryWrapper = new QueryWrapper<>();
            erpPartsDetailQueryWrapper.in("parts_id", param.getPartsId());
            erpPartsDetailService.remove(erpPartsDetailQueryWrapper);

            List<ErpPartsDetail> partsList = new ArrayList<>();

            for (ErpPartsDetailParam partsDetailParam : param.getParts()) {
                ErpPartsDetail erpPartsDetail = new ErpPartsDetail();
                erpPartsDetail.setSpuId(partsDetailParam.getSpuId());
                erpPartsDetail.setPartsId(param.getPartsId());
                erpPartsDetail.setNote(partsDetailParam.getNote());
                erpPartsDetail.setNumber(partsDetailParam.getNumber());
//                String toJsonStr = JSONUtil.toJsonStr(partsDetailParam.getPartsAttributes());
//                erpPartsDetail.setAttribute(toJsonStr);
                erpPartsDetail.setSkuId(partsDetailParam.getSkuId());
                partsList.add(erpPartsDetail);
            }
            erpPartsDetailService.saveBatch(partsList);
        }

        Parts oldEntity = getOldEntity(param);
        Parts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
    public PageInfo<PartsResult> findPageBySpec(PartsParam param) {
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
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
        for (PartsResult datum : data) {
            pids.add(datum.getPartsId());
            userIds.add(datum.getCreateUser());
        }
        List<Parts> parts = pids.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(Parts::getPartsId, pids).list();
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.query().in("user_id", userIds).list();

        for (PartsResult datum : data) {
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

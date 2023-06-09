package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.mapper.InstockLogDetailMapper;
import cn.atsoft.dasheng.erp.model.params.InstockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.pojo.SkuLogDetail;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
@Service
public class InstockLogDetailServiceImpl extends ServiceImpl<InstockLogDetailMapper, InstockLogDetail> implements InstockLogDetailService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private ActivitiProcessTaskService processTaskService;


    @Override
    public void add(InstockLogDetailParam param) {
        InstockLogDetail entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 查询物料操作记录
     *
     * @param skuId
     * @return
     */

    @Override
    public List<SkuLogDetail> skuLogDetail(Long skuId) {
        List<SkuLogDetail> skuLogDetails = new ArrayList<>();
        InstockLogDetailParam param = new InstockLogDetailParam();
        param.setSkuId(skuId);
        List<InstockLogDetailResult> results = this.baseMapper.skuLogDetail(param);
        this.format(results);

        List<Long> formIds = new ArrayList<>();
        for (InstockLogDetailResult result : results) {
            switch (result.getSource()) {
                case "instock":
                    formIds.add(result.getSourceId());
                    break;
            }
        }

        List<ActivitiProcessTask> taskList = formIds.size() == 0 ? new ArrayList<>() : processTaskService.query().in("form_id", formIds).list();

        for (InstockLogDetailResult result : results) {

            SkuLogDetail skuLogDetail = new SkuLogDetail();
            skuLogDetail.setType(result.getSource());
            skuLogDetail.setTime(result.getCreateTime());
            skuLogDetail.setUserName(result.getUser().getName());
            skuLogDetail.setPosition(result.getStorehousePositionsResult().getName());
            skuLogDetail.setBrandName(result.getBrandResult().getBrandName());
            skuLogDetail.setOperationNum(Math.toIntExact(result.getNumber()));
            skuLogDetail.setBalance(Math.toIntExact(result.getNumber()+result.getCurrentNumber()));

            for (ActivitiProcessTask activitiProcessTask : taskList) {
                if (result.getSourceId().equals(activitiProcessTask.getFormId())) {
                    skuLogDetail.setTheme(activitiProcessTask.getTheme());
                    break;
                }
            }
            skuLogDetails.add(skuLogDetail);
        }


        return skuLogDetails;
    }


    /**
     * 历史记录
     *
     * @param param
     * @return
     */
    @Override
    public List<InstockLogDetailResult> history(InstockLogDetailParam param) {
        List<InstockLogDetailResult> allList = this.baseMapper.customList(param);
        format(allList);
        Map<String, InstockLogDetailResult> map = new HashMap<>();

        for (InstockLogDetailResult instockLogDetailResult : allList) {
            if (ToolUtil.isEmpty(instockLogDetailResult.getBrandId())) {
                instockLogDetailResult.setBrandId(0L);
            }
        }
        for (InstockLogDetailResult instockLogDetailResult : allList) {
            map.put(instockLogDetailResult.getSkuId()
                    + instockLogDetailResult.getBrandId()
                    + instockLogDetailResult.getCustomerId()
                    + instockLogDetailResult.getType(), instockLogDetailResult);
        }

        List<InstockLogDetailResult> results = new ArrayList<>();
        for (String key : map.keySet()) {
            InstockLogDetailResult instockLogDetailResult = map.get(key);
            List<StorehousePositionsResult> positionsResults = new ArrayList<>();
            for (InstockLogDetailResult logDetailResult : allList) {
                if (logDetailResult.getSkuId().equals(instockLogDetailResult.getSkuId()) &&
                        logDetailResult.getBrandId().equals(instockLogDetailResult.getBrandId()) &&
                        logDetailResult.getCustomerId().equals(instockLogDetailResult.getCustomerId()) &&
                        logDetailResult.getType().equals(instockLogDetailResult.getType())) {
                    StorehousePositionsResult storehousePositionsResult = logDetailResult.getStorehousePositionsResult();
                    if (ToolUtil.isEmpty(storehousePositionsResult)) {
                        storehousePositionsResult = new StorehousePositionsResult();
                    }
                    storehousePositionsResult.setCreateTime(logDetailResult.getCreateTime());
                    storehousePositionsResult.setUser(logDetailResult.getUser());
                    storehousePositionsResult.setNum(Math.toIntExact(logDetailResult.getNumber()));
                    positionsResults.add(logDetailResult.getStorehousePositionsResult());
                }
            }
            instockLogDetailResult.setPositionsResults(positionsResults);
            results.add(instockLogDetailResult);
        }

        return results;
    }

    @Override
    public List<InstockLogDetailResult> timeHistory(InstockLogDetailParam param) {

        List<InstockLogDetailResult> allList = this.baseMapper.customList(param);
        List<InstockLogDetailResult> detailResults = new ArrayList<>();

        for (InstockLogDetailResult instockLogDetailResult : allList) {
            instockLogDetailResult.setInkindIds(new ArrayList<Long>() {{
                add(instockLogDetailResult.getInkindId());
            }});
        }
        format(allList);
        /**
         * 通过时间组合
         */
        Map<String, InstockLogDetailResult> map = new HashMap<>();
        for (InstockLogDetailResult instockLogDetailResult : allList) {
            InstockLogDetailResult detailResult = map.get(instockLogDetailResult.getCreateTime().toString()
                    + instockLogDetailResult.getSkuId()
                    + instockLogDetailResult.getBrandId()
                    + instockLogDetailResult.getStorehousePositionsId()
                    + instockLogDetailResult.getType()
            );

            if (ToolUtil.isNotEmpty(detailResult)) {
                List<Long> inkindIds = detailResult.getInkindIds();
                inkindIds.addAll(instockLogDetailResult.getInkindIds());
                detailResult.setInkindIds(inkindIds);
                map.put(instockLogDetailResult.getCreateTime().toString()
                        + instockLogDetailResult.getSkuId()
                        + instockLogDetailResult.getBrandId()
                        + instockLogDetailResult.getStorehousePositionsId()
                        + instockLogDetailResult.getType(), detailResult);
            } else {
                map.put(instockLogDetailResult.getCreateTime().toString()
                        + instockLogDetailResult.getSkuId()
                        + instockLogDetailResult.getBrandId()
                        + instockLogDetailResult.getStorehousePositionsId()
                        + instockLogDetailResult.getType(), instockLogDetailResult);
            }
        }

        for (String key : map.keySet()) {
            InstockLogDetailResult instockLogDetailResult = map.get(key);
            detailResults.add(instockLogDetailResult);
        }
        return detailResults;
    }

    @Override
    public void delete(InstockLogDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockLogDetailParam param) {
        InstockLogDetail oldEntity = getOldEntity(param);
        InstockLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockLogDetailResult findBySpec(InstockLogDetailParam param) {
        return null;
    }

    @Override
    public List<InstockLogDetailResult> findListBySpec(InstockLogDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockLogDetailResult> findPageBySpec(InstockLogDetailParam param) {
        Page<InstockLogDetailResult> pageContext = getPageContext();
        IPage<InstockLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockLogDetailParam param) {
        return param.getInstockLogDetailId();
    }

    private Page<InstockLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockLogDetail getOldEntity(InstockLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private InstockLogDetail getEntity(InstockLogDetailParam param) {
        InstockLogDetail entity = new InstockLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<InstockLogDetailResult> resultsByLogIds(List<Long> logIds) {
        if (ToolUtil.isEmpty(logIds) || logIds.size() == 0) {
            return new ArrayList<>();
        }
        List<InstockLogDetail> instockLogDetails = this.query().in("instock_log_id", logIds).list();
        List<InstockLogDetailResult> results = new ArrayList<>();
        for (InstockLogDetail instockLogDetail : instockLogDetails) {
            InstockLogDetailResult result = new InstockLogDetailResult();
            ToolUtil.copyProperties(instockLogDetail, result);
            results.add(result);
        }
        this.format(results);
        return results;
    }

    @Override
    public void format(List<InstockLogDetailResult> results) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> instockOrderId = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();


        for (InstockLogDetailResult result : results) {
            skuIds.add(result.getSkuId());
            if (ToolUtil.isNotEmpty(result.getInstockOrderId())) {
                instockOrderId.add(result.getInstockOrderId());
            }
            if (ToolUtil.isEmpty(result.getBrandId()) || result.getBrandId() == 0) {
                result.setBrandResult(new BrandResult() {{
                    setBrandName("无品牌");
                }});
            }
            brandIds.add(result.getBrandId());
            if (ToolUtil.isNotEmpty(result.getCustomerId())) {
                customerIds.add(result.getCustomerId());
            }
            userIds.add(result.getCreateUser());
            positionIds.add(result.getStorehousePositionsId());
        }

        instockOrderId = instockOrderId.stream().distinct().collect(Collectors.toList());
        List<InstockList> instockLists = instockOrderId.size() == 0 ? new ArrayList<>() : instockListService.query().in("instock_order_id", instockOrderId).list();
        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        List<StorehousePositionsResult> positionsResults = storehousePositionsService.details(positionIds);


        for (InstockLogDetailResult result : results) {

            Integer stockNumber = stockDetailsService.getNumberByStock(result.getSkuId(), result.getBrandId(), result.getStorehousePositionsId());
            result.setStockNumber(stockNumber);

            for (StorehousePositionsResult position : positionsResults) {
                if (ToolUtil.isNotEmpty(result.getStorehousePositionsId()) && result.getStorehousePositionsId().equals(position.getStorehousePositionsId())) {
                    result.setStorehousePositionsResult(position);
                    break;
                }
            }

            for (User user : userList) {
                if (result.getCreateUser().equals(user.getUserId())) {
                    result.setUser(user);
                    break;
                }
            }

            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (result.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    result.setSkuResult(skuSimpleResult);
                }
            }
            for (InstockList instockList : instockLists) {
                if (ToolUtil.isNotEmpty(result.getInstockOrderId()) && instockList.getInstockOrderId().equals(result.getInstockOrderId()) && instockList.getSkuId().equals(result.getSkuId())) {
                    result.setListNumber(Math.toIntExact(instockList.getNumber()));
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(result.getBrandId()) && brandResult.getBrandId().equals(result.getBrandId())) {
                    result.setBrandResult(brandResult);
                    break;
                }
            }
            for (Customer customer : customerList) {
                if (ToolUtil.isNotEmpty(result.getCustomerId()) && customer.getCustomerId().equals(result.getCustomerId())) {
                    result.setCustomer(customer);
                    break;
                }
            }
        }

    }

    @Override
    public List<InstockLogDetailResult> getOutStockLogs(InstockLogDetailParam param) {
        List<InstockLogDetailResult> instockLogDetailResults = this.baseMapper.customList(param);
        List<InstockLogDetailResult> totalList = new ArrayList<>();
        instockLogDetailResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()) + "_" + item.getCreateTime(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new InstockLogDetailResult() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                        setCreateUser(a.getCreateUser());
                        setCreateTime(a.getCreateTime());
                    }}).ifPresent(totalList::add);
                }
        );

        List<InstockLogDetailResult> results = totalList.stream().sorted(Comparator.comparing(InstockLogDetailResult::getCreateTime).reversed()).collect(Collectors.toList());
        this.format(results);
        return results;
    }

}

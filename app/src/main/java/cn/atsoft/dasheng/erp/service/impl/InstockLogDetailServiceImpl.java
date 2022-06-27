package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.InstockLogDetailMapper;
import cn.atsoft.dasheng.erp.model.params.InstockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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


    @Override
    public void add(InstockLogDetailParam param) {
        InstockLogDetail entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 历史记录
     *
     * @param param
     * @return
     */
    @Override
    public List<InstockLogDetailResult> history(InstockLogDetailParam param) {

//        List<InstockLogDetailResult> results = new ArrayList<>();
        List<InstockLogDetailResult> allList = this.baseMapper.customList(param);
//        Map<String, List<InstockLogDetailResult>> map = new HashMap<>();
//
//        for (InstockLogDetailResult result : allList) {
//            if (ToolUtil.isEmpty(result.getBrandId())) {
//                result.setBrandId(0L);
//            }
//            List<InstockLogDetailResult> detailResults = map.get(result.getSkuId() + result.getBrandId() + result.getCustomerId()+result.getType());
//            if (ToolUtil.isEmpty(detailResults)) {
//                detailResults = new ArrayList<>();
//            }
//            detailResults.add(result);
//            map.put(result.getSkuId() + result.getBrandId() + result.getCustomerId()+result.getType(), detailResults);
//        }
//
//        for (String key : map.keySet()) {
//            List<InstockLogDetailResult> logDetailResults = map.get(key);
//            long number = 0L;
//            for (InstockLogDetailResult logDetailResult : logDetailResults) {
//                number = number + logDetailResult.getNumber();
//            }
//            InstockLogDetailResult instockLogDetailResult = logDetailResults.get(0);
//            instockLogDetailResult.setNumber(number);
//            results.add(instockLogDetailResult);
//        }
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
    private void format(List<InstockLogDetailResult> results) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> instockOrderId = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();


        for (InstockLogDetailResult result : results) {
            skuIds.add(result.getSkuId());
            if (ToolUtil.isNotEmpty(result.getInstockOrderId())){
                instockOrderId.add(result.getInstockOrderId());
            }
            brandIds.add(result.getBrandId());
            if (ToolUtil.isNotEmpty(result.getCustomerId())){
                customerIds.add(result.getCustomerId());
            }
            userIds.add(result.getCreateUser());
            positionIds.add(result.getStorehousePositionsId());
        }

        instockOrderId = instockOrderId.stream().distinct().collect(Collectors.toList());
        List<InstockList> instockLists = instockOrderId.size() == 0 ? new ArrayList<>() : instockListService.query().in("instock_order_id", instockOrderId).list();
        List<BrandResult> brandResults =brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<SkuSimpleResult> skuSimpleResults =skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());


        for (InstockLogDetailResult result : results) {

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
    public List<InstockLogDetailResult> getOutStockLogs(InstockLogDetailParam param){
        List<InstockLogDetailResult> instockLogDetailResults = this.baseMapper.customList(param);
        this.format(instockLogDetailResults);
        return instockLogDetailResults;
    }

}

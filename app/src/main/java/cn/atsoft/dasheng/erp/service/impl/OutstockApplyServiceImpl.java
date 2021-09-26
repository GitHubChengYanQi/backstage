package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.service.ContactsBindService;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.mapper.OutstockApplyMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.erp.service.OutstockApplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhlabs.image.ErodeAlphaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 出库申请 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
@Service
public class OutstockApplyServiceImpl extends ServiceImpl<OutstockApplyMapper, OutstockApply> implements OutstockApplyService {
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private ApplyDetailsService applyDetailsService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private ContactsBindService contactsBindService;

    @Override
    public void add(OutstockApplyParam param) {
        Integer count = customerService.lambdaQuery().eq(Customer::getCustomerId, param.getCustomerId()).count();
        if (count==0) {
            throw new ServiceException(500, "请填写客户，再进行操作");
        }
        if (ToolUtil.isEmpty(param.getContactsId())) {
            throw new ServiceException(500, "请填写联系人，再进行操作");
        }
        //验证联系人与客户是否匹配
        List<ContactsBind> contactsBinds = contactsBindService.lambdaQuery().in(ContactsBind::getCustomerId, param.getCustomerId()).list();
        boolean f = false;
        for (ContactsBind contactsBind : contactsBinds) {
            if (contactsBind.getContactsId()!=null && contactsBind.getContactsId().equals(param.getContactsId())) {
                f = true;
                break;
            }
        }
        if (!f) {
            throw new ServiceException(500, "联系人与客户不匹配");
        }


        OutstockApply entity = getEntity(param);
        this.save(entity);
        //添加发货申请详情数据
        List<ApplyDetails> applyDetailsList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getApplyDetails())) {
            for (ApplyDetailsParam applyDetailsParam : param.getApplyDetails()) {
                ApplyDetails applyDetails = new ApplyDetails();
                applyDetails.setOutstockApplyId(entity.getOutstockApplyId());
                applyDetails.setBrandId(applyDetailsParam.getBrandId());
                applyDetails.setItemId(applyDetailsParam.getItemId());
                applyDetails.setNumber(applyDetailsParam.getNumber());
                applyDetailsList.add(applyDetails);
            }
            if (ToolUtil.isNotEmpty(applyDetailsList)) {
                applyDetailsService.saveBatch(applyDetailsList);
            }
        }

    }

    @Override
    public void delete(OutstockApplyParam param) {
        OutstockApply byId = this.getById(param.getOutstockApplyId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "所删除目标不存在");
        } else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(OutstockApplyParam param) {

        OutstockApply oldEntity = getOldEntity(param);
        OutstockApply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        if (newEntity.getApplyState().equals(2)) {
            //添加发货单
            DeliveryParam deliveryParam = new DeliveryParam();
            deliveryParam.setCustomerId(param.getCustomerId());
            deliveryParam.setPhoneId(param.getPhoneId());
            deliveryParam.setAdressId(param.getAdressId());
            deliveryParam.setContactsId(param.getContactsId());
            Long add = deliveryService.add(deliveryParam);

            //添加出库单
            OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
            outstockOrderParam.setOutstockApplyId(newEntity.getOutstockApplyId());
            OutstockOrder outstockOrder = outstockOrderService.add(outstockOrderParam);
            List<OutstockListing> outstockListings = new ArrayList<>();
            for (ApplyDetailsParam applyDetail : param.getApplyDetails()) {
                OutstockListing outstockListing = new OutstockListing();
                outstockListing.setBrandId(applyDetail.getBrandId());
                outstockListing.setItemId(applyDetail.getItemId());
                outstockListing.setNumber(applyDetail.getNumber());
                outstockListing.setOutstockApplyId(applyDetail.getOutstockApplyId());
                outstockListing.setOutstockOrderId(outstockOrder.getOutstockOrderId());
                outstockListing.setDeliveryId(add);
                outstockListings.add(outstockListing);

            }
            if (ToolUtil.isNotEmpty(outstockListings)) {
                outstockListingService.saveBatch(outstockListings);
            }

        }


    }

    @Override
    public OutstockApplyResult findBySpec(OutstockApplyParam param) {
        return null;
    }

    @Override
    public List<OutstockApplyResult> findListBySpec(OutstockApplyParam param) {
        return null;
    }

    @Override
    public PageInfo<OutstockApplyResult> findPageBySpec(OutstockApplyParam param) {
        Page<OutstockApplyResult> pageContext = getPageContext();
        IPage<OutstockApplyResult> page = this.baseMapper.customPageList(pageContext, param);

        format(page.getRecords());

        for (OutstockApplyResult record : page.getRecords()) {
            QueryWrapper<ApplyDetails> applyDetailsQueryWrapper = new QueryWrapper<>();
            applyDetailsQueryWrapper.in("outstock_apply_id", record.getOutstockApplyId());
            List<ApplyDetails> list = applyDetailsService.list(applyDetailsQueryWrapper);
            record.setApplyDetails(list);
        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockApplyParam param) {
        return param.getOutstockApplyId();
    }

    private Page<OutstockApplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockApply getOldEntity(OutstockApplyParam param) {
        return this.getById(getKey(param));
    }

    private OutstockApply getEntity(OutstockApplyParam param) {
        OutstockApply entity = new OutstockApply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<OutstockApplyResult> data) {
        List<Long> customerIds = new ArrayList<>();
        List<Long> adressIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();
        List<Long> phoneIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        for (OutstockApplyResult datum : data) {
            customerIds.add(datum.getCustomerId());
            adressIds.add(datum.getAdressId());
            contactsIds.add(datum.getContactsId());
            stockIds.add(datum.getStockId());
            phoneIds.add(datum.getPhoneId());
            userIds.add(datum.getUserId());
        }

        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.lambdaQuery()
                .in(Customer::getCustomerId, customerIds)
                .list();

        List<Contacts> contacts = contactsIds.size() == 0 ? new ArrayList<>() : contactsService.lambdaQuery()
                .in(Contacts::getContactsId, contactsIds)
                .list();

        List<Adress> adresses = adressIds.size() == 0 ? new ArrayList<>() : adressService.lambdaQuery()
                .in(Adress::getAdressId, adressIds)
                .list();

        List<Storehouse> stocks = stockIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery()
                .in(Storehouse::getStorehouseId, stockIds)
                .list();

        List<Phone> phones = phoneIds.size() == 0 ? new ArrayList<>() : phoneService.lambdaQuery()
                .in(Phone::getPhoneId, phoneIds)
                .list();

        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery()
                .in(User::getUserId, userIds)
                .list();

        for (OutstockApplyResult datum : data) {
            for (User user : users) {
                if (user.getUserId().equals(datum.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }

            for (Storehouse storehouse : stocks) {
                if (datum.getStockId().equals(storehouse.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStockResult(storehouseResult);
                    break;
                }
            }
            for (Customer customer : customers) {
                if (datum.getCustomerId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    datum.setCustomerResult(customerResult);
                    break;
                }
            }
            for (Adress adress : adresses) {
                if (datum.getAdressId().equals(adress.getAdressId())) {
                    AdressResult adressResult = new AdressResult();
                    ToolUtil.copyProperties(adress, adressResult);
                    datum.setAdressResult(adressResult);
                    break;
                }
            }
            for (Contacts contact : contacts) {
                if (datum.getContactsId().equals(contact.getContactsId())) {
                    ContactsResult contactsResult = new ContactsResult();
                    ToolUtil.copyProperties(contact, contactsResult);
                    datum.setContactsResult(contactsResult);
                    break;
                }
            }
            for (Phone phone : phones) {
                if (datum.getPhoneId().equals(phone.getPhoneId())) {
                    PhoneResult phoneResult = new PhoneResult();
                    ToolUtil.copyProperties(phone, phoneResult);
                    datum.setPhoneResult(phoneResult);
                    break;
                }
            }

        }
    }
}

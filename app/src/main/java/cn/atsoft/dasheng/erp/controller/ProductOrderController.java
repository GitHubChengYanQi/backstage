package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.params.ProductOrderRequest;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
import cn.atsoft.dasheng.erp.service.ProductOrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 产品订单控制器
 *
 * @author song
 * @Date 2021-10-20 16:18:02
 */
@RestController
@RequestMapping("/productOrder")
@Api(tags = "产品订单")
public class ProductOrderController extends BaseController {

    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ProductOrderDetailsService productOrderDetailsService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private UserService userService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductOrderParam productOrderParam) {
        this.productOrderService.add(productOrderParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改产品订单", key = "name", dict = ProductOrderParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductOrderParam productOrderParam) {

        this.productOrderService.update(productOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除产品订单", key = "name", dict = ProductOrderParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductOrderParam productOrderParam) {
        this.productOrderService.delete(productOrderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductOrderParam productOrderParam) {
        ProductOrder detail = this.productOrderService.getById(productOrderParam.getProductOrderId());
        if (ToolUtil.isEmpty(detail)) {
            throw new ServiceException(500, "没有当前信息");
        }
        List<ProductOrderDetails> productOrderDetails = productOrderDetailsService.lambdaQuery()
                .in(ProductOrderDetails::getProductOrderId, detail.getProductOrderId())
                .list();
        List<ProductOrderDetails> productOrderDetailsList = new ArrayList<>();
        for (ProductOrderDetails productOrderDetail : productOrderDetails) {
            productOrderDetailsList.add(productOrderDetail);
        }

        ProductOrderRequest productOrderRequest = JSONUtil.toBean(detail.getCustomer(), ProductOrderRequest.class);

        ProductOrderResult result = new ProductOrderResult();
        ToolUtil.copyProperties(detail, result);

        Adress adress = adressService.query().eq("adress_id", productOrderRequest.getAdressId()).one();
        if (ToolUtil.isNotEmpty(adress)) {
            AdressResult adressResult = new AdressResult();
            ToolUtil.copyProperties(adress, adressResult);
            result.setAdressResult(adressResult);
            result.setAdressId(adressResult.getAdressId());
        }
        Contacts contacts = contactsService.query().eq("contacts_id", productOrderRequest.getContactsId()).one();
        if (ToolUtil.isNotEmpty(contacts)) {
            ContactsResult contactsResult = new ContactsResult();
            ToolUtil.copyProperties(contacts, contactsResult);
            result.setContactsResult(contactsResult);
            result.setContactsId(contactsResult.getContactsId());
        }

        Phone phone = phoneService.query().eq("phone_id", productOrderRequest.getPhoneId()).one();
        if (ToolUtil.isNotEmpty(phone)) {
            PhoneResult phoneResult = new PhoneResult();
            ToolUtil.copyProperties(phone, phoneResult);
            result.setPhoneResult(phoneResult);
            result.setPhoneId(phoneResult.getPhoneId());

        }
        result.setOrderDetail(productOrderDetailsList);

        User user = userService.getById(detail.getCreateUser());
        result.setUser(user);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductOrderResult> list(@RequestBody(required = false) ProductOrderParam productOrderParam) {
        if (ToolUtil.isEmpty(productOrderParam)) {
            productOrderParam = new ProductOrderParam();
        }
        return this.productOrderService.findPageBySpec(productOrderParam);
    }


}



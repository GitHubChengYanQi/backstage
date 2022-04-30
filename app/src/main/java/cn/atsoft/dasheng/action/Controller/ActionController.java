package cn.atsoft.dasheng.action.Controller;

import cn.atsoft.dasheng.action.Enum.*;
import cn.atsoft.dasheng.action.model.param.ActionParam;
import cn.atsoft.dasheng.action.model.param.AddAction;
import cn.atsoft.dasheng.action.model.param.StatusParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import cn.atsoft.dasheng.form.model.params.DocumentsStatusParam;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.atsoft.dasheng.form.service.DocumentsActionService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/statueAction")
public class ActionController {

    @Autowired
    private DocumentStatusService documentStatusService;
    @Autowired
    private DocumentsActionService documentsActionService;

    /**
     * 新增采购申请动作
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/addAction", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid AddAction param) {

        List<AddAction.Action> actions = param.getActions();
        if (ToolUtil.isEmpty(param.getOrderEnum())) {
            throw new ServiceException(500, "请填写单据类型");
        }
        for (AddAction.Action action : param.getActions()) {    // 删除之前老动作 重新添加新动作
            documentsActionService.removeAll(action.getStatusId());
        }

        switch (param.getOrderEnum()) {
            case purchaseAsk:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (PurchaseActionEnum purchaseActionEnum : action.purchaseActionEnums) {
                        purchaseActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case instockAsk:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (InStockActionEnum inStockActionEnum : action.inStockActionEnums) {
                        inStockActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case productionQuality:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (ProductionQualityActionEnum productionQualityActionEnum : action.productionQualityActionEnums) {
                        productionQualityActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case instockError:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (InStockActionEnum inStockActionEnum : action.getInStockActionEnums()) {
                        inStockActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case inQuality:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (InQualityActionEnum inQualityActionEnum : action.getInQualityActionEnums()) {
                        inQualityActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case outstock:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (OutStockActionEnum outStockActionEnum : action.outStockActionEnums) {
                        outStockActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case payAsk:

                break;
            case SO:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (SoOrderActionEnum soOrderActionEnum : action.soOrderActionEnums) {
                        soOrderActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case PO:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (PoOrderActionEnum poOrderActionEnum : action.getPoOrderActionEnums()) {
                        poOrderActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
        }


        return ResponseData.success();
    }

    /**
     * 添加状态
     *
     * @param statusParam
     * @return
     */
    @RequestMapping(value = "/addState", method = RequestMethod.POST)
    public ResponseData addState(@RequestBody @Valid StatusParam statusParam) {
        Long id;
        switch (statusParam.getOrderEnum()) {
            case purchaseAsk:
            case instockAsk:
            case PO:
            case SO:
            case payAsk:
            case outstock:
            case inQuality:
            case instockError:
            case productionQuality:
                DocumentsStatusParam status = statusParam.getParam();
                status.setFormType(statusParam.getOrderEnum().name());
                id = documentStatusService.add(status);
                break;
            default:
                id = null;

        }
        return ResponseData.success(id);
    }
}

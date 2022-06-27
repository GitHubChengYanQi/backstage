package cn.atsoft.dasheng.action.Controller;

import cn.atsoft.dasheng.action.Enum.*;
import cn.atsoft.dasheng.action.dict.PurchaseAskDictEnum;
import cn.atsoft.dasheng.action.model.param.AddAction;
import cn.atsoft.dasheng.action.model.param.StatusParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
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

    @RequestMapping(value = "/selectDict", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData selectDict() {
        return ResponseData.success(PurchaseAskDictEnum.enumList());
    }

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
        if (ToolUtil.isEmpty(param.getReceiptsEnum())) {
            throw new ServiceException(500, "请填写单据类型");
        }
        // 删除之前老动作 重新添加新动作
        DocumentsAction documentsAction = new DocumentsAction();
        documentsAction.setDisplay(0);
        documentsActionService.update(documentsAction, new QueryWrapper<DocumentsAction>() {
            {
                eq("form_type", param.receiptsEnum);
            }
        });
        switch (param.getReceiptsEnum()) {
            case PURCHASE:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (PurchaseActionEnum purchaseActionEnum : action.purchaseActionEnums) {
                        String value = purchaseActionEnum.getValue();
                        purchaseActionEnum.setStatus(action.getStatusId(), param.getReceiptsEnum().name(), value, i);
                        i++;
                    }
                }
                break;
            case INSTOCK:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (InStockActionEnum inStockActionEnum : action.inStockActionEnums) {
                        String value = inStockActionEnum.getValue();
                        inStockActionEnum.setStatus(action.getStatusId(), param.getReceiptsEnum().name(), value, i);
                        i++;
                    }
                }
                break;
            case INSTOCKERROR:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (InstockErrorActionEnum instockErrorActionEnum : action.instockErrorActionEnums) {
                        String value = instockErrorActionEnum.getValue();
                        instockErrorActionEnum.setStatus(action.getStatusId(), param.getReceiptsEnum().name(), value, i);
                        i++;
                    }
                }
                break;
            case OUTSTOCK:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (OutStockActionEnum outStockActionEnum : action.outStockActionEnums) {
                        String value = outStockActionEnum.getValue();
                        outStockActionEnum.setStatus(action.getStatusId(), param.getReceiptsEnum().name(), value, i);
                        i++;
                    }
                }
                break;
            case Stocktaking:
                for (AddAction.Action action : actions) {
                    int i = 0;
                    for (StocktakinEnum stocktakinEnum : action.getStocktakinEnums()) {
                        String value = stocktakinEnum.getValue();
                        stocktakinEnum.setStatus(action.getStatusId(), param.getReceiptsEnum().name(), value, i);
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
        if (ToolUtil.isEmpty(statusParam.getReceiptsEnum())) {
            throw new ServiceException(500, "请传入单据类型枚举");
        }
        switch (statusParam.getReceiptsEnum()) {
            case PURCHASE:
            case INSTOCK:
            case OUTSTOCK:
            case INSTOCKERROR:
            case PURCHASEORDER:
            case Stocktaking:
                DocumentsStatusParam status = statusParam.getParam();
                status.setFormType(statusParam.getReceiptsEnum().name());
                id = documentStatusService.add(status);
                break;
            default:
                id = null;

        }
        return ResponseData.success(id);
    }
}

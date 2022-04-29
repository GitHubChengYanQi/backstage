package cn.atsoft.dasheng.action.Controller;

import cn.atsoft.dasheng.action.Enum.InStockActionEnum;
import cn.atsoft.dasheng.action.Enum.PurchaseActionEnum;
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
    @RequestMapping(value = "/addPurchaseAction", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid AddAction param) {

        List<AddAction.Action> actions = param.getActions();
        if (ToolUtil.isEmpty(param.getOrderEnum())) {
            throw new ServiceException(500, "请填写单据类型");
        }
        switch (param.getOrderEnum()) {
            case purchaseAsk:
                for (AddAction.Action action : actions) {
                    documentsActionService.remove(new QueryWrapper<DocumentsAction>() {{
                        eq("documents_status_id", action.getStatusId());
                    }});
                    int i = 0;
                    for (PurchaseActionEnum purchaseActionEnum : action.purchaseActionEnums) {
                        purchaseActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case instockAsk:
                for (AddAction.Action action : actions) {
                    documentsActionService.remove(new QueryWrapper<DocumentsAction>() {{
                        eq("documents_status_id", action.getStatusId());
                    }});
                    int i = 0;
                    for (InStockActionEnum inStockActionEnum : action.inStockActionEnums) {
                        inStockActionEnum.setStatus(action.getStatusId(), param.getOrderEnum().name(), i);
                        i++;
                    }
                }
                break;
            case productionQuality:

                break;
            case instockError:

                break;
            case inQuality:

                break;
            case outstock:

                break;
            case payAsk:

                break;
            case SO:

                break;
            case PO:

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

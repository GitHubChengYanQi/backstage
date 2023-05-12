package cn.atsoft.dasheng.action.Controller;

import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.action.dict.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static cn.atsoft.dasheng.model.response.ResponseData.success;

@RestController
@RequestMapping("/permission")
public class DictPermissionController {


    @RequestMapping(value = "/selectDict", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData selectDict(ReceiptsEnum receiptsEnum) {
        if (ToolUtil.isEmpty(receiptsEnum)) {
            return null;
        }
        switch (receiptsEnum) {
            case PURCHASE:
                return success(PurchaseAskDictEnum.enumList());
            case INSTOCK:
                return ResponseData.success(InStockDictEnum.enumList());
            case QUALITY:
                return ResponseData.success(QualityDictEnum.enumList());
            case OUTSTOCK:
                return ResponseData.success(OutStockDictEnum.enumList());
            case ERROR:
                return ResponseData.success(InstockErrorDictEnum.enumList());
            case PURCHASEORDER:
                return ResponseData.success(OrderDictEnum.enumList());
        }
        return success();
    }
}

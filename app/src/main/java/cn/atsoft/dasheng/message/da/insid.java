package cn.atsoft.dasheng.message.da;

import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.message.entity.MessageEntity;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class insid {
    @Autowired
    private ContractService contractService;
    public void create(MessageEntity messageEntity){
        switch (messageEntity.getInsideType()) {
            case "CONTACT" :

                JSON.parseObject(messageEntity.getObject().toString());
//                contractService.orderAddContract(entity.getOrderId(), param.getContractParam());
                break;
        }
    }
}

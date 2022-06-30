package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemarksMessageService {
    /**
     * 在此Service中添加逻辑
     */

    @Autowired
    private RemarksService remarksService;


    public void remarksServiceDo(RemarksEntity remarksEntity) {
        /**
         * switch动作 可在case中调用其本身service中方法
         */
        RemarksParam remarksParam = remarksEntity.getRemarksParam();
        switch (remarksEntity.getOperationType()) {
            case ADD:
                remarksService.addByMQ(remarksParam);
                break;
            case SAVE:
                Remarks entity = new Remarks();
                remarksService.save(entity);
                break;
            case DELETE:
                remarksService.delete(remarksParam);
                break;
            case UPDATE:
                break;
            default:
                break;
        }

    }
}

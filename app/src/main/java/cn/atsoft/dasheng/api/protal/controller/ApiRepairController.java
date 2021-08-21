package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiRepairController {

    @Autowired
    private RepairService repairService;

    @RequestMapping(value = "/saveRepair", method = RequestMethod.POST)
    public Long saveRepair(@RequestBody RepairParam repairParam) {
        Long repairId = this.repairService.add(repairParam);
        return repairId;
    }
}
package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import cn.atsoft.dasheng.portal.navigation.entity.Navigation;
import cn.atsoft.dasheng.portal.navigation.service.NavigationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiNavigationController {
    @Autowired
    private NavigationService navigationService;
    @RequestMapping(value = "/getNavigation",method = RequestMethod.GET)
    public ResponseData getNavigation (@RequestParam("classId")Long classId){
        QueryWrapper<Navigation> navigationQueryWrapper = new QueryWrapper<>();
        navigationQueryWrapper.in("difference",classId);
        List<Navigation> list =navigationService.list(navigationQueryWrapper);
        return  ResponseData.success(list);
    }
}

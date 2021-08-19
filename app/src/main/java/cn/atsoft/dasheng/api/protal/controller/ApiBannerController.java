package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.service.BannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiBannerController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/getBanner", method = RequestMethod.POST)
    public ResponseData getBanner(@RequestBody BannerParam bannerParam) {
        QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
        bannerQueryWrapper.in("difference", bannerParam.getDifference()).orderByAsc("sort").in("display",1);
        List<Banner> list = bannerService.list(bannerQueryWrapper);
        return ResponseData.success(list);
    }
}

/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.atsoft.dasheng.sys.modular.rest.controller;

import cn.atsoft.dasheng.sys.core.constant.dictmap.DeleteDict;
import cn.atsoft.dasheng.sys.core.constant.dictmap.NoticeMap;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestNotice;
import cn.atsoft.dasheng.sys.modular.system.entity.Notice;
import cn.atsoft.dasheng.sys.modular.system.warpper.NoticeWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageFactory;
import cn.atsoft.dasheng.sys.modular.rest.service.RestNoticeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 通知控制器
 *
 * @author fengshuonan
 * @Date 2017-05-09 23:02:21
 */
@RestController
@RequestMapping("/rest/notice")
public class RestNoticeController extends BaseController {

    @Autowired
    private RestNoticeService restNoticeService;

    /**
     * 获取通知详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping("/detail/{noticeId}")
    public ResponseData noticeUpdate(@PathVariable Long noticeId) {
        RestNotice notice = this.restNoticeService.getById(noticeId);
        return new SuccessResponseData(notice);
    }

    /**
     * 获取通知列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/list")
    public Object list(String condition) {
        Page<Map<String, Object>> list = this.restNoticeService.list(condition);
        Page<Map<String, Object>> wrap = new NoticeWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 新增通知
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "新增通知", key = "title", dict = NoticeMap.class)
    public Object add(@RequestBody RestNotice restNotice) {
        if (ToolUtil.isOneEmpty(restNotice, restNotice.getTitle(), restNotice.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        restNotice.setCreateUser(LoginContextHolder.getContext().getUser().getId());
        restNotice.setCreateTime(new Date());
        this.restNoticeService.save(restNotice);
        return SUCCESS_TIP;
    }

    /**
     * 删除通知
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除通知", key = "noticeId", dict = DeleteDict.class)
    public Object delete(@RequestParam Long noticeId) {

        this.restNoticeService.removeById(noticeId);

        return SUCCESS_TIP;
    }

    /**
     * 修改通知
     *
     * @author fengshuonan
     * @Date 2018/12/23 6:06 PM
     */
    @RequestMapping(value = "/update")
    @BussinessLog(value = "修改通知", key = "title", dict = NoticeMap.class)
    public Object update(@RequestBody Notice notice) {
        if (ToolUtil.isOneEmpty(notice, notice.getNoticeId(), notice.getTitle(), notice.getContent())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        RestNotice old = this.restNoticeService.getById(notice.getNoticeId());
        old.setTitle(notice.getTitle());
        old.setContent(notice.getContent());
        this.restNoticeService.updateById(old);
        return SUCCESS_TIP;
    }

}

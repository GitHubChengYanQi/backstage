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
package cn.stylefeng.guns.sys.modular.rest.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.guns.base.log.BussinessLog;
import cn.stylefeng.guns.base.pojo.node.ZTreeNode;
import cn.stylefeng.guns.base.pojo.page.PageFactory;
import cn.stylefeng.guns.base.pojo.page.PageInfo;
import cn.stylefeng.guns.sys.core.constant.dictmap.DeptDict;
import cn.stylefeng.guns.sys.core.constant.factory.ConstantFactory;
import cn.stylefeng.guns.sys.modular.rest.entity.RestDept;
import cn.stylefeng.guns.sys.modular.rest.service.RestDeptService;
import cn.stylefeng.guns.sys.modular.system.model.DeptDto;
import cn.stylefeng.guns.sys.modular.system.warpper.DeptWrapper;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 *
 * @author fengshuonan
 * @Date 2017年2月17日20:27:22
 */
@RestController
@RequestMapping("/rest/dept")
public class RestDeptController extends BaseController {

    @Autowired
    private RestDeptService restDeptService;

    /**
     * 获取部门的tree列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping(value = "/tree")
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.restDeptService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 新增部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "添加部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/add")
    public ResponseData add(@RequestBody RestDept restDept) {
        this.restDeptService.addDept(restDept);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有部门列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/list")
    public PageInfo list(@RequestParam(value = "condition", required = false) String condition,
                         @RequestParam(value = "deptId", required = false) Long deptId) {
        Page<Map<String, Object>> list = this.restDeptService.list(condition, deptId);
        Page<Map<String, Object>> wrap = new DeptWrapper(list).wrap();

        return PageFactory.createPageInfo(wrap);
    }

    /**
     * 部门详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{deptId}")
    public Object detail(@PathVariable("deptId") Long deptId) {
        RestDept dept = restDeptService.getById(deptId);
        DeptDto deptDto = new DeptDto();
        BeanUtil.copyProperties(dept, deptDto);
        deptDto.setPName(ConstantFactory.me().getDeptName(deptDto.getPid()));
        return deptDto;
    }

    /**
     * 修改部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "修改部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/update")
    public ResponseData update(@RequestBody RestDept restDept) {
        restDeptService.editDept(restDept);
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")
    public ResponseData delete(@RequestParam("deptId") Long deptId) {
        restDeptService.deleteDept(deptId);
        return SUCCESS_TIP;
    }

}
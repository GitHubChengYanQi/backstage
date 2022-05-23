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
package cn.atsoft.dasheng.base.pojo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 分页结果的封装(for Layui Table)
 */
@Data
@ApiModel(description = "统一翻页交结果")
public class PageInfo<T> {

    @ApiModelProperty("业务状态码")
    private Integer errCode = 0;

    private String msg = "请求成功";

    @ApiModelProperty("列表数据")
    private List<T> data;

    @ApiModelProperty("总数")
    private long count;

    @ApiModelProperty("当前页")
    private long current;

    @ApiModelProperty("每页条数")
    private long pageSize;

    @ApiModelProperty("搜索")
    private List<Object> search;

}

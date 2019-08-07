package com.real.name.common.utils;

import com.real.name.common.result.ResultVo;
import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.Map;

public class PageUtils {

    public static ResultVo pageResult(PageInfo pageInfo, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("total", pageInfo.getTotal());
        map.put("data", data);
        return ResultVo.success(map);
    }

}

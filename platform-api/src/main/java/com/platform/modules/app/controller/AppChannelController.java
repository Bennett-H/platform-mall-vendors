package com.platform.modules.app.controller;

import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallChannelEntity;
import com.platform.modules.mall.service.MallChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/app/channel")
@Api(tags = "AppChannelController|首页频道接口")
public class AppChannelController extends AppBaseController {

    @Autowired
    private MallChannelService mallChannelService;


    /**
     * 商品分类列表
     *
     * @param type 分类等级
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/list")
    @ApiOperation(value = "首页频道列表", notes = "首页频道列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "商品分区：good  资讯分区：topic", example = "商品分区：good  资讯分区：topic", dataType = "string")
    })
    public RestResponse categoryList(@RequestParam String type) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("type", type);
        List<MallChannelEntity> list = mallChannelService.queryAll(map);
        return RestResponse.success().put("data", list);
    }
}

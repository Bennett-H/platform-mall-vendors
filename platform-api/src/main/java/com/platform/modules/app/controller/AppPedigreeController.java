package com.platform.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.PedigreeApplicationEntity;
import com.platform.modules.mall.service.PedigreeApplicationService;
import com.platform.modules.mall.service.PedigreeMemberService;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberAppTreeVO;
import com.platform.modules.mall.vo.pedigree.PedigreeMemberVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "AppPedigreeController|族谱管理控制器")
@RestController
@RequestMapping("/app/pedigree")
public class AppPedigreeController extends AppBaseController {

    @Autowired
    private PedigreeMemberService pedigreeMemberService;

    @Autowired
    private PedigreeApplicationService pedigreeApplicationService;

    /**
     * 族谱列表
     *
     * @param current 当前页码
     * @param limit   每页条数
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("list")
    @ApiOperation(value = "族谱列表", notes = "族谱列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "isFamous", value = "是否名人：1是 0不是", example = "是否名人：1是 0不是", dataType = "int")
    })
    public RestResponse pedigreeList(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit,
                                     @RequestParam(defaultValue = "") Integer isFamous) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("isFamous", isFamous);
        map.put("page", current);
        map.put("limit", limit);
        Page data = pedigreeMemberService.queryAppPage(map);
        return RestResponse.success().put("data", data);
    }

    /**
     * 族谱成员详情
     *
     * @param id 族谱成员id
     * @return RestResponse
     */
    @IgnoreAuth
    @ApiOperation(value = "族谱成员详情", notes = "族谱成员详情")
    @GetMapping("detail")
    @ApiImplicitParam(paramType = "query", name = "id", value = "族谱成员id", example = "族谱成员id", required = true, dataType = "string")
    public RestResponse pedigreeDetail(@RequestParam String id) {
        PedigreeMemberVO pedigreeMemberVO = pedigreeMemberService.info(id);
        return RestResponse.success().put("data", pedigreeMemberVO);
    }

    /**
     * 族谱树
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @ApiOperation(value = "族谱树", notes = "族谱树")
    @GetMapping("tree")
    @ApiImplicitParams({
    })
    public RestResponse pedigreeTree() {
        List<PedigreeMemberAppTreeVO> tree = pedigreeMemberService.pedigreeTree();
        return RestResponse.success().put("data", tree);
    }

    /**
     * 寻亲认祖
     *
     * @param jsonParam jsonParam
     * @return RestResponse
     */
    @IgnoreAuth
    @ApiOperation(value = "寻亲认祖", notes = "寻亲认祖")
    @PostMapping("/application")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "name", value = "彭xx"),
                    @ExampleProperty(mediaType = "phone", value = "123xxxx1234"),
                    @ExampleProperty(mediaType = "identity", value = "身份"),
                    @ExampleProperty(mediaType = "wechatNo", value = "1234"),
                    @ExampleProperty(mediaType = "wechatName", value = "xxx"),
                    @ExampleProperty(mediaType = "province", value = "广东省"),
                    @ExampleProperty(mediaType = "city", value = "深圳市"),
                    @ExampleProperty(mediaType = "region", value = "宝安区"),
                    @ExampleProperty(mediaType = "job", value = "xxxxxx")
            }), required = true, dataType = "string")
    })
    public RestResponse save(@LoginUser MallUserEntity loginUser, @RequestBody PedigreeApplicationEntity jsonParam) {
        jsonParam.setStatus(0);
        Date date = new Date();
        jsonParam.setCreateTime(date);
        jsonParam.setUpdateTime(date);
        if (loginUser != null) {
            jsonParam.setCreateUserId(loginUser.getId());
            jsonParam.setUpdateUserId(loginUser.getId());
        }
        pedigreeApplicationService.add(jsonParam);
        return RestResponse.success();
    }
}

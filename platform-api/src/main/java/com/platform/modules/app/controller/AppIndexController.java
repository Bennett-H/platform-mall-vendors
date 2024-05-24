/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.app.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.*;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.entity.SmsConfig;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.service.SysCaptchaService;
import com.platform.modules.sys.service.SysConfigService;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/index")
@Api(tags = "AppIndexController|APP首页接口")
public class AppIndexController extends AppBaseController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MallBannerService bannerService;
    @Autowired
    private MallChannelService channelService;
    @Autowired
    private MallBulletinService bulletinService;
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysSmsLogService smsLogService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallTemplateConfService templateConfService;
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    //private final QueryWrapper<MallBannerEntity> bannerListWrapper = new QueryWrapper<>();
    //private final QueryWrapper<MallChannelEntity> channelListWrapper = new QueryWrapper<>();
    //private final QueryWrapper<MallBulletinEntity> bulletinListWrapper = new QueryWrapper<>();
    //private final QueryWrapper<MallTemplateConfEntity> getTemplateIdWrapper = new QueryWrapper<>();

    /**
     * 根据key获取value
     *
     * @param key key
     * @return RestResponse
     * @deprecated Use getTreaty() instead
     */
    @Deprecated
    @IgnoreAuth
    @GetMapping("/getConfigByKey")
    @ApiOperation(value = "根据key获取value", notes = "根据key获取value")
    @ApiImplicitParam(paramType = "query", name = "key", value = "key", example = "1", required = true, dataType = "string")
    public RestResponse getConfigByKey(@RequestParam String key) {
        String value = sysConfigService.getValue(key);
        return RestResponse.success().put("data", value);
    }

    /**
     * 获取协议
     *
     * @param type 1：用户协议  2：隐私协议  3：客服电话  4：免运费门槛  5：是否开启余额支付  6：是否开启分销功能  7：商城名称  8：腾讯位置KEY  9：LOGO  10：快递鸟kdnBusinessId  11：快递鸟kdnAppKey
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/getTreaty")
    @ApiOperation(value = "获取协议", notes = "获取协议")
    @ApiImplicitParam(paramType = "query", name = "type", example = "1", allowableValues = "1,2,3,4,5,6,7,8,9,10,11", required = true, dataType = "string",
            value = "1：用户协议  2：隐私协议  3：客服电话  4：免运费门槛  5：是否开启余额支付  6：是否开启分销功能  7：商城名称  8：腾讯位置KEY  9：LOGO  10：快递鸟kdnBusinessId  11：快递鸟kdnAppKey")
    public RestResponse getTreaty(@RequestParam String type) {
        String key = Constant.TELEPHONE;
        if (Constant.STR_ONE.equals(type)) {
            key = Constant.USER_TREATY;
        }
        if (Constant.STR_TWO.equals(type)) {
            key = Constant.PRIVACY_TREATY;
        }
        if (Constant.STR_THREE.equals(type)) {
            key = Constant.TELEPHONE;
        }
        if (Constant.STR_FOUR.equals(type)) {
            key = Constant.SHIPPING_FEE_FREE;
        }
        if (Constant.STR_FIVE.equals(type)) {
            key = Constant.RECHARGE_STATUS;
        }
        if (Constant.STR_SIX.equals(type)) {
            key = Constant.DISTRIBUTION_STATUS;
        }
        if (Constant.STR_SEVEN.equals(type)) {
            key = Constant.PROJECT_NAME;
        }
        if (Constant.STR_EIGHT.equals(type)) {
            key = Constant.QQ_MAP_KEY;
        }
        if (Constant.STR_NINE.equals(type)) {
            key = Constant.LOGO_URL;
        }
        if (Constant.STR_TEN.equals(type)) {
            key = Constant.KDN_BUSINESS_ID;
        }
        if (Constant.STR_EELEVEN.equals(type)) {
            key = Constant.KDN_APP_KEY;
        }
        String treaty = sysConfigService.getValue(key);
        return RestResponse.success().put("data", treaty);
    }

    /**
     * 获取协议
     *
     * @param type 1：用户协议  2：隐私协议
     * @return String
     */
    @IgnoreAuth
    @GetMapping("/getTreatyData")
    @ApiOperation(value = "获取协议", notes = "获取协议")
    @ApiImplicitParam(paramType = "query", name = "type", value = "1：用户协议  2：隐私协议", allowableValues = "1,2", example = "1", required = true, dataType = "string")
    public String getTreatyData(@RequestParam String type) {
        String key = Constant.USER_TREATY;
        if (Constant.STR_TWO.equals(type)) {
            key = Constant.PRIVACY_TREATY;
        }
        return sysConfigService.getValue(key);
    }

    /**
     * 获取族谱信息
     *
     * @return String
     */
    @IgnoreAuth
    @GetMapping("/getPedigreeInfo")
    @ApiOperation(value = "获取族谱信息", notes = "获取族谱信息")
    public RestResponse getPredigreeInfo() {
        return RestResponse.success().put("data", sysConfigService.getValue("PEDIGREE_INFO"));
    }

    /**
     * 获取订阅消息id
     *
     * @param templateTypes 模板类型集合
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/getTemplateId")
    @ApiOperation(value = "获取订阅消息id", notes = "获取订阅消息id")
    @ApiImplicitParam(paramType = "query", name = "templateTypes", value = "模板类型", example = "'1,2'", required = true, dataType = "string")
    public RestResponse getTemplateId(@RequestParam String templateTypes) {
        Object[] templates = templateTypes.split(Constant.COMMA);
        List<String> list = templateConfService.queryAllTemplateIdsByWrapper(new QueryWrapper<MallTemplateConfEntity>().in("TEMPLATE_TYPE", templates));

        return RestResponse.success().put("data", list);
    }

    /**
     * 首页轮播图列表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/bannerList")
    @ApiOperation(value = "首页轮播图列表", notes = "首页轮播图列表")
    @ApiImplicitParam(paramType = "query", name = "type", value = "轮播图分区", example = "'index,pedigree'", required = true, dataType = "string")
    public RestResponse bannerList(String type) {
        List<MallBannerEntity> bannerEntityList = bannerService.queryAllByWrapper(new QueryWrapper<MallBannerEntity>().eq("ENABLED", 1)
                .eq("TYPE", type).ge("END_TIME", new Date()));
        return RestResponse.success().put("data", bannerEntityList);
    }

    /**
     * 首页轮播图详情
     *
     * @param bannerId 轮播ID
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/bannerDetail")
    @ApiOperation(value = "首页轮播详情", notes = "根据轮播ID获取详情")
    @ApiImplicitParam(paramType = "query", name = "bannerId", value = "轮播ID", example = "1", required = true, dataType = "string")
    public RestResponse bannerDetail(@RequestParam String bannerId) {
        MallBannerEntity bannerEntity = bannerService.getById(bannerId);
        return RestResponse.success().put("data", bannerEntity);
    }

    /**
     * 首页频道列表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/channelList")
    @ApiOperation(value = "首页频道列表", notes = "首页频道列表")
    public RestResponse channelList(HttpServletRequest request) {
        List<MallChannelEntity> bannerEntityList = channelService.queryAllByWrapper(new QueryWrapper<MallChannelEntity>().eq("STATUS", 1).orderByDesc("SORT"));
        return RestResponse.success().put("data", bannerEntityList);
    }

    /**
     * 商城公告列表
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @GetMapping("/bulletinList")
    @ApiOperation(value = "商城公告列表", notes = "商城公告列表")
    public RestResponse bulletinList() {
        List<MallBulletinEntity> bulletinEntityList = bulletinService.queryAllByWrapper(new QueryWrapper<MallBulletinEntity>().eq("ENABLED", 1).orderByDesc("SORT"));
        return RestResponse.success().put("data", bulletinEntityList);
    }

    /**
     * 个人中心统计
     *
     * @param user 登录用户
     * @return RestResponse
     */
    @GetMapping("userCount")
    @ApiOperation(value = "个人中心统计", notes = "个人中心统计数据")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string")
    public RestResponse userCount(@LoginUser MallUserEntity user) {
        // 统计个人中心订单数据
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", user.getId());
        return RestResponse.success().put("countMap", orderService.queryUserCountMap(params));
    }

    /**
     * 获取是否开启余额支付功能
     * 1: 开启（默认）
     * 2：禁用
     *
     * @return RestResponse
     * @deprecated Use getTreaty() instead
     */
    @Deprecated
    @IgnoreAuth
    @GetMapping("getRechargeStatus")
    @ApiOperation(value = "获取是否开启余额支付功能", notes = "获取是否开启余额支付功能")
    public RestResponse getRechargeStatus() {
        return RestResponse.success().put("rechargeStatus", sysConfigService.getValue(Constant.RECHARGE_STATUS, "2"));
    }

    /**
     * 获取是否开启分销功能
     * 1: 开启（默认）
     * 2：禁用
     *
     * @return RestResponse
     * @deprecated Use getTreaty() instead
     */
    @Deprecated
    @IgnoreAuth
    @GetMapping("getDistributionStatus")
    @ApiOperation(value = "获取是否开启分销功能", notes = "获取是否开启分销功能")
    public RestResponse getDistributionStatus() {
        return RestResponse.success().put("distributionStatus", sysConfigService.getValue(Constant.DISTRIBUTION_STATUS, "2"));
    }

    /**
     * 发送短信
     *
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @IgnoreAuth
    @PostMapping("smsCode")
    @ApiOperation(value = "发送短信", notes = "发送短信验证码")
    @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
            @ExampleProperty(mediaType = "phone", value = "15209831990")
    }), required = true, dataType = "string")
    public RestResponse smsCode(@RequestBody JSONObject jsonParam) {
        String phone = jsonParam.getString("phone");
        if (!StringUtils.isMobile(phone)) {
            return RestResponse.error("请输入正确的手机号");
        }
        // 五分钟之内不能重复发送短信
        Object code = redisTemplateUtil.get(Constant.PRE_SMS + phone);
        if (!StringUtils.isNullOrEmpty(code)) {
            return RestResponse.success("短信已发送");
        }

        //生成验证码
        String smsCode = CharUtil.getRandomNum(4);
        //获取云存储配置信息
//        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
//        if (StringUtils.isNullOrEmpty(config)) {
//            return RestResponse.error("请先配置短信平台信息");
//        }
//        if (Objects.equals(config.getType(), Constant.SmsType.TX.getValue())) {
//            if (StringUtils.isNullOrEmpty(config.getAppid())) {
//                return RestResponse.error("请先配置短信平台APPID");
//            }
//            if (StringUtils.isNullOrEmpty(config.getAppkey())) {
//                return RestResponse.error("请先配置短信APP_KEY");
//            }
//            if (StringUtils.isNullOrEmpty(config.getTemplateId())) {
//                return RestResponse.error("请先配置短信templateId");
//            }
//        }
//        if (Objects.equals(config.getType(), Constant.SmsType.ALI.getValue())) {
//            if (StringUtils.isNullOrEmpty(config.getAccessKeyId())) {
//                return RestResponse.error("请先配置短信平台accessKeyId");
//            }
//            if (StringUtils.isNullOrEmpty(config.getAccessSecret())) {
//                return RestResponse.error("请先配置短信accessSecret");
//            }
//            if (StringUtils.isNullOrEmpty(config.getTemplateCode())) {
//                return RestResponse.error("请先配置短信templateCode");
//            }
//        }
//        if (StringUtils.isNullOrEmpty(config.getSign())) {
//            return RestResponse.error("请先配置短信平台签名");
//        }
//        // 短信记录
//        SysSmsLogEntity smsLogVo = new SysSmsLogEntity();
//        smsLogVo.setCode(smsCode);
//        smsLogVo.setMobile(phone);
//        smsLogVo.setStime(new Date());
//        smsLogVo.setSign(config.getSign());

        // 过期时间
        int expireTime = 15;
        redisTemplateUtil.set(Constant.PRE_SMS + phone, "1234", expireTime * 60);

        /**
         * 您的验证码是{1}，请于{2}分钟内填写。如非本人操作，请忽略本短信。
         * 暂时去掉sms
         */
        // 腾讯云短信
//        if (config.getType() == 1) {
//            SmsSingleSenderResult result = SmsUtil.crSendSms(config.getAppid(), config.getAppkey(), "86", phone, config.getTemplateId(), new String[]{smsCode, String.valueOf(expireTime)}, config.getSign());
//            smsLogVo.setTemplateId(config.getTemplateId().toString());
//            smsLogVo.setReturnMsg(result.errMsg);
//
//            if (result.result == 0) {
//                smsLogVo.setSendStatus(result.result);
//                smsLogVo.setSendId(result.sid);
//                smsLogVo.setSuccessNum(1);
//                smsLogService.save(smsLogVo);
//            } else {
//                smsLogVo.setSuccessNum(0);
//                smsLogVo.setSendStatus(1);
//                smsLogService.save(smsLogVo);
//                return RestResponse.error("短信发送失败");
//            }
//        } else {
//            // 阿里云短信
//            Map<String, Object> params = new HashMap<>();
//            params.put("code", smsCode);
//            params.put("time", expireTime);
//            SendSmsResponse response;
//            try {
//                smsLogVo.setTemplateId(config.getTemplateCode());
//                response = SmsUtil.aliSendSms(config.getAccessKeyId(), config.getAccessSecret(), phone, config.getTemplateCode(), params, config.getSign());
//                smsLogVo.setReturnMsg(response.getBody().getMessage());
//                if ("OK".equals(response.getBody().code)) {
//                    smsLogVo.setSendStatus(0);
//                    smsLogVo.setSendId(response.getBody().getBizId());
//                    smsLogVo.setSuccessNum(1);
//                    smsLogService.save(smsLogVo);
//                } else {
//                    smsLogVo.setSuccessNum(0);
//                    smsLogVo.setSendStatus(1);
//                    smsLogService.save(smsLogVo);
//                    return RestResponse.error("短信发送失败");
//                }
//            } catch (Exception e) {
//                smsLogVo.setSuccessNum(0);
//                smsLogVo.setSendStatus(1);
//                smsLogVo.setReturnMsg(e.getMessage());
//                smsLogService.save(smsLogVo);
//                return RestResponse.error("短信发送失败：" + e);
//            }
//        }
        return RestResponse.success("短信发送成功");
    }

    /**
     * 绑定手机
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("bindMobile")
    @ApiOperation(value = "绑定手机", notes = "校验验证码绑定手机")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "mobileCode", value = "1234"),
                    @ExampleProperty(mediaType = "password", value = "123456")
            }), required = true, dataType = "string")
    })
    public RestResponse bindMobile(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {

        String mobile = jsonParam.getString("mobile");
        String mobileCode = jsonParam.getString("mobileCode");
        String password = jsonParam.getString("password");

        if (!StringUtils.isMobile(mobile)) {
            return RestResponse.error("请输入正确的手机号");
        }
        Object smsCode = redisTemplateUtil.get(Constant.PRE_SMS + mobile);
        if (StringUtils.isNullOrEmpty(smsCode)) {
            return RestResponse.error("验证码已失效，请重新获取");
        }
        if (!mobileCode.equals(smsCode)) {
            return RestResponse.error("验证码错误");
        }
        MallUserEntity userVo = userService.getById(loginUser.getId());
        userVo.setMobile(mobile);
        userVo.setPassword(DigestUtils.sha256Hex(password));
        userService.update(userVo);

        //验证通过后删除redis中的验证码
        redisTemplateUtil.del(Constant.PRE_SMS + mobile);
        return RestResponse.success("手机绑定成功");
    }


    /**
     * 根据手机号修改密码
     *
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @IgnoreAuth
    @PostMapping("modifyPw")
    @ApiOperation(value = "修改密码", notes = "校验验证码修改密码，无需用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "mobileCode", value = "1234"),
                    @ExampleProperty(mediaType = "password", value = "123456")
            }), required = true, dataType = "string")
    })
    public RestResponse modifyPw(@RequestBody JSONObject jsonParam) {

        String mobile = jsonParam.getString("mobile");
        String mobileCode = jsonParam.getString("mobileCode");
        String password = jsonParam.getString("password");

        if (!StringUtils.isMobile(mobile)) {
            return RestResponse.error("请输入正确的手机号");
        }
        Object smsCode = redisTemplateUtil.get(Constant.PRE_SMS + mobile);
        if (StringUtils.isNullOrEmpty(smsCode)) {
            return RestResponse.error("验证码已失效，请重新获取");
        }
        if (!mobileCode.equals(smsCode)) {
            return RestResponse.error("验证码错误");
        }
        MallUserEntity userVo = userService.queryByMobile(mobile);
        userVo.setMobile(mobile);
        userVo.setPassword(DigestUtils.sha256Hex(password));
        userService.update(userVo);

        //验证通过后删除redis中的验证码
        redisTemplateUtil.del(Constant.PRE_SMS + mobile);
        return RestResponse.success("手机绑定成功");
    }

    /**
     * 验证码
     *
     * @param response response
     * @param uuid     uuid
     * @throws IOException
     */
    @IgnoreAuth
    @GetMapping("captcha.jpg")
    @ApiImplicitParam(paramType = "query", name = "uuid", value = "uuid", required = true, dataType = "string")
    public void captcha(HttpServletResponse response, @RequestParam String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 根据原密码修改密码
     *
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("modifyPwByOldPassword")
    @ApiOperation(value = "修改密码", notes = "校验验证码修改密码，无需用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "captcha", value = "1234"),
                    @ExampleProperty(mediaType = "key", value = "1234"),
                    @ExampleProperty(mediaType = "oldPwd", value = "1234"),
                    @ExampleProperty(mediaType = "newPwd", value = "123456")
            }), required = true, dataType = "string")
    })
    public RestResponse modifyPwByOldPassword(@LoginUser MallUserEntity userEntity, @RequestBody JSONObject jsonParam) {
        String oldPwd = jsonParam.getString("oldPwd");
        String key = jsonParam.getString("key");
        String captcha = jsonParam.getString("captcha");
        String newPwd = jsonParam.getString("newPwd");

        boolean captchaValid = sysCaptchaService.validate(key, captcha);
        if (!captchaValid) {
            return RestResponse.error("验证码不正确");
        }
        if (StringUtils.isNotBlank(userEntity.getPassword())
                && !DigestUtils.sha256Hex(oldPwd).equals(userEntity.getPassword())) {
            return RestResponse.error("旧密码不正确");
        }
        userService.update(Wrappers.<MallUserEntity>lambdaUpdate().set(MallUserEntity::getPassword, DigestUtils.sha256Hex(newPwd))
                .eq(MallUserEntity::getId, userEntity.getId()));

        return RestResponse.success("密码修改成功");
    }
}

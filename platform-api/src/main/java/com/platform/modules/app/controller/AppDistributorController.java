package com.platform.modules.app.controller;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.entpay.EntPayBankResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.*;
import com.platform.config.RedisTemplateUtil;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallDistInvitationService;
import com.platform.modules.mall.service.MallDistOrderService;
import com.platform.modules.mall.service.MallDistService;
import com.platform.modules.mall.service.MallUserBankCardService;
import com.platform.modules.oss.cloud.UploadFactory;
import com.platform.modules.sys.service.SysConfigService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author cxd
 */
@Slf4j
@RestController
@RequestMapping("/app/distributor")
@Api(tags = "AppDistributorController|分销接口")
public class AppDistributorController extends AppBaseController {
    @Autowired
    MallDistService mallDistService;
    @Autowired
    MallDistInvitationService mallDistInvitationService;
    @Autowired
    MallDistOrderService mallDistOrderService;
    @Autowired
    SysConfigService sysConfigService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private WxMaQrcodeService wxMaQrcodeService;
    @Autowired
    private MallUserBankCardService mallUserBankCardService;

    /**
     * 获取邀请信息，如果扫过邀请码，强制使用该邀请码
     *
     * @param loginUser 登录用户
     * @param code      邀请码
     * @param isShare   是否通过扫码分享
     * @return RestResponse
     */
    @GetMapping("/getInviter")
    @ApiOperation(value = "获取邀请信息", notes = "获取邀请信息，如果扫过邀请码，强制使用该邀请码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "邀请码", dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "isShare", value = "是否通过扫码分享", allowableValues = "true,false", required = true, dataType = "boolean", example = "false")
    })
    public RestResponse getInviter(@LoginUser MallUserEntity loginUser, String code, @RequestParam boolean isShare) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }

        Map<String, Object> data = new HashMap<>(8);
        data.put("inviter", null);
        data.put("disabled", false);
        data.put("user", null);
        // 已申请过了
        MallDistEntity user = mallDistService.getByUserId(userId);
        if (user != null) {
            data.put("user", user);
        }

        // 获取已绑定的上级激活码
        final List<MallDistInvitationEntity> bindInviters = mallDistInvitationService.queryAll(Collections.singletonMap("userId", userId));
        if (bindInviters.size() > 0) {
            final MallDistInvitationEntity bindInviter = bindInviters.get(0);
            data.put("code", bindInviter.getInvitationCode());
            data.put("inviter", bindInviter.getInviter());
            data.put("disabled", true);
            return RestResponse.success().put("data", data);
        }

        if (code != null) {
            final List<MallDistEntity> codeEntities = mallDistService.queryAll(Collections.singletonMap("code", code));

            // 邀请码不存在
            if (codeEntities.size() == 0) {
                return RestResponse.success().put("data", data);
            }
            final MallDistEntity codeEntity = codeEntities.get(0);

            // 如果是通过扫码分享，则绑定上级
            if (isShare) {
                final MallDistInvitationEntity invitationEntity = new MallDistInvitationEntity();
                invitationEntity.setUserId(userId);
                invitationEntity.setSuperiorId(codeEntity.getId());
                try {
                    mallDistInvitationService.add(invitationEntity);
                } catch (Exception e) {
                    // 自己扫自己的海报友好提示
                    return RestResponse.error("您已提交过申请，请勿重复提交");
                }
                data.put("disabled", true);
            }

            data.put("code", codeEntity.getInvitationCode());
            data.put("inviter", codeEntity.getNickname());
        }
        return RestResponse.success().put("data", data);
    }

    /**
     * 申请成为分销商
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     */
    @PostMapping("/apply")
    @ApiOperation(value = "申请成为分销商", notes = "申请成为分销商")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "name", value = "姓名"),
                    @ExampleProperty(mediaType = "mobile", value = "手机号"),
                    @ExampleProperty(mediaType = "mobileCode", value = "短信验证码"),
                    @ExampleProperty(mediaType = "code", value = "邀请码")
            }), required = true, dataType = "string")
    })
    public RestResponse apply(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }

        final String name = jsonParam.getString("name");
        final String mobile = jsonParam.getString("mobile");
        final String mobileCode = jsonParam.getString("mobileCode");
        final String code = jsonParam.getString("code");

        // 检查姓名是否为空
        if (StringUtils.isBlank(name)) {
            return RestResponse.error("姓名不能为空");
        }

        // 检查短信验证码
//        if (StringUtils.isBlank(mobileCode)) {
//            return RestResponse.error("验证码不能为空");
//        }
//        Object smsCode = redisTemplateUtil.get(Constant.PRE_SMS + mobile);
//        if (StringUtils.isNullOrEmpty(smsCode)) {
//            return RestResponse.error("验证码已失效，请重新获取");
//        }
//        if (!mobileCode.trim().equals(smsCode)) {
//            return RestResponse.error("验证码错误");
//        }

        // 检查是否已绑定的上级激活码
        final List<MallDistInvitationEntity> bindInviters = mallDistInvitationService.queryAll(Collections.singletonMap("userId", userId));
        if (bindInviters.size() > 0) {
            final MallDistInvitationEntity bindInviter = bindInviters.get(0);
            if (!bindInviter.getInvitationCode().equals(code)) {
                return RestResponse.error("邀请码无效");
            }
        }

        // 获取上级ID
        Integer superiorId = null;
        if (StringUtils.isNotBlank(code)) {
            final List<MallDistEntity> codeEntities = mallDistService.queryAll(Collections.singletonMap("code", code));
            if (codeEntities.size() == 0) {
                return RestResponse.error("邀请码不存在");
            }
            superiorId = codeEntities.get(0).getId();
        }

        // 新建分销商记录
        final MallDistEntity distEntity = new MallDistEntity();
        final boolean audioEnabled = "1".equals(sysConfigService.getValue(Constant.DISTRIBUTION_AUDIT, "1"));
        distEntity.setIsAudit(!audioEnabled);
        distEntity.setName(name.trim());
        distEntity.setUserId(userId);
        distEntity.setSuperiorId(superiorId);
        final long invitationCode = ThreadLocalRandom.current().nextLong(10000000000L, 100000000000L);
        distEntity.setInvitationCode(Long.toString(invitationCode));
        mallDistService.add(distEntity);

        return RestResponse.success();
    }

    /**
     * 获得分销商基本信息
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("/getDistributorInfo")
    @ApiOperation(value = "获取分销商信息", notes = "获取分销商信息")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse getDistributorInfo(@LoginUser MallUserEntity loginUser) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> data = new HashMap<>(4);
        MallDistEntity mallDistEntity = mallDistService.queryByUserId(userId);
        data.put("mallDistEntity", mallDistEntity);
        if (null == mallDistEntity) {
            return RestResponse.error("数据为空");
        }
        // 团队人数
        List<Integer> firstTeamIds = mallDistService.getFirstTeamIds(mallDistEntity.getId());
        Integer secondTeamCount = mallDistService.getSecondTeamCount(firstTeamIds);
        Integer teamCount = firstTeamIds.size() + secondTeamCount;
        data.put("teamCount", teamCount);
        // 分销订单数目
        Integer orderCount = mallDistOrderService.getOrderCount(userId).intValue();
        data.put("orderCount", orderCount);

        return RestResponse.success().put("data", data);
    }

    /**
     * 获取当日、当周、当月、当季的收益、订单数
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("/getIncomeDetailsByType")
    @ApiOperation(value = "获取当日、当周、当月、当季的收益、订单数", notes = "获取当日、当周、当月、当季的收益、订单数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：DAY:当日 WEEK:当周 MONTH:当月 QUARTER:当季", required = true, allowableValues = "DAY,WEEK,MONTH,QUARTER", dataType = "string")
    })
    public RestResponse getIncomeDetailsByType(@LoginUser MallUserEntity loginUser, @RequestParam String type) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> data = new HashMap<>(4);
        data.put("income", new BigDecimal("0.00"));
        data.put("orderCount", 0);

        Date[] result = new Date[2];

        switch (type) {
            case "DAY":
                result = DateUtils.getDayStartAndEndByDate(new Date());
                break;
            case "WEEK":
                result = DateUtils.getWeekStartAndEndByDate(new Date());
                break;
            case "MONTH":
                result = DateUtils.getMonthStartAndEnd(new Date());
                break;
            case "QUARTER":
                result = DateUtils.getQuarterStartAndEndByDate(new Date());
                break;
            default:
        }
        //收益，分销和推广，并且订单是确认收货状态
        BigDecimal earning = mallDistOrderService.getIncomeDetails(
                userId,
                new Timestamp(result[0].getTime()),
                new Timestamp(result[1].getTime())
        );

        Integer orderCount = mallDistOrderService.getOrderCount(userId, new Timestamp(result[0].getTime()), new Timestamp(result[1].getTime())).intValue();

        data.put("income", earning);
        data.put("orderCount", orderCount);

        return RestResponse.success().put("data", data);
    }

    /**
     * 获取当月收益、当日收益
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("/getIncomeDetails")
    @ApiOperation(value = "获取当月收益、当日收益", notes = "获取当月收益、当日收益")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse getIncomeDetails(@LoginUser MallUserEntity loginUser) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, Object> data = new HashMap<>(4);
        data.put("monthIncome", new BigDecimal("0.00"));
        data.put("dayIncome", new BigDecimal("0.00"));
        // 获取本月和当日开始、结束的时间戳
        Map<String, Long> time = DateUtils.getTime();
        //收益，分销和推广，并且订单是确认收货状态
        BigDecimal monthEarning = mallDistOrderService.getIncomeDetails(
                userId,
                new Timestamp(time.get("CUR_MONTH_START")),
                new Timestamp(time.get("CUR_MONTH_END"))
        );
        BigDecimal dayEarning = mallDistOrderService.getIncomeDetails(
                userId,
                new Timestamp(time.get("CUR_DAY_START")),
                new Timestamp(time.get("CUR_DAY_END"))
        );

        if (monthEarning != null) {
            data.put("monthIncome", monthEarning);
        }
        if (dayEarning != null) {
            data.put("dayIncome", dayEarning);
        }
        return RestResponse.success().put("data", data);
    }

    /**
     * 收益明细列表（代理、推广、佣金提现）
     *
     * @param loginUser 登录用户
     * @param type      类型：ALL:全部 PROXY:代理 SALE:推广 WITHDRAW:提现
     * @param current   当前页码
     * @param limit     每页条数
     * @return RestResponse
     */
    @GetMapping("/incomeList")
    @ApiOperation(value = "获取收益明细列表", notes = "获取收益明细列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：ALL:全部 PROXY:代理 SALE:推广 WITHDRAW:提现", required = true, allowableValues = "ALL,PROXY,SALE,WITHDRAW", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse incomeList(@LoginUser MallUserEntity loginUser, @RequestParam String type, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Page<IncomeDetailsEntity> page = new Page<>(current, limit);
        QueryWrapper<IncomeDetailsEntity> queryWrapper = new QueryWrapper<>();
        if (!Constant.ALL.equals(type)) {
            queryWrapper.eq("T.TYPE", Constant.DistOrderType.valueOf(type).getValue());
        }
        //当前用户大于0的佣金才显示
        queryWrapper.eq("T.USER_ID", userId).gt("T.INCOME", 0).orderByDesc("T.INCOME_TIME");
        //当前用户，指定类型，按照结算时间排序
        IPage<IncomeDetailsEntity> data = mallDistOrderService.getIncomeDetailsPage(page, queryWrapper);

        data.getRecords().forEach(incomeDetailsEntity -> incomeDetailsEntity.setArriveAfterDay(Integer.parseInt(sysConfigService.getValue("ALLOW_REFUND_TIME"))));

        return RestResponse.success().put("data", data);
    }

    /**
     * 提现
     *
     * @param loginUser 登录用户
     * @param jsonParam JSON格式参数
     * @return RestResponse
     * @throws WxPayException
     */
    @PostMapping("/getAmount")
    @ApiOperation(value = "提现", notes = "提现")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "type", value = "提现类型: ENT_PAY:付款到余额 PAY_BANK:付款到银行卡"),
                    @ExampleProperty(mediaType = "amount", value = "金额"),
                    @ExampleProperty(mediaType = "cardId", value = "银行卡ID"),
                    @ExampleProperty(mediaType = "phone", value = "手机号码"),
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse getAmount(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) throws WxPayException {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        String typeStr = jsonParam.getString("type");
        String phone = jsonParam.getString("phone");
        Integer type = typeStr.equals(Constant.WithdrawType.PAY_BANK.name())?Constant.WithdrawType.PAY_BANK.getValue():Constant.WithdrawType.ENT_PAY.getValue();
        BigDecimal amount = new BigDecimal(jsonParam.getString("amount"));
        String cardId = StringUtils.trimToNull(jsonParam.getString("cardId"));
        String bankNo = null;
        String trueName = null;
        String bankCode = null;

        System.out.println("====================" + cardId);
        if (type.equals(Constant.WithdrawType.PAY_BANK.getValue())) {
            if (Constant.BLANK.equals(cardId) || cardId == null) {
                throw new BusinessException("提现到银行：信息不完整。");
            } else {
                //获取银行卡信息
                MallUserBankCardEntity cardEntity = mallUserBankCardService.getCardSimpleInfoById(cardId);
                if (cardEntity == null) {
                    throw new BusinessException("银行卡信息有误！");
                }
                bankNo = cardEntity.getCardNumber();
                trueName = cardEntity.getCardName();
                bankCode = cardEntity.getBankCode();
            }
        }

        if (amount.compareTo(new BigDecimal(Constant.STR_ZERO)) <= 0) {
            throw new BusinessException("提现金额不可小于等于0");
        }

        Map<String, BigDecimal> amountMap = new HashMap<>(4);
        BigDecimal deductionAmount = amount;
        amountMap.put("amount", amount);
        amountMap.put("deductionAmount", deductionAmount);
        // 校验金额与用户额度是否合法，并计算金额
        amountMap = mallDistService.checkAmountAvailable(userId, type, amountMap);
        amount = amountMap.get("amount");
        deductionAmount = amountMap.get("deductionAmount");
        log.info("用户提取金额：" + amount + ",实际扣除金额：" + deductionAmount);

        // 添加提现订单
        final MallDistOrderEntity mallDistOrderEntity = new MallDistOrderEntity();
        String orderSn = StringUtils.generateOrderNumber();
        final boolean withdrawAuditEnabled = "1".equals(sysConfigService.getValue(Constant.WITHDRAW_AUDIT, "1"));
        mallDistOrderEntity.setAuditStatus(withdrawAuditEnabled ? Constant.AuditStatus.AUDITING.getValue() : Constant.AuditStatus.AUDIT_PASSED.getValue());
        mallDistOrderEntity.setUserId(userId);
        mallDistOrderEntity.setBuyerId(userId);
        mallDistOrderEntity.setOrderId(orderSn);
        mallDistOrderEntity.setType(Constant.DistOrderType.WITHDRAW.getValue());
        mallDistOrderEntity.setIncome(amount.negate());
        mallDistOrderEntity.setIncomeTime(new Date());
        mallDistOrderEntity.setWithdrawType(type);
        mallDistOrderEntity.setEncBankNo(bankNo);
        mallDistOrderEntity.setEncTrueName(trueName);
        mallDistOrderEntity.setPhone(phone);
        mallDistOrderEntity.setBankCode(bankCode);
        // 扣除用户佣金
        mallDistService.updateAmount(userId, deductionAmount, amount);
        if (!withdrawAuditEnabled) {
            // 不需要审核，直接请求微信方
            // 区分付款到余额与银行卡
//            if (type == 1) {
//                EntPayResult entPayResult = mallDistService.entPay(userId, amount, orderSn);
//                log.info(String.valueOf(entPayResult));
//                mallDistOrderEntity.setPaymentNo(entPayResult.getPaymentNo());
//            } else {
//                EntPayBankResult payBankResult = mallDistService.payBank(amount, orderSn, bankNo, trueName, bankCode);
//                log.info(String.valueOf(payBankResult));
//                mallDistOrderEntity.setPaymentNo(payBankResult.getPaymentNo());
//            }
        }
        // 若需要审核，待审核通过再请求微信方
        mallDistOrderService.add(mallDistOrderEntity);
        Map<String, Object> data = new HashMap<>(4);
        data.put("incomeTime", mallDistOrderEntity.getIncomeTime());
        return RestResponse.success().put("data", data);
    }

    /**
     * 分销订单列表
     *
     * @param loginUser 登录用户
     * @param type      类型：ALL:全部 YFK:已付款 YJS:已结算 YSX:已失效
     * @param current   当前页码
     * @param limit     每页条数
     * @return RestResponse
     */
    @GetMapping("/distOrderList")
    @ApiOperation(value = "分销订单列表", notes = "分销订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：ALL:全部 YFK:已付款 YJS:已结算 YSX:已失效", required = true, allowableValues = "ALL,YFK,YJS,YSX", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse distOrderList(@LoginUser MallUserEntity loginUser, @RequestParam String type, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }

        Page<MallDistOrderDetailEntity> page = new Page<>(current, limit);
        // 查询条件
        QueryWrapper<MallDistOrderDetailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.eq("T.USER_ID", userId).gt("T.INCOME", 0).isNotNull("T.COMMISSION_TYPE");
        List<Integer> orderStatus = new ArrayList<>();
        switch (type) {
            case "ALL":
                break;
            case "YFK":
                orderStatus.add(Constant.OrderStatus.YFK.getValue());
                orderStatus.add(Constant.OrderStatus.YFH.getValue());
                queryWrapper = queryWrapper.in("O.ORDER_STATUS", orderStatus);
                break;
            case "YJS":
                orderStatus.add(Constant.OrderStatus.QRSH.getValue());
                queryWrapper = queryWrapper.in("O.ORDER_STATUS", orderStatus);
                break;
            case "YSX":
                orderStatus.add(Constant.OrderStatus.TK.getValue());
                orderStatus.add(Constant.OrderStatus.THTK.getValue());
                queryWrapper = queryWrapper.in("O.ORDER_STATUS", orderStatus);
                break;
            default:
        }

        //当前用户，指定类型
        IPage<MallDistOrderDetailEntity> data = mallDistOrderService.distOrderPage(page, queryWrapper);

        data.getRecords().forEach(r -> r.setBuyerNickname(mallDistService.dealNickname(r.getBuyerNickname())));

        return RestResponse.success().put("data", data);
    }

    /**
     * 我的分销团队列表
     *
     * @param loginUser 登录用户
     * @param type      类型：FL:一级团队 SL:二级团队
     * @param id        分销商id
     * @param current   当前页码
     * @param limit     每页条数
     * @return RestResponse
     */
    @GetMapping("/myDistTeam")
    @ApiOperation(value = "我的分销团队列表", notes = "我的分销团队列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "分销商id", required = true, example = "0", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：FL:一级团队 SL:二级团队", required = true, allowableValues = "FL,SL", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse myDistTeam(@LoginUser MallUserEntity loginUser, @RequestParam String type, @RequestParam(defaultValue = "0") Integer id, @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }

        //获取分销商id
        Integer distId;
        if (Constant.FL.equals(type)) {
            distId = mallDistService.queryByUserId(userId).getId();
        } else {
            //二级团队，使用上级分销商id
            distId = id;
        }
        Page<MallDistTeamEntity> page = new Page<>(current, limit);
        QueryWrapper<MallDistTeamEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.eq("SUPERIOR_ID", distId);

        //当前用户，指定类型，按照结算时间排序
        IPage<MallDistTeamEntity> data = mallDistService.teamPage(page, queryWrapper);

        if (data.getRecords().size() > 0 && StringUtils.isNotBlank(type) && Constant.SL.equals(type)) {
            //二级团队，处理nickname，统一头像s
            data.getRecords().forEach(r -> {
                r.setNickname(mallDistService.dealNickname(r.getNickname()));
                r.setHeadImgUrl(null);
                r.setTeamCount(0);
            });
        }

        return RestResponse.success().put("data", data);
    }

    /**
     * 获取推广信息
     *
     * @param loginUser 登录用户
     * @return RestResponse
     */
    @GetMapping("/getInvitationInfo")
    @ApiOperation(value = "获取推广二维码", notes = "获取推广二维码")
    @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    public RestResponse getInvitationInfo(@LoginUser MallUserEntity loginUser) {
        String userId = loginUser.getId();
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException("用户为空!");
        }
        Map<String, String> data = new HashMap<>(8);
        MallDistEntity mallDistEntity = mallDistService.getInvitationInfo(userId);
        String invitationCode = mallDistEntity.getInvitationCode();
        data.put("userId", mallDistEntity.getUserId());
        data.put("nickname", mallDistEntity.getNickname());
        data.put("invitationCode", invitationCode);
        data.put("headImgUrl", mallDistEntity.getHeadImgUrl());
        String page = "pages/dcenter/apply/apply";
        // 获取小程序码
        try {
            byte[] maQrcodeBytes = wxMaQrcodeService.createWxaCodeUnlimitBytes(invitationCode, page, false, "release", 430, false, new WxMaCodeLineColor("0", "0", "0"), false);

            String maQrcodeUrl = UploadFactory.build().uploadSuffix(maQrcodeBytes, ".png");
            System.out.println(maQrcodeUrl);
            data.put("qrCodeUrl", maQrcodeUrl);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取小程序码失败！");
        }

        return RestResponse.success().put("data", data);
    }
}

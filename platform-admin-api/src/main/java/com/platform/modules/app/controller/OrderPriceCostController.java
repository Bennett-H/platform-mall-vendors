package com.platform.modules.app.controller;


import com.platform.common.CommonRestResult;
import com.platform.cost.IPromotionPirceCal;
import com.platform.cost.model.acct.AcctModel;
import com.platform.cost.rule.impl.MyOrderRule;
import com.platform.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/price")
@Api(tags = "订单价格计算")
public class OrderPriceCostController extends AbstractController {

    @Autowired
    public MyOrderRule rule ;

    @Autowired
    public IPromotionPirceCal promotionPirceCal;

    @PostMapping("/priceCost")
    @ApiOperation(value = "计算价格", notes="计算价格")
    @ApiImplicitParams({

    })
    public CommonRestResult priceCost(@RequestBody AcctModel acctModel){

        //校验
        promotionPirceCal.CostOrderPrice(acctModel);

        return CommonRestResult.success(acctModel);
    }
}

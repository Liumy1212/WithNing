package com.lmy.withning.controller;

import com.lmy.withning.common.Result.Result;
import com.lmy.withning.pojo.entity.ExchangeApply;
import com.lmy.withning.pojo.entity.ExchangeRecord;
import com.lmy.withning.pojo.entity.Product;
import com.lmy.withning.pojo.vo.ExchangeApplyVo;
import com.lmy.withning.service.MallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mall")
@Slf4j
public class MallController {

    @Autowired
    private MallService mallService;

    @GetMapping("/products")
    public Result getProducts() {
        log.info("获取商品列表");
        List<Product> list = mallService.getProducts();
        return Result.success(list);
    }

    @GetMapping("/products/{productId}")
    public Result getProductById(@PathVariable Long productId) {
        log.info("获取商品详情，productId: {}", productId);
        Product product = mallService.getProductById(productId);
        return Result.success(product);
    }

    @PostMapping("/products")
    public Result addProduct(@RequestBody Product product) {
        log.info("添加商品");
        mallService.addProduct(product);
        return Result.success();
    }

    @DeleteMapping("/products/{productId}")
    public Result deleteProduct(@PathVariable Long productId) {
        log.info("删除商品，productId: {}", productId);
        mallService.deleteProduct(productId);
        return Result.success();
    }

    @PutMapping("/products/{productId}/online")
    public Result onlineProduct(@PathVariable Long productId) {
        log.info("上架商品，productId: {}", productId);
        mallService.onlineProduct(productId);
        return Result.success();
    }

    @PutMapping("/products/{productId}/offline")
    public Result offlineProduct(@PathVariable Long productId) {
        log.info("下架商品，productId: {}", productId);
        mallService.offlineProduct(productId);
        return Result.success();
    }

    @PostMapping("/exchange/apply/{productId}")
    public Result applyExchange(@PathVariable Long productId) {
        log.info("申请兑换商品，productId: {}", productId);
        mallService.applyExchange(productId);
        return Result.success();
    }

    @GetMapping("/exchange/my-applies")
    public Result getMyExchangeApplies() {
        log.info("获取我的兑换申请");
        List<ExchangeApply> list = mallService.getMyExchangeApplies();
        return Result.success(list);
    }

    @GetMapping("/exchange/applies")
    public Result getExchangeApplies() {
        log.info("获取兑换申请列表");
        List<ExchangeApplyVo> list = mallService.getExchangeApplies();
        return Result.success(list);
    }

    @PutMapping("/exchange/{applyId}/review")
    public Result reviewExchangeApply(@PathVariable Long applyId, @RequestBody Map<String, Integer> request) {
        log.info("审核兑换申请，applyId: {}", applyId);
        mallService.reviewExchangeApply(applyId, request.get("status"));
        return Result.success();
    }

    @GetMapping("/exchange/records")
    public Result getExchangeRecords() {
        log.info("获取兑换记录");
        List<ExchangeRecord> list = mallService.getExchangeRecords();
        return Result.success(list);
    }
}

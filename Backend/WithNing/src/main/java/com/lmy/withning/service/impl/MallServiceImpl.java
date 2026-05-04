package com.lmy.withning.service.impl;

import com.lmy.withning.mapper.AdminMapper;
import com.lmy.withning.mapper.MallMapper;
import com.lmy.withning.pojo.entity.ExchangeApply;
import com.lmy.withning.pojo.entity.ExchangeRecord;
import com.lmy.withning.pojo.entity.PointRecord;
import com.lmy.withning.pojo.entity.Product;
import com.lmy.withning.pojo.vo.ExchangeApplyVo;
import com.lmy.withning.service.MallService;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MallServiceImpl implements MallService {

    @Autowired
    private MallMapper mallMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Product> getProducts() {
        log.info("获取商品列表");
        return mallMapper.getProducts();
    }

    @Override
    public Product getProductById(Long productId) {
        log.info("获取商品详情，productId: {}", productId);
        return mallMapper.getProductById(productId);
    }

    @Override
    public void addProduct(Product product) {
        log.info("添加商品");
        product.setPublisherId(UserContextHolder.getUser().getId());
        mallMapper.addProduct(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("删除商品，productId: {}", productId);
        mallMapper.deleteProduct(productId);
    }

    @Override
    public void onlineProduct(Long productId) {
        log.info("上架商品，productId: {}", productId);
        mallMapper.onlineProduct(productId);
    }

    @Override
    public void offlineProduct(Long productId) {
        log.info("下架商品，productId: {}", productId);
        mallMapper.offlineProduct(productId);
    }

    @Override
    public void applyExchange(Long productId) {
        log.info("申请兑换商品，productId: {}", productId);
        Product product = mallMapper.getProductById(productId);
        if (product != null) {
            ExchangeApply exchangeApply = new ExchangeApply();
            exchangeApply.setUserId(UserContextHolder.getUser().getId());
            exchangeApply.setProductId(productId.intValue());
            exchangeApply.setProductName(product.getName());
            exchangeApply.setPrice(product.getPrice());
            mallMapper.applyExchange(exchangeApply);
        }
    }

    @Override
    public List<ExchangeApply> getMyExchangeApplies() {
        log.info("获取我的兑换申请");
        Integer userId = UserContextHolder.getUser().getId();
        return mallMapper.getMyExchangeApplies(userId);
    }

    @Override
    public List<ExchangeApplyVo> getExchangeApplies() {
        log.info("获取兑换申请列表");
        return mallMapper.getExchangeApplies();
    }

    @Override
    public void reviewExchangeApply(Long applyId, Integer status) {
        log.info("审核兑换申请，applyId: {}, status: {}", applyId, status);
        Integer reviewerId = UserContextHolder.getUser().getId();
        mallMapper.reviewExchangeApply(applyId, status, reviewerId);

        if (status == 1) {
            ExchangeApply exchangeApply = mallMapper.getExchangeApplyById(applyId);
            if (exchangeApply != null) {
                mallMapper.deductUserPoints(exchangeApply.getUserId(), exchangeApply.getPrice());
                mallMapper.decreaseProductStock(exchangeApply.getProductId());

                PointRecord pointRecord = new PointRecord();
                pointRecord.setUserId(exchangeApply.getUserId());
                pointRecord.setPoints(-exchangeApply.getPrice());
                pointRecord.setDescription("兑换商品：" + exchangeApply.getProductName());
                pointRecord.setType(3);
                adminMapper.addPointRecord(pointRecord);

                ExchangeRecord exchangeRecord = new ExchangeRecord();
                exchangeRecord.setUserId(exchangeApply.getUserId());
                exchangeRecord.setProductId(exchangeApply.getProductId());
                exchangeRecord.setProductName(exchangeApply.getProductName());
                exchangeRecord.setPrice(exchangeApply.getPrice());
                mallMapper.addExchangeRecord(exchangeRecord);
            }
        }
    }

    @Override
    public List<ExchangeRecord> getExchangeRecords() {
        log.info("获取兑换记录");
        Integer userId = UserContextHolder.getUser().getId();
        return mallMapper.getExchangeRecords(userId);
    }
}

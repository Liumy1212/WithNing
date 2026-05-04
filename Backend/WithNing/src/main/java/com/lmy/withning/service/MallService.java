package com.lmy.withning.service;

import com.lmy.withning.pojo.entity.ExchangeApply;
import com.lmy.withning.pojo.entity.ExchangeRecord;
import com.lmy.withning.pojo.entity.Product;
import com.lmy.withning.pojo.vo.ExchangeApplyVo;

import java.util.List;

public interface MallService {
    List<Product> getProducts();
    Product getProductById(Long productId);
    void addProduct(Product product);
    void deleteProduct(Long productId);
    void onlineProduct(Long productId);
    void offlineProduct(Long productId);
    void applyExchange(Long productId);
    List<ExchangeApply> getMyExchangeApplies();
    List<ExchangeApplyVo> getExchangeApplies();
    void reviewExchangeApply(Long applyId, Integer status);
    List<ExchangeRecord> getExchangeRecords();
}

package com.lmy.withning.mapper;

import com.lmy.withning.pojo.entity.ExchangeApply;
import com.lmy.withning.pojo.entity.ExchangeRecord;
import com.lmy.withning.pojo.entity.Product;
import com.lmy.withning.pojo.vo.ExchangeApplyVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MallMapper {
    @Select("SELECT * FROM product WHERE status = 1")
    List<Product> getProducts();

    @Select("SELECT * FROM product WHERE id = #{productId}")
    Product getProductById(Long productId);

    @Insert("INSERT INTO product(name, description, price, image_url, stock, status, publisher_id, create_time) VALUES(#{name}, #{description}, #{price}, #{imageUrl}, #{stock}, 1, #{publisherId}, NOW())")
    void addProduct(Product product);

    @Delete("DELETE FROM product WHERE id = #{productId}")
    void deleteProduct(Long productId);

    @Update("UPDATE product SET status = 1 WHERE id = #{productId}")
    void onlineProduct(Long productId);

    @Update("UPDATE product SET status = 2 WHERE id = #{productId}")
    void offlineProduct(Long productId);

    @Insert("INSERT INTO exchange_apply(user_id, product_id, product_name, price, status, create_time) VALUES(#{userId}, #{productId}, #{productName}, #{price}, 2, NOW())")
    void applyExchange(ExchangeApply exchangeApply);

    @Select("SELECT * FROM exchange_apply WHERE user_id = #{userId}")
    List<ExchangeApply> getMyExchangeApplies(Integer userId);

    @Select("SELECT ea.*, u.nickname as userNickname, p.name as productName FROM exchange_apply ea JOIN user u ON ea.user_id = u.id JOIN product p ON ea.product_id = p.id")
    List<ExchangeApplyVo> getExchangeApplies();

    @Select("SELECT * FROM exchange_apply WHERE id = #{applyId}")
    ExchangeApply getExchangeApplyById(Long applyId);

    @Update("UPDATE exchange_apply SET status = #{status}, reviewer_id = #{reviewerId}, review_time = NOW() WHERE id = #{applyId}")
    void reviewExchangeApply(@Param("applyId") Long applyId, @Param("status") Integer status, @Param("reviewerId") Integer reviewerId);

    @Update("UPDATE user SET points = points - #{points} WHERE id = #{userId}")
    void deductUserPoints(@Param("userId") Integer userId, @Param("points") Integer points);

    @Update("UPDATE product SET stock = stock - 1 WHERE id = #{productId}")
    void decreaseProductStock(Integer productId);

    @Insert("INSERT INTO exchange_record(user_id, product_id, product_name, price, exchange_time) VALUES(#{userId}, #{productId}, #{productName}, #{price}, NOW())")
    void addExchangeRecord(ExchangeRecord exchangeRecord);

    @Select("SELECT er.*, p.name as productName FROM exchange_record er JOIN product p ON er.product_id = p.id WHERE er.user_id = #{userId}")
    List<ExchangeRecord> getExchangeRecords(Integer userId);
}

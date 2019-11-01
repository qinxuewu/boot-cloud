package com.github.inventory.mapper;
import com.github.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
  * 功能描述: 库存数量Mapper
  * @author: qinxuewu
  * @date: 2019/11/1 9:51
  * @since 1.0.0
  */
@Mapper
public interface ProductInventoryMapper {

	/**
	 * 更新库存数量
	 * @param pro 商品库存
	 */
	@Update("update product_inventory set inventory_cnt=#{pro.inventoryCnt} where product_id=#{pro.productId}")
	void updateProductInventory(@Param("pro") ProductInventory pro);
	
	/**
	 * 根据商品id查询商品库存信息
	 * @param productId 商品id
	 * @return 商品库存信息
	 */
	@Select("select product_id as 'productId',inventory_cnt as 'inventoryCnt' from product_inventory where product_id=#{productId}")
	ProductInventory findProductInventory(@Param("productId") Integer productId);
	
}

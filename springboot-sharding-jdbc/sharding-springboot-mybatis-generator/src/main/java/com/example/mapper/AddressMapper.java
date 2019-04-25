package com.example.mapper;


import com.example.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  25/4/2019 下午 6:01
 * @email 870439570@qq.com
 */
@Mapper
public interface AddressMapper {
	/**
	 * 保存
	 */
	void save(Address address);
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	Address get(Long id);
}

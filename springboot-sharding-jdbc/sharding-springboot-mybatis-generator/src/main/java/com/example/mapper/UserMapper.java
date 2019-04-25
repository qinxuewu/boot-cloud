package com.example.mapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  25/4/2019 下午 6:01
 * @email 870439570@qq.com
 */
@Mapper
public interface UserMapper {
	/**
	 * 保存
	 */
	void save(User user);
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	User get(Long id);
}

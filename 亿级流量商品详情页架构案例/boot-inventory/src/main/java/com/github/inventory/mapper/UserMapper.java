package com.github.inventory.mapper;
import com.github.inventory.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;


/**
 * 功能描述: 用户信息mapper
 * @author: qinxuewu
 * @date: 2019/11/1 9:51
 * @since 1.0.0
 */
@Mapper
public interface UserMapper {


	@Select("select name,age from user")
	 List<User> findUserInfo();
	
}

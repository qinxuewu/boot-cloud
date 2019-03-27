package com.example.repository;
import com.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  26/3/2019 下午 3:04
 * @email 870439570@qq.com
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor {

    /**
     * 自动生成查询sql语句
     * @param userName
     * @return
     */
    User findByUserName(String userName);
    User findByUserNameOrPassWord(String userName, String passWord);

    /**
     * 分页查询
     * @param pageable
     * @return
     */;
    Page<User> findByUserName(String userName,Pageable pageable);


    /**
     * 自定义SQL 类似hql语句 使用类名查询
     * 查询方法上面用@Query注解
     * 删除和修改在需要加上@Modifying
     */
    @Transactional
    @Modifying
    @Query("update User u set u.userName = ?1 where u.id = ?2")
    int updateUserByuserNameAndId(String  userName, Integer id);


    @Transactional
    @Modifying
    @Query("delete from User where userName=?1")
    void deleteByUserName(String userName);


    //@Transactional对事物的支持，查询超时的设置
    @Transactional(timeout = 5)
    @Query("select u from User u where u.userName = ?1")
    User findByEmailAddress(String userName);

    //原生sql查询语句
    @Query(nativeQuery =true,value = "select * from sys_user where user_name= :userName")
    List<User> getListSql(@Param("userName") String userName);


    @Query(nativeQuery =true,value = "select id,user_name from sys_user where user_name= :userName")
    List<Map<String,Object>> getListSqlObj(@Param("userName") String userName);


}

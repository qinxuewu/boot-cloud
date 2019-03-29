package com.example;
import com.example.entity.User;
import com.example.repository.UserRepository;;
import com.example.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootEhcaheApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    public void save() {
        for (int i = 0; i < 20; i++) {
            User user=new User();
            user.setUserName("test1"+i);
            user.setPassWord("123456"+i);
            user.setCreateTime(new Date());
            user.setStatus(1);
            userRepository.save(user);
        }
    }

    @Test
    public void findA() {
        User user=userRepository.findByUserNameOrPassWord("test1", "123456");
        System.err.println("条件查询对象："+user.toString());

        System.err.println("查询所有："+userRepository.findAll().size());
        List<User> list= userRepository.getListSql("test1");
        System.err.println("原生sql查询："+list.size());


        List<Map<String,Object>> listMaps= userRepository.getListSqlObj("test1");
        System.err.println("原生sql查询2："+listMaps.size());
        listMaps.forEach(map->{
            System.err.println("userName:"+map.get("user_name")+",id:"+map.get("id"));
        });

    }


    /**
     * 分页查询
     * @throws Exception
     */
    @Test
    public void pageQuery() throws Exception {
        /**
         * page：当前页码
         * size：每页获取的条数
         * direction：排序方式，ASC、DESC
         * properties：排序的字段
         * sort：排序对象
         */
        Pageable pageable = PageRequest.of(1,10, Sort.Direction.DESC, "id");
        Page<User> result = userRepository.findAll(pageable);
        System.err.println("总条数："+result.getTotalElements());
        System.err.println("总页数："+result.getTotalPages());
        System.err.println("每页显示的条数："+result.getPageable().getPageSize());
        System.err.println("当前页数："+result.getPageable().getPageNumber());

        List<User> list=result.getContent();
        for (int i = 0; i <list.size() ; i++) {
            System.err.println(list.get(i).toString());
        }
        userRepository.findByUserName("test1",pageable);

    }


    /**
     * 动态条件查询
     * @throws Exception
     */
    @Test
    public void specificationQuery()  {
        String name="test1";
        int status=1;
        Specification<User> query = new Specification<User>() {


            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //用于暂时存放查询条件的集合
                List<Predicate> predicatesList = new ArrayList<>();

                //--------------------------------------------
                //查询条件示例
                //equal示例
                predicatesList.add(cb.equal(root.get("status"),status));

                //like示例
                if (!StringUtils.isEmpty(name)){
                    Predicate nickNamePredicate = cb.like(root.get("userName"), '%'+name+'%');
                    predicatesList.add(nickNamePredicate);
                }

                //between示例
                predicatesList.add( cb.between(root.get("id"), 1, 10));



                //排序示例(先根据学号排序，后根据姓名排序)
                query.orderBy(cb.desc(root.get("id")));


                //最终将查询条件拼好然后return
                Predicate[] predicates = new Predicate[predicatesList.size()];
                return cb.and(predicatesList.toArray(predicates));
            }
        };
        List<User> list= userRepository.findAll(query);
        list.forEach(user->{
            System.err.println(user.toString());
        });

    }

    @Test
    public void testSave() {
        //第一次查询，不存在会查数据库 。然后缓存。
        String userName="test19";
        User user1= userService.select(userName);
        System.err.println("user1:"+user1.toString());

        //二次查询 直接走缓存
        User user2= userService.select(userName);
        System.err.println("user2:"+user2.toString());


        //更新数据，并删除缓存
        userService.update(userName,11);

        //在查询数据， 先走db 在存缓存
        User user3 =userService.select(userName);
        System.err.println("user3:"+user3.toString());



    }




}

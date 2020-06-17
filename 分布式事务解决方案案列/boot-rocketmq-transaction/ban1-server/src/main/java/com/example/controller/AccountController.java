package com.example.controller;
import com.example.server.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;


/**
 *
 * 功能描述: 扣减账户余额
 * @author: qinxuewu
 * @date: 2019/11/25 17:27
 * @since 1.0.0
 */
@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /*** 转账接口*/
    @RequestMapping("decrease")
    public String decrease(@RequestParam("userId") Long userId, @RequestParam("toUserId") Long toUserId,
                           @RequestParam("money") Double money){
        String txNo= UUID.randomUUID().toString();
        // 发z转账事务消息至MQ中
        accountService.sendUpdateAccontChange(userId,toUserId,money,txNo);
        return "转账接口 请求成功.....";
    }

}

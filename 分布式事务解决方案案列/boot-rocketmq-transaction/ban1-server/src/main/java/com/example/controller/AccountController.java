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

    /**
     * 转账接口
     * @param userId 用户id
     * @param money 金额
     * @return
     */
    @RequestMapping("decrease")
    public String decrease(@RequestParam("userId") Long userId, @RequestParam("toUserId") Long toUserId,@RequestParam("money") Double money){
        String txNo= UUID.randomUUID().toString();
        accountService.sendUpdateAccontChange(userId,toUserId,money,txNo);
        return "转账接口 请求成功..... 预计最晚明天12点前到账";
    }
}

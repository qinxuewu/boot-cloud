package com.github.controller;
import com.github.server.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;


/**
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
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     * @return
     */
    @RequestMapping("decrease")
    public String decrease(@RequestParam("userId") Long userId,@RequestParam("money") BigDecimal money){
        accountService.decrease(userId,money);
        return "扣减账户余额 success";
    }
}

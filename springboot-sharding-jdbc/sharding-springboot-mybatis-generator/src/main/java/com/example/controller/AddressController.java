package com.example.controller;
import com.example.entity.Address;
import com.example.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * @author qinxuewu
 * @version 1.00
 * @time  25/4/2019 下午 6:26
 * @email 870439570@qq.com
 */
@Controller
public class AddressController {

	@Autowired
	private AddressMapper addressMapper;

	@RequestMapping("/address/save")
	@ResponseBody
	public String save() {
		for (int i = 0; i <10 ; i++) {
			Address address=new Address();
			address.setCode("code_"+i);
			address.setName("name_"+i);
			address.setPid(i+"");
			address.setType(0);
			address.setLit(i%2==0?1:2);
			addressMapper.save(address);
		}

		return "success";
	}
	
	@RequestMapping("/address/get/{id}")
	@ResponseBody
	public Address get(@PathVariable Long id) {
		return addressMapper.get(id);
	}
}

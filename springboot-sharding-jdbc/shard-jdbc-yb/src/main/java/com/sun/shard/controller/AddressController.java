package com.sun.shard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.shard.bean.Address;
import com.sun.shard.service.IAddressService;
@Controller
public class AddressController {
	
	@Autowired
	private IAddressService addressService;
	
	@RequestMapping("/address/save")
	@ResponseBody
	public String save(Address address) {
		addressService.save(address);
		return "success";
	}
	
	@RequestMapping("/address/get/{id}")
	@ResponseBody
	public Address get(@PathVariable Long id) {
		return addressService.get(id);
	}
}

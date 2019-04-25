package com.sun.shard.service;

import com.sun.shard.bean.Address;

public interface IAddressService {
	void save(Address address);

	Address get(Long id);
}

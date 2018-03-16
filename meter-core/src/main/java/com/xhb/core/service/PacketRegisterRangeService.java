package com.xhb.core.service;

import java.util.List;

import com.xhb.core.entity.PacketRegisterRange;

public interface PacketRegisterRangeService extends ApplicationService {

	List<PacketRegisterRange> findAll();

}

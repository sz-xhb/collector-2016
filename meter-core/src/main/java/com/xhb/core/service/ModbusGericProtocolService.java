package com.xhb.core.service;

import java.util.List;

import com.xhb.core.entity.ModbusGericProtocol;

public interface ModbusGericProtocolService extends ApplicationService {

	List<ModbusGericProtocol> findAll();

}

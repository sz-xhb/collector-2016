package com.xhb.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.ModbusGericProtocolDAO;
import com.xhb.core.entity.ModbusGericProtocol;
import com.xhb.core.service.ModbusGericProtocolService;

@Service
@Transactional
public class ModbusGericProtocolServiceImpl extends ApplicationServiceImpl implements ModbusGericProtocolService {

	@Resource
	private ModbusGericProtocolDAO modbusGericProtocolDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return modbusGericProtocolDAO;
	}
	@Override
	public List<ModbusGericProtocol> findAll() {
		return modbusGericProtocolDAO.findAll();
	}

}

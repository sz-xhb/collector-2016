package com.xhb.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.PacketRegisterRangeDAO;
import com.xhb.core.entity.PacketRegisterRange;
import com.xhb.core.service.PacketRegisterRangeService;

@Service
@Transactional
public class PacketRegisterRangeServiceImpl extends ApplicationServiceImpl
		implements PacketRegisterRangeService {

	@Resource
	private PacketRegisterRangeDAO packetRegisterRangeDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return packetRegisterRangeDAO;
	}
	@Override
	public List<PacketRegisterRange> findAll() {
		return packetRegisterRangeDAO.findAll();
	}

}

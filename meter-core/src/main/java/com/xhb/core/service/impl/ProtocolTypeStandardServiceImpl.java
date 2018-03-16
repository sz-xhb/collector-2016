package com.xhb.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.ProtocolTypeStandardDAO;
import com.xhb.core.entity.ProtocolTypeStandard;
import com.xhb.core.service.ProtocolTypeStandardService;

@Service
@Transactional
public class ProtocolTypeStandardServiceImpl extends ApplicationServiceImpl implements ProtocolTypeStandardService {

	@Resource
	private ProtocolTypeStandardDAO protocolTypeStandardDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return protocolTypeStandardDAO;
	}
	@Override
	public List<ProtocolTypeStandard> getAll() {
		return protocolTypeStandardDAO.getAll();
	}

}

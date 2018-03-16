package com.xhb.core.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public class Services {

	@Resource
	public CompanyServcie companyServcie ;

	@Resource
	public CustomerService customerService;
	
	@Resource 
	public ReceiptCollectorService receiptCollectorService;
	
	@Resource 
	public ReceiptMeterService ReceiptMeterService;
	
	@Resource
	public ReceiptCircuitService receiptCircuitService;
	
	@Resource
	public DataElectricityService dataElectricityService;
	
	@Resource
	public MeterStatusService meterStatusService;
	
	@Resource
	public DataHarmonicService dataHarmonicService;
	
	@Resource
	public DataSwitchService dataSwitchService;
	
	@Resource
	public DataTemperatureService dataTemperatureService;
	
	@Resource
	public CollectorStatusService collectorStatusService;
	
	@Resource
	public DataSteamService dataSteamService;
	
	@Resource
	public DataWaterService dataWaterService;
	
	@Resource
	public DataAmHarmService dataAmHarmService;
	
	@Resource
	public DataRateService dataRateService;
	
	@Resource
	public DataElectricity3PhaseService dataElectricity3PhaseService;

	@Resource
	public DataElecOilService dataElecOilService;
	
	@Resource
	public ModbusGericProtocolService modbusGericProtocolService;
	
	@Resource
	public PacketRegisterRangeService packetRegisterRangeService;
	
	@Resource
	public ElecParamAnalyseService elecParamAnalyseService;
	
	@Resource
	public DataLingbuService dataLingbuService;
	
	@Resource
	public ProtocolTypeStandardService protocolTypeStandardService;
	
	@Resource
	public BuildInfoService buildInfoService;
}

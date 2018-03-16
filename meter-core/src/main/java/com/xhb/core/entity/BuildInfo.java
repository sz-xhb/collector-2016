package com.xhb.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the build_info database table.
 * 
 */
@Entity
@Table(name="build_info")
@NamedQuery(name="BuildInfo.findAll", query="SELECT r FROM BuildInfo r")
public class BuildInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4807340547528376604L;

	@Id
	@Column(name="id")
	private String buildNO;
	
	@Column(name="name")
	private String buildName;
	
	@Column(name="code_type")
	private String codeType;
	
	@Column(name="city_code")
	private String cityCode;
	
	@Column(name="city")
	private String city;
	
	@Column(name="area")
	private Long area;
	
	@Column(name="air_area")
	private Long airArea;
	
	@Column(name="build_type")
	private String buildType;
	
	@Column(name="quale")
	private String quale;
	
	@Column(name="address")
	private String address;
	
	@Column(name="years")
	private Long years;
	
	@Column(name="owner")
	private String owner;
	
	@Column(name="Tenement")
	private String tenement;
	
	@Column(name="floor")
	private Long floor;
	
	@Column(name="up_floor")
	private Long upFloor;
	
	@Column(name="under_floor")
	private Long underFloor;
	
	@Column(name="floor_height")
	private Long floorHeight;
	
	@Column(name="height")
	private Long height;
	
	@Column(name="heating_area")
	private Long heatingArea;
	
	@Column(name="heating_ways")
	private String heatingWays;
	
	@Column(name="shape_factor")
	private String shapeFactor;
	
	@Column(name="structural_style")
	private String structuralStyle;
	
	@Column(name="material_outwall")
	private String materialOutwall;
	
	@Column(name="Insulation_outwall")
	private String insulationOutwall;
	
	@Column(name="Insulation_roof")
	private String insulationRoof;
	
	@Column(name="window_type")
	private String windowType;
	
	@Column(name="glass_type")
	private String glassType;
	
	@Column(name="material_windowframe")
	private String materialWindowframe;
	
	@Column(name="collection_frequency")
	private String collectionFrequency;
	
	@Column(name="description")
	private String description;
	
	@Column(name="img_url")
	private String imgUrl;

	public String getBuildNO() {
		return buildNO;
	}

	public void setBuildNO(String buildNO) {
		this.buildNO = buildNO;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	

	

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public String getQuale() {
		return quale;
	}

	public void setQuale(String quale) {
		this.quale = quale;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTenement() {
		return tenement;
	}

	public void setTenement(String tenement) {
		this.tenement = tenement;
	}

	public String getHeatingWays() {
		return heatingWays;
	}

	public void setHeatingWays(String heatingWays) {
		this.heatingWays = heatingWays;
	}

	public String getShapeFactor() {
		return shapeFactor;
	}

	public void setShapeFactor(String shapeFactor) {
		this.shapeFactor = shapeFactor;
	}

	public String getStructuralStyle() {
		return structuralStyle;
	}

	public void setStructuralStyle(String structuralStyle) {
		this.structuralStyle = structuralStyle;
	}

	public String getMaterialOutwall() {
		return materialOutwall;
	}

	public void setMaterialOutwall(String materialOutwall) {
		this.materialOutwall = materialOutwall;
	}

	public String getInsulationOutwall() {
		return insulationOutwall;
	}

	public void setInsulationOutwall(String insulationOutwall) {
		this.insulationOutwall = insulationOutwall;
	}

	public String getInsulationRoof() {
		return insulationRoof;
	}

	public void setInsulationRoof(String insulationRoof) {
		this.insulationRoof = insulationRoof;
	}

	public String getWindowType() {
		return windowType;
	}

	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}

	public String getGlassType() {
		return glassType;
	}

	public void setGlassType(String glassType) {
		this.glassType = glassType;
	}

	public String getMaterialWindowframe() {
		return materialWindowframe;
	}

	public void setMaterialWindowframe(String materialWindowframe) {
		this.materialWindowframe = materialWindowframe;
	}

	public String getCollectionFrequency() {
		return collectionFrequency;
	}

	public void setCollectionFrequency(String collectionFrequency) {
		this.collectionFrequency = collectionFrequency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getArea() {
		return area;
	}

	public void setArea(Long area) {
		this.area = area;
	}

	public Long getAirArea() {
		return airArea;
	}

	public void setAirArea(Long airArea) {
		this.airArea = airArea;
	}

	public Long getYears() {
		return years;
	}

	public void setYears(Long years) {
		this.years = years;
	}

	public Long getFloor() {
		return floor;
	}

	public void setFloor(Long floor) {
		this.floor = floor;
	}

	public Long getUpFloor() {
		return upFloor;
	}

	public void setUpFloor(Long upFloor) {
		this.upFloor = upFloor;
	}

	public Long getUnderFloor() {
		return underFloor;
	}

	public void setUnderFloor(Long underFloor) {
		this.underFloor = underFloor;
	}

	public Long getFloorHeight() {
		return floorHeight;
	}

	public void setFloorHeight(Long floorHeight) {
		this.floorHeight = floorHeight;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getHeatingArea() {
		return heatingArea;
	}

	public void setHeatingArea(Long heatingArea) {
		this.heatingArea = heatingArea;
	}
	
}

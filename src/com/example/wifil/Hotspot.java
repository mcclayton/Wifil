package com.example.wifil;

public class Hotspot {
	
	private String SSID = null;
	private String Mac = null;
	private double lat = 0;
	private double lon = 0;
	private double radius;
	
	public Hotspot(String SSID, String Mac, double lat, double lon, double radius) {
		this.SSID = SSID;
		this.Mac = Mac;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
	}
	
	public String getSSID() {return this.SSID;} 
	public String getMac() {return this.Mac;} 
	public double getLat() {return this.lat;} 
	public double getLon() {return this.lon;} 
	public double getRadius() {return this.radius;} 
	
	public void setSSID(String SSID) {this.SSID = SSID;} 
	public void setMac(String Mac) {this.Mac = Mac;} 
	public void setLat(double lat) {this.lat = lat;} 
	public void setLon(double lon) {this.lon = lon;} 
	public void setRadius(double radius) {this.radius = radius;} 
}

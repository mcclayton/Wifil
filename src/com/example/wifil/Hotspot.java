package com.example.wifil;

public class Hotspot {
	
	private String SSID = null;
	private String Mac = null;
	private long lat = 0;
	private long lon = 0;
	private long radius;
	
	public Hotspot(String SSID, String Mac, long lat, long lon, long radius) {
		this.SSID = SSID;
		this.Mac = Mac;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
	}
	
	public String getSSID() {return this.SSID;} 
	public String getMac() {return this.Mac;} 
	public long getLat() {return this.lat;} 
	public long getLon() {return this.lon;} 
	public long getRadius() {return this.radius;} 
	
	public void setSSID(String SSID) {this.SSID = SSID;} 
	public void setMac(String Mac) {this.Mac = Mac;} 
	public void setLat(long lat) {this.lat = lat;} 
	public void setLon(long lon) {this.lon = lon;} 
	public void setRadius(long radius) {this.radius = radius;} 
}

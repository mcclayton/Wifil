package com.example.wifil;

public class Hotspot {
	
	private String SSID = null;
	private String Mac = null;
	private double lat = 0;
	private double lon = 0;
	private double radius = 0;
	private int isPublic = 0;
	
	public Hotspot(String SSID, String Mac, double lat, double lon, double radius, int isPublic) {
		this.SSID = SSID;
		this.Mac = Mac;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
		this.isPublic = isPublic;
	}
	
	public String getSSID() {return this.SSID;} 
	public String getMac() {return this.Mac;} 
	public double getLat() {return this.lat;} 
	public double getLon() {return this.lon;} 
	public double getRadius() {return this.radius;} 
	public int getIsPublic() {return this.isPublic;} 
	
	public void setSSID(String SSID) {this.SSID = SSID;} 
	public void setMac(String Mac) {this.Mac = Mac;} 
	public void setLat(double lat) {this.lat = lat;} 
	public void setLon(double lon) {this.lon = lon;} 
	public void setRadius(double radius) {this.radius = radius;} 
	public void setIsPublic(int isPublic) {this.isPublic = isPublic;} 
}

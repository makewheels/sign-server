/**
  * Copyright 2019 bejson.com 
  */
package sign.bean.position;

/**
 * Auto-generated: 2019-02-07 23:47:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Coords {

	private double latitude;
	private double longitude;
	private int accuracy;
	private int altitude;
	private String heading;
	private int speed;
	private int altitudeAccuracy;

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getHeading() {
		return heading;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setAltitudeAccuracy(int altitudeAccuracy) {
		this.altitudeAccuracy = altitudeAccuracy;
	}

	public int getAltitudeAccuracy() {
		return altitudeAccuracy;
	}

}
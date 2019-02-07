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
public class Position {

	private String coordsType;
	private Address address;
	private String addresses;
	private Coords coords;
	private long timestamp;

	public void setCoordsType(String coordsType) {
		this.coordsType = coordsType;
	}

	public String getCoordsType() {
		return coordsType;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setCoords(Coords coords) {
		this.coords = coords;
	}

	public Coords getCoords() {
		return coords;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
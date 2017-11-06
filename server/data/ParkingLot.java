package server.data;

import com.google.gson.annotations.SerializedName;

public class ParkingLot {
	@SerializedName( "id" ) public int lotID;
	public String latitude;
	public String longitude;
	public String description;
	
}

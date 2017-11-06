package server.forms;

import java.util.ArrayList;

import server.data.ParkingLot;

public class LotInformationRequest extends JSONRequest {
	public static String myAction = "lotInfoRequest";
	public String latitude;
	public String longitude;
	
	public LotInformationRequest() {
		super(myAction);
	}

	@Override
	public JSONResponse getResponder() {
		ArrayList<ParkingLot> lots = new ArrayList<ParkingLot>();
		// Query the database for nearby lots
		
		return new LotInformationResponse(lots);
	}
}

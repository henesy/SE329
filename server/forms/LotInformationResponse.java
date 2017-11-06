package server.forms;

import java.util.ArrayList;
import java.util.List;

import server.data.ParkingLot;

public class LotInformationResponse extends JSONResponse {
	public static String myAction = "lotInfoResponse";
	public List<ParkingLot> lots = new ArrayList<ParkingLot>();
	
	public LotInformationResponse(List<ParkingLot> lots) {
		super(myAction);
		this.lots = lots;
	}
}

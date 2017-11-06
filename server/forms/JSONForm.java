package server.forms;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;


public abstract class JSONForm {
	public String action = "";
	
	public JSONForm(String action) {
		this.action = action;
	}
	
	public String toJSON() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(this);
	}
}

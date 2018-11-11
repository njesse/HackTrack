/*
 * 
 * {

    
    "data": [
        { 
            "id": "",
            "time": "";
            [x,y,z]
        },
        { 
            "id": "",
            "time": "";
            "x": "",
            "y": "",
            "z": ""
        },
         { 
            "id": "",
            "time": "";
            "x": "",
            "y": "",
            "z": ""
        }
    ] 
}
 * 
 * Usage:
 * RestInterface = new RestInterface("modus");
 * RestInterface.addDataset(id,time,x,y,z);
 * RestInterface.addDataset(id,time,x,y,z);
 * something like result = RestInterface.send(); 
 * 
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;




public class RestInterface {
	private String model ="";
	private String modelURL ="";
	JSONArray allLines = new JSONArray();
	
	
	public RestInterface(String givenModel) {
		this.model = givenModel;		 
	}
	
	public RestInterface() {
		// TODO Auto-generated constructor stub
	}

	public void setModel(String newModel)
	{
		this.model = newModel;
		if (newModel.equals("tm")) {
			modelURL = "http://localhost:3030/api/v1/predict/model1";
		}
		else if (newModel.equals("mm"))
		{
			modelURL = "http://localhost:3030/api/v1/predict/model2";
		}
		
	}
	
	public String getModel()
	{
		return this.model;
	}
	
	public void readFileContent(String Filestring)
	{
		String[] lines = Filestring.split(System.getProperty("line.separator"));
		for (int i  = 1;i<lines.length;i++)
		{
			String line = lines[i];
			String[] entrys = line.split(",");
			if (entrys.length>=5)
			{
			addDataset(entrys[0],entrys[1],entrys[2],entrys[3],entrys[4]);
			}
		}
		
	}

	public void addDataset(String id, String time, String x, String y, String z) {
		
		 JSONObject obj = new JSONObject();
		 JSONArray array = new JSONArray();
		 array.add(new Double(x));
		 array.add(new Double(y));
		 array.add(new Double(z));
		 obj.put("values", array);
		obj.put("id",new Double(id));
		obj.put("time",time);
		allLines.add(obj);
		
	}

	
	public String dontGetValues() {
		// debugging: use the json to be send as result
		return allLines.toJSONString();
	}
	
	public String sendValues() {

		Client client = Client.create();
        
		WebResource webResource = client
		   .resource(modelURL);

		String input = allLines.toJSONString();

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			     + response.getStatus());
		}

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);

	    return output;
		
	}

	
	
	

}

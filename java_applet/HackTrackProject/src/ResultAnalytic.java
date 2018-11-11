import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ResultAnalytic {

	private String starttime ="";
	private int startIndex;
	private int endIndex;
	private String endtime = "";
	private double bestAccuracy = 0;
	DefaultTableModel model = new DefaultTableModel();
	private ArrayList<ResultLine> results = new ArrayList();
	
	public DefaultTableModel gettableModel() {
		model.setColumnCount(4);		
		model.setColumnIdentifiers(new Object[]{"Type",  "Start", "End", "Accuracy"});
		
		String start = results.get(0).time;
	
		String type =results.get(0).activityAsString();	
		
		for (int i = 1;i<results.size();i++)
		{
			String currentType = results.get(i).activityAsString();	
			if (!currentType.equals(type))
			{
				// Type changed, fill one row with old type
				model.addRow(new Object[] {type,start,results.get(i-1).time,results.get(i-1).accuracy });
				// set values for new type
				start=results.get(i).time;
				type =results.get(i).activityAsString();	
			}
		}
		// finish the last line
		int lastindex= results.size()-1;
		model.addRow(new Object[] {type,start,results.get(lastindex).time,results.get(lastindex).accuracy });
		
		
		
		return model;
	}

	public Double getBestAccuracy() {
		return bestAccuracy;
	}
	
	public String getStarttime()
	{
		return starttime;
	}
	
	public String getEndtime()
	{
		return endtime;
	}
	
	public int getStartIndex()
	{
		return startIndex;
	}
	
	public int getEndIndex()
	{
		return endIndex;
	}
	
	
	
	public ResultAnalytic(String restResult) {
		boolean trainfound = false;
		int lastTrain;
		JSONArray JSONArray_inner = new JSONArray();
		JSONParser parser = new JSONParser(); 
		try {
			JSONObject jsonObject_outer = (JSONObject) parser.parse(restResult);
			
		//	System.out.println(jsonObject);
			
			 JSONArray_inner = (JSONArray) jsonObject_outer.get("output");
           
			
			
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		 if (JSONArray_inner.size()>0)
		 {
			 for (int i = 0; i<JSONArray_inner.size();i++)
			 {
				 
				 JSONObject entry_outer = (JSONObject) JSONArray_inner.get(i);
				 JSONObject entry_inner = (JSONObject) entry_outer.get("readingVal"+(i+1));
				 int act = 0;
				 switch ((String)entry_inner.get("Act")) {
				 case "walk":
					 act = 2;
					 break;
				 case "train":
					 act = 1;
					 break;
				 case "bus":
					 act = 3;				 
				 }
				 if (act == 1 )
				 {
					 if (!trainfound)
					 {
					 trainfound = true;
					 starttime = (String)entry_inner.get("timestamp");
					 startIndex = i;
					 }
					 
						 endtime= (String)entry_inner.get("timestamp");
						 endIndex = i;
					  
				 }		
				 		 
				 double accuracy = (Double)entry_inner.get("accuracy");
				 if (accuracy>bestAccuracy)
				 {
					 bestAccuracy=accuracy;
				 }
				 
				 ResultLine line = new ResultLine(act,(Double)entry_inner.get("accuracy"),(String)entry_inner.get("timestamp"));
				 results.add(line);
			 }
		 } //*/
		 // Generate Test-Data:
	/*for (int i = 0;i<20;i++) {
		  results.add(new ResultLine(0,30,"time"));	
		}
		for (int i = 0;i<20;i++) {
			  results.add(new ResultLine(1,30,"time"));	
			}
		for (int i = 0;i<20;i++) {
			  results.add(new ResultLine(2,30,"time"));	
			}
		for (int i = 0;i<20;i++) {
			  results.add(new ResultLine(1,90,"time"));	
			}
		for (int i = 0;i<30;i++) {
			  results.add(new ResultLine(2,30,"time"));	
			}
		for (int i = 0;i<20;i++) {
			  results.add(new ResultLine(1,80,"time"));	
			}
		for (int i = 0;i<30;i++) {
			  results.add(new ResultLine(2,60,"time"));	
			}
		
		starttime = "30/10/2018 19:32:02";
		startIndex = 20;
		endIndex = 80;
		endtime ="30/10/2018 21:32:02"; //*/
				
		// put restResult in ResultLines
	
		// find first and last appereance of train
	}
	
	private int getIndexAtPercent(double percent)
	{
	
		int numberofvalues = results.size();
		
		int index = (int) Math.floor(percent * numberofvalues / 100);
		if (index >= numberofvalues)
		{
			index = numberofvalues-1;
		}
			
		return index;
	}
	

	public Color getColorAtPercent(double percent)
	{
		
	 ResultLine result = results.get(getIndexAtPercent(percent));

	 if (result.activity==0) {
		 return Color.WHITE;
	 }
	 if (result.activity==1) {
		 Color c = new Color(255,(int)Math.floor(255-result.accuracy/100*255),(int)Math.floor(255-result.accuracy/100*255));
		 return c;
	 }
	 if (result.activity==2) {
		 Color c = new Color((int)Math.floor(255-result.accuracy/100*255),255,(int)Math.floor(255-result.accuracy/100*255));
		 return c;
	 }
	 if (result.activity==3) {
		 Color c = new Color((int)Math.floor(255-result.accuracy/100*255),(int)Math.floor(255-result.accuracy/100*255),255);
		 return c;
	 }
	 
	 return Color.BLACK;
	}

}

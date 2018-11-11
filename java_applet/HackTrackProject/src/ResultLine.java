
public class ResultLine {

	  public double accuracy;
	  public String time;
	  
  public ResultLine(int act, double acc, String timec) {
		// TODO Auto-generated constructor stub
	  this.activity=act;
	  this.accuracy=acc;
	  this.time=timec;
	}
  public int activity;
  
  public String activityAsString() {
	 switch(this.activity)
	 {
	 case 1:
		 return "Train";
	 case 2:
		 return "Walk";
	 case 3:
		 return "Bus/Car";
	 }
	 return "undefined";
  }
  
  // activity 0: undefined
  // 1: train
  // 2: walk
  // 3: bus/car
  
  

}

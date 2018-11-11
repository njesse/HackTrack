
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
  
  // activity 0: undefined
  // 1: train
  // 2: walk
  // 3: bus/car
  
  

}

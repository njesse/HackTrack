import java.awt.EventQueue;

public class start {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hacktrackGUI window = new hacktrackGUI();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

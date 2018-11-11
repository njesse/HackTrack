
public class TestRestInterface {

	public static void main(String[] args) {
		// TOhO Auto-generated method stub
		RestInterface client = new RestInterface("modus");
		client.addDataset("123","12:45","0.1","0.5","-3.2");
		client.addDataset("143","12:45","0.1","0.5","-3.2");
		client.addDataset("163","12:45","0.1","0.5","-3.2");
		client.addDataset("173","12:45","0.1","0.5","-3.2");
		client.sendValues();
	}

}

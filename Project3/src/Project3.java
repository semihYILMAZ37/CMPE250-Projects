import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Project3 {

	public static void main(String[] args) throws IOException {
		Scanner line;
		File input = new File(args[0]);
		FileWriter f_writer = new FileWriter(args[1]);
		Scanner reader = new Scanner(input);
		String list[];
		int list2[] = new int[21];
		String id, flightCode, accCode, departureAirportCode, arrivalAirportCode;
		HashMap<String,ACC> ACCList = new HashMap<String,ACC>(); 
		int addmissionTime;
		
		line = new Scanner(reader.nextLine());
		int A = line.nextInt();
		int F = line.nextInt();
		for(int i = 0;i<A;i++) {
			line = new Scanner(reader.nextLine());
			id = line.next("[A-Z]+");
			list = line.nextLine().strip().split(" ");
			ACCList.put(id,new ACC(id, list));
			line.close();
		}
		for(int j = 0;j<F;j++) {
			line = new Scanner(reader.nextLine());
			addmissionTime = line.nextInt();
			flightCode = line.next();
			accCode = line.next();
			departureAirportCode = line.next(); 
			arrivalAirportCode = line.next();
			list = line.nextLine().strip().split(" ");
			for(int k =0;k<21;k++) {
				list2[k] = Integer.valueOf(list[k]);
			}
			ACCList.get(accCode).addFlight(new Flight(addmissionTime, flightCode, departureAirportCode, arrivalAirportCode, list2.clone()));
			line.close();
		}
		for(ACC element : ACCList.values()) {
			System.out.println(element.getId() + " " + element.ACCEndTime() + element.HashTable());
			f_writer.write(element.getId() + " " + element.ACCEndTime() + element.HashTable() + "\n");
		}
		reader.close();
		f_writer.close();
	}

}

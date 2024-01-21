import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ACC {

	private String id;
	private HashMap<String, Airport> airports = new HashMap<String, Airport>();
	//private ArrayList<Flight> allFlights = new ArrayList<Flight>();
	//private LinkedList<Flight> readyQueue = new LinkedList<Flight>();
	//private PriorityQueue<POFlights> waitingQueue = new PriorityQueue<POFlights>();
	//private PriorityQueue<POAirports> workingAirports = new PriorityQueue<POAirports>();
	private PriorityQueue<Flight> flights = new PriorityQueue<Flight>();
	
	int busyUntil;
	Time time;
	private Flight currentProcess;
	private Airport[] airportHashTable;
	
	public ACC(String id, String[] airports) {
		time = new Time();
		airportHashTable = new Airport[1000];
		this.id = id;
		for(int i = 0;i<airports.length;i++) {
			this.airports.put(airports[i], airportHashTable[hash(airports[i])]);
		}
		busyUntil = 0;
	}
	
	public void addFlight(Flight flight) {
		flights.add(flight);
	}
	
	
	public int ACCEndTime() {
		while (!flights.isEmpty()) {

			currentProcess = flights.poll();
			String flightCode = currentProcess.getFlightCode();
			int lastOperationTime = currentProcess.getLastOperationTime();
			
			if(currentProcess.finished()) {
				//System.out.println(busyUntil + " | " + busyUntil + " | " + flightCode + " | Finished | 0");
				continue;
			}
			
			
			String operationPlace = currentProcess.operationPlace();
			
			// Waiting operations (seems like done)
			if(operationPlace==null) {
				//System.out.println(lastOperationTime + " | " + lastOperationTime + " | " + flightCode + " | " + "waiting | " + (currentProcess.getLastOperationTime()-lastOperationTime));
			}
			// ACC operations 
			else if(operationPlace.compareTo("acc")==0) {
				int startTime = Math.max(lastOperationTime, busyUntil);
				int duration = currentProcess.getAccDuration();
				int realDuration = duration>30 ? 30 : duration;
				busyUntil = startTime + realDuration;
				//System.out.println(lastOperationTime + " | " + startTime + " | " + flightCode + " | " + id + " Running | " + duration);
				currentProcess.setLastOperationTime(busyUntil);
			}
			// ATC operations
			else {
				Airport airport = airports.get(operationPlace);
				int startTime = Math.max(lastOperationTime, airport.getBusyUntil());
				int duration = currentProcess.getAtcDuration();
				airport.setBusyUntil(startTime+duration);
				//System.out.println(lastOperationTime + " | " + startTime + " | " + flightCode + " | " + airport.getAirportID() + " Running | " + duration);
				currentProcess.setLastOperationTime(airport.getBusyUntil());
			}
			
//			if(!currentProcess.finished()) {
				flights.add(currentProcess);
//			}
				
		}
		return busyUntil;
	}
	
	
	public String getId() {
		return id;
	}
	
	public String HashTable() {
		String string = "";
		String number = "";
		for(int i = 0;i<airportHashTable.length;i++) {
			if(airportHashTable[i]!=null) {
				if(i==0) {
					number = "000";
				}
				else if(i<10)
					number = "00"+i;
				else if(i<100) {
					number = "0" +i;
				}
				else {
					number = "" + i;
				}
				string = string + " " + airportHashTable[i].getAirportID() + number;
			}
		}
		return string;
	}
	
	private int hash(String airportID) {
		//int i = airportID.hashCode()%1000;
		double i = 0;
		for(int j=0;j<airportID.length();j++) {
			i = i + airportID.charAt(j) * Math.pow(31, j);
		}
		int hash = (int) (i%1000);
		while(true) {
			if(airportHashTable[hash]==null) {
				airportHashTable[hash] = new Airport(airportID, this, time);
				break;
			}
			hash = (hash+1)%1000;
		}
		//System.out.println(airportID + " " + hash);
		return hash;
	}
	
//	private class POAirports implements Comparable<POAirports>{
//		public Airport airport;
//		public int time;
//		
//		public POAirports(Airport airport, int time) {
//			this.airport = airport;
//			this.time = time;
//		}
//		
//		@Override
//		public int compareTo(POAirports o) {
//			// TODO Auto-generated method stub
//			if(time>o.time) {
//				return 1;
//			}
//			else if(time<o.time) {
//				return -1;
//			}
//			return 0;
//		}
//	}
	
//	private class POFlights implements Comparable<POFlights>{
//	public Flight flight;
//	public int time;
//	
//	public POFlights(Flight flight, int time) {
//		this.flight = flight;
//		this.time = time;
//	}
//	
//	@Override
//	public int compareTo(POFlights o) {
//		if(time>o.time) {
//			return 1;
//		}
//		else if(time<o.time) {
//			return -1;
//		}
//		return flight.compareTo(o.flight);
//	}
//}
	
}

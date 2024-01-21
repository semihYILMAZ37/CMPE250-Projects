import java.util.LinkedList;
import java.util.PriorityQueue;

public class Airport {

	private String airportID, accID, atcID;
	private int busyUntil;
	private Time time;
	private ACC acc;
	
	
	private LinkedList<Flight> readyQueue = new LinkedList<Flight>();
	private PriorityQueue<POFlights> waitingQueue = new PriorityQueue<POFlights>();
	private Flight currentProcess;

	public Airport(String airportID, ACC acc, Time time) {
		this.airportID = airportID;
		this.acc = acc;
		this.accID = acc.getId();
//		atcID = hash(accID, airportID);
		this.time = time;
		busyUntil = 0;
	}
	
//	private String hash(String accID,String airportID) {
//		return "";
//	}
//	
//	private int hash(String airportID) {
//		
//		return 0;
//	}
	
//	public int ATCMain() {
//		//System.out.println("--- " + time.getTime() + " " + airportID + " is called");
//		//System.out.println(time.getTime()+" " + airportID );
//		while (!waitingQueue.isEmpty()) {
//			if (waitingQueue.peek().time==time.getTime()) {
//				readyQueue.addLast(waitingQueue.remove().flight);
//			}
//			else {
//				break;
//			}
//		}
//			
//		if(busyUntil==time.getTime()&&(currentProcess != null)) {
//			finishProcess();
//		}
//		
//		if((busyUntil<=time.getTime())&&(!readyQueue.isEmpty())) {
//			// start the process
//			Flight flight = readyQueue.pop();
//			System.out.println(time.getTime() + " " +flight.getFlightCode() + " started at "+ airportID);
//			busyUntil = time.getTime() + flight.getProcessDuration();
//			currentProcess = flight;
//		}
//
//		int nextWaitingOperation = waitingQueue.isEmpty() ? 2000000 : waitingQueue.peek().time ; 
//		int nextOperation = currentProcess==null ? 2000000 : busyUntil;
//		return Math.min(nextOperation,nextWaitingOperation);
//		// maybe check for whether ready queue is empty or not
//	}
//	
//	private void finishProcess(){
//		if(currentProcess==null) {
//			System.out.println("current is null");
//			return;
//		}
//		// no need to get duration but processDuration handles some operations
//		int duration = currentProcess.processDuration(time.getTime());
//		if(currentProcess.transferToAcc()) {
//			currentProcess.nextStep();
//			acc.transferTo(currentProcess);
//			System.out.println(time.getTime() + " " + currentProcess.getFlightCode() + " transfered back to the ATC from " + airportID);
//		}
//		else {
//			System.out.println(time.getTime() + " " + currentProcess.getFlightCode() + " pushed to waiting at " + airportID );
//			currentProcess.nextStep();
//			waitingQueue.add(new POFlights(currentProcess, currentProcess.getWaitingDuration()+time.getTime()));
//		}
//		currentProcess = null;
//	}
//	
	
	private class POFlights implements Comparable<POFlights>{
		public Flight flight;
		public int time;
		
		public POFlights(Flight flight, int time) {
			this.flight = flight;
			this.time = time;
		}
		
		@Override
		public int compareTo(POFlights o) {
			if(time>o.time) {
				return 1;
			}
			else if(time<o.time) {
				return -1;
			}
			return flight.getFlightCode().compareTo(o.flight.getFlightCode());
		}
	}
	
	public int getBusyUntil() {
		return busyUntil;
	}

	public void setBusyUntil(int busyUntil) {
		this.busyUntil = busyUntil;
	}
	
//	public void transferTo(Flight flight) {
//		//readyQueue.addLast(flight);
//		waitingQueue.add(new POFlights(flight, time.getTime()));
//	}

	public String getAirportID() {
		return airportID;
	}
}
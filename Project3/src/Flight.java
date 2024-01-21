
public class Flight implements Comparable<Flight> {

	public int addmissionTime;
	private String flightCode, departureAirportCode, arrivalAirportCode;
	private int timings[];
	private boolean timeSliced;
	private int currentStep;
	private int lastOperationTime;
	
	public Flight(int addmissionTime, String flightCode, String departureAirportCode,String arrivalAirportCode,
			int[] list) {
		this.addmissionTime = addmissionTime;
		this.flightCode = flightCode;
		this.departureAirportCode = departureAirportCode;
		this.arrivalAirportCode = arrivalAirportCode;
		this.timings = list;
		timeSliced = false;
		currentStep = 0;
		lastOperationTime = addmissionTime;
	}

	@Override
	public int compareTo(Flight o) {
		
		if(this.lastOperationTime>o.lastOperationTime) {
			return 1;
		}
		else if (this.lastOperationTime<o.lastOperationTime) {
			return -1;
		}
		else {
			if(this.timeSliced&&!o.timeSliced) {
				return 1;
			}
			else if(!this.timeSliced&&o.timeSliced) {
				return -1;
			}
			return this.flightCode.compareTo(o.flightCode);
		}
	}
	
	public String operationPlace() {
		if((currentStep==1)||(currentStep==4)||(currentStep==6)||(currentStep==8)||(currentStep==11)||(currentStep==14)||(currentStep==16)||(currentStep==18)||(currentStep==1)) {
			lastOperationTime = lastOperationTime + timings[currentStep];
			currentStep++;
			timeSliced = false;
			return null;
		}
		else if((currentStep==0)||(currentStep==2)||(currentStep==10)||(currentStep==12)||(currentStep==20)) {
			return "acc";
		}
		else if((currentStep==3)||(currentStep==5)||(currentStep==7)||(currentStep==9)) {
			return departureAirportCode;
		}
		else if((currentStep==13)||(currentStep==15)||(currentStep==17)||(currentStep==19)) {
			return arrivalAirportCode;
		}
		else {
			System.out.println("iswaitingstep error");
			return "something is wrong";
		}
	}
	
	
	///////////////// time sliced might need a solution
	
	
	public int getAtcDuration() {
		int duration = timings[currentStep];
		currentStep++;
		return duration;
	}
	
	public int getAccDuration() {
		int duration = timings[currentStep];
		if(duration>30) {
			timeSliced = true;
			timings[currentStep] = timings[currentStep] - 30;
		}
		else {
			currentStep++;
			timeSliced = false;
		}
		return duration;
	}
	
	public boolean finished() {
		if(currentStep ==21)
			return true;
		else
			return false;
	}
	
	public String getFlightCode() {
		return flightCode;
	}
	
	public int getLastOperationTime() {
		return lastOperationTime;
	}
	
	public void setLastOperationTime(int lastOperationTime) {
		this.lastOperationTime = lastOperationTime;
	}
	
	///////////////////////////////////////
//	
//	// returns and process
//	public int getWaitingDuration() {
//		timeSliced = false;
//		int duration = timings[currentStep];
//		currentStep++;
//		lastOperationTime = lastOperationTime + duration;
//		return duration;
//	}
//	
//
//	// just returns
//	public String getNextAirport() {
//		if(currentStep ==2)
//			return departureAirportCode;
//		else if(currentStep ==12)
//			return arrivalAirportCode;
//		else
//			return null;
//	}
//	
//	// no need for this for now
//	// returns and process
//	public String transferToNextAirport() {
//		
//		return departureAirportCode;
//	}
//	
//	
//	// returns and updates the time processed
//	public int processDuration(int time) {
//		lastOperationTime = time;
//		timeSliced = true;
//		int duration = timings[currentStep];
//		//currentStep++;
//		return duration;
//	}
//	
//	public void nextStep() {
//		currentStep++;
//	}
//	
//	public boolean transferToAcc() {
//		if(currentStep ==9)
//			return true;
//		else if(currentStep ==19)
//			return true;
//		else
//			return false;
//	}
	
}
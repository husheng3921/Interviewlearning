import java.util.Date;

/**
 * Chesses
 */
public class Cheese {

    public static final Cheese cheese=new Cheese();
	private final long produceTimes;
	private static final long produceDate =new Date(119,8,1).getTime();
	
	
	private Cheese() {
		produceTimes=new Date().getTime()-produceDate;
	}
	
	public long produceTimes() {
		return produceTimes;
	}
	
	public static void main(String[] args) {
		System.out.println("current time in day(from 1900:00:00) : "+new Date().getTime()/(1000*60*60*24L));
		
		System.out.println("cheese had produces : "+ cheese.produceTimes()/(1000*60*60*24L) +" days");
		
	}
}
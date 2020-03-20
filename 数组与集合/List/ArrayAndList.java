import java.util.ArrayList;
import java.util.List;

/**
 * ArrayAndList
 */
public class ArrayAndList {
    private static final int batch = 100000;
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		insert2Array();
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		insert2List();
		System.out.println(System.currentTimeMillis()-end);		
	}
	
	static void insert2Array() {
		String[] nums = new String[batch];
		for(int i = 0;i < batch;i++) {
			nums[i]=""+i;
		}
	}
	
	static void insert2List() {
		List<String> nums=new ArrayList<String>();
		for(int i=0;i<batch;i++) {
			nums.add(""+i);
		}
      }
}
package List;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * ArrayAndList
 */
public class ArrayAndList {
    private static final int batch = 10;
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		insert2Array();
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		System.out.println("*****************");
		insert2List();
		System.out.println("*******");	
		List<String> sList = new LinkedList<>();
		insert3List();
		testList();	
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
		ListIterator<String> itr = nums.listIterator(0);
		while (itr.hasNext()) {
			System.out.println("index="+itr.nextIndex());
			System.out.println("value="+itr.next());
		}
	  }
	
	static void insert3List() {
		List<String> nums = new LinkedList<>();
		for(int i=0;i<batch;i++) {
			nums.add(""+i);
		}
		//10表示开始的索引 [0,size]
		ListIterator<String> itr = nums.listIterator(10);
		//itr.nextIndex()
		while (itr.hasPrevious()) {
			System.out.println("index="+itr.nextIndex());
			System.out.println(itr.previous());
		}
	}

	static void testList(){
		ArrayList<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		/* for(int i = 0; i <list.size(); i++){
			if( i > 1 && i <5){
				list.remove(i);
			}
		}
		for(Integer temp : list)
		{
			System.out.println(temp);
		}  //不抛出fail-fast
		*/

		for(Integer i : list){
			if( i > 1 && i <5){
				list.remove(i);
			}
		}
		//ConcurrentModificationException,说明foreach使用了Iterator
	}
}
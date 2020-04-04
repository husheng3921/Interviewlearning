import java.util.Date;

/**
 * Chesses
 * 实例的初始化讲究顺序
 * 1.static字段先设置默认值，其中cheese 被设置成null,produceDate设置成0；
 * 2.然后static初始器执行，按照声明出现的顺序执行；
 * 如果先执行cheese，调用cheese（）构造方法，此时produceDate = 0；链接(验证、准备、解析)中准备阶段
 * 如果先执行produceDate，此时值为2020-04-04，再调用cheese()构造方法
 */
public class Cheese {

    public static final Cheese cheese=new Cheese();//先执行的话，调用构造方法时，produceDate 为0
	private final long produceTimes;
	private static final long produceDate =new Date(119,8,1).getTime();
	//public static final Cheese cheese=new Cheese();// 后执行，produceDate已经初始化
	
	private Cheese() {
		System.out.println(produceDate);
		produceTimes=new Date().getTime()-produceDate;
	}
	
	public long produceTimes() {
		return produceTimes;
	}
	
	public static void main(String[] args) {
		System.out.println("current time in day(from 1900:00:00) : "+new Date().getTime()/(1000*60*60*24L));
		
		System.out.println("cheese had produces : "+ cheese.produceTimes()/(1000*60*60*24L) +" days");
		
		System.out.println(produceDate);

	}
}
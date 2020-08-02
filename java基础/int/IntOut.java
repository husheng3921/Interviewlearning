
/**
 * IntOut
 * 整数加减溢出
 */
public class IntOut {

    public static void main(String[] args) {
        int x = -2000000000;
		int z = 2000000000;
        System.out.println(x - z);
        int max=Integer.MAX_VALUE;
        System.out.println(max);
		System.out.println(Integer.toHexString(x));
		System.out.println(Integer.toHexString(z));
		System.out.println(Integer.toHexString(x-z));
    }
}
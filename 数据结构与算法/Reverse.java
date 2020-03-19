
package algorithm;

/**
 * Reverse
 * LeetCode 7 简单
 * 整数翻转，溢出返回0
 */
public class Reverse {

    public static void main(String[] args) {
        System.out.println(reveseInt(3934));
    }
    private static int reveseInt(int x){
        int res = 0;
        while (x!=0) {
            int pop = x % 10;
            x = x / 10;
            //
            if(res > Integer.MAX_VALUE/10 || res == Integer.MAX_VALUE/10 && pop > Integer.MAX_VALUE%10) return 0;
            if(res < Integer.MIN_VALUE/10 || res == Integer.MIN_VALUE/10 && pop < Integer.MIN_VALUE%10) return 0;
            res = res*10 + pop;
        }
        return res;
    }
}



import java.util.HashMap;
import java.util.Map;

/**
 * FeiBoList 青蛙跳阶数,一次可以跳1步或2步 n =1 只有1种 n = 2 ,一次跳1阶跳两次，一次跳2阶跳一次
 */
public class FeiBoList {

    public static void main(String[] args) {
        System.out.println(solve1(4));
        System.out.println(solve2(4));
        System.out.println(solve3(4));
        System.out.println(fib1(5));
        System.out.println(fib2(5));
        System.out.println(fib3(5));
    }

    /**
     * n阶的跳法为f(n),
     * 跳到n阶可以是一步跳到n阶，对应f(n-1) n阶一次跳一步的跳法
     * 也可以是跳2步到n阶，对应n-2阶数的跳法f(n-2) n阶一次跳2步的跳法
     * 则f(n)=f(n-1)+f(n-2)
     * @param n
     * @return
     */
    private static int solve1(int n) {
        if(n <=2){
            return n;
        }else{
            return solve1(n-1)+solve1(n-2);
        }
    }

    /**
     * 递归树中有重复的计算,消耗外存储
     * @param n
     * @return
     */
    private static int solve2(int n) {
        Map<Integer, Integer> map = new HashMap<>();
        if(n <= 2){
            return n;
        }else{//判断是否计算过
            if (map.containsKey(n)) {
                return map.get(n);
            }else{
                int result = solve2(n-1)+solve2(n-2);
                map.put(n, result);
                return result;
            }
        }
    }
    /**
     * 斐波拉契数列变形
     * @param n
     * @return
     */
    private static int solve3(int n) {
        int f1 = 0;
        int f2 = 1;
        int sum = 0;
        for(int i = 1; i <= n; i++){
            sum = f1 + f2;
            f1 = f2;
            f2 = sum;
        }
        return sum;
    }

    /**
     * 斐波拉契数列 1 1 2 3 5 8
     * @param n
     * @return
     */
    private static int fib1(int n){
        if(n <= 2){
            return 1;
        }
        return fib1(n-1)+fib1(n-2);
    }

    private static int fib2(int n){
        int f1 =1, f2 =1;
        // int t = 0;
        for(int i = 3; i<=n;i++) {
            f2 = f1 + f2;// t =新f2 = f1 + 旧f2
            f1 = f2 - f1;// 新f1 = 旧f2=t-新f1
        }
        return f2;
    }

    private static int fib3(int n) {
        int[] fib = new int[n+1];
        fib[0] = 0;
        fib[1] = fib[2] = 1;
        for(int i = 3; i <=n;i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        return fib[n];
    }
}
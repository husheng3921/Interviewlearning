
package algorithm;

/**
 * QingWaTiao
 * 青蛙跳，一次可以跳1阶，也可以跳n阶
 * https://www.cnblogs.com/fanguangdexiaoyuer/p/10760135.html
 * f(1)= 1
 * f(2)=f(2-1)+f(2-2)
 * f(3)=f(3-1)+f(3-2)+f(3-3)
 * 
 * f(n)=2*f(n-1)
 */
public class QingWaTiao {

    public static void main(String[] args) {
        
        System.out.println(solve(5));
        System.out.println(solve1(5));
        System.out.println(solve2(5));
    }

    /**
     * 
     * @param n
     * @return
     */
    private static int solve(int n) {
        if(n <= 0){
            return 0;
        }else{
            return 1 << (n - 1);
        }
    }

    /**
     * 递归解决
     * @param n
     * @return
     */
    private static int solve1(int n){
        if(n <= 1){
            return n;
        }else{
            return 2*solve1(n-1);
        }
    }

    /**
     * 其实我们知道f(n)=f(n-1)+f(n-2)+.......f(n-n)
     * @param n
     * @return
     */
    private static int solve2(int n) {
        if(n<=1){
            return n;
        }
        if(n == 2){
            return 2;
        }
        if(n == 3){
            return 4;
        }
        int index = 1;
        int sum = 0;
        while(index <= n){
            sum = sum + solve2(n - index);
            index++;
        }
        return sum + 1;
    }
}
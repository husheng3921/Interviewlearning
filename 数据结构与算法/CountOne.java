
package algorithm;

/**
 * CountBit 
 * Leetcode 191 位1的个数
 */
public class CountOne {

    public static void main(String[] args) {
        System.out.println(solveOne(10000000));
        System.out.println(solveOneE(10000000));
        System.out.println(solveTwo(10000000));
        System.out.println(solveThree(10000000));
    }

    /**
     * 时间复杂度O(logn)
     * 空间复杂度O(1）
     */
    private static int solveOne(int n) {
        int bits = 0;
        while(n != 0){
            if(n%2 == 1){
                bits ++;
            }
            n = n >>1;
        }
        return bits;
    }

    /**
     * m每次获取
     * @param n
     * @return
     */
    private static int solveOneE(int n) {
        int curs = 0;
        int bits = 0;
        for(int i = 0;i < 32; i++){
            curs = n & 1; //获取当前位上值0/1
            if(curs != 0){
                bits ++;
            }
            n = n >> 1;
        }
        return bits;
    }
    /**
     * 采用掩码的思想，
     * 0000 0001 第0位为1
     * 0000 0010 第1位为1
     * 掩码每次左移1
     * 时间复杂度O(1) 32位整数，最多比较32次
     * 空间复杂度O(1)
     * @param n
     * @return
     */
    private static int solveTwo(int n){
        int bits = 0;
        int mask = 1;
        for(int i = 0;i<32;i++){
            if((n & mask )!=0 ){
                bits ++;
            }
            mask = mask << 1;
        }
        return bits;
    }

    /**
     * X=X&(X-1) 清零X的最低位的1
     * @param n
     * @return
     */
    private static int solveThree(int n) {
        int bits = 0;
        while( n != 0 ){
            bits ++;
            n = n&(n-1);
        }
        return bits;
    }
}
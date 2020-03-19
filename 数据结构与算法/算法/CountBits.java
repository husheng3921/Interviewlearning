
package algorithm;

/**
 * CountBits
 * 时间要求 O(n)
 * 思考：肯定需要递推
 */
public class CountBits {

    public static void main(String[] args) {
        
    }
    /**
     * X&(X-1)是将X的最低位1去掉，意思就是 x 的数与 X&(X-1)相差1 
     * @param n
     * @return
     */
    private static int[] counBits(int n) {
        
        int[] res = new int[n + 1];
        for(int i = 0; i < n; i++){
            res[i] = res[i &(i-1)] + 1;
        }
        return res;
    }

    /**
     * i 与 i>>1 就是差了一位，如果那最低位是1，这相差1，是0则相等
     * 可以用i&1 获取最低位
     * @param n
     * @return
     */
    private static int[] solveTwo(int n) {
        int[] res = new int[n + 1];
        for(int i = 0; i <32; i++){
            res[i] = res[i>>1] + (1 & i);
        }
        return res;
    }
}
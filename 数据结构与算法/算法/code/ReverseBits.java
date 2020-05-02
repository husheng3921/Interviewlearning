
package algorithm;

import java.lang.annotation.Retention;

/**
 * LeetCode 190 32位无符号数字位颠倒
 * ReverseBits
 */
public class ReverseBits {

    public static void main(String[] args) {
        
    }

    private static int solveOne(int n) {
        int res = 0;
        int curs = 0;
        for(int i = 0; i < 32; i++){
            curs = n&1;//获取最低位的值0/1
            //直接将该位的值移到31-i的位置
            res = res + (curs<<(31-i));
            //然后将数更新，右移1位，看次位
            n = n >> 1;
        }
        return res;
    }
}
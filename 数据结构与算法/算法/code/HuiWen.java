
package algorithm;

/**
 * HuiWen 
 * LeetCode 9 简单
 * 回文数判断
 */
public class HuiWen {

    public static void main(String[] args) {
        System.out.println(check(1239321));
        System.out.println(check2(1239321));
    }
    /**
     * 翻转后计算，比较两个值是否相等
     * 
     * @param x
     * @return
     */
    private static boolean check(int x) {
        
        if(x < 0){
            return false;
        }
        //翻转之后可能溢出
        long temp = 0;
        int init = x;
        while (x!=0) {
            int pop = x % 10;
            temp = temp*10 + pop;
            x = x / 10;
        }
        return temp == init;
    }

    /**
     * 字符串翻转
     * @param x
     * @return
     */
    private static boolean check2(int x) {
        if(x < 0){
            return false;
        }
        String str = String.valueOf(x);
        String res = new StringBuilder(str).reverse().toString();
        
        return str.equals(res);
    }

    private static boolean check3(int x) {
        if(x < 0){
            return false;
        }
        //标记获得是个，十 ，白
        //边界判断
        if (x < 0) return false;
        int div = 1;
        //
        while (x / div >= 10) div *= 10;
        while (x > 0) {
            int left = x / div;
            int right = x % 10;
            if (left != right) return false;
            x = (x % div) / 10;
            div /= 100;
        }
        return true;
    }
}
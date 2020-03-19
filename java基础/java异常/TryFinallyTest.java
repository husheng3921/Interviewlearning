
package throwable;

/**
 * TryFinallyTest
 * 无异常 101 2 10 101
 * 有异常 101 1 11 101
 */
public class TryFinallyTest {

    static int x = 1;
    static int y = 10;
    static int z = 100;
    public static void main(String[] args){
        int value = finallyReturn();
        System.out.println(value);
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
    }

    public static int finallyReturn(){
        try{
            int c = 10 / 0;
            ++x;
        }catch(Exception e){
            e.printStackTrace();
            return ++y;
        }finally{
            return ++z;
        }
    }
    
}
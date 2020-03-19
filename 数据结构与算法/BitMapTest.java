
package algorithm;

/**
 * BitMapTest
 */
public class BitMapTest {

    public static void main(String[] args) {
        //可判断[0,1000)数，1000不能
        BitMapOne bitMapOne = new BitMapOne(1000);
        bitMapOne.set(88, true);
        System.out.println(bitMapOne.get(88));
        System.out.println(bitMapOne.get(89));
    }
}
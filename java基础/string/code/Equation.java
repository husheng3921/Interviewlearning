/**
 * Equation
 */
public class Equation {

    public static void main(String[] args) {
        String a = new String("ab1");
        String b = new String("ab1");
        String aa = "ab1";
        String bb = "ab1";
        boolean c = a == b;
        boolean c1 = aa == bb;
        System.out.println(a==b);//false 
        System.out.println(a.equals(b));//true
        System.out.println(aa == bb);//true 重写== 字符比较

        Long al = 100L;
        Long bl = 100L;
        Long a1 = 300L;
        Long b1 = 300L;
        System.out.println(al == bl);//true 缓存 127
        System.out.println(a1 == b1);//false
        
    }
}
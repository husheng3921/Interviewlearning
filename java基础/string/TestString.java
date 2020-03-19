/**
 * TestString
 */
public class TestString {

    public static void main(String[] args) {
        /**
         * String对象不可变，则每次会重新生成对象，再将指针指向新对象
         * private final char value[];
         */
        String str = "husheng coding";
        System.out.println(str.hashCode());
        str = str + "23";
        System.out.println(str.hashCode());
        /**
         * StringBuffer和StringBuilder都继承AbstractStringBuilder
         * char[] value;
         * StringBuffer重写了父类的length等函数，synchronized加锁
         * 同步，线程安全
         * StringBuilder线程不安全，性能好
         */
        StringBuffer str1 = new StringBuffer("husheng coding1");
        System.out.println(str1.hashCode());
        str1.append("23");
        System.out.println(str1.hashCode());
        StringBuilder str2 = new StringBuilder("husheng coding2");
        System.out.println(str2.hashCode());
        str2.append("23");
        System.out.println(str2.hashCode());
        System.out.println(str+"-"+str1+"-"+str2);
        /**
         * -475097458
            -1302122161
            366712642
            366712642
            1829164700
            1829164700
         */
    }
}
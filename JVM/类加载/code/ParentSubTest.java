
/**
 * ParentSubTest
 */
public class ParentSubTest {

    public static void main(String[] args){
        System.out.println(Sub.B);
        // 输出2
        /**
         * 先执行父类Parent的<clinit>方法
         * <clinit>：所有类变量(staitc)和静态语句块(static{})
         */
    }
}
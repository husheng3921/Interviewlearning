
/**
 * Equation1
 */
public class Equation1 {

    public static void main(String[] args) {
        Integer num1 = 56;
        Integer num2 = 56;
        System.out.println(num1 == num2);//相等 true
        System.out.println(num1.equals(num2));// true

        Integer num3 = 34567;
        Integer num4 = 34567;
        System.out.println(num3 == num4);//false
        System.out.println(num3.equals(num4));//true

        Integer num21 = -56;
        Integer num22 = -56;
        System.out.println(num21 == num22);// true
        System.out.println(num21.equals(num22));// true 

        Integer num23 = -34567;
        Integer num24 = -34567;
        System.out.println(num23 == num24);//false
        System.out.println(num23.equals(num24));//true

        System.out.println("******************");
        String hello = "hello world!";
        String hello1 = new String("hello world!");
        System.out.println(hello == hello1); // 1 false
        String hello2 = "hello world!";
        System.out.println(hello == hello2); // 2 true
        String append = "hello" + " world!";
        System.out.println(hello == append); // 3 false
        final String pig = "length: 10";
        final String dog = "length: " + pig.length();
        System.out.println(pig == dog); // 4 false
        final String dog1 = ("length: " + pig.length()).intern();
        System.out.println(pig == dog1); // 5true
        System.out.println("Animals are equal: " + pig == dog);// 6false
    }
}
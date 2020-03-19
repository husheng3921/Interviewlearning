/**
 * Pig
 */
public class Pig extends Animal{

    @Override
    public float test(int a) {
        System.out.println("子类test");
        return 2.3f;
    }

    //报错，不能缩小可见范围
   /* @Override
    protected void test(int a){
        System.out.println("子类test");
    }*/

    public static void main(String[] args) {
        Pig pig = new Pig();
        pig.test(2);
    }
}

/**
 * TestOverload
 * 方法重载，必须保证参数不同（类型或个数）
 */
public class TestOverload {

    public void test(int a){

    }
    
    /*public float test(int a) {
        return 2.3f;
    }*/
    public int test(float b){
        
        return 1;
    }

    protected void test(){

    }

    public void test(int a, int b){

    }

    public void test(int b, float a){

    }

    public void test(int a, int b, int c) {
        
    }
    public static void main(String[] args) {
        TestOverload testOverload = new TestOverload();
        testOverload.test(2.3f);
        /**
         * 如果参数列表一样，返回类型不同，则执行下面语句
         * 编译器该如何选择
         */
        testOverload.test(2);
        /**
         * 到底是无返回值还是float
         */
    }
}
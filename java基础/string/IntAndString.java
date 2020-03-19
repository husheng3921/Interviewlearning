import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IntAndString
 * 接口原输出[60,90,100]
 * 现要求输出[60,中度,90,哈哈]
 * 类型擦除，伪泛型
 */
public class IntAndString {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        try {
            list.add(60);
            list.getClass().getMethod("add", Object.class).invoke(list, "活跃中度[1-100]");
            list.add(1500);
            list.getClass().getMethod("add", Object.class).invoke(list, "粉丝数,排名 3689 位");
                
            //todo get evaluation
            list.add(90);	
            list.getClass().getMethod("add", Object.class).invoke(list, "用户评价,超越 92%的用户");
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(list.toArray()));
    }
}
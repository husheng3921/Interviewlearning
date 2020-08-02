import java.util.ArrayList;
import java.util.List;

/**
 * SubListFailFast
 */
public class SubListFailFast {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("one");
        list.add("one2");
        list.add("one3");
        list.add("one4");
        list.add("one5");
        list.add("one6");

        List<String> slist = list.subList(0, 3);

        //不注释抛出java.util.ConcurrentModificationException
       /*  list.remove(0);
        list.add("one7");
        list.clear(); */

        slist.clear();
        slist.add("six");
        slist.add("seven");

        for(Object t : slist){
            System.out.println(t);
        }

        System.out.println(slist);
    }
}


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ReflectTest 
 * 参考
 * https://www.jianshu.com/p/9be58ee20dee
 */
public class ReflectTest {

    public static void main(String[] args) {
        try {
            //初始化静态块
            Class<?> class1 = Class.forName("Book");
            //不会链接，初始化
            //ClassLoader class2 = Book.class.getClassLoader();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
       
       /*  reflectNewInstance();
        reflectPrivateFiled();
        reflectPrivateMethod(); */
    }
    /**
     * 反射
     * 根据类名全限定查找类
     */
    public static void reflectNewInstance() {
        try {
            Class<?> clazz = Class.forName("reflect.Book");
            Object objectBook = clazz.newInstance();
            Book book = (Book) objectBook;
            book.setAuthor("husheng");
            book.setName("WHU");
            System.out.println(book.toString());
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 通过反射访问私有属性
     */
    public static void reflectPrivateFiled() {
        try {
            Class<?> clazz = Class.forName("reflect.Book");
            Object objectBook = clazz.newInstance();
            Field fieldTag = clazz.getDeclaredField("TAG");
            fieldTag.setAccessible(true);
            String tag = (String) fieldTag.get(objectBook);
            System.out.println(tag);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public static void reflectPrivateMethod() {
        try {
            Class<?> clazz = Class.forName("reflect.Book");
            Object bObject = (Book)clazz.newInstance();
            Method method = clazz.getDeclaredMethod("declaredMethod", int.class);
            method.setAccessible(true);
            String str = (String) method.invoke(bObject, 0);
            System.out.println(str);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}
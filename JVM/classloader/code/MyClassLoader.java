import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * MyClassLoader
 */
public class MyClassLoader extends ClassLoader {

    private String path;
    private String classLoaderName;

    
    public static void main(String[] args) {
        MyClassLoader classLoader = new MyClassLoader("E:\\WorkSpace\\InterviewLearning\\JVM\\classloader\\Hu.class","myClassLoader");
        try {
            Class<?> clazz = classLoader.loadClass("Hu");
            System.out.println(clazz.getClassLoader());
            System.out.println(clazz.getClassLoader().getParent());
            System.out.println(clazz.getClassLoader().getParent().getParent());
            System.out.println(clazz.getClassLoader().getParent().getParent().getParent());
            clazz.newInstance();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO Auto-generated method stub
        try {
            byte[] result = getClassFromCustomPath(name);
            if(result == null){
                throw new FileNotFoundException();
            }else{
                return defineClass(name, result, 0, result.length);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return super.findClass(name);
    }
    
    private byte[] getClassFromCustomPath(String name) {
        name = path;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(new File(name));
            out = new ByteArrayOutputStream();
            int i = 0;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }finally{
            try {
                out.close();
                in.close();
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

    public MyClassLoader(String path, String classLoaderName) {
        this.path = path;
        this.classLoaderName = classLoaderName;
    }
}
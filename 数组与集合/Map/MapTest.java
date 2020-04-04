package Map;

import java.util.HashMap;
import java.util.Map;

/**
 * MapTest
 */
public class MapTest {

    /**
     * User
     */
    public class User {
    
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + id;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            User other = (User) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (id != other.id)
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        private MapTest getEnclosingInstance() {
            return MapTest.this;
        }
        
    }
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("husheng", 26);

        Map<User, String> maps = new HashMap<>();
        User user1 = new User(1, "hh");
        User user2 = new User(1, "hh");
        maps.put(user1,"hh");
        maps.put(user2, "hh");
        
        
    }
}
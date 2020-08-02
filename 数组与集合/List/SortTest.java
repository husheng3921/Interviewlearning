import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * SortTest
 */
public class SortTest {

    /**
     * SortData
     */ 
    public class SortDTO{
        private String sortTarget;

        public SortDTO(String sortTarget) {
        this.sortTarget = sortTarget;
        }

        public String getSortTarget() {
            return sortTarget;
        }

        public void setSortTarget(String sortTarget) {
            this.sortTarget = sortTarget;
        }
        
    }

    private static void testSort() {
        List<SortDTO> sList = new ArrayList<>();
        sList.add(new SortDTO("20"));
        sList.add(new SortDTO("10"));
        sList.add(new SortDTO("2"));
        sList.add(new SortDTO("30"));
        sList.add(new SortDTO("13"));

        SortDTO[] aDtos = new [sList.size()];
        sList.toArray(aDtos);
        Arrays.sort(aDtos, Comparator.comparing(SortDTO::getSortTarget));

    }
    public static void main(String[] args) {
        
    }
}
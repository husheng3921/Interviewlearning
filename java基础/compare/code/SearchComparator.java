import java.util.Comparator;

import javax.naming.directory.SearchResult;

/**
 * SearchComparator
 * 为比较对象定义外部比较器
 */
public class SearchComparator implements Comparator<SearchResult> {

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        // TODO Auto-generated method stub
        if (o1.relativeRatio != o2.relativeRatio) {
            return o1.relativeRatio > o2.relativeRatio ? 1 : -1;
        }
        if (o1.count != o2.count) {
            return o1.count > o2.count ? 1 : -1;
        }
       if (o1.recentOrders != o2.recentOrders) {
           return o1.recentOrders > o2.recentOrders ? 1 : -1;
       }
        return 0;
    } 
}
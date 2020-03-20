
/**
 * SearchResult 自定义排序，先根据相关度排序、再根据浏览数排序
 */
public class SearchResult implements Comparable<SearchResult> {

    int relativeRatio;
    long count;
    int recentOrders;

    public SearchResult(int relativeRatio, long count) {
        this.relativeRatio = relativeRatio;
        this.count = count;
    }

    @Override
    public int compareTo(SearchResult o) {
        // TODO Auto-generated method stub
        if (this.relativeRatio != o.relativeRatio) {
            return this.relativeRatio > o.relativeRatio ? 1 : -1;
        }
        if (this.count != o.count) {
            return this.count > o.count ? 1 : -1;
        }
        return 0;
    }

    
}
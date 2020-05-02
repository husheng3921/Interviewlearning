


/**
 * BinSearch
 * 二分搜索变形
 */
public class BinSearch {

    public static void main(String[] args) {
        int[] nums={1, 5, 6,6, 12, 30};
        System.out.println(search1(nums, 6));
        System.out.println(search2(nums, 6));
        System.out.println(search1(nums, 7));
        System.out.println(search2(nums, 7));
    }

    /**
     * 查找第一个不小于目标值得数
     * @param nums
     * @param tagert
     * @return
     */
    private static int search1(int[] nums, int tagert){

        int left = 0;
        int right = nums.length -1;
        while (left< right) {
            int mid = left + (right -left)/2;
            if(nums[mid]< tagert){
                left = mid + 1;
            }else{
                right = mid;
            }
        }
        return right ;
        //查找最后一个小于目标值得数字索引 return right -1 ;
    }

    /**
     * 查找第一个大于目标的数
     * @param nums
     * @param tagert
     * @return
     */
    private static int search2(int[] nums, int tagert){
        int left = 0;
        int right = nums.length -1;
        while (left< right) {
            int mid = left + (right -left)/2;
            if(nums[mid]<= tagert){
                left = mid + 1;
            }else{
                right = mid;
            }
        }
        return right ;
    }
}
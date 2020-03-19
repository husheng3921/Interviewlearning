
package algorithm;

/**
 * SearchInsert
 * Leetcode 35 
 * 排序数组中找到n则返回索引，没找到则返回按顺序插入的位置
 */
public class SearchInsert {

    public static void main(String[] args) {
        int[] nums = {1, 3, 5,5, 6};
        System.out.println(searchIndex(nums, 5));
        System.out.println(searchIndex(nums, 2));
        System.out.println(searchIndex(nums, 7));
        System.out.println(searchIndex(nums, 0));
        System.out.println("********************");
        System.out.println(searchIndex1(nums, 5));
        System.out.println(searchIndex1(nums, 2));
        System.out.println(searchIndex1(nums, 7));
        System.out.println(searchIndex1(nums, 0));
    }
    private static int searchIndex(int[] nums, int n){
        int left = 0;
        int right = nums.length - 1;
        while(left <= right){
            int mid = (left + right)>>>1;
            if( nums[mid] < n){
                left = mid + 1;
            }else if( nums[mid] > n){
                right = mid -1;
            }else{
                return mid;
            }
        }

        return left;
        /**
         * 如果是返回小于或等于目标值得最大索引，return right
         */

    }
    private static int searchIndex1(int[] nums, int n){
        int left = 0;
        int right = nums.length;
        while(left < right){
            int mid = (left + right)>>>1;
            if( nums[mid] < n){
                left = mid + 1;
            }else {
                right = mid;
            }
        }

        return left;
        /**
         * 如果是返回小于或等于目标值得最大索引，return right
         */

    }

    private static int searchIndex2(int[] nums, int target){
        // 返回大于等于 target 的索引，有可能是最后一个
        int len = nums.length;

        if (len == 0) {
            return 0;
        }

        int left = 0;
        // 如果 target 比 nums里所有的数都大，则最后一个数的索引 + 1 就是候选值，因此，右边界应该是数组的长度
        int right = len;
         // 二分的逻辑一定要写对，否则会出现死循环或者数组下标越界
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

}
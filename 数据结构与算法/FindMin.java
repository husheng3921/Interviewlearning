package algorithm;

/**
 * FindMin
 * Leetcode 704 
 * 升序数组，旋转后，查找最小
 * [1,3,4,5,6]->[4,5,6,1,3]
 */
public class FindMin {

    public static void main(String[] args) {
        int[] nums={4,5,6,1,3};
        System.out.println(findMinNum(nums));
    }
    private static int findMinNum(int[] nums){
        if (nums.length == 1) {
            return nums[0];
        }
        int left = 0;
        int right = nums.length-1;
        if(nums[right]>nums[0]){
            return nums[0];
        }
        while (left <= right) {
            
            int mid = left + (right - left)/2;
            if(nums[mid] > nums[mid+1]){
                return nums[mid+1];
            }
            if (nums[mid -1]> nums[mid]) {
                return nums[mid];
            }
            if( nums[mid] > nums[0]){
                left = mid + 1;
            }else{
                right = mid -1;
            }
            
            
        }
        return -1 ;
    }
    /**
     * class Solution {
  public int findMin(int[] nums) {
    // If the list has just one element then return that element.
    if (nums.length == 1) {
      return nums[0];
    }

    // initializing left and right pointers.
    int left = 0, right = nums.length - 1;

    // if the last element is greater than the first element then there is no rotation.
    // e.g. 1 < 2 < 3 < 4 < 5 < 7. Already sorted array.
    // Hence the smallest element is first element. A[0]
    if (nums[right] > nums[0]) {
      return nums[0];
    }
    
    // Binary search way
    while (right >= left) {
      // Find the mid element
      int mid = left + (right - left) / 2;

      // if the mid element is greater than its next element then mid+1 element is the smallest
      // This point would be the point of change. From higher to lower value.
      if (nums[mid] > nums[mid + 1]) {
        return nums[mid + 1];
      }

      // if the mid element is lesser than its previous element then mid element is the smallest
      if (nums[mid - 1] > nums[mid]) {
        return nums[mid];
      }

      // if the mid elements value is greater than the 0th element this means
      // the least value is still somewhere to the right as we are still dealing with elements
      // greater than nums[0]
      if (nums[mid] > nums[0]) {
        left = mid + 1;
      } else {
        // if nums[0] is greater than the mid value then this means the smallest value is somewhere to
        // the left
        right = mid - 1;
      }
    }
    return -1;
  }
}

作者：LeetCode
链接：https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array/solution/xun-zhao-xuan-zhuan-pai-lie-shu-zu-zhong-de-zui-xi/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
}
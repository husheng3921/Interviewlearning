
package algorithm;

/**
 * BitMapOne
 * 位图数据结构，bit为存储单位 
 * 给定第i位，存在1 不存在 0
 * 节省空间高效
 * 参考 https://www.cnblogs.com/simpleito/p/10740288.html
 */
public class BitMapOne {

    // 1 byte = 8bit
    private byte[] bytes;
    private int length;

    /**
     * 位图实际可操作区间[0, length)
     * @param length
     */
    public BitMapOne (int length) {
        this.length = length;
        //bytes = new byte[length%8 == 0 ? length/8 : length/8 +1];
        bytes = new byte[length&7 == 0 ? length>>3 : (length>>3 +1)];
    }

    /**
     * 在目标位置设 1
     * @param n
     * @param value
     */
    public void set(int n, boolean value) {
        if(value){
            //先找到第几个bytes桶
            int bytesIndex = n >> 3;//n/8
            //再找到bytes桶内偏移
            // bytesOffset = n & 7;// n % 8
            byte bytesOffset = (byte) (n&7);
            //得到偏移量后，将1左移 byteOffset位
            // | 或操作表示不同的索引都存在这个桶中，这些索引值是或的关系
            // bytes[bytesIndex] = bytes[bytesIndex] | byte(1 << bytesOffset;
            bytes[n >> 3] |= 1<<(n&7);
        }else{
            //目标位置0 其他位为1
            bytes[n >> 3] &= ~(1 << (n&7));
        }
    }

    /**
     * 返回 指定索引处值是否存在
     * @param n
     * @return
     */
    public boolean get(int n) {
        
        //得到n所在的bytes桶号，和bytes桶中的偏移，设偏移处的值 1 << (n&7)
        // 得到桶号bytes，n若在原bytes数组中，则该桶号中的数与 偏移值作与操作为1，否则为0
        return (bytes[n >> 3] & (1 << (n&7)) )== 1 ? true : false;
    }
}
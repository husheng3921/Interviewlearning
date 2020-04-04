




## 常见面试题
* HashMap的容量为啥是2的n次方？  
1、分布均匀，减少hash碰撞；  
2、计算方便，我们会用取模%操作,采用二进制&操作高效，
```Java
//取余操作中如果除数是2的幂次，则等价于
hash % length == hash&(length-1);//length是2的n次幂，采用二进制位操作效率高。
```
* 扰动函数  
>主要是为了加大hash值低位的随机性，使得分布更加均匀，从而提高对应数组存储下标位置的随机性&均匀性，最终减少hash冲突，两次就够了，  

JDK1.7
```Java
/**
 * Applies a supplemental hash function to a given hashCode, which
 * defends against poor quality hash functions.  This is critical
 * because HashMap uses power-of-two length hash tables, that
 * otherwise encounter collisions for hashCodes that do not differ
 * in lower bits. Note: Null keys always map to hash 0, thus index 0.
 */
static int hash(int h) {
    // This function ensures that hashCodes that differ only by
    // constant multiples at each bit position have a bounded
    // number of collisions (approximately 8 at default load factor).

    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
}
```
JDK1.8 
```Java
/**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.  Because the table uses power-of-two masking, sets of
     * hashes that vary only in bits above the current mask will
     * always collide. (Among known examples are sets of Float keys
     * holding consecutive whole numbers in small tables.)  So we
     * apply a transform that spreads the impact of higher bits
     * downward. There is a tradeoff between speed, utility, and
     * quality of bit-spreading. Because many common sets of hashes
     * are already reasonably distributed (so don't benefit from
     * spreading), and because we use trees to handle large sets of
     * collisions in bins, we just XOR some shifted bits in the
     * cheapest possible way to reduce systematic lossage, as well as
     * to incorporate impact of the highest bits that would otherwise
     * never be used in index calculations because of table bounds.
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```
> key.hashCode()返回int型，范围能映射大约40亿空间，40亿长的数组内存放不下，所以散列值不能直接拿来用，需要对数组长度取模。
> ```Java
> bucketIndex = indexFor(hash, table.length);//JDK7
> ```
> indexFor的代码就是散列值与数组长度进行与操作：  
> ```Java
> static int indexFor(int h, int length){
>   return h & (length-1);//JDK7
> }
> ```
> 这里也说明为啥数组长度是2的幂，数组长度-1，正好低位掩码，与操作将散列值的高位全归0，只保留低位，用来做数组下标访问。  
> 要是只有最后几位，碰撞冲突会很严重，<strong>扰动函数</strong>的价值体现出来了，  
> ![](img/HashMap-4.jpg)
> 右移16位，正好是32bit的一半，自己的高半区和低半区作异或，就是为了<strong>混合原始哈希码的高位和低位</strong>，以此来增加低位的随机性。而且混合后的低位掺杂了高位的部分特征，这样高位的信息也变相保留下来。

* HashMap在1.7和1.8中有哪些不同
|不同|JDK1.7|JDK1.8|
|--|--|--|
|存储结构|数组+链表|数组+链表+红黑树|
|初始化方式|单独函数:inflateTable()|集成到了扩容函数resize()|
|hash值计算方式|扰动处理=9次扰动=4次位运算+5次异或运算|扰动处理=2次扰动=1次位运算+1次异或运算|
|存放数据|无冲突，放数组；冲突，存放链表|无冲突：放数组；冲突-链表长度<8:存放单链表;链表长度大于8：树化并存放红黑树，树节点小于6时转换为链表|
|插入数据方式|头插法(先将原位置的数据移到后1位，再插入数据到该位置)|尾插法(插入到链表尾部或红黑树)|
|扩容后存储位置计算|全部按照原来方法进行计算(hashCode->扰动函数->(h&(length-1)))|按照扩容后规律计算(扩容后位置=原位置or 原位置+旧容量)|

## 小结
[Java集合14问](https://cloud.tencent.com/developer/article/1184097)
* HashMap为什么不直接使用hashCode() 作为处理后的hash值直接作为table的下标呢？
> hashCode()方法返回的是int整数类型，其范围为-(2 ^ 31)~(2 ^ 31 - 1)，约有40亿个映射空间， 而HashMap的容量范围是在16（初始化默认值）~2 ^ 30，HashMap通常情况下是取不到最大值的，并且设备上也难以提供这么多的存储空间， 从而导致通过hashCode()计算出的哈希值可能不在数组大小范围内，进而无法匹配存储位置

* 为啥数组长度要保证为2的幂次方？
>1.只有当数组长度为2的幂次方时，h&(length-1)才等价于h%length，即实现了key的定位，2的幂次方也可以减少冲突次数，提高HashMap的查询效率；  
>2.如果length 为 2 的次幂 则 length-1 转化为二进制必定是 11111……的形式， 在于 h 的二进制与操作效率会非常的快，而且空间不浪费；如果 length 不是 2 的次幂，比如 length 为 15，则 length - 1 为 14，对应的二进制为 1110，在于 h 与操作，最后一位都为 0 ，而 0001，0011，0101，1001，1011，0111，1101 这几个位置永远都不能存放元素了，空间浪费相当大， 更糟的是这种情况中，数组可以使用的位置比数组长度小了很多，这意味着进一步增加了碰撞的几率，减慢了查询的效率！这样就会造成空间的浪费.  
<strong>length为2的幂保证了按位与最后一位的有效性，使哈希表散列更均匀。</strong>

* HashMap7死循环问题  
[死循环](https://www.jianshu.com/p/1e9cf0ac07f4)
[参考](https://blog.csdn.net/xuefeng0707/article/details/40797085)

* Map与Set使用时，注意自定义对象作为key时，重写hashCode和equals
  
* 为啥装载因子是0.75
[参考](https://www.jianshu.com/p/dff8f4641814)
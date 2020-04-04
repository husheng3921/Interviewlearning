# HashMap Jdk7源码

## 属性
```java
/**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;//JDK8 中 1<<4

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    transient Entry[] table;

    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;

    /**
     * The next size value at which to resize (capacity * load factor).
     * @serial
     */
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    final float loadFactor;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */
    transient int modCount;
```

## 初始化
```Java
/**
     * 生成一个空HashMap，传入容量与负载因子
     * @param initialCapacity 初始容量
     * @param loadFactor 负载因子
     */
    public HashMap(int initialCapacity, float loadFactor) {
        //初始容量不能小于0
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        //初始容量不能大于默认的最大容量
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;

        //负载因子不能小于0，且不能为"NaN"（NaN（"不是一个数字（Not a Number）"的缩写））
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        //将传入的负载因子赋值给属性
        this.loadFactor = loadFactor;

        //此时并不会创建容器，因为没有 传具体值
        // 没下次扩容大小
        /**
         * 此时并不会创建容器，因为没有传具体值。
         * 当下次传具体值的时候，才会"根据这次的初始容量"，创建一个内部数组。
         * 所以此次的初始容量只是作为下一次扩容（新建）的容量。
         */
        threshold = initialCapacity;

        //该方法只在LinkedHashMap中有实现，主要在构造函数初始化和clone、readObject中有调用。
        init();
    }

    /**
     * 生成一个空hashmap，传入初始容量，负载因子使用默认值（0.75）
     * @param initialCapacity 初始容量
     */
    public HashMap(int initialCapacity) {
        //生成空数组，并指定扩容值
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 生成一个空hashmap，初始容量和负载因子全部使用默认值。
     */
    public HashMap() {
        //生成空数组，并指定扩容值
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 根据已有map对象生成一个hashmap，初始容量与传入的map相关，负载因子使用默认值
     * @param m Map对象
     */
    public HashMap(Map<? extends K, ? extends V> m) {
        //生成空数组，并指定扩容值
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);

        //由于此时数组为空，所以使用"扩容临界值"新建一个数组
        inflateTable(threshold);

        //将传入map的键值对添加到初始数组中
        putAllForCreate(m);
    }
```

## hash、索引计算
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
        //9次扰动，4次位运算，5次异或
    }

    /**
     * Returns index for hash code h.
     * 取模运算换成位运算，前提长度是2的幂次方
     */
    static int indexFor(int h, int length) {
        return h & (length-1);
    }
/**
     * 只在LinkedHashMap中有实现，主要在构造函数初始化和clone、readObject中有调用。
     */
    void init() {
    }

    /**
     * 新建一个空的内部数组
     * @param toSize 新数组容量
     */
    private void inflateTable(int toSize) {
        //内部数组的大小必须是2的n次方，所以要找到"大于"toSize的"最小的2的n次方"。
        int capacity = roundUpToPowerOf2(toSize);

        //下次扩容临界值
        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);

        table = new Entry[capacity];

        //根据数组长度初始化hashseed
        initHashSeedAsNeeded(capacity);
    }

    /**
     * 找到number的最小的2的n次方
     * @param number
     * @return
     */
    private static int roundUpToPowerOf2(int number) {

        return number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
    }

    /**
     * 根据内部数组长度初始化hashseed
     * @param capacity 内部数组长度
     * @return hashSeed是否初始化
     */
    final boolean initHashSeedAsNeeded(int capacity) {
        boolean currentAltHashing = hashSeed != 0;
        boolean useAltHashing = sun.misc.VM.isBooted() && (capacity >= Holder.ALTERNATIVE_HASHING_THRESHOLD);
        boolean switching = currentAltHashing ^ useAltHashing;

        //为true则赋初始化值
        if (switching) {
            hashSeed = useAltHashing
                    ? sun.misc.Hashing.randomHashSeed(this)
                    : 0;
        }
        return switching;
    }

    /**
     * 静态内部类，提供一些静态常量
     */
    private static class Holder {

        /**
         * 容量阈值，初始化hashSeed的时候会用到该值
         */
        static final int ALTERNATIVE_HASHING_THRESHOLD;

        static {
            //获取系统变量jdk.map.althashing.threshold
            String altThreshold = java.security.AccessController.doPrivileged(
                    new sun.security.action.GetPropertyAction(
                            "jdk.map.althashing.threshold"));

            int threshold;
            try {
                threshold = (null != altThreshold)
                        ? Integer.parseInt(altThreshold)
                        : ALTERNATIVE_HASHING_THRESHOLD_DEFAULT;

                // jdk.map.althashing.threshold系统变量默认为-1，如果为-1，则将阈值设为Integer.MAX_VALUE
                if (threshold == -1) {
                    threshold = Integer.MAX_VALUE;
                }
                //阈值需要为正数
                if (threshold < 0) {
                    throw new IllegalArgumentException("value must be positive integer.");
                }
            } catch(IllegalArgumentException failed) {
                throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);
            }

            ALTERNATIVE_HASHING_THRESHOLD = threshold;
        }
    }

    /**
     * 添加指定map里面的所有键值对
     * @param m
     */
    private void putAllForCreate(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
            putForCreate(e.getKey(), e.getValue());
    }

    /**
     * 添加键值对
     * @param key 键值名
     * @param value 键值
     */
    private void putForCreate(K key, V value) {
        //如果key为null，则hash值为0，否则根据key计算hash值
        int hash = null == key ? 0 : hash(key);

        //根据hash值和数组的长度找到：该key所属entry在table中的位置i
        int i = indexFor(hash, table.length);

        /**
         * 数组中每一项存的都是一个链表，
         * 先找到i位置，然后循环该位置上的每一个entry，
         * 如果发现存在key与传入key相等，则替换其value。然后结束侧方法。
         * 如果没有找到相同的key，则继续执行下一条指令，将此键值对存入链表头
         */
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k)))) {
                e.value = value;
                return;
            }
        }

        //将该键值对存入指定下标的链表头中
        createEntry(hash, key, value, i);
    }

    /**
     * 根据传入的key生成hash值
     * @param k  键值名
     * @return hash值
     */
    final int hash(Object k) {
        int h = hashSeed;

        //如果key是字符串类型，就使用stringHash32来生成hash值
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }

        //一次散列
        h ^= k.hashCode();

        //二次散列
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * 返回hash值的索引，采用除模取余法，h & (length-1)操作 等价于 hash % length操作， 但&操作性能更优
     */
    /**
     * 根据key的hash值与数组长度，找到该key在table数组中的下标
     * @param h hash值
     * @param length 数组长度
     * @return 下标
     */
    static int indexFor(int h, int length) {
        //除模取余,相当于hash % length，&速度更快
        return h & (length-1);
    }

    /**
     * 将键值对与他的hash值作为一个entry，插入table的指定下标中的链表头中
     * @param hash hash值
     * @param key 键值名
     * @param value 键值
     * @param bucketIndex 被插入的下标
     */
    void createEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K,V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        size++;
    }
```

## 添加元素
```java
/**
     * 存入一个键值对，如果key重复，则更新value
     * @param key 键值名
     * @param value 键值
     * @return 如果存的是新key则返回null，如果覆盖了旧键值对，则返回旧value
     */
    public V put(K key, V value) {
        //如果数组为空，则新建数组
        if (table == EMPTY_TABLE) {
            inflateTable(threshold);
        }

        //如果key为null，则把value放在table[0]中
        if (key == null)
            return putForNullKey(value);

        //生成key所对应的hash值
        int hash = hash(key);

        //根据hash值和数组的长度找到：该key所属entry在table中的位置i
        int i = indexFor(hash, table.length);

        /**
         * 数组中每一项存的都是一个链表，
         * 先找到i位置，然后循环该位置上的每一个entry，
         * 如果发现存在key与传入key相等，则替换其value。然后结束侧方法。
         * 如果没有找到相同的key，则继续执行下一条指令，将此键值对存入链表头
         */
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        //map操作次数加一
        modCount++;

        //查看是否需要扩容，并将该键值对存入指定下标的链表头中
        addEntry(hash, key, value, i);

        //如果是新存入的键值对，则返回null
        return null;
    }

    /**
     * 将传入map的所有键值对存入本map
     * @param m 传入map
     */
    public void putAll(Map<? extends K, ? extends V> m) {
        //传入数组的键值对数
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0)
            return;

        //如果本地数组为空，则新建本地数组
        if (table == EMPTY_TABLE) {
            //从当前扩容临界值和传入数组的容量中选择大的一方作为初始数组容量
            inflateTable((int) Math.max(numKeysToBeAdded * loadFactor, threshold));
        }

        //如果传入map的键值对数比"下一次扩容后的内部数组大小"还大，则对数组进行扩容。（因为当前数组即使扩容后也装不下它）
        if (numKeysToBeAdded > threshold) {
            //确定新内部数组所需容量
            int targetCapacity = (int)(numKeysToBeAdded / loadFactor + 1);
            //不能大于最大容量
            if (targetCapacity > MAXIMUM_CAPACITY)
                targetCapacity = MAXIMUM_CAPACITY;
            //当前数组长度
            int newCapacity = table.length;
            //从当前数组长度开始增加，每次增加一个"2次方"，直到大于所需容量为止
            while (newCapacity < targetCapacity)
                newCapacity <<= 1;

            //如果发现内部数组长度需要增加，则扩容内部数组
            if (newCapacity > table.length)
                resize(newCapacity);
        }

        //遍历传入map，将键值对存入内部数组
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }
/**
     * 如果key为null，则将其value存入table[0]的链表中
     * @param value 键值
     * @return 如果覆盖了旧value，则返回value，否则返回null
     */
    private V putForNullKey(V value) {
        //迭代table[0]中的链表里的每一个entry
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            //如果找到key为null的entry，则覆盖其value，并返回旧value
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        //操作次数加一
        modCount++;

        //查看是否需要扩容，然后将entry插入table的指定下标中的链表头中
        addEntry(0, null, value, 0);
        return null;
    }

    /**
     * 查看是否需要扩容，然后添加新节点
     * @param hash key的hash值
     * @param key 结点内key
     * @param value 结点内value
     * @param bucketIndex 结点所在的table下标
     */
    void addEntry(int hash, K key, V value, int bucketIndex) {
        //如果当前键值对数量达到了临界值，或目标table下标不存在，则扩容table
        if ((size >= threshold) && (null != table[bucketIndex])) {
            //容量扩容一倍
            resize(2 * table.length);
            //由于数组扩容了，重新计算hash值
            hash = (null != key) ? hash(key) : 0;
            //重新计算存储位置
            bucketIndex = indexFor(hash, table.length);
        }

        //将键值对与他的hash值作为一个entry，插入table的指定下标中的链表头中
        createEntry(hash, key, value, bucketIndex);
    }
```
## 扩容
```Java
    /**
     * 对数组扩容，即创建一个新数组，并将旧数组里的东西重新存入新数组
     * @param newCapacity 新数组容量
     */
    void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;

        //如果当前数组容量已经达到最大值了，则将扩容的临界值设置为Integer.MAX_VALUE(Integer.MAX_VALUE是容量的临界点)
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        //创建一个扩容后的新数组
        Entry[] newTable = new Entry[newCapacity];

        //将当前数组中的键值对存入新数组
        transfer(newTable, initHashSeedAsNeeded(newCapacity));

        //用新数组替换旧数组
        table = newTable;

        //计算下一个扩容临界点
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }

    /**
     * 将现有数组中的内容重新通过hash计算存入新数组
     * @param newTable 新数组
     * @param rehash
     */
    void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;

        //遍历现有数组中的每一个单链表的头entry
        for (Entry<K,V> e : table) {
            //查找链表里的每一个entry
            while(null != e) {
                Entry<K,V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }

                //根据新的数组长度，重新计算此entry所在下标i
                int i = indexFor(e.hash, newCapacity);

                //将entry放入下标i处链表的头部（将新数组此处的原有链表存入entry的next指针）
                e.next = newTable[i];

                //将链表存回下标i
                newTable[i] = e;

                //查看下一个entry
                e = next;
            }
        }
    }
```
## get
```java
/**
     * 返回此hashmap中存储的键值对个数
     * @return 键值对个数
     */
    public int size() {
        return size;
    }

    /**
     * 根据key找到对应value
     * @param key 键值名
     * @return 键值value
     */
    public V get(Object key) {
        //如果key为null，则从table[0]中取value
        if (key == null)
            return getForNullKey();

        //如果key不为null，则先根据key，找到其entry
        Entry<K,V> entry = getEntry(key);

        //返回entry节点里的value值
        return null == entry ? null : entry.getValue();
    }

    /**
     * 返回一个set集合，里面装的都是hashmap的value。
     * 因为map中的key不能重复，set集合中的值也不能重复，所以可以装入set。
     *
     * 在hashmap的父类AbstractMap中，定义了Set<K> keySet = null;
     * 如果keySet为null，则返回内部类KeySet。
     * @return 含有所有key的set集合
     */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    /**
     * 返回一个Collection集合，里面装的都是hashmap的value。
     * 因为map中的value可以重复，所以装入Collection。
     *
     * 在hashmap的父类AbstractMap中，定义了Collection<V> values = null;
     * 如果values为null，则返回内部类Values。
     */
    public Collection<V> values() {
        Collection<V> vs = values;
        return (vs != null ? vs : (values = new Values()));
    }

    /**
     * 返回一个set集合，里面装的是所有的entry结点
     * （相当于把map集合转化成set集合）
     * @return 含有所有entry的set集合
     */
    public Set<Map.Entry<K,V>> entrySet() {
        return entrySet0();
    }

    /**
     * 生成一个新的hashmap对象，新hashmap中数组也是新生成的，
     * 但数组中的entry节点还是引用就hashmap中的元素。
     * 所以对目前已有的节点进行修改会导致：原对象和clone对象都发生改变。
     * 但进行新增或删除就不会影响对方，因为这相当于是对数组做出的改变，clone对象新生成了一个数组。
     * @return clone出的hashmap
     */
    public Object clone() {
        HashMap<K,V> result = null;
        try {
            result = (HashMap<K,V>)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        if (result.table != EMPTY_TABLE) {
            result.inflateTable(Math.min(
                    (int) Math.min(
                            size * Math.min(1 / loadFactor, 4.0f),

                            HashMap.MAXIMUM_CAPACITY),
                    table.length));
        }
        result.entrySet = null;
        result.modCount = 0;
        result.size = 0;
        result.init();
        result.putAllForCreate(this);

        return result;
    }
```

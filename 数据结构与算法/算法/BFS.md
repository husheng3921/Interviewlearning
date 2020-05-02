# BFS 框架本质

## 算法框架
<strong> 问题的本质就是让你在一幅图中找到起点<font color="red">start</font> 到终点<font color="red">end</font>的最近距离。</strong>

```java
//计算从起点start 到终点 target的最近距离
int BFS(Node start, Node target) {
    Queue<Node> q;//核心数据结构
    Set<Node> visited;//避免走回头路

    q.offer(start);//将起点加入队列
    visited.add(start);
    int step = 0;//记录扩散步骤

    while(q not empty) {
        int sz = q.size();
        /* 将当前队列中的所有节点向四周扩散*/
        for ( int i = 0; i < sz; i++) {
            Node cur = q.poll();
            /* 划重点，这里判断是否到达终点*/
            if (cur is target) {
                return step;
            }
            /* 将cur的相邻结点加入队列*/
            for (Node x : cur.ajd()) {
                if ( x not in visited) {
                    q.offer(x);
                    vistied.add(x);
                }
            }
        }
        /* 划重点，更新步数*/
        step++;
    }
}
```

# SQL 语句操作

## 分数排名
![](./img/178.png)

```sql
select a.Score as Score,
(select count(distinct b.Score) from Scores b where b.Score >= a.Score) as `Rank`
from Scores a
order by a.Score DESC
```
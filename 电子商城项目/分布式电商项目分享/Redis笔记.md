# Redis

NoSql

```bash
# 1.商品基本信息
	名称、价格、商家信息：
	关系型数据库
	
# 2.商品的描述、评论（文字较多）
	文档型数据库中，MongoDB
	
# 3.图片
	分布式文件系统 FastDFS
	- 淘宝自己的  TFS
	- Google    GFS
	- Hadoop    HDFS
	- 阿里云     oss
	
# 4.商品关键字
  - 搜索引擎 Solr ElasiticSearch
  
# 5.商品热门的波段信息
	- 内存数据库 Redis Tair Memcache
	
# 6.商品交易，支付接口
	- 三方应用
```

## 四大分类

##### KV键值对：

- 百度阿里Redis memcache
- 美团 Redis Tair
- 新浪 Redis

##### 文档型数据库（bson格式）

- MongoDB
  - 分布式文件存储的数据库，C++编写，处理大量文档
  - 介于关系型数据库和非关系型数据库中间的产品，是nosql中最像关系型数据库的
- CouchDB

##### 列存储数据库

- HBase
- 分布式文件系统

##### 图形数据库

社交网络，推荐系统，最短路径

- Neo4J
- InfoGrid

### Redis

==Re==mote ==Di==ctionary ==S==erver(远程字典服务)，C语言编写

为什么快：c语言编写，纯内存操作，单线程

功能：

- 内存存储、持久化（RDB/AOF）
- 效率高，可以高速缓存
- 发布、订阅系统
- 地图信息分析
- 计时器、计数器（浏览量）

和Memcache区别：

Memcache只支持简单数据类型，不支持持久化，不支持主从、分片

命令:

```sh
keys [pattern]
select [index] # 默认16个数据库
flushdb 
flushall
set key value
expire key seconds # ttl key (-2不存在，-1永久，正整数剩余秒数)
move key
del key key1 key2
exists key
append key
```

默认端口：6379（MERZ）

## 五大数据类型 

### string

```sh
# 增
SETEX key seconds value #过期时间
SETNX key value #相当于map的putIfAbsent

# 删
DEL key [key ...]

# 改
INCR key # 增加 不存在直接创建一个
INCRBY key [increment] #指定步长
DECY key #减少
DECYBY key [increment]

SETRANGE key offset value #从偏移量开始进行替换，场景:比如我们要记录用户名（或者其他信息）。假定单个用户名字不超过64字节。新用户id通过一个incre指令获得, 名字存储可以这样：setrange username (id-1)*64 real-name，也就是id为1的用户放在0到63，2的在64到127，….取的时候，getrange username (id-1) (id-1+63)，简单的几句就完成存储和读取了。归根结底，redis就是给了一个大的数组，并提供o(1)的随机访问。相当于我们有了一块“大的内存空间”和几个访问修改函数。一般的思维模式可能是直接使用list等之类

# 查
GETRANGE key start end #支持负数
GETSET key value #设置新的值

MSET k1 v1 k2 v2 ... #muti set 
MGET k1 v2 ...
MSETNX k1 v1 k2 v2 ... #原子操作
```

### list 

本质双向链表，和LinkedList完全相同，结构中包含头尾节点、链表长度

使用lpush+rpop可以实现简单的异步队列，没有消息时可以使用while循环+sleep或者brpop来等待消息，如果需要实现一个生产多个消费可使用pub/sub（发布订阅）

```sh
# 增
lpush key value1 value2... #rpush

# 删
lpop key #弹出key中的一个值，lpush + rpop=简单异步队列
blpop key [key...] timeout # 弹出指定list中的一个值，没有值时阻塞直到某个list中有值或超时，用于阻塞队列
lrem key 1[次数] element

# 改
ltrim key 1 2 #相当于java的subList，会改变数据 
lset key 0[index] value #指定下标位置替换成另一个值，key必须存在且下标位置有值

# 查
lrange ley 0 -1 #类似getrange命令


```

### set

```sh
#增
SADD key value
#查
SMEMBERS key #所有成员

SISMEMBER key member #是否包含
SCARD key #元素个数

srandom key 2[count]
#删
srem key member [...]
spop key [count]#随机删除一个或多个元素
# 数据处理
sdiff key1 key2 [...]#差集
sinter key1 key2 [...]#交集 如共同好友
sunion key key1 [...]#并集
```

### zset

ziplist或skipList（跳表），链表与类似二分法的结合，用多层索引来提高查找的性能，时间复杂度O(logn)

> 视频网站弹幕，以时间戳作为score，

```sh
#增
ZADD key [NX|XX] [CH] [INCR] score member [score member ...]

#查
zcard key #size
zcount key min max #统计分数之间的个数，闭区间

ZRANGE key start stop [WITHSCORES]# 按照分数升序排序，start stop指定返回元素的位置区间
ZREVRANGE # 同上，逆序，从大到小
ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count] #min和max默认开区间，如果需要闭区间在前面使用左括号"("，可使用+/-inf表示无穷
ZREVRANGEBYSCORE  #逆序 从大到小

#改
ZINCRBY key increment member #增加成员的分数

#删
zrem key member [member...]
```



### hash

本质，类似于hashmap，但没有红黑树，使用渐进式rehash（维护两个hash表，每次增删改查都会将key rehash到新表中，全部keyrehash完毕）

```sh
#增
hset key field value
hmset key field value [field value ...]
hsetnx key field value
#删
hdel key field
#改

hincrby key field 1
#查
hget key field
hmget key field [field ...]
hgetall key #依次打印key，value
hexists key field #等价map.contains()
hlen key #获取size

hkeys key
hvals key
```

### 三种特殊数据类型

#### geospatial  [ˈdʒioʊˈspeɪʃl] 地理位置

```sh
GEOADD key longitude latitude member [longitude latitude member ...]
GEOPOS key member [member ...]# 获取位置
GEODIST key member1 member2 [unit] # 距离 unit:m/km

GEORADIUS key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count]# 获取指定位置指定半径内的元素
GEORADIUSBYMEMBER key member radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count]# 获取指定元素指定半径内的元素

```

#### Hyperloglog

一种估算基数的近似最优算法，可以用来统计（估算）集合中不重复的元素个数，空间复杂度O(log(log(nmax)))

> 统计网站网页的UV（独立访客，每个用户每天只记录一次）

```sh
pfadd key member [member...]
pfcount key
pfmerge destKey key1 key2 #合并到destKey
```

> pf: Hyperloglog作者Philippe Flajolet，2007年提出



#### Bitmap

一个数据用1bit的0和1表示是存在与否，所以一亿数据只需要100,000,000/8/1024/1024=12M，空间复杂度O(nmax)

## 其他命令

### scan

keys命令会阻塞redis（扫描一百万个密钥数据库需要40毫秒），可以使用scan命令，scan每次获取的数量不一定是指定的count，且可能会返回重复key，需要进一步去重

```sh
SCAN cursor [MATCH pattern] [COUNT count] [TYPE type]
```

### 运维

```sh
./redis-cli -h 10.230.51.78 -p 5001 -c
```

集群部署时，随便找一台机器，-c代表集群模式

## 事务

redis的事务使用multi命令开启，开启事务后输入的每条命令不会立即执行而将会加入一个队列，直到使用exec（提交）或discard（取消）事务。

在事务中输入的命令如果**解析**失败，则事务提交时一定报错且所有命令都不会执行。而如果是某条命令在**运行**时失败，那么其他命令还是会成功执行并提交事务。

所以，严格来讲，redis事务不是原子性的。注意，官方文档的描述是：

> - 事务是一个单独的隔离操作：事务中的所有命令都会序列化、按顺序地执行。事务在执行的过程中，不会被其他客户端发送来的命令请求所打断。
> - 事务是一个原子操作：事务中的命令要么全部被执行，要么全部都不执行。

上述描述中的原子操作针对的是命令的执行，而不是执行成功，为什么redis不做回滚：

1. redis命令执行失败的原因只可能是语法问题（和数据库的事务中的异常不同），这些是程序员自己造成的错误，应该在开发环境中被发现而不应该出现在生产环境中
2. 设计回滚机制增加了复杂度，没有回滚机制，redis可以保持简单和快速

==没有回滚机制，也没有隔离级别==

```sh
multi
...
exec/discard
```



## 乐观锁（watch）

一旦监视的key(s) 执行了修改相关的指令，不管有没有更改值，都会使事务提交失败（返回nil）。

原理是使用hashmap，key为监视的key，value为监视该key的客户端的链表，调用watch命令时会把客户端添加到链表尾部，一旦key执行了修改相关指令，则将watch了该key的所有客户端状态置为CLIENT_DIRTY_CAS

```sh
watch key [...key]#必须在multi之前开启监视
multi
...
exec/discard #提交和放弃事务都会自动unwatch，如果监听的key的值被修改，则事务提交失败，返回nil
```

> key到期自动删除不会触发watch

## 分布式锁

- 加锁
  - 必须给锁设置失效时间，避免死锁
  - 每个节点产生一个随机字符串，避免误删
  - 写入随机值和设置失效时间必须是原子的
- 解锁
  - 保证获取数据、判断一致和删除数据三个操作是原子的——使用lua脚本
- 问题：

1. 主从架构锁失效问题：

> 在Redis的master节点上拿到了锁;但是这个加锁的key还没有同步到slave节点;master故障，发生故障转移，slave节点升级为master节点，导致锁丢失。

解决方案，作者提出**Redlock**，redisson已经有实现

2. 如何解决redis分布式锁过期时间到了业务没执行完问题

  - 服务一搬会设置超时时间，锁过期时间可以大于等于超时时间
  - 如果没有超时时间，那么用续期的方式，任务执行的时候，开辟一个守护线程，在守护线程中每隔一段时间重新设置过期时间



## 过期策略&回收策略

对于有设置过期的key有三种处理方式

1. 被动删除：这个key下一次被访问到的时候才会删除。
2. 主动删除：Redis会定期主动淘汰一批已过期的key
3. 当前已用内存超过maxmemory限定时，触发主动清理策略

在config中用`maxmemory 10mb`设置redis内存上限（默认无限制（64位），3G（32位）），`maxmemory-policy 策略`设置回收策略，一共8种回收策略：

```sh
volatile-lru #在设置了过期时间的key中使用lru回收
allkeys-lru #所有key里使用lru回收
volatile-lfu #在设置了过期时间的key中使用lfu回收
allkeys-lfu  #所有key里使用lfu回收
volatile-random #在设置了过期时间的key中随机删除
allkeys-random #所有key里随机删除
volatile-ttl #在设置了过期时间里的key中按到期时间由近到远删除
noeviction #不删除任何key，写操作直接返回错误（读操作不影响）
```

> LRU（Least Recently Used）最久使用 
> LFU（Least Frequently Used）最少使用

直接删除大key是有风险的，key过大，直接删除时会导致Redis阻塞，不同类型的大key有不同的删除方式，

Large Hash Key 可使用[hscan命令](http://redis.io/commands/hscan)，每次获取500个字段，再用[hdel命令](http://redis.io/commands/hdel)，每次删除1个字段。

Large Set Key 可使用[sscan命令](http://redis.io/commands/sscan)，每次扫描集合中500个元素，再用[srem命令](http://redis.io/commands/srem)每次删除一个键。

Large List Key可通过[ltrim命令](http://redis.io/commands/ltrim)每次删除少量元素

Large Sorted Set Key使用sortedset自带的[zremrangebyrank命令](http://redis.io/commands/zremrangebyrank),每次删除top 100个元素

## 持久化（RDB&AOF）

### Redis-DB

将内存数据备份到dump.rdb文件，

生成机制：

1. save或bgsave或flushall
2. 满足redis.config中配置的save规则(相当于触发bgsave)
3. 主从复制，主节点自动触发
4. shutdown命令，且没开启aof功能时
5. kill redis服务也会触发

关闭rdb功能：注释掉所有save配置，或在所有save配置之后添加一行

```sh
save ""
```

关闭后除了手动执行save/bgsave命令，不会备份数据到rdb文件中。

> RDB不能增量生成，每次save都要生成新文件，然后替换旧文件，所以数据量大后效率较低，不能高频率保存，适合数据不重要，甚至丢失15分钟的数据也可以接受的场景，如数据可以容易的再次从表里查询所得

### Append-Only File

把所有的修改命令写入到appendonly.aof文件，默认不开启，在config中设置：

```sh
appendonly yes
```

重启服务并加载配置文件即可生效。

```sh
redis-server /path/to/redis.conf
```
三种策略：
```sh
# appendfsync always
appendfsync everysec #默认每秒append一次aof文件，如果没有执行修改命令则忽略
# appendfsync no
```
> AOF写入频率高（虽然写入效率快），生成的文件大，恢复速度慢，所以默认不开启，适合数据非常重要，不能接受丢失的场景，如大数据表的热点排名数据，不存在表中的重要数据。

文件太大了怎么办？redis有AOF重写机制，以下配置表示aof文件达到64mb的100%时触发rewrite操作

```sh
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
```

也可以使用`BGREWRITEAOF`命令手动触发。

重写时redis并不会分析原来的aof文件，而是基于redis数据库状态使用最少的命令（遍历数据库的key，然后根据类型使用set/expireAt/rpush/sadd/hmset/zadd）生成一份新的aof文件，两份文件保存的数据库状态是相同的

重写时会fork子进程，并开启一个AOF重写缓存区，主进程在执行写命令后会同时写入AOF缓存和AOF重写缓存中，AOF缓存用于定期写入AOF文件，所以即使重写失败也能保证旧的AOF文件能正常工作。

若rdb文件和aof文件有异常，可以使用`redis-check-aof`和`redis-check-rdb`命令进行修复

## 发布订阅

```sh
subscribe  channel [channel...]
publish channel message
```

redis的消息不能保证可靠，发布时如果订阅端下线，再上线是无法收到消息的，需使用专业的消息队列

## 主从复制和哨兵模式

redis服务启动默认是主机，从机只能读不可写，第一次同步过程：

1. slave发送sync命令到master
2. master启动一个进程，将数据快照保存到rdb中（bgsave），同时将保存快照时接收的写命令缓存起来
3. rdb写入完成后，将rdb和增量写命令发送给slave，slave加载数据

配置redis.conf：

```sh
replicaof 127.0.0.1 6379 #配置跟随的主机
replica-read-only yes #从机只能读，不能写
```

主从复制不具备高可用，主机挂掉了就不能用了。 

哨兵是主从复制的升级版，

哨兵配置`redis-sentinel.conf`

```sh
port 26379 #如果要配置哨兵集群，修改此端口
sentinel monitor mymaster 127.0.0.1 6379 2 #最后一个数字代表认为主机客观下线时至少需要几个哨兵同意
sentinel down-after-milliseconds mymaster 30000 #认为主节点下线的等待时间（毫秒）

sentinel notification-script mymaster /var/redis/notify.sh #配置故障转移主机发生变动时执行的脚本，用来邮件、短信等通知管理员
sentinel client-reconfig-script mymaster /var/redis/reconfig.sh #故障转移时执行脚本来通知客户端配置文件已改变以及主机master的地址发生变化
```

## 集群

Redis单点故障，可以通过主从复制`replication`，和自动故障转移`sentinel`哨兵机制。但Redis单`Master`实例提供读写服务，仍然有容量和压力问题，因此需要数据分区

集群模式， 每个节点都有主从机，从机只用于数据同步，不提供读写服务，以备从机宕机，

主机宕机时有可能丢失数据，分布式锁有可能短暂失效？

> 集群模式执行lua脚本需要保证操作的key都在同一个节点上

## 缓存击穿、缓存穿透、缓存雪崩

### 缓存击穿

大量请求查询同一个key，像很多子弹打到同一点上，如果key失效了，请求打到数据库造成宕机

解决方案：热点数据永不过期；设置分布式锁（或单机锁，这里只是为了限流，影响不大），查询redis查不到后，只有一个线程能获取锁去数据库请求数据，其他线程获取锁失败后自旋，获取锁后再次从redis中查询新数据

### 缓存穿透

缓存没命中，请求穿透到数据库，如果有大量恶意请求都不命中缓存而打到数据库，则可能造成数据库宕机

解决方案：非法请求直接拦截，布隆过滤器，缓存空对象（设定过期时间）

### 缓存雪崩

大量key同时过期，数据库请求瞬间加大；或者某个缓存节点宕机，宕机节点的请求全部打到下一个节点上导致下一个也挂了

不要设置同一时间过期，加随机数

缓存高可用，使用Redis Sentinel（哨兵），cluster（集群）

缓存限流降级，通过加锁或者队列来控制读取数据库写缓存的数量，利用ehcache等本地缓存

数据预热

暂停部分非核心服务，如淘宝双十一不能退货、修改收货地址

### 如何保持mysql和redis中数据的一致性

https://www.zhihu.com/question/319817091

## redis使用实践

1. 一个离线任务，临时存的数据，多个服务会用到，这个离线任务可能耗时最多2小时，那redis超时时间我就可以设置为2小时
2. 后台和底层内部交互获取token，底层token失效时间是30分钟，那我上层业务将token放入缓存失效时间就应该小于30分钟，不然上层没失效，底层失效了
3. 商品这种数据可以设置永不过期，A端修改了商品信息后再调刷reids接口

## 慢查询（slowlog ）

相关配置，可使用config get/set [配置名] 来查看和修改

> slowlog-log-slower-than 10000 --慢查询时间阈值，超过则记录，单位微秒，默认是10000即10毫秒
>
> slowlog-max-len 128 --记录慢查询条数，超过数量后将移除最早的记录，建议调整到1000

使用slowlog get 来查看慢查询列表

**RedisTemplate存入null值实际上在redis中存入的是空字符串**

## 开发规范

阿里云Redis开发规范：

https://developer.aliyun.com/article/531067

### RedisTemplate

我们知道，例如下面这样2条命令，实际上会分别前后开启2个不同的链接去设置值

```java
redisTemplate.opsForValue().set("key1", "value1");
redisTemplate.opsForValue().set("key2", "value2");
```


为了在一个redis链接中执行2条以上的命令，我们可以使用 SessionCallback 或者 RedisCallback
首先说SessionCallback,他比后者封装的更友好，实际开发中应当优先选择他:

```java
redisTemplate.execute(new SessionCallback(){
   @Override
   public Object execute(RedisOperation ro) throw DataAccessException{
     ro,opsForValue().set("key1", "value1");
     ro,opsForValue().put("hash", "field", "hvalue1");
     return null;
   }
});
```

其次是RedisCallback，他更加底层，需要处理的内容也更多，可读性也更差

```java
redisTemplate.execute(new RedisCallback() {
  @Override
  public Object doInRedis(RedisConnection rc) throw DataAccessException{
    rc.set("key1".getBytes(), "value1".getBytes());
    rc.hSet("hash".getBytes(), "field".getBytes(), "hvalue1".getBytest());
    return null
  }
});
```

 

#### 泛型问题

在业务类中注入泛型的redisTemplate可以减少强制转换，但需要注意按照类型注入是区分泛型的，所以只能使用name注入

```java
@Resource //使用Resource注入，默认是以name注入，如果用@Autowired则须使用属性指明按类型注入
private RedisTemplate<String, SomeBean> redisTemplate;

public SomeBean getSomeBean(String id){
  return redisTemplate.opsForValue().get(id);
}

```


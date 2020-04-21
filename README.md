## Java技术栈
### 多线程与高并发
- synchronized 内置锁（monitor）原子性（lock cmpxchg 指令）、内存可见性（锁缓存或锁总线）
- volatile 内存可见（lock 指令  锁总线）、禁止指令重排序（jvm内存屏障）
- CAS 原子性
- Lock
- Condition
- AQS
- LockSupport
- Unsafe
- 原子类 AtomicXXX
- 并发容器类 BlockingQueue
- 同步工具类 ReentrantLock
- 线程池
 
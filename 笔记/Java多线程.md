### Java多线程

> 1. 平常有用到什么锁，synchronized底层原理是什么
>
> 2. synchronized是公平锁还是非公平锁,ReteranLock是公平锁吗？是怎么实现的
>
> 3. synchronized跟ReentranLock有什么区别？
>
> 4. synchronized与ReentranLock发生异常的场景.
>
> 5. synchronized的同步原语
>
> 6. 讲一下锁，synchronized和Lock。CAS原理
>
> 7. synchronized实现。非静态方法A和B在同一个类中，方法A用synchronized修饰，当A方法因为多线程请求有线程阻塞在对象锁上的时候，B方法的访问受不受影响？
>
> 8. Lock的实现，以及与synchronized的区别
>
> 9. Java的线程同步方式；synchronized和Lock的实现及区别
>
> 10. Synchronized底层原理，java锁机制
>
> 11. 讲下synchronized和volatile；读写锁和ReentrantLock，synchronized和读写锁的区别。
>
> 12. 锁之间的区别
>
> 13. 锁的几种状态
>
> 14. AQS了解吗？
>
>     
>
>     
>
> 15. 多线程如何实现？有哪些方式？
>
> 16. 多线程为何不安全
>
> 17. 线程间同步的方法
>
> 18. 如何让两个线程循环交替打印
>
> 19. 怎么中止一个线程，Thread.Interupt一定有效吗？
>
> 20. Java为何会有线程安全问题？如何解决？
>
> 21. t1、t2、t3三个线程，如何让三个线程按照顺序依次打印1-100。手写。
>
>     
>
> 22. 阿里编程规范不建议使用线程池，为什么？
>
> 23. 四种线程池原理？
>
> 24. 线程池了解多少？拒绝策略有几种,为什么有newSingleThread
>
> 25. 线程池的参数
>
> 26. 线程的使用。讲下线程池的类型，线程池对象的参数，线程池最大线程数和核心线程数的关系，task的优先级如何实现？（优先级队列）
>
> 27. 线程池如何配置，核心线程数你一般给多少
>
> 28. 线程池原理



#### 一、线程基础问题

##### 1、线程的概念？

一个程序同时执行多个任务，通常，每一个任务称为一个线程（thread），它是线程控制的简称。

线程是 CPU 调度的最小单元。

##### 2、线程与进程的区别？

- 定义方面：进程是程序在某个数据集合上的一次运行活动；线程是进程中的一个执行路径。（进程可以创建多个线程）
- 角色方面：在支持线程机制的系统中，进程是系统资源分配的单位，线程是CPU调度的单位。
- 资源共享方面：进程之间不能共享资源，而线程共享所在进程的地址空间和其它资源。同时线程还有自己的栈和栈指针，程序计数器等寄存器。
- 独立性方面：进程有自己独立的地址空间，而线程没有，线程必须依赖于进程而存在。
- 开销方面。进程切换的开销较大。线程相对较小。（引入线程也出于了开销的考虑。）



##### 3、线程的几种状态？

Java语言定义了 5 种线程状态，在任意一个时间点，一个线程只能有且只有其中的一种状态：

- 新建（New）：创建后尚未启动的线程处于这种状态。
- 运行（Runnable）：Runnable 包括了操作系统线程状态中的 Running 和 Ready，也就是处于此状态的线程有可能正在执行，也有可能正在等待着 CPU 为它分配执行时间。
- 无限期等待（Waiting）：处于这种状态的线程不会被分配 CPU 执行时间，它们要等待被其它线程显示地唤醒。以下方法会让线程陷入无限期的等待状态：
  - 没有设置 Timeout 参数的 `Object.wait()` 方法
  - 没有设置 Timeout 参数的 `Thread.join()` 方法
  - `LockSupport.park()` 方法
- 限期等待（Timed Waiting）：处于这种状态的线程也不会被分配 CPU 执行时间，不过无需等待被其它线程显式地唤醒，在一定时间之后它们会由系统自动唤醒。以下方法会让线程进入限期等待状态：
  - `Thread.sleep()` 方法
  - 设置了 Timeout 参数的 `Object.wait()` 方法
  - 设置了 Timeout 参数的 `Thread.join()` 方法
  - `LockSupport.parkNanos()` 方法
  - `LockSupport.parkUntil()` 方法
- 阻塞（Blocked）：线程被阻塞了，“阻塞状态”与“等待状态”的区别是：“阻塞状态”在等待着获取到一个排他锁，这个事件将在另外一个线程放弃这个锁的时候发生；而“等待状态”则是等待一段时间，或者唤醒动作的发生。在程序等待进入同步区域的时候，线程将进入这种状态。
- 结束（Terminated）：已终止线程的线程状态，线程已经结束执行。

##### 4、wait 和 sleep 的区别？

- sleep方法是Thread类中静态方法，wait方法是Object中的实例方法。

- sleep方法不会释放锁，而wait方法释放了锁，使得其他线程可以使用同步方法或代码块。

  **sleep方法不让出系统资源???**，wait方法是进入线程等待池等待，让出系统资源，其他线程可以占用CPU。

- sleep方法不需要被唤醒，到时之后自动退出阻塞或者调用 `interrupt()` 打断，wait方法需要其他线程调用对象的`notify()`或者`notifyAll()`

- sleep方法在任何地方都可以使用，但是要 try catch，wait方法必须在同步方法或者同步代码块中使用



##### 5、线程执行完会释放吗

> Java线程是系统级线程。通过Java API创建的线程最终会交给操作系统管理。
>
> 通常线程将它的任务代码执行结束之后就会被操作系统回收。线程是一种系统资源，甚至可以说线程就是一块内存，内存中有线程执行堆栈数据等。一个系统的资源是有限的，所以线程资源也通常会在线程执行结束后被操作系统回收，需要时再继续创建。

比如 Android 中的主线程，为了不让代码执行完就终止，在 ActivityThread 中使用 Looper 进入一个死循环中，保持 App 的状态。



#### 二、多线程安全问题

##### 1、锁机制？锁的种类？锁的状态？

<font color='red'>四种锁的状态：无锁、偏向锁、轻量级锁、重量级锁。</font>

- **自旋锁**

  <font color='red'>阻塞或唤醒一个 Java 线程需要操作系统切换 CPU 状态来完成，这种状态转换需要耗费处理器时间。如果同步代码中的内容过于简单，状态转换消耗的时间有可能比用户代码执行的时间还要长。</font>

  线程自旋说白了就是让 CPU 做无用功，比如：可以执行几次 for 循环，可以执行几条空的汇编指令，目的是占着 CPU 时间片不放，等待获取锁的机会。如果旋的时间过长会影响整体性能，时间短又达不到延迟阻塞的目的。

  <font color='red'>自旋锁的实现原理是 CAS</font>，AtomicInteger 中调用 unsafe() 进行自增操作的源码中的 do-while 循环就是一个自旋操作，如果修改数值失败则通过循环来执行自旋，直至修改成功。

  <font color='red'>适应性自旋锁</font>：自适应意味着自旋的时间（次数）不再固定，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。 

- **偏向锁**

  偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁，降低获取锁的代价。

  当锁对象第一次被线程获取的时候，虚拟机会将对象头中的标志位设为“01”，即偏向模式。同时使用 CAS 操作把获取到这个锁的线程 ID 记录在对象的 Mark Word 中，如果操作成功，持有偏向锁的线程以后每次进入这个锁相关的同步块时，虚拟机都可以不再进行任何同步操作。

  说白了就是置个变量，如果发现为 true 则无需再走各种加锁/解锁流程。

- **轻量级锁**

  https://blog.csdn.net/choukekai/article/details/63688332

  <font color='red'>轻量级锁的本意是在没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗。</font>

  - 在代码进入同步块的时候，如果此同步对象没有被锁定（锁标志位为“01”状态），虚拟机首先将在当前线程的栈帧中建立一个名为锁记录（Lock Record）的空间，用于存储锁对象目前的 Mark Word 的拷贝。
  - 然后虚拟机将使用 CAS 操作尝试将对象的 Mark Word 更新为指向 Lock Record 的指针。如果更新操作成功，那么这个线程就拥有了该对象的锁，并且对象 Mark Word 的锁标志位将转变为“00”，即表示此对象处于轻量级锁定状态。
  - 如果更新失败，虚拟机首先会检查对象的 Mark Word 是否指向当前线程的栈帧
    - 如果是说明当前线程已经拥有了这个对象的锁，那就可以直接进入同步块继续执行；
    - 否则说明这个锁对象已经被其他线程抢占了。

  <font color='red'>轻量级锁能提升程序同步性能的依据是“对于绝大部分的锁，在整个同步周期内都是不存在竞争的”。</font>

  适用场景：线程交替执行同步块的场合，如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁。

- **重量级锁**

  synchronized 通过 Monitor 来实现线程同步，Monitor 是依赖于底层的操作系统的 Mutex Lock（互斥锁）来实现的线程同步。“<font color='red'>阻塞或唤醒一个 Java 线程需要操作系统切换 CPU 状态来完成，这种状态转换需要耗费处理器时间</font>”，这种依赖于操作系统 Mutex Lock 所实现的锁我们称之为“重量级锁”。
  
- **乐观锁/悲观锁**

  乐观锁与悲观锁是一种广义上的概念，体现了看待线程同步的不同角度。

  - 悲观锁

    对于同一个数据的并发操作，悲观锁认为自己在使用数据的时候一定有别的线程来修改数据，因此在获取数据的时候会先加锁，确保数据不会被别的线程修改。Java中，synchronized关键字和Lock的实现类都是悲观锁。

  - 乐观锁

    乐观锁认为自己在使用数据时不会有别的线程修改数据，所以不会添加锁，只是在更新数据的时候去判断之前有没有别的线程更新了这个数据。如果这个数据没有被更新，当前线程将自己修改的数据成功写入。如果数据已经被其他线程更新，则根据不同的实现方式执行不同的操作（例如报错或者自动重试）。

    乐观锁在Java中是通过使用无锁编程来实现，最常采用的是CAS算法，Java原子类中的递增操作就通过CAS自旋实现的。

  > 适用场景：
  >
  > - 悲观锁：适合写操作多的场景，先加锁可以保证写操作时数据正确；
  > - 乐观锁：适合读操作多的场景，不加锁的特点能够使其读操作的性能大幅提升；

- **公平锁/非公平锁**

  - 公平锁是指多个线程按照申请锁的顺序来获取锁，线程直接进入队列中排队，队列中的第一个线程才能获得锁。

    > 优点：是等待锁的线程不会饿死。
    >
    > 缺点：是整体吞吐效率相对非公平锁要低，等待队列中除第一个线程以外的所有线程都会阻塞，CPU唤醒阻塞线程的开销比非公平锁大。

  - 非公平锁是多个线程加锁时直接尝试获取锁，获取不到才会到等待队列的队尾等待。但如果此时锁刚好可用，那么这个线程可以无需阻塞直接获取到锁，所以非公平锁有可能出现后申请锁的线程先获取锁的场景。

    > 优点：是可以减少唤起线程的开销，整体的吞吐效率高，因为线程有几率不阻塞直接获得锁，CPU不必唤醒所有线程。
    >
    > 缺点：是处于等待队列中的线程可能会饿死，或者等很久才会获得锁。

- **可重入锁/非可重入锁**

  可重入锁又名递归锁，是指同一个线程在外层方法获取锁的时候，再进入该线程的内层方法会自动获取锁（前提锁对象得是同一个对象或者class），不会因为之前已经获取过还没释放而阻塞。
  
  非可重入锁，某线程的第一个子流程可以获取到锁，该线程的其他子流程无法获取到这个锁，导致当前线程发生死锁，整个等待队列中的所有线程无法都无法被唤醒。
  
- **独享锁/共享锁**

  <font color='red'>独享锁也叫排他锁，是指该锁一次只能被一个线程所持有。</font>如果线程T对数据A加上排它锁后，则其他线程不能再对A加任何类型的锁。获得排它锁的线程既能读数据又能修改数据。JDK中的synchronized和JUC中Lock的实现类就是互斥锁。
  <font color='red'>共享锁是指该锁可被多个线程所持有。</font>如果线程T对数据A加上共享锁后，则其他线程只能对A再加共享锁，不能加排它锁。获得共享锁的线程只能读数据，不能修改数据。

  独享锁和共享锁也是通过 AQS 来实现的。

- **互斥锁/读写锁**

  互斥锁，在访问共享资源之前进行加锁操作，在访问完成之后进行解锁操作。加锁后，任何其他试图再次加锁的线程都会被阻塞，直到当前进行解锁。

  读写锁，读写锁既是互斥锁，又是共享锁，read 模式是共享，write 是互斥。



<font color='red'>知乎问题：Java中线程同步锁和互斥锁有啥区别？</font>

> 不要钻概念牛角尖。这样没意义。
>
> 也许java语法层面包装成了sycnchronized或者明确的XXXLock，但是道理都是一样的。无非就是哪种写起来方便。
>
> 锁的目的就是避免多个线程对同一个共享的数据并发修改带来的数据混乱。
>
> 锁的实现要处理的大概就只有这4个问题：
>
> - “**谁拿到了锁“这个信息存哪里**（可以是当前class，当前instance的markword，还可以是某个具体的Lock的实例）
> - **谁能抢到锁的规则**（只能一个人抢到 - Mutex；能抢有限多个数量 - Semaphore；自己可以反复抢 - 重入锁；读可以反复抢到但是写独占 - 读写锁……）
> - **抢不到时怎么办**（抢不到玩命抢；抢不到暂时睡着，等一段时间再试/等通知再试；或者二者的结合，先玩命抢几次，还没抢到就睡着）
> - **如果锁被释放了还有其他等待锁的怎么办**（不管，让等的线程通过超时机制自己抢；按照一定规则通知某一个等待的线程；通知所有线程唤醒他们，让他们一起抢……）
>
> 有了这些选择，你就可以按照业务需求组装出你需要锁。
>
> ———— 更新一下———— 
>
> 关于“互斥”和“同步”的概念的答案很清楚了。
>
> - 互斥就是线程A访问了一组数据，线程BCD就不能同时访问这些数据，直到A停止访问了
> - 同步就是ABCD这些线程要约定一个执行的协调顺序。比如D要执行，B和C必须都得做完，而B和C要开始，A必须先得做完。
>
> 这是两种典型的并发问题。恰当的使用锁，可以解决同步或者互斥的问题。
>
> 你可以说Mutex是专门被设计来解决互斥的；Barrier，Semaphore是专门来解决同步的。但是这些都离不开上述对上述4个问题的处理。同时，如果遇到了其他的具体的并发问题，你也可以定制一个锁来满足需要。





##### 2、synchronized可以保证什么？

1. synchronized 可以保证原子性、可见性、有序性。

2. synchronized 的三种应用方式：

   - 修饰实例方法，作用于当前实例加锁，进入同步代码前要获得当前实例的锁；
   - 修饰静态方法，作用于当前类对象加锁，进入同步代码前要获得当前类对象的锁；
   - 修饰代码块，指定加锁对象，对给定对象加锁，进入同步代码块前要获得给定对象的锁；

3. synchronized 底层语义原理

   Java 虚拟机中的同步（Synchronization）基于进入和退出管程（Monitor）对象实现，无论是显式同步（有明确的 monitorenter 和 monitorexit 指令，即同步代码块）还是隐式同步都是如此。

   - 同步代码块

     编译成字节码后包含 monitorenter、monitorexit 两个字节码指令（包含 1 个 monitorenter 和 2 个 monitorexit，因为虚拟机要保证当异常发生时也能释放锁，2 个 monitorexit 一个是代码正常执行结束后释放锁，一个是在代码执行异常时释放锁）；

   - 同步方法

     编译成字节码后，方法的 flags 属性中会被标记为 ACC_SYNCHRONIZED 标志。当虚拟机访问一个被标记为 ACC_SYNCHRONIZED 的方法时，会自动在方法的开始和结束（或异常）位置添加 monitorenter 和 monitorexit 指令。

   > 关于 monitorenter 和 monitorexit，可以理解为一把具体的锁。在这个锁中保存着两个比较重要的属性：计数器和指针。
   >
   > - 计数器：代表当前线程一共访问了几次这把锁；
   > - 指针：指向持有这把锁的线程。
   >
   > 锁计数器默认为 0，当执行 monitorenter 指令时，如锁计数器值为 0，说明这把锁并没有被其它线程持有。那么这个线程会将计数器加 1，并将锁中的指针指向自己。当执行 monitorexit 指令时，会将计数器值减 1。



##### 3、synchronized 原理?优化？

###### 3.1 synchronized的原理

https://kaiwu.lagou.com/course/courseInfo.htm?courseId=67#/detail/pc?id=1863

要了解 synchronized 的原理需要先理清楚两件事情：对象头和 Monitor。

- **对象头**

  <font color='red'>Java 对象在内存中的布局分为 3 部分：对象头、实例数据、对齐填充。</font>当我们使用 new 创建一个对象的时候，JVM 会在堆中创建一个 instanceOopDesc 对象，这个对象包含了对象头以及实例数据。

  这个对象中有两个字段 _mark 和 _metadata，它们一起组成了对象头。

  其中 <font color='red'>_mark，一般称它为标记字段（Mark Word），主要存储了对象的 hashCode、分代年龄、锁标志位、是否偏向锁等。</font>

  32 位 Java 虚拟机的 Mark Word 的默认存储结构如下：

  ![img](https://s0.lgstatic.com/i/image3/M01/12/B8/Ciqah16ekKGAFBIfAAFvVAWQ_js924.png)默认情况下，没有线程进行加锁操作，所以锁对象中的 Mark Word 处于无锁状态。但是考虑到 JVM 的空间效率，Mark Word 被设计成为一个非固定的数据结构，以便存储更多的有效数据，它会根据对象本身的状态复用自己的存储空间。

  在 Java 6 之前，并没有轻量级锁和偏向锁，只有重量级锁，也就是通常说的 synchronized 的对象锁，锁标志为 10。从上图可以看出：<font color='red'>当锁是重量级锁时，对象头中 Mark Word 会用 30 bit 来指向一个“互斥量”，而这个互斥量就是 Monitor。</font>

- **monitor**

  Monitor 可以把它理解为一个同步工具，也可以描述为一种同步机制。实际上，它是一个保存在对象头中的一个对象。

  通过 monitor() 方法创建一个 ObjectMonitor 对象，而 ObjectMonitor 就是 Java 虚拟机中的 Monitor 的具体实现。因此 Java 中每个对象都会有一个对应的 ObjectMonitor 对象，这也是 Java 中所有的 Object 都可以作为锁对象的原因。

  ObjectMonitor 的结构：

  ```c++
  ObjectMonitor() {
     _header = NULL;
     _count = 0; 			// 用来记录该线程获取锁的次数
     _waiters = 0,
     _recursions = 0;		// 锁重入次数
     _object = NULL;
     _owner = NULL;		// 指向持有ObjectMonitor对象的线程
     _WaitSet = NULL; 	// 处于 wait 状态的线程，会被加入到 _WaitSet 队列
     _WaitSetLock = 0 ;
     _Responsible = NULL ;
     _succ = NULL ;
     _cxq = NULL ;
     FreeNext = NULL ;
     _EntryList = NULL ; 	// 处于等待锁 block 状态的线程，会被加入到该列表
     _SpinFreq = 0 ;
     _SpinClock = 0 ;
     OwnerIsThread = 0 ;
  }
  ```

  <font color='red'>当多个线程同时访问一段同步代码时，首先会进入 _EntryList 队列中，当某个线程通过竞争获取到对象的 monitor 后，monitor 会把 _owner 变量设置为当前线程，同时 monitor 中的计数器 _count 加 1，即获得对象锁。</font>

  <font color='red'>若持有 monitor 的线程调用 wait() 方法，将释放当前持有的 monitor，_owner 变量恢复为 null，_count 自减 1，同时该线程进入 _WaitSet 集合中等待被唤醒。若当前线程执行完毕也将释放 monitor（锁）并复位变量的值，以便其它线程进入获取 monitor（锁）。</font>

  > 实际上，ObjectMonitor 的同步机制是 JVM 对操作系统级别的 Mutex Lock（互斥锁）的管理过程，其间都会转入操作系统内核态。也就是说 synchronized 实现锁，在“重量级锁”状态下，当多个线程之间切换上下文时，还是一个比较重量级的操作。



###### 3.2 虚拟机对 synchronized 关键字的优化？

从 Java 6 开始，虚拟机对 synchronized 关键字做了多方面的优化，主要目的就是，避免 ObjectMonitor 的访问，减少“重量级锁”的使用次数，并最终减少线程上下文切换的频率。其中主要做了以下几个优化：**锁自旋、轻量级锁、偏向锁**。



##### 4、CAS 原理

CAS 全称 Compare And Swap（比较与交换），是一种无锁算法。在不使用锁（没有线程被阻塞）的情况下实现多线程之间的变量同步。java.util.concurrent 包中的原子类就是通过 CAS 来实现了乐观锁。

CAS 算法涉及到三个操作数：

- 需要读写的内存值 V；
- 进行比较的值 A；
- 要写入的新值 B；

当且仅当 V 的值等于 A 时，CAS 通过原子方式用新值 B 来更新 V 的值（“比较+更新“ 整体是一个原子操作），否则不会执行任何操作。一般情况下，“更新”是一个不断重试的操作。

CAS 存在的三个的问题：

- **ABA问题**。CAS 需要在操作值的时候检查内存值是否发生变化，没有发生变化才会更新内存值。但是如果内存值原来是 A，后来变成 B，然后又变成 A，那么 CAS 进行检查时会发现没有发生变化，但是实际上是有变化的。ABA 问题的解决思路就是在变量前面添加版本号。

  > JDK 从 1.5 开始提供 AtomicStampedReference 类来解决 ABA 问题。

- **循环时间长开销大**。CAS 操作如果长时间不成功，会导致其一直自旋，给 CPU 带来非常大的开销。

- **只能保证一个共享变量的原子操作**。

  > JDK 从 1.5 开始提供了 AtomicReference 类来保证引用对象之间的原子性，可以把多个变量放在一个对象里来进行 CAS 操作。



##### 5、AQS？

https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html

AQS 是通过一个共享变量来同步状态，变量的状态由子类去维护，而 AQS 框架做的是：

- 线程阻塞队列的维护
- 线程阻塞和唤醒

<font color='red'>AQS 核心思想是，如果被请求的共享资源空闲，那么就将当前请求资源的线程设置为有效的工作线程，将共享资源设置为锁定状态；如果共享资源被占用，就需要一定的阻塞等待唤醒机制来保证锁分配。这个机制主要用的是 CLH 队列的变体实现的，将暂时获取不到锁的线程加入到队列中。</font>

<font color='red'>AQS使用一个 Volatile 的 int 类型的成员变量 State 来表示同步状态，通过内置的 FIFO 队列来完成资源获取的排队工作，通过 CAS 完成对 State 值的修改。</font>

共享变量的修改都是通过 Unsafe 类提供的 CAS 操作完成的。AbstractQueuedSynchronizer 类的主要方法是 acquire() 和 release() ，下面四个方法由子类去实现：

```java
protected boolean tryAcquire(int arg)
protected boolean tryRelease(int arg)
protected int tryAcquireShared(int arg)
protected boolean tryReleaseShared(int arg)
```

acquire() 用来获取锁，返回 true 说明线程获取成功继续执行，一旦返回 false 则线程加入到等待队列中，等待被唤醒，release() 用来释放锁。一般来说实现的时候这两个方法被封装为 lock() 和 unlock() 中。

以非公平锁为例：

- 获取锁的流程

  ```java
  final void lock() {
      if (compareAndSetState(0, 1))//通过 CAS 来修改 State 状态，表示争抢锁的操作
          setExclusiveOwnerThread(Thread.currentThread());//设置当前获得锁的线程
      else
          acquire(1);//修改状态失败，尝试去获取锁
  }
  ```

  - 若通过 CAS 设置变量 State（同步状态）成功，即获取锁成功，则将当前线程设置为独占线程。
  - 若通过 CAS 设置变量 State 失败，即获取锁失败，则进入 acquire() 进行后续的处理。

  acquire() 定义在 AQS 类中：

  ```java
  public final void acquire(int arg) {
      if (!tryAcquire(arg) &&
          acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
          selfInterrupt();
  }
  ```

  > 1. tryAcquire() ：主要目的是尝试获取锁；（需要子类去实现）
  > 2. addWaiter() ：如果尝试获取锁失败则调用该方法将当前线程添加到一个等待队列中；
  > 3. acquireQueued()：处理加入到队列中的节点，通过自旋去尝试获取锁，根据情况将线程挂起或取消；

  ReentrantLock#NonfairSync#tryAcquire() 方法实现：

  ```java
  protected final boolean tryAcquire(int acquires) {
      return nonfairTryAcquire(acquires);
  }
  final boolean nonfairTryAcquire(int acquires) {
      final Thread current = Thread.currentThread();//获取当前执行的线程
      int c = getState();//获得 state 值
      if (c == 0) {//c=0 说明当前是无锁状态
          if (compareAndSetState(0, acquires)) {//通过cas操作修改state值为1
              setExclusiveOwnerThread(current);//设置当前线程持有独占锁
              return true;
          }
      }
      //如果是同一个线程来获取锁，则直接增加重入次数
      else if (current == getExclusiveOwnerThread()) {
          int nextc = c + acquires;
          if (nextc < 0) // overflow
              throw new Error("Maximum lock count exceeded");
          setState(nextc);
          return true;
      }
      return false;
  }
  ```

  获取锁失败后续流程

  - 首先调用 addWaiter() 方法，将当前获取锁失败的线程添加到一个等待队列的末端；

  - acquireQueued() 方法中并不会立即挂起该节点中的线程，因为在插入节点的过程中，之前持有锁的线程可能已经执行完毕并解锁，所以这里使用自旋再次去尝试获取锁。如果自旋操作还是没有获取到锁，那么就将该线程挂起（阻塞）。

    其中有个字段 waitStatus，根据不同的状态值进行不同的操作。

  > 总结获取锁的流程：
  >
  > - AQS 的模板方法 acquire() 通过调用子类自定义实现的 tryAcquire() 获取锁；
  > - 如果获取锁失败，通过 addWaiter() 将线程构造成 Node 节点插入到同步队列队尾；
  > - 在 acquireQueued() 方法中以自旋的方法尝试获取锁，如果失败则判断是否需要将当前线程阻塞，如果需要阻塞则最终执行 LockSupport(Unsafe) 中的 native API 来实现线程阻塞。

- 解锁流程

  ```java
  //ReentrantLock
  public void unlock() {
      sync.release(1);
  }
  
  //AQS
  public final boolean release(int arg) {
      if (tryRelease(arg)) {
          Node h = head;
          if (h != null && h.waitStatus != 0)
              unparkSuccessor(h);
          return true;
      }
      return false;
  }
  ```

  首先调用 tryRelease() 尝试释放锁，如果成功最终会调用 AQS 中的 unparkSuccessor() 来实现释放锁的操作。

  ```java
  private void unparkSuccessor(Node node) {
      //获取头结点 waitStatus
      int ws = node.waitStatus;
      if (ws < 0)
          compareAndSetWaitStatus(node, ws, 0);
  	//获取当前节点（实际是head节点）的下一个节点
      Node s = node.next;
      //如果下个节点是null或者下个节点是 CANCEL 状态，就找到的队列最开始的非 CANCEL 的节点
      if (s == null || s.waitStatus > 0) {
          s = null;
          //从尾部节点开始找，到队首，找到队列第一个waitStatus<0的节点
          for (Node t = tail; t != null && t != node; t = t.prev)
              if (t.waitStatus <= 0)
                  s = t;
      }
      //如果当前节点的下个节点不为空，而且状态<=0，就把当前节点unpark
      if (s != null)
          LockSupport.unpark(s.thread);
  }
  ```

  首先获取当前节点（实际上传入的是 head 节点）的状态，如果 head 节点的下一个节点是 null，或者下一个节点的状态为 CANCEL，则从等待队列的尾部开始遍历，直到寻找第一个 waitStatus 小于 0 的节点。

  如果最终遍历到的节点不为 null，再调用 LockSupport.unpark 方法，调用底层方法唤醒线程。 至此，线程被唤醒的时机也分析完毕。





##### 6、ReentrantLock原理？ReentrantLock和synchronized的区别？

###### 6.1 ReentrantLock原理

ReentrantLock 依赖于 AQS，底层是 CAS+volatile。

公平锁和非公平锁：公平锁就是通过同步队列来实现多个线程按照申请锁的顺序来获取锁，从而实现公平的特性。非公平锁加锁时不考虑排队等待问题，直接尝试获取锁，所以存在后申请却先获得锁的情况。

```java
protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        //hasQueuedPredecessors() 查询是否有线程等待获取的时间长于当前线程。
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0)
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```



###### 6.2 ReentrantLock和synchronized的区别？

| 类别     | synchronized                                                 | Lock（底层实现主要是 volatile + CAS）                        |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 存在层次 | Java 关键字，JVM实现同步                                     | Java 提供的类                                                |
| 锁的释放 | 1、已获取锁的线程执行完同步代码，释放锁<br/>2、线程执行发生异常，JVM会让线程释放锁 | 在finally必须释放锁，不然容易造成死锁                        |
| 锁的获取 | 假设A线程获得锁，B线程等待。如果A线程阻塞，B线程会一直等待。 | 分情况而定，Lock有多个锁获取的方式，大致就是可以尝试获得锁，线程可以不用一直等待。 |
| 锁状态   | 无法判断                                                     | 可以判断                                                     |
| 锁类型   | 可重入、不可中断、非公平                                     | 可重入、可中断、可公平                                       |
| 性能     | 少量同步                                                     | 大量同步                                                     |



##### 7、读写锁 ReentrantReadWriteLock

ReentrantReadWriteLock 有两把锁，一个读锁，一个写锁，合称“读写锁”。ReadLock 和 WriteLock 是靠内部类 Sync 实现的锁。Sync 是 AQS 的一个子类，这种结构在 CountDownLatch、ReentrantLock、Semaphore 里面也都存在。

在 ReentrantReadWriteLock 里面，读锁和写锁的锁主体都是 Sync，但读锁和写锁的加锁方式不一样。读锁是共享锁，写锁是独享锁。

当写锁被获取到时，后续的读写锁都会被阻塞，写锁释放之后，所有操作继续执行。



##### 8、单例模式

###### 8.1 volatile 在 DCL 中的作用？它是如何保证有序性的？

```java
public class Singleton {
    private static volatile Singleton sInstance = null;
    private Singleton() {}
    public void doSomething() {
        
    }
    public static Singleton getInstance() {
        if(sInstance == null) {//避免不必要的同步
            synchronized(Singleton.class) {
                if(sInstance == null) {//为了在为null的情况下创建实例
                    sInstence = new Singleton();
                }
            }
        }
        return sInstance;
    }
}
```

sInstance = new Singleton() 不是一个原子性操作，这句代码最终会被编译成多条汇编指令：

1. 给 Singleton 的实例分配内存
2. 调用 Singleton 的构造函数，初始化成员字段
3. 将 sInstance 对象指向分配的内存空间（此时 sInstance 就不是 null 了）

如果线程 A 执行到这一步，Java编译器乱序执行，步骤是1-3-2，此时如果还未执行第二步，切换到了线程 B，这时 sInstance 已经不为空了，这时 第一层判空拦不住线程 B 进来，如果线程 B 使用了单例类的成员字段，就可能会出现错误。

用 volatile 来修饰单例类实例，保证顺序执行。

<font color='red'>volatile 是通过内存屏障来保证执行顺序的。</font>

###### 8.2 **静态内部类方式的单例模式，怎样敢保证线程安全？**

```java
public class Singleton {
    private Singleton(){}
    
    public static Singleton getInstance() {
        return Holder.sInstance;
    }
    
    private static class Holder {
        private static final Singleton sInstance = new Singleton();
    }
}
```

当第一次加载 Singleton 类时并不会初始化 sInstance，只有在第一次调用 Singleton 的 getInstance() 时才会导致 sInstance 被初始化。

静态内部类不会随着外部类的加载而加载 ,只有静态内部类的静态成员被调用时才会进行加载 , 这样既保证的惰性初始化（Lazy-Initialazation），又由JVM保证了多线程并发访问的正确性。

在类加载过程中 加载、验证、准备、解析、初始化 几个过程中，初始化阶段，虚拟机会保证一个类的 `<clinit>()` 方法在多线程环境中被正确地加锁、同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的 `<clinit>()` 方法，其他线程都需要阻塞等待，直到活动线程执行 `<clinit>()` 方法完毕。

需要注意的是，其他线程虽然会被阻塞，但如果执行 `<clinit>()` 方法的那条线程退出 `<clinit>()` 方法后，其他线程唤醒之后不会再次进入 `<clinit>()` 方法。同一个类加载器下，一个类型只会初始化一次。

> **通过静态内部类实现的单例模式有哪些优点？**
>
> 1. 不用 synchronized，节省时间
> 2. 调用 getInstance() 的时候才会创建对象，不调用不创建，节省空间，类似懒汉式。
>





#### 三、多线程问题

##### 1、多线程如何实现？有哪些方式？

Java 多线程实现方式主要有四种：

- 继承 Thread 类

- 实现 Runnable 接口

  <font color='red'>建议使用 Runnable 的方式创建线程，避免单继承的局限性</font>

  Thread 类也是实现了 Runnable 接口，实现了 run() 方法，有一个 Runnable 类型的成员变量 target，即通过 Thread 构造器传入的 Runnable 实例对象。

- 通过 Callable 和 FutureTask 创建线程

- 通过线程池创建线程 ExecutorService



##### 2、多线程为何不安全？

多线程同时访问同一个共享变量时，可能造成共享变量的数据不准确。

##### 3、线程间同步的方法？

- synchronized 关键字
- volatile 关键字
- 可重入锁 ReentrantLock
- 使用局部变量实现线程同步 ThreadLocal
- 使用阻塞队列实现线程同步
- 使用原子变量实现线程同步 AtomicInteger



##### 4、线程同步相关的例题？

###### 4.1 如何让两个线程循环交替打印？

- LockSupport

  > LockSupport 是通过控制变量 _counter 来对线程阻塞唤醒进行控制的。可以在任何场合使线程阻塞，可以指定任何线程进行唤醒，并且不用担心阻塞和唤醒操作的顺序。
  >
  > 注意：连续多次唤醒的效果和一次唤醒是一样的。

- synchronized

  ```java
  public class ConcurrencyIssues {
      static Thread t1 = null, t2 = null, t3 = null, t4 = null;
      public static void main(String[] args) {
          char[] aI = "1234567".toCharArray();
          char[] aC = "ABCDEFG".toCharArray();
  
          t1 = new Thread(new Runnable() {
              @Override
              public void run() {
                  for (int i = 0; i < aC.length; i++) {
                      // 起始先打印一个字母
                      System.out.println(aC[i]);
                      // 打印完唤醒t2打印数字
                      LockSupport.unpark(t2);
                      // 自己阻塞，等待唤醒
                      LockSupport.park();
                  }
              }
          });
          t2 = new Thread(new Runnable() {
              @Override
              public void run() {
                  for (int i = 0; i < aI.length; i++) {
                      // 起始先阻塞等待
                      LockSupport.park();
                      // 被唤醒后打印数字
                      System.out.println(aI[i]);
                      // 唤醒t1
                      LockSupport.unpark(t1);
                  }
              }
          });
          //t1.start();
          //t2.start();
          
          Object lock = new Object();
          t3 = new Thread(new Runnable() {
              @Override
              public void run() {
                  for (int i = 0; i < aC.length; i++) {
                      synchronized (lock) {
                          System.out.println(aC[i]);
                          lock.notify();
                          try {
                              lock.wait();
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }
              }
          });
          t4 = new Thread(new Runnable() {
              @Override
              public void run() {
                  for (int i = 0; i < aI.length; i++) {
                      synchronized (lock) {
                          System.out.println(aI[i]);
                          lock.notify();
                          try {
                              lock.wait();
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }
              }
          });
          t3.start();
          t4.start();
      }
  }
  ```

  

- BlockingQueue

- SynchronizeQueue

###### 4.2 怎么中止一个线程，Thread.Interupt一定有效吗？

1. 在线程内设置标质量，在外面改变，使其循环判断时终止
2. stop()方法，已被弃用
3. 使用 interrupt 函数，但是此函数只会在线程循环结束后调用，如果需要强行停止任务要在循环中加上 interrupted 判断函数，及时跳出

没有可以强制线程终止的方法，interrupt() 方法可以用来请求终止线程。

当对一个线程调用 interrupt() 时，线程的中断状态将被置位。这是每一个线程都具有的 boolean 标志。每个线程都应该不时地检查这个标志，以判断线程是否被中断。

如果线程被阻塞，调用 interrupt() 将会抛出 InterruptedException。

- 若线程未阻塞，直接调用 interrupt()，在线程 run() 中通过循环判断 isInterrupted() 来跳出循环，结束任务；
- 若线程阻塞，调用 interrupt() 会抛出 InterruptedException，在 catch 异常中需要再次调用 interrupt()，重置中断状态，否则在循环中判断 isInterrupted()，会导致死循环。

###### 4.3 t1、t2、t3三个线程，如何让三个线程按照顺序依次打印1-100。手写。

- 三个线程按顺序打印可以使用 join；
- 依次打印1-100，可以使用 LockSupport 工具类中的 park()、unpark() 方法；

###### 4.4 怎样使得多个线程同时执行？

- 使用 CyclicBarrier
- 使用 CountDownLatch

> CyclicBarrier 和 CountDownLatch 都可以用来让一组线程等待其它线程。与 CyclicBarrier 不同的是，CountdownLatch 不能重新使用。







#### 四、线程池问题

https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html

##### 1、阿里编程规范不建议使用线程池，为什么？

> 【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
> Executors 返回的线程池对象的弊端如下:
>
> - FixedThreadPool 和 SingleThreadPool : 
>
>   传入的队列 LinkedBlockingQueue 允许的请求队列长度为 Integer.MAX_VALUE，理论上可以无限添加任务到线程池。当核心线程执行时间很长，而新提交的任务还在不断地插入到阻塞队列中，可能会堆积大量的请求，从而导致 OOM。
>
> - CachedThreadPool 和 ScheduledThreadPool : 
>
>   允许的创建线程数量为 Integer.MAX_VALUE，当核心线程耗时很久，线程池会尝试创建新的线程来执行提交的任务，当内存不足时就会导致 OOM 报无法创建线程的错误。



##### 2、为什么使用线程池？线程池的好处？

- 减少资源开销

  复用线程池中的线程，减少线程的创建和销毁所带来的性能开销

- 提升系统响应速度

  每次请求到来时，由于线程的创建已经完成，故可以直接执行任务，因此提高了响应速度。

- 提高线程的可管理性

  线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，因此，需要使用线程池来管理线程。



##### 3、线程池参数？

ThreadPoolExecutor 是线程池的真正实现，它的构造方法提供了一系列参数来配置线程池。

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         threadFactory, defaultHandler);
}
```

- **corePoolSize**

  线程池的核心线程数，默认情况下，核心线程会在线程池中一直存活，即使它们处于闲置状态。如果将 ThreadPoolExecutor 的 allowCoreThreadTimeOut 属性设置为 true，那么闲置的核心线程在等待新任务到来时会有超时策略，这个时间间隔由 keepAliveTime 所指定，当等待时间超过 keepAliveTime 所指定的时长后，核心线程就会被终止。

- **maximumPoolSize**

  线程池所能容纳的最大线程数，当活动线程数达到这个数值后，后续的新任务将会被阻塞。

- **keepAliveTime**

  非核心线程闲置的超时时长，超过这个时长，非核心线程就会被回收。当 ThreadPoolExecutor 的 allowCoreThreadTimeOut 属性设置为 true 时，keepAliveTime 同样会作用于核心线程。

- **unit**

  用于指定 keepAliveTime 参数的时间单位，这是一个枚举，常用的有毫秒、秒以及分钟等。

- **workQueue**

  线程池中的任务队列，通过线程池的 execute 方法提交的 Runnable 对象会存储在这个参数中。

- **threadFactory**

  线程工厂，为线程池提供创建新线程的功能。ThreadFactory 是一个接口，它只有一个方法：Thread newThread(Runnable r)。

- **handler**

  当线程池无法执行新任务时，可能是由于任务队列已满或者是无法成功执行任务，这个时候 ThreadPoolExecutor 会调用 handler 的 rejectedExecution 方法来通知调用者，默认情况下 rejectedExecution() 方法会直接抛出一个 RejectedExecutionException。ThreadPoolExecutor 为 RejectedExecutionHandler 提供了几个可选值：

  | 可选值              | 说明                                       |
  | ------------------- | ------------------------------------------ |
  | CallerRunsPolicy    | 只用调用者所在线程来运行任务。             |
  | AbortPolicy（默认） | 直接抛出RejectedExecutionException异常。   |
  | DiscardPolicy       | 丢弃掉该任务，不进行处理。                 |
  | DiscardOldestPolicy | 丢弃队列里最近的一个任务，并执行当前任务。 |



##### 4、线程池的分类

1. **Executors#newFixedThreadPool()，一种线程数量固定的线程池**

   ```java
   public static ExecutorService newFixedThreadPool(int nThreads) {
       return new ThreadPoolExecutor(nThreads, nThreads,
                                     0L, TimeUnit.MILLISECONDS,
                                     new LinkedBlockingQueue<Runnable>());
   }
   ```

   - 当线程处于空闲状态时，它们并不会被回收，除非线程池被关闭了。
   - 当所有的线程都处于活动状态时，新任务都会处于等待状态，直到有线程空闲出来。
   - 由于在传参中核心线程数和最大线程数均为 nThreads，即它只有核心线程并且这些核心线程不会被回收，这意味着它能够更加快速的响应外界的请求。
   - 没有超时机制，任务队列也没有大小的限制

2. **Executors#newCachedThreadPool()，一种线程数量不定的线程池**

   ```java
   public static ExecutorService newCachedThreadPool() {
       return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                     60L, TimeUnit.SECONDS,
                                     new SynchronousQueue<Runnable>());
   }
   ```

   - <font color='red'>核心线程数为0</font>，最大线程数为 Integer.MAX_VALUE，即最大线程数可以任意大；
   - 当线程池中的线程都处于活动状态时，线程池会创建新的线程来处理新任务，否则就会利用空闲的线程来处理新任务；
   - 线程池中的空闲线程都有超时机制，超市时长 60 秒；
   - 当整个线程池都处于闲置状态时，线程池中的线程都会超时而被停止，这个时候 CachedThreadPool 之中实际上是没有任何线程的，它几乎是不占用任何系统资源的；
   - <font color='red'>这类线程比较适合执行大量的耗时较少的任务；</font>

3. **Executors#newScheduledThreadPool()，一种支持执行定时任务的线程池**

   ```java
   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
       return new ScheduledThreadPoolExecutor(corePoolSize);
   }
   public ScheduledThreadPoolExecutor(int corePoolSize) {
       super(corePoolSize, Integer.MAX_VALUE,
             DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
             new DelayedWorkQueue());
   }
   ```

   - 核心线程数量固定，最大线程数任意大；
   - 超时时长 10 秒，当非核心线程闲置时会被立即回收；
   - 主要用于执行定时任务和具有固定周期的重复任务；

4. **Executors#newSingleThreadExecutor()，一种单线程化的线程池**

   ```java
   public static ExecutorService newSingleThreadExecutor() {
       return new FinalizableDelegatedExecutorService
           (new ThreadPoolExecutor(1, 1,
                                   0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>()));
   }
   ```

   - 只有一个核心线程，它确保所有的任务都在同一个线程中按顺序执行；
   - 意义在于统一所有的外界任务到一个线程中，这使得在这些任务之间不需要处理线程同步的问题；

##### 5、线程池原理？

ThreadPoolExecutor 执行任务时大致遵循如下规则：

1. 如果线程池中的线程数量未达到核心线程的数量，那么会直接启动一个核心线程来执行任务；
2. 如果线程池中的线程数量已经达到或者超过核心线程的数量，那么任务会被插入到任务队列中排队等待执行；
3. 如果任务队列已满，无法将任务插入到队列中。如果线程数量未达到线程池规定的最大值，那么会立刻启动一个非核心线程来执行任务。
4. 如果线程数量已经达到线程池规定的最大值，那么就拒绝执行此任务，ThreadPoolExecutor 会调用 RejectedExecutionHandler 的 rejectedExecution 方法来通知调用者。



线程池任务执行流程：

> 1. 使用线程池执行任务时，会调用 execute(Runnable r) 方法；
>    - 判断当前活跃线程数是否小于corePoolSize,如果小于，则调用addWorker创建线程执行任务；
>    - 如果不小于corePoolSize，则将任务添加到workQueue队列；
>    - 如果放入workQueue失败，则创建线程执行任务，如果这时创建线程失败(当前线程数不小于maximumPoolSize时)，就会调用reject(内部调用handler)拒绝接受任务。
> 2. addWorker() 方法中
>    - 根据参数中的是否核心线程 core，判断已有线程数是否大于最大线程数；
>    - 创建 Worker 对象，其构造函数中会通过线程工厂实例化一个线程；
>    - 启动这个线程，执行到 Worker 的 run() 中；
> 3. Worker 继承自 AQS，实现了 Runnable 接口，可作为一个任务执行，里面有一个 thread 成员变量，即工作线程，这个线程是由 threadFactory创建的，创建的线程时传入的就是当前Worker（<font color='red'>所以启动该线程 t.start() 时会执行 Worker 的run()</font>）；
> 4. runWorker()里面会获取 task 任务，getTask()，最终执行 task.run()，即我们自己通过线程池执行的 runnable 里的run()方法getTask()里面是通过 阻塞队列的 poll()或者 take()方法获取的。

![img](https://img-blog.csdnimg.cn/2018122411100636)



##### 6、线程池状态

| 运行状态   | 状态描述                                                     |
| :--------- | :----------------------------------------------------------- |
| RUNNING    | 能接受新提交的任务，并且也能处理阻塞队列中的任务。           |
| SHUTDOWN   | 关闭状态，不再接受新提交的任务，但却可以继续处理阻塞队列中已保存的任务。 |
| STOP       | 不能接受新任务，也不处理队列中的任务，会中断正在处理任务的线程。 |
| TIDYING    | 所有的任务都已终止了，workerCount（有效线程数）为 0。        |
| TERMINATED | 在 terminated() 方法执行完后进入该状态。                     |

##### 7、线程池最大线程数怎么确定的？

一般说来，大家认为线程池的大小经验值应该这样设置：（其中N为CPU的核数）

- 如果是CPU密集型应用，则线程池大小设置为N+1
- 如果是IO密集型应用，则线程池大小设置为2N+1（Android）

比如AsyncTask中对线程池的配置，核心线程书为 N+1，最大线程数为 2*N+1

##### 8、线程池怎样保证线程安全的？

ReentrantLock

volatile

线程池的ctl控制参数使用高低位，高位存储线程池状态，低位存储线程池线程数。依托于CAS+自旋也就是前面回答提到的乐观锁+多重检查机制，保证了线程池的状态变更和数量变更是线程安全的。所以在线程池状态变更和数量变更的并发操作时，不需要加锁。注意到线程池其实内部还是有一个锁的，不过锁定区间非常小，是用于保证线程池可执行状态时，可以正确的向workers中添加worker的。

worker 继承自 AQS




















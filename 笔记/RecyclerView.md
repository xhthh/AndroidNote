### RecyclerView

> 基于滑动场景解析RecyclerView的回收复用机制原理 https://www.jianshu.com/p/40820ea48457
>
>
> RecyclerView的回收复用机制解密 https://www.jianshu.com/p/9306b365da57
>
> RecyclerView的基本设计结构 https://www.jianshu.com/p/88314f56545d
>
> 让你彻底掌握RecyclerView的缓存机制 https://www.jianshu.com/p/3e9aa4bdaefd?utm_source=desktop&utm_medium=timeline
>
> RecyclerView源码分析 https://www.jianshu.com/p/f7f8814bd09a
>
> [再也不用担心问RecycleView了——面试真题详解 ](https://www.cnblogs.com/jimuzz/p/14040674.html)
>
> RecyclerView 和 ListView 使用对比分析 https://www.jianshu.com/p/f592f3715ae2?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin&from=singlemessage&isappinstalled=1
>
> RecyclerView源码解析 https://www.jianshu.com/p/c52b947fe064
>
> RecyclerView 问题汇总 https://juejin.cn/post/6844903837724213256

#### 1、缓存机制

通过 Recycler 完成的，主要用来缓存屏幕内 ViewHolder 以及部分屏幕外 ViewHolder；

```java
public final class Recycler {
    final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();
    ArrayList<ViewHolder> mChangedScrap = null;

    final ArrayList<ViewHolder> mCachedViews = new ArrayList<ViewHolder>();

    RecycledViewPool mRecyclerPool;

    private ViewCacheExtension mViewCacheExtension;

    static final int DEFAULT_CACHE_SIZE = 2;
}
```

Recycler 的缓存根据访问优先级从上到下可以分为 4 级，如下：

- 第一级缓存：mAttachedScrap、mChangedScrap；

  View 的 scrap 状态，是指 View 在 RecyclerView 布局期间进入分离状态的子视图。即它已经被 detach 了。这种 View 是可以立即被复用的。在复用时，如果数据没有更新，是不需要调用 onBindViewHolder() 的，如果数据更新了，则需要调用；

  这两个 ArrayList，主要用来缓存屏幕内的 ViewHolder，notify 时会产生很多 scrap 状态的 View。这里是根据 position 来获取缓存的；如果 notifyxxx() 时 data 已经被移除掉，那么其中对应的 ViewHolder 也会被移除掉；

- 第二级缓存：mCachedViews；

  用来缓存移除屏幕之外的 ViewHolder，默认情况缓存个数是 2，可以通过 setViewCacheSize() 来改变缓存的容量大小。如果 mCachedViews 已满，则会根据 FIFO 的规则将旧 ViewHolder 抛弃，添加新的 ViewHolder；

  mCachedViews 里存放的 ViewHolder 只有原本位置的卡位才能复用

- 第三级缓存：ViewCacheExtension；

  用户自定义

- 第四级缓存：RecyclerViewPool；

  用来缓存屏幕外的 ViewHolder，当 mCachedViews 中的个数已满，则从 mCachedViews 中淘汰出来的 ViewHolder 会先缓存到 RecyclerViewPool 中，且会将内部的数据清除，因此从 RecyclerViewPool 中取出来的 ViewHolder 需要重新调用 onBindViewHolder() 绑定数据。

  > 多个 RV 之间可以共享一个 RecycledViewPool，这对于多 tab 界面的优化效果会很显著。需要注意的是，RecycledViewPool 是根据 type 来获取 ViewHolder，每个 type 默认最大缓存 5 个。因此多个 RecyclerView 共享 RecycledViewPool 时，必须确保共享的 RecyclerView 使用的 Adapter 是同一个，或 view type 是不会冲突的。

  ViewPool 会根据不同的 item type 创建不同的 List，每个 List 默认大小为5个。

- 如果 RecyclerViewPool 中没有找到，则调用 Adapter#onCreateViewHolder() 创建一个新的 ViewHolder；



复用回收按不同场景：

RecyclerView 滑动，并不会涉及到 mChangedScrap 和 mAttachedScrap。

mChangedScrap 按命名，应该是数据发生改变，notifyxx() 的时候会从这里获取缓存；



举个栗子：

​	屏幕内显示 2 行item，每行 5 个，向下滑动

> 1. **向下滑动，先回收还是先复用？**
>
>    先复用，再回收，新一行的 5 个卡位先去目前的 mCachedViews 和 ViewPool 的缓存中寻找复用，没有就重新创建，然后移出屏幕的那一行再回收缓存到 mCachedViews 和 ViewPool 里面；
>
> 2. **为什么当 RecyclerView 再次向上滑动重新显示第一行的 5 个卡位时，只有后面 3 个触发了 onBindViewHolder()**
>
>    滑动场景下回收和复用涉及到的结构是 mCachedViews 和 ViewPool，大小分别为 2 和 5。所以第三行显示出来后，第一行 5 个被回收，2个缓存在 mCachedViews，3 个缓存到 ViewPool 中，至于是哪2个缓存在 mCachedViews，是由 LayoutManager 控制。
>
>    mCachedViews 里存放的 ViewHolder 只有原本位置的卡位才能复用，所以2个直接复用，3个需要重新绑定数据。
>
> 3. **滑出第4行，一共需要创建多少个 ViewHolder？接下去不管怎么滑动，都不会调用 onCreateViewHolder()，但是有时一行 5 个卡位需要重新绑定数据，有时一行只有3个需要重新绑定，为啥？**
>
>    滑出第4行时，先复用，此时 mCachedViews 中 2个，ViewPool 中3个，但是 mCachedViews 中的只有相同位置才能复用，所以需要重新创建两个，即一共创建17个；
>
>    有时只需要3个重新绑定，因为刚好滑动到 mCachedViews 缓存的位置，可以直接复用；



#### 2、数据绑定

notifyxxx() 更新数据，使用了观察者模式

在 setAdapter() 的时候 registerAdapterDataObserver() 注册观察者，notify 的时候遍历观察者进行通知 onChanged()，这里进行 requestLayout() 进行页面布局更新；



#### 3、LayoutManager

布局管理者，RecyclerView 在 onLayout() 的时候，利用它来 layoutChildren()

1. 测量子 View；
2. 对子 View 进行布局；
3. 对子 View 进行回收；
4. 子 View 的动画调度；
5. 负责 RecyclerView 的滚动实现；
6. ...



#### 4、RecyclerView 与 ListView 的区别？

1. 缓存机制不同
   - RecyclerView 缓存的是 ViewHolder，ListView 缓存的是 view；
   - 缓存层级不同，4 比 2；
2. 布局效果、常用功能不同
   - ListView 只有纵向效果，RecyclerView 还有横向、网格等效果；
   - RecyclerView 局部刷新；
   - RecyclerView 有动画效果；
   - 点击事件，ListView 有封装好的，RecyclerView 需要自己实现；
   - 嵌套滑动机制，RecyclerView 实现了，ListView 没有；
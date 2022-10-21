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
>
> 深入理解 RecyclerView 的缓存机制 https://blog.csdn.net/qq_21118431/article/details/106102184
>
> 详细聊聊 RecyclerView 缓存机制 https://jishuin.proginn.com/p/763bfbd55a86
>
> [深入理解Android RecyclerView的缓存机制](https://segmentfault.com/a/1190000040421118)



### 一、基本设计

- **ViewHolder**

  对于`Adapter`来说，一个`ViewHolder`就对应一个`data`。它也是`Recycler缓存池`的基本单元。

- **Adapter**

  它的工作是把`data`和`View`绑定，即上面说的一个`data`对应一个`ViewHolder`。主要负责`ViewHolder`的创建以及数据变化时通知`RecycledView`。

- **AdapterDataObservable**

  `Adapter`是数据源的直接接触者，当数据源发生变化时，它需要通知给`RecyclerView`。这里使用的模式是`观察者模式`。`AdapterDataObservable`是数据源变化时的被观察者。

- **RecyclerViewDataObserver**

  它是`RecycledView`用来监听`Adapter`数据变化的观察者。

- **LayoutManager**

  它是`RecyclerView`的布局管理者，`RecyclerView`在`onLayout`时，会利用它来`layoutChildren`,它决定了`RecyclerView`中的子View的摆放规则。但不止如此, 它做的工作还有:

  1. 测量子View
  2. 对子View进行布局
  3. 对子View进行回收
  4. 子View动画的调度
  5. 负责`RecyclerView`滚动的实现
  6. ...

- **Recycler**

  对于`LayoutManager`来说，它是`ViewHolder`的提供者。对于`RecyclerView`来说，它是`ViewHolder`的管理者，是`RecyclerView`最核心的实现。

  - scrap list
  - mCacheViews
  - RecycledViewPool

### 二、刷新机制

> 1、adapter.notifyxxx() 时 RV 的 UI 刷新逻辑，即 子View 是如何添加到 RV 中的？
>
> 2、在数据存在的情况下，滑动 RV 时 子View 是如何添加到 RV 并滑动的？

##### 1、adapter.notifyDataSetChanged()引起的刷新

根据观察者模式，adapter.notifyDataSetChanged() 会引起 requestLayout()，会触发 onMeasure() 和 onLayout()，主要逻辑在 onLayout()

##### 2、RecyclerView.onLayout

onLayout() 直接调用了 dispatchLayout()，dispatchLayout() 主要分为3步，dispatchLayoutStep1、2、3()，第一步用来存储当前子 View 的状态并确定是否要执行动画，第三步是用来执行动画的，第二步 dispatchLayoutStep2()，主要调用了 mLayout#onLayoutChildren() 将布局工作交给了 LayoutManager。

onLayoutChildren() 的布局逻辑：

1. 确定锚点，设置好 AnchorInfo；

2. 根据锚点view确定有多少布局空间 mLayoutState.mAvailable 可用；

3. 根据当前设置的 LinearLayoutManager 方向开始摆放子 View；

   使用 fill() 方法填充 view，在填充之前确定有多少布局空间可用。

##### 3、fill() 摆放子 view

`fill towards end` 是从 `锚点view` 向 RV 底部来摆放子 View。

fill() 中核心是调用  layoutChunk() 来不断消耗 mLayoutState.mAvailable，直至消耗完毕。

layoutChunk() 主要逻辑：

1. 从 Recycler 中获取一个 View；
2. 添加到 RV 中；
3. 调整 View 的布局参数，调用其 measure、layout 方法；

##### 4、RV 滑动时的刷新逻辑

RecyclerView 在 onTouchEvent() 中对滑动事件做了监听，然后派发到 scrollStep() 中，在 scrollStep() 中将滑动给处理交给了 mLayout，比如竖直方向上的滑动，scrollVerticallBy()，其中调用了 scrollBy() 方法，这个是 LinearLayoutManager 处理滑动的核心。

scrollBy()主要逻辑：

1. 根据布局方向和滑动的距离来确定可用布局空间 mLayoutState.mAvailable；

2. 调用 fill() 来摆放子 View；

3. 滚动 RecyclerView；

   调用了 mOrientationHelper.offsetChildren()，最终调用到了 RecyclerView#offsetChildrenVertical()，主要逻辑就是改变当前子 View 布局的 top 和 bottom 来达到滚动效果；



### 三、复用机制

##### 1、从 Recycler 中获取一个 ViewHolder 的逻辑

LayotuManager 会调用 Recycler#getViewForPosition() 来获取一个指定位置的 view，getViewForPosition() 会调用 tryGetViewHolderForPositionByDeadline()，这个方法是从 Recycler 中获取一个 ViewHolder 的核心方法。

tryGetViewHolderForPositionByDeadline() 主要逻辑：

1. 尝试`根据position`从`scrap集合`、`hide的view集合`、`mCacheViews(一级缓存)`中寻找一个`ViewHolder`；
2. 根据`stable id(用来表示ViewHolder的唯一，即使位置变化了)`从`scrap集合`和`mCacheViews(一级缓存)`中寻找一个`ViewHolder`；
3. 根据`position和viewType`尝试从用户自定义的`mViewCacheExtension`中获取一个`ViewHolder`；
4. 根据`ViewType`尝试从`RecyclerViewPool`中获取一个`ViewHolder`；
5. 调用`mAdapter.createViewHolder()`来创建一个`ViewHolder`；
6. 如果需要的话调用`mAdapter.bindViewHolder`来设置`ViewHolder`。
7. 调整`ViewHolder.itemview`的布局参数为`Recycler.LayoutPrams`，并返回Holder；

**即从几个缓存集合中获取`ViewHolder`，如果实在没有就创建。**

##### 2、不同情形下的 ViewHolder 的创建和缓存

- **由无到有**

  所有 ViewHolder 都是新创建的，即会调用 Adapter.createViewHolder() 和 Adapter.bindViewHolder()。

  这时候新创建的 ViewHolder 并不会被缓存起来。

- **在原有数据的情况下进行整体刷新**

  相当于用户做了下拉刷新的操作，此时 Recycler 复用了老的卡牌。

  `Recycler刷新机制`中，`LinearLayoutManager`在确定好`布局锚点View`之后就会把当前`attach`在`RecyclerView`上的`子View`全部设置为`scrap状态`:

  ```java
  void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
      ...
      onAnchorReady(recycler, state, mAnchorInfo, firstLayoutDirection);  // RecyclerView指定锚点，要准备正式布局了
      detachAndScrapAttachedViews(recycler);   // 在开始布局时，把所有的View都设置为 scrap 状态，即ViewHolder被标记为FLAG_TMP_DETACHED状态，并且其itemview的parent被设置为null。
      ...
  }
  ```

  detachAndScrapAttachedViews() 就是把所有 view 转成 ViewHolder 后保存到 Recycler#mAttachedScrap 集合中。

  复用时，LinearLayoutManager 会根据当前子view 的位置向 Recycler 要一个 view，最终会**尝试`根据position`从`scrap集合`、`hide的view集合`、`mCacheViews(一级缓存)`中寻找一个`ViewHolder`**；

  即如果`mAttachedScrap中holder`的位置和`入参position`相等，并且`holder`是有效的话这个`holder`就是可以复用的。

  情形二下所有的`ViewHolder`几乎都是复用`Recycler中mAttachedScrap集合`中的。

  **并且重新布局完毕后`Recycler`中是不存在可复用的`ViewHolder`的**？？？

- **滚动复用**

  滚出屏幕的 View 优先保存到 mCachedViews，如果 mCachedViews 中保存满了，就会保存到 RecyclerViewPool 中。

  RV 在滑动时会调用 fill() 来根据滚动的距离向 RV 填充子 View，在填充完子 View 后就会把滚出屏幕的 View 进行回收。

  recycleByLayoutState() 层层调用最终到 Recycler#recycleViewHolderInternal(holder)，主要逻辑：

  1. 检查`mCacheViews集合`中是否还有空位，如果有空位，则直接放到`mCacheViews集合`；
  2. 如果没有的话就把`mCacheViews集合`中最前面的`ViewHolder`拿出来放到`RecyclerViewPool`中，然后再把最新的这个ViewHolder放到`mCacheViews集合`；
  3. 如果没有成功缓存到`mCacheViews集合`中，就直接放到`RecyclerViewPool`；

  此时用户继续上滑时，会从 mCachedViews 中获取 ViewHolder，并且不需要调用 Adapter.bindViewHolder()。

  **所以在普通的滚动复用的情况下，`ViewHolder`的复用主要来自于`mCacheViews集合`, 旧的`ViewHolder`会被放到`mCacheViews集合`, `mCacheViews集合`挤出来的更老的`ViewHolder`放到了`RecyclerViewPool` 中。**

##### 3、预取功能（Prefetch）

这个功能是rv在版本25之后自带的，也就是说只要你使用了25或者之后版本的rv，那么就自带该功能，并且默认就是处理开启的状态，通过LinearLayoutManager的setItemPrefetchEnabled()我们可以手动控制该功能的开启关闭。

预取的管件类 GapWorker 是一个 Runnable，RV 在 onTouchEvent() 中触发预取的判断逻辑，根据 dx 和 dy 判断是否预取下一个可能要显示的 item 的数据，调用 mGapWorker.postFromTraversal(this, dx, dy);

这个 runnable 执行即将显示的 item 的预取操作。



##### 4、payload 局部刷新

如果 RV 的 itemView 布局比较复杂，调用 notifyItemChanged(position) 时会造成这个 itemView 所有的视图都重新布局显示，会造成性能损耗。

```java
public void onBindViewHolder(@NonNull VH holder, int position,
        @NonNull List<Object> payloads) {
}
```

比如 notifyItemChanged(position, payload)，可以根据 payload 去处理只刷新某个控件。

> notifyItemChanged(position) 第一次调用会走 onCreateViewHolder() 和 onBindViewHolder() 后边再次调用只会走 onBindViewHolder()；
>
> notifyItemChanged(position, payload) 调用时只会走 onBindViewHolder(holder, position, payloads)；

=======================================================================

### 四、缓存复用

##### 1、缓存机制

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

  一般调用adapter的notifyItemRangeChanged被移除的viewholder会保存到mChangedScrap，其余的notify系列方法(不包括notifyDataSetChanged)移除的viewholder会被保存到mAttachedScrap中。

  > ViewHolder 只有在满足下面情况才会被添加到 mChangedScrap：当它关联的 item 发生了变化（notifyItemChanged 或者 notifyItemRangeChanged 被调用），并且 ItemAnimator 调用 ViewHolder#canReuseUpdatedViewHolder 方法时，返回了 false。否则，ViewHolder 会被添加到AttachedScrap 中。
  >
  > canReuseUpdatedViewHolder 返回 “false” 表示我们要执行用一个 view 替换另一个 view 的动画，例如淡入淡出动画。 “true”表示动画在 view 内部发生。
  >
  > mAttachedScrap 在 整个布局过程中都能使用，但是 changed scrap — 只能在预布局阶段使用。

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



**一共 20 条数据，每页显示 5 个，列表向下滑动至底部，会调用多少次 onCreateViewHolder()？**

> 8次
>
> - 关闭 预取 功能；
>
> - 首次展示 创建了 5 个 itemView；
>
> - 向上滑动 2 个 创建 2 个新的，此时 滑出屏幕的 item 存在 mCachedViews 中，因为这个是按照 position 取的，只能是同位置的 item 才能复用，且 mCachedViews 大小为 2；
>
> - 继续向上滑动一个，又会创建一个新的 item，mCachedViews 中取出最早存入的一个，存进 RecycledViewPool 中，刚滑出去的存在 mCachedViews 中；
>
>   此时一共调用了 8 次 onCreateViewHolder()，即 5（满屏item数量）+ 2（mCachedViews size）+ 1
>
> - 再向上滑动会去 pool 中取；



**RecyclerView 缓存复用流程：**

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/5/3/171d878fdd157587~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

##### 2、数据绑定

notifyxxx() 更新数据，使用了观察者模式

在 setAdapter() 的时候 registerAdapterDataObserver() 注册观察者，notify 的时候遍历观察者进行通知 onChanged()，这里进行 requestLayout() 进行页面布局更新；



##### 3、LayoutManager

布局管理者，RecyclerView 在 onLayout() 的时候，利用它来 layoutChildren()

1. 测量子 View；
2. 对子 View 进行布局；
3. 对子 View 进行回收；
4. 子 View 的动画调度；
5. 负责 RecyclerView 的滚动实现；
6. ...



##### 4、RecyclerView 与 ListView 的区别？

1. 缓存机制不同
   - RecyclerView 缓存的是 ViewHolder，ListView 缓存的是 view；
   - 缓存层级不同，4 比 2；
2. 布局效果、常用功能不同
   - ListView 只有纵向效果，RecyclerView 还有横向、网格等效果；
   - RecyclerView 局部刷新；
   - RecyclerView 有动画效果；
   - 点击事件，ListView 有封装好的，RecyclerView 需要自己实现；
   - 嵌套滑动机制，RecyclerView 实现了，ListView 没有；



##### 5、ListView

ListView 的缓存有两级，RecycleBin 有两个对象 mActiveViews 和 mScrapViews，mActiveViews 是第一级，mScrapViews 是第二级。

- **Active View**：是缓存在屏幕内的ItemView，当列表数据发生变化时，屏幕内的数据可以直接拿来复用，无须进行数据绑定。
- **Scrap view**：缓存屏幕外的ItemView，这里所有的缓存的数据都是"脏的"，也就是数据需要重新绑定，也就是说屏幕外的所有数据在进入屏幕的时候都要走一遍getView（）方法。

当Active View和Scrap View中都没有缓存的时候就会直接create view。





##### 6、RecyclerView 使用中可以优化的地方

- RecycledViewPool 的使用，在 ViewPager + RecyclerView 的使用中可以使用一个 pool；

- 降低 itemView 的布局层次；

- diffUtil

- 视情况设置 imAnimator 动画

  RV 默认支持动画效果，在开启动画的情况下会额外处理很多的逻辑判断，notify 的增删改查操作都会对应相应的动画效果。如果不需要动画效果可以关闭，可以简化 RV 的内部处理逻辑。

- setHasFixedSize

  设置 RV 的固定高度，可以避免 RV 重复 measure 调用。

  在调用 notifyxxx() 方法的时候（除了 notifyDataSetChanged()）会执行到一个 triggerUpdateProcessor()，如果没有设置 hasFixedSize，会进行 requestLayout()，导致 View 树进行重绘，onMeasure、onLayout() 都会调用。

  最大的一个好处是，嵌套的 RV 不会触发 requestLayout()，从而不会导致外层的 RV 重绘。



### 五、RecyclerView 优化

https://blog.csdn.net/hxl517116279/article/details/107058425/

- 减少 `onCreateViewHolder` 调用次数

  - 两个数据源大部分相似时使用swapAdapter代替setAdapter

    > 这两个方法最大的不同之处就在于setadapter会直接清空rv上的所有缓存，而swapadapter会将rv上的holder保存到pool中，google提供swapadapter方法考虑到的一个应用场景应该是两个数据源有很大的相似部分的情况下，直接使用setadapter重置的话会导致原本可以被复用的holder全部被清空，而使用swapadapter来代替setadapter可以充分利用rv的缓存机制

  - 共用回收池

    > 对于一个页面中的多个RecyclerView,如果使用同一个Adapter，可以使用setRecycledViewPool(pool)，共用回收池，
    > 避免来每一个RecyclerView都创建一个回收池，特别是RecyclerView嵌套RecyclerView时候，内部的RecyclerView必定使用的都是同一个Adapter，这个时候就很有必要使用回收池了

  - 增加RecycledViewPool缓存数量

    > 此方法是拿空间换时间，要充分考虑应用内存问题，根据应用实际使用情况设置大小。

- 减少`onCreateViewHolder`执行时间

  - 减少item的过度绘制

    > 减少布局层级，尽量少的布局嵌套，尽量少的控件

  - Prefetch预取

    > 如果你使用的是RecyclerView默认的布局管理器，你自动的就得到了这些优化。但是如果你使用嵌套的RecyclerView，或者你自己写LayoutManager，则需要自己实现Prefetch，重写`collectAdjacentPrefetchPositions`

- 减少`onBindViewHolder`调用次数

  - 使用局部刷新

    > 如果必须用 notifyDataSetChanged()，那么最好设置 mAdapter.setHasStableIds(true)，并重写getItemId()来给每个Item一个唯一的ID。
    >
    > 这样，当我们刷新数据时，RecyclerView就能确认是否数据没有变化，ViewHolder也直接复用，减少重新布局的烦恼。但这个使用的前提是数据的id一定是唯一的。如果id不变，但数据发生变化，可能就不会刷新了。

  - 使用DiffUtil去局部刷新数据

    > 通过比对新、旧两个[数据集](https://so.csdn.net/so/search?q=数据集&spm=1001.2101.3001.7020)的差异，生成旧数据到新数据的最小变动，然后对有变动的数据项，进行局部刷新。

  - 视情况使用setItemViewCacheSize(size)来加大RecyclerView缓存数目，用空间换取时间提高流畅度

    > RecyclerView可以设置自己所需要的ViewHolder缓存数量，默认大小是2。如果对于可能来回滑动的RecyclerView，把CacheViews的缓存数量设置大一些，可以省去bindView的时间，加快布局显示。
    >
    > 此方法是拿空间换时间，要充分考虑应用内存问题，根据应用实际使用情况设置大小。

- 减少`onBindViewHolder`执行时间

  - 数据处理与视图绑定分离

    > onBindViewHolder这个方法是绑定数据，并且是在UI线程，如果在该方法进行耗时操作，将会影响滑动的流畅性。

  - 有大量图片时，滚动时停止加载图片，停止后再去加载图片

  - 使用setHasFixedSize避免requestLayout

    > 如果item的高度固定的话可以设置setHasFixedSize(true)，这样RecyclerView在onMeasure阶段可以直接计算出高度，不需要多次计算子ItemView的高度。
    >
    > setHasFixedSize(true)时如果是通过Adapter的增删改插方法去刷新RecyclerView，那么将不需要requestLayout()。如果是通过notifyDataSetChanged()刷新界面，还是会重新调用requestLayout()

  - 不要在onBindViewHolder中设置点击事件

    > onBindViewHolder中设置点击事件会导致快速滑动时重复创建很多对象，可以采取复用OnClickListener对象，然后在onBindViewHolder()方法中通过setTag(position) 和getTag() 的方式，来传递点击事件的position给listener。
    >
    > ```java
    > public class TestAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    >    ...
    >    @Override
    >    public void onBindViewHolder(final Holder holder, final int position) {
    >        holder.itemView.setOnClickListener(this);
    >        holder.itemView.setTag(position);
    >        ...
    >    }
    >    @Override
    >    public void onClick(View v) {
    >        int position = (Integer) v.getTag();
    >        Log.d("onClick", "testBtn" + String.valueOf(position));
    >    }
    > }
    > ```

- 其他

  - 对于RecyclerView，如果不需要动画，就把item动画取消

    > 默认在开启item动画的情况下会使rv额外处理很多的逻辑判断，notify的增删改操作都会对应相应的item动画效果，所以如果你的应用不需要这些动画效果的话可以直接关闭掉，这样可以在处理增删改操作时大大简化rv的内部逻辑处理。可以通过 ((SimpleItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false); 把默认动画关闭。

  - 使用getExtraLayoutSpace为LayoutManager设置更多的预留空间

  - 优化解耦 RecyclerView.Adapter

    > 我们在使用 RecyclerView 的时候，总会遇到多项 ItemType 的场景。随着业务复杂度的增加，ItemType 会越变越多，导致代码量越来越多，最终发展为 “上帝类”，需要设计出一种模式，使得增删改一种 ItemType 时的成本降到最低

### 六、RecyclerView 常见问题

#### 1、判断滑动到底部

https://www.cnblogs.com/joahyau/p/10874511.html

- 比较 lastItem 的 positon

  通过比较当前屏幕可见最后一个item的position和整个RV的最后一个item的position，是同一个则到达底部。

  ```java
  public static boolean isVisBottom(RecyclerView recyclerView){  
      LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();  
      //屏幕中最后一个可见子项的position
      int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();  
      //当前屏幕所看到的子项个数
      int visibleItemCount = layoutManager.getChildCount();  
      //当前RecyclerView的所有子项个数
      int totalItemCount = layoutManager.getItemCount();  
      //RecyclerView的滑动状态
      int state = recyclerView.getScrollState();  
      if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE){   
          return true; 
      } else {   
          return false;  
      }
  }
  ```

  

- 比较高度

  通过比较屏幕高度+View滑过的高度与View的总高度进行判断。

  ```java
  public static boolean isSlideToBottom(RecyclerView recyclerView) {    
     if (recyclerView == null) return false; 
     if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() 
          >= recyclerView.computeVerticalScrollRange())   
       return true;  
     return false;
  }
  ```

  computeVerticalScrollExtent()是当前屏幕显示的区域高度，computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，而computeVerticalScrollRange()是整个View控件的高度。

- 滚动实验法

  即尝试是否能滚动。

  ```java
  RecyclerView.canScrollVertically(1); // false表示已经滚动到底部
  RecyclerView.canScrollVertically(-1); // false表示已经滚动到顶部
  ```

  > 实际与第二种是相同的

- 计算 item 高度与 RV 高度比较

  算出滑过的item的距离，加上屏幕高度，再与RV高度比较。

  > 实际与方法二原理一致


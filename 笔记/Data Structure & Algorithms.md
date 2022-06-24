# Data Structure & Algorithms

## 数据结构

### 一、数组

### 二、链表

### 三、栈

### 四、树

#### 4.1 概念

- **二叉树**

  每个节点最多只有两个子节点。

- **满二叉树**

  一个二叉树的所有非叶子节点都存在左右孩子，并且所有叶子节点都在同一层级上，那么这个树就是满二叉树。

- **完全二叉树**

  最后一个节点之前的节点都齐全即可。

- **二叉查找树**

  - 如果左子树不为空，则左子树上所有节点的值均小于根节点的值；
  - 如果右子树不为空，则右子树上所有节点的值均大于等于根节点的值；
  - 左、右子树也都是二叉查找树；

  > 对于一个节点分部相对均衡的二叉查找树来说，如果节点总树是 n，那么搜索节点的时间复杂度为 O(logn)，和树的深度是一样的。
  >
  > 缺点：
  >
  > 极限情况，会变成线性查找，比如插入一组从大到小有序的数据。
  >
  > 解决方法：
  >
  > 二叉树的自平衡，比如红黑树、AVL树、树堆。

- **二叉堆**

- **红黑树**

  - 结点是红色或黑色；
  - 根结点是黑色；
  - 每个叶子结点都是黑色的空结点（NIL结点）；
  - 每个红色结点的两个子结点都是黑色（从每个叶子到根的所有路径上不能有两个连续的红色结点）；
  - 从任意结点到其每个叶子的所有路径都包含相同数目的黑色结点；

  > 当插入或删除结点的时候，红黑树的规则有可能被打破。这时候需要做出一些调整：
  >
  > - 变色
  >
  >   尝试把红色结点变为黑色，或者把黑色结点变为红色；
  >
  > - 左旋
  >
  > - 右旋



### 五、排序





## 算法

### 一、动态规划

动态规划(dynamic programming) 是一种分阶段求解策略问题的数学思想。它有三个概念：

- 最优子结构
- 边界
- 状态转移公式



#### 1、台阶问题

##### 1.1 递归

```java
//时间复杂度 O(2^n)，许多重复计算
private static int getClimbingWays(int n) {
    if (n < 1) {
        return 0;
    }
    if (n == 1) {
        return 1;
    }
    if (n == 2) {
        return 2;
    }
    
    return getClimbingWays(n-1) + getClimbingWays(n-2);
}
```



##### 1.2 备忘录算法

使用缓存，创建一个哈希表，每次把不同参数的计算结果存入表中，当遇到相同参数的时候，再从表中取出来。

```java
//时间复杂度和空间复杂度都为 O(n)
private static int getClimbingWays(int n, HashMap<Integer, Integer> map) {
    if (n < 1) {
        return 0;
    }
    if (n == 1) {
        return 1;
    }
    if (n == 2) {
        return 2;
    }
    if (map.contains(n)) {
        return map.get(n);
    } e lse {
    	int value = getClimbingWays(n-1) + getClimbingWays(n-2);
        map.put(n, value);
        return value;
    }
}
```



##### 1.3 动态规划，自底向上求解

```java
private static int getClimbingWays(int n) {
    if (n < 1) {
        return 0;
    }
    if (n == 1) {
        return 1;
    }
    if (n == 2) {
        return 2;
    }
    int a = 1;
    int b = 2;
    int temp = 0;
    for (int i = 3; i <= n; i++) {
        temp = a + b;
        a = b;
        b = temp;
    }
    return temp;
}
```



#### 2、金矿问题

10个工人，5座金矿，400kg/5人，500kg/5人，200kg/3人，300kg/4人，350kg/3人，求获得金矿最优收益？

把金矿数量设为n，工人数量设为w，金矿的含金量设为数组g[]，金矿所需开采人数设为数组p[]，设F（n，w）为n个金矿、w个工人时的最优收益函数，状态转移方程为：

- **F(n,w) = 0 (n=0或w=0)**

  问题边界，金矿数为0或工人数为0的情况。

- **F(n,w) = F(n-1,w) (n≥1, w<p[n-1])**
  当所剩工人不够挖掘当前金矿时，只有一种最优子结构。

- **F(n,w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1]) (n≥1,w≥p[n-1])**
  在常规情况下，具有两种最优子结构（挖当前金矿或不挖当前金矿）。

##### 2.1 递归求解

```java
/**
 * 时间复杂度为 O(2^n)
 * 做了很多重复的计算
 */
public static int getBestGoldMining(int w, int n, int[] p, int[] g) {
    if (w == 0 || n == 0) {
        return 0;
    }
    if (w < p[n - 1]) {
        return getBestGoldMining(w, n - 1, p, g);
    }
    return Math.max(getBestGoldMining(w, n - 1, p, g),
                getBestGoldMining(w - p[n - 1], n - 1, p, g) + g[n - 1]);
}
```

![image-20220623112041514](C:\Users\E480\AppData\Roaming\Typora\typora-user-images\image-20220623112041514.png)



##### 2.2 二维数组

**动态规划的另一个核心要点：自底向上求解。**

![image-20220623112213323](C:\Users\E480\AppData\Roaming\Typora\typora-user-images\image-20220623112213323.png)

```java
/**
 * F(n,w) = max(F(n-1,w), F(n-1,w-p[n-1])+g[n-1]) (n>1, w≥p[n-1]);
 * 时间复杂度和空间复杂度都是 O(nw)
 * @param w
 * @param p
 * @param g
 * @return
 */
private static int getBestGoldMiningV2(int w, int[] p, int[] g) {
    //创建表格
    int[][] resultTable = new int[g.length + 1][w + 1];
    //填充表格
    for (int i = 1; i <= g.length; i++) {
        for (int j = 1; j <= w; j++) {
            if (j < p[i - 1]) {
                resultTable[i][j] = resultTable[i - 1][j];
            } else {
                resultTable[i][j] = Math.max(resultTable[i - 1][j], 
                        resultTable[i - 1][j - p[i - 1]] + g[i - 1]);
            }
        }
    }
    //返回最后一个格子的值
    return resultTable[g.length][w];
}
```

##### 2.3 一维数组

优化空间复杂度，上面的表格除了第一行外每一行都是由上一行数据推导出来的，所以，在程序中并不需要保存整个表格，无论金矿有多少座，我们只保存1行的数据即可。在计算下一行时，要**从右向左**统计，把旧的数据一个一个替换掉。

```java
//空间复杂度为 O(n)
public static int getBestGoldMiningV3(int w, int[] p, int[] g) {
    //创建当前结果
    int[] results = new int[w + 1];
    //填充一维数组，外层金矿，内层人数
    for (int i = 1; i <= g.length; i++) {
        for (int j = w; j >= 1; j--) {
            if (j >= p[i - 1]) {
                results[j] = Math.max(results[j],
                        results[j - p[i - 1]] + g[i - 1]);
            }
        }
    }
    //返回最后1个格子的值
    return results[w];
}
```


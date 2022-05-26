### 一、Java基础

#### 1、多态的理解？有什么好处？

**定义：** 指允许不同类的对象对同一消息作出响应。即同一消息可以根据发送对象的不同而采用多种不同的行为方式。（发送消息就是函数调用）

**实现多态的技术成为：** <font color='red'>动态绑定（dynamic binding）</font>，是指在执行期间判断所引用对象的实际类型，根据其实际的类型调用其相应的方法。

**作用：** 消除类型之间的耦合关系。

**多态的三个必要条件：**

- 继承父类
- 重写父类的方法
- 父类的引用指向子类实例

**多态的好处：**

1. 可替换性，多态对已存在代码具有可替换性。
2. 可扩充性，多态对代码具有可扩充性。增加新的子类不影响已存在的类的多态性、继承性，以及其他特性的运行和操作。
3. 接口性，多态是超类通过方法签名，向子类提供了一个共同个接口，由子类来完善或者覆盖它而实现的。
4. 灵活性，它在应用中体现了灵活多样的操作，提高了使用效率。
5. 简化性，多态简化对应用软件的代码编写和修改过程，尤其在处理大量对象的运算和操作时，这个特点尤为突出和重要。

多态提现为**父类引用变量可以指向子类对象。**

**Java中多态的实现方法：** 接口实现、继承父类进行方法重写，同一个类中进行方法重载。

```java
Animal s = new Dog();
s.eat();
```

注意：

- 在使用多态后的父类引用变量调用方法时，会调用子类重写后的方法。
- 左编译，右运行，编译时变量不能调用子类独有的方法，会报错。



#### 2、== 与 equals的区别？

`==`：它的作用是判断两个对象的地址是不是相等。即判断两个对象是不是同一个对象。（基本数据类型==比较的是值，引用数据类型==比较的是内存地址）

> 因为 Java 只有值传递，所以，对于 == 来说，不管是比较基本数据类型，还是引用数据类型的变量，其本质比较的都是值，只是引用类型变量存的值是对象的地址。

`equals()`：它的作用也是判断两个对象是否相等，它是 Object 类中的方法，所以不能比较基本数据类型的变量。

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

- 默认情况下，如果类没有覆写该方法，使用的是 Object 下的 equals()，等价于通过“==”比较两个对象，比较的是两个对象的地址值
- 如果类覆写了该方法，比如 String 类重写了 equals()，比较的就是两个字符串对象的内容



#### 3、hashCode() 和 equals() 的区别？

##### 1）hashCode() 介绍：

hashCode() 的作用是获取哈希码，也称为散列码；它实际上是返回一个 int 整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。hashCode() 定义在 Object 类中，是一个本地方法，也就是用 c 语言或 c++ 实现的，该方法通常用来将对象的内存地址转换为整数之后返回。

```java
public native int hashCode();
```

散列表存储的键值对（key-value），它的特点是：能根据“键”快速的检索出对应的“值”。这其中就用到了散列码。（可以快速找到所需要的对象）

##### 2）为什么要有 hashCode？

以 HashSet 为例：

当你把对象加入 HashSet 时，HashSet 会先计算对象的 hashCode 值来判断对象加入的位置，同时也会与其他已经加入的对象的 hashCode 值作比较，如果没有相符的 hashCode，HashSet 会假设对象没有重复出现。但如果发现有相同 hashCode 值的对象，这时就会调用 equals() 方法来检查 hashCode 相等的对象是否相同。如果两者相同，HashSet 就不会让其加入操作成功。如果不相同的话，就会重新散列到其他位置。这样就大大减少了 equals() 的次数，提高了执行速度。

```java
public static void main(String[] args) {
    Key k1 = new Key(1);
    Key k2 = new Key(1);
    HashMap<Key, String> hm = new HashMap<Key, String>();
    hm.put(k1, "Key with id is 1");
    System.out.println(hm.get(k2));
    
    //hm.put(k2, "Key with id is 2");
    //System.out.println(hm.get(k1));//Key with id is 2，因为key相同，value2覆盖了value1
}


static class Key {
    private Integer id;

    public Key(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    //故意先注释掉equals和hashCode方法
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Key)) {
            return false;
        } else {
            return this.getId().equals(((Key) o).getId());
        }
    }

    public int hashCode() {
        return id.hashCode();
    }
}
```

> 比如使用自定义对象作为 HashMap 的 key，对象 Key 只有一个 id，认为 id 相同的即是同一个对象。
>
> - 如果不重写 hashCode() 的话
>
>   存 k1 时会使用 Object 的 hashCode() 方法，k1 对象的内存地址，所以取 k2 是取不到的，返回null；
>
> - 重写 hashCode，不重写 equals
>
>   仍然返回 null，因为虽然重写了 hashCode()，k1 和 k2 具有相同的 hash 值，但是 HashMap 使用链地址法处理哈希冲突，在数组相同位置可能通过链表存储着多个元素，在 hash 值相同的时候，通过 equals() 比较两个对象是否仙童。
>
>   由于没有重写 equals() 方法，使用的是 Object 中的 equals() 方法，比较的还是对象的内存地址，所以存 k1 取 k2 是取不到的，返回null。
>
>   重写两个方法就可以实现。





##### 3）hashCode 和 equals() 的关系？

- 不会创建“类对应的散列表”

  > 即不会在 HashMap、HashSet 等这些本质是散列表的数据结构中用到该类。
  >
  > 这种情况该类的 hashCode() 和 equals() 没有任何关系，equals() 用来比较该类的两个对象是否相等，而 hashCode() 没啥用，不用理会；

- 会创建“类对应的散列表”

  > 比如，把这个类作为 key 来存储其他的 value 的话，这种情况两者之间是有关系的：
  >
  > - 如果两个对象相等，那么它们的 hashCode 值一定相同。这里的相等是指，通过 equals() 比较两个对象返回 true；
  > - 如果两个对象的 hashCode 相同，它们并不一定相等。因为在散列表中，hashCode() 相等，即两个键值对的哈希值相等，但并不一定得出键值对相等，此时就出现了所谓的 **哈希冲突** 场景。



##### 4）为什么重写 equals() 时，必须重写 hashCode()？

hashCode() 和 equals() 关系如下：

- 如果两个对象相等，那么它们一定有相同的哈希值（hashCode）。
- 如果两个对象的哈希值相等，那么这两个对象可能相等，也可能不相等，需要再通过 equals() 来判断。

要考虑到类似HashMap、HashTable、HashSet的这种散列的数据类型的运用，当我们重写equals时，是为了用自身的方式去判断两个自定义对象是否相等，然而如果此时刚好需要我们用自定义的对象去充当hashmap的键值使用时，就会出现我们认为的同一对象，却因为hash值不同而导致hashmap中存了两个对象，从而才需要进行hashcode方法的覆盖。



#### 4、final、finally、finalize

- **final** 用于修饰类、方法、属性，作用分别是类不能被继承，方法不能被重写，属性值不能被修改。

  在方法参数前用final修饰，是为了防止数据在方法体中被修改：

  - 基本数据类型，值不能被修改
  - 引用数据类型，所指向的引用不能边，即不能将一个新的对象赋值给该值，但是可以修改引用变量中的属性值

- **finally**，是 Java 保证重点代码一定要被执行的一种机制。我们可以使用 try-finally 或者 try-catch-finally 来进行类似关闭 JDBC 连接、保证 unlock 锁等动作。

  <font color='red'>try catch finally  在try中return  finally还会执行吗？</font>

  ①try中有return, 会先将值暂存，无论finally语句中对该值做什么处理，最终返回的都是try语句中的暂存值。
  ②当try与finally语句中均有return语句，会忽略try中return。

- **finalize** 是基础类 java.lang.Object 的一个方法，它的设计目的是保证对象在被垃圾收集前完成特定资源的回收。finalize 机制现在已经不推荐使用。



#### 5、Java 泛型的作用？好处？什么是类型擦除？通配符？

Java 泛型（generics）是 JDK 5 中引入的一个新特性, 泛型提供了编译时类型安全检测机制，该机制允许程序员在编译时检测到非法的类型。泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。

好处：

1. 类型安全。类型错误现在在编译阶段就被捕获到了，而不是在运行时当作 `java.lang.ClassCastException`展示出来，将类型检查从运行时挪到编译时有助于开发者更容易找到错误，并提高程序的可靠性。
2. 消除了代码中许多的强制类型转换，增强了代码的可读性。
3. 为较大的优化带来了可能。



泛型一般有三种使用方式:泛型类、泛型接口、泛型方法。泛型对于集合类尤其有用。



**Java的泛型是伪泛型，这是因为Java在编译期间，所有的泛型信息都会被擦掉，这也就是通常所说类型擦除。**

**常用的通配符为： T，E，K，V，？**

- ？ 表示不确定的 java 类型
- T (type) 表示具体的一个java类型
- K V (key value) 分别代表java键值中的Key Value
- E (element) 代表Element



#### 6、重载和重写的区别

**重载：**

发生在同一个类中，方法名必须相同，参数类型不同、个数不同、顺序不同，方法返回值和访问修饰符可以不同。

重载就是同一个类中多个同名方法根据不同的传参来执行不同的逻辑处理。

重载发生在编译期。

**重写：**

重写发生在运行期，是子类对父类的允许访问的方法的实现过程进行重新编写。

1. 返回值类型、方法名、参数列表必须相同，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类。
2. 如果父类方法访问修饰符为 `private/final/static` 则子类就不能重写该方法，但是被 static 修饰的方法能够被再次声明。
3. 构造方法无法被重写

重写就是子类对父类方法的重新改造，外部样子不能改变，内部逻辑可以改变



#### 7、接口和抽象类的区别？

1. 接口的方法默认是 public，所有方法在接口中不能有实现(Java 8 开始接口方法可以有默认实现），而抽象类可以有非抽象的方法。
2. 接口中除了 static、final 变量，不能有其他变量，而抽象类中则不一定。
3. 一个类可以实现多个接口，但只能实现一个抽象类。接口自己本身可以通过 extends 关键字扩展多个接口。
4. 接口方法默认修饰符是 public，抽象方法可以有 public、protected 和 default 这些修饰符（抽象方法就是为了被重写所以不能使用 private 关键字修饰！）。
5. 从设计层面来说，抽象是对类的抽象，是一种模板设计，而接口是对行为的抽象，是一种行为的规范。

> 备注：
>
> 1. 在 JDK8 中，接口也可以定义静态方法，可以直接用接口名调用。实现类和实现是不可以调用的。如果同时实现两个接口，接口中定义了一样的默认方法，则必须重写，不然会报错。
> 2. jdk9 的接口被允许定义私有方法 。

总结一下 jdk7~jdk9 Java 中接口概念的变化：

1. 在 jdk 7 或更早版本中，接口里面只能有常量变量和抽象方法。这些接口方法必须由选择实现接口的类实现。
2. jdk8 的时候接口可以有默认方法和静态方法功能。
3. Jdk 9 在接口中引入了私有方法和私有静态方法。



#### 8、静态内部类和内部类的区别？

1. 是否能拥有静态成员
   静态内部类可以有静态成员(方法，属性)，而非静态内部类则不能有静态成员(方法，属性)。

2. 访问外部类的成员
   静态内部类只能够访问外部类的静态成员,而非静态内部类则可以访问外部类的所有成员(方法，属性)。

3. 静态内部类和非静态内部类在创建时有区别

   //假设类A有静态内部类B和非静态内部类C，创建B和C的区别为：
   A a=new A();
   A.B b=new A.B();
   A.C c=a.new C();

静态内部类：只是为了降低包的深度，方便类的使用，静态内部类适用于包含在类当中，但又不依赖与外在的类，不用使用外在类的非静态属性和方法，只是为了方便管理类结构而定义。在创建静态内部类的时候，不需要外部类对象的引用。

非静态内部类：持有外部类的引用，可以自由使用外部类的所有变量和方法。



#### 9、Java 反射效率低的原因？怎样提高效率？

Java反射机制是在运行状态时，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为Java语言的反射机制。



**getMethods() 和 getDeclaredMethods() 的区别？**

- public Method[] getMethods():获取所有"公有方法"；（包含了父类的方法也包含Object类）
- public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的)



**反射效率低的原因：**

- Method#invoke 方法会对参数做封装和解封操作
- 需要检查方法可见性
- 需要校验参数
- 反射方法难以内联
- JIT无法优化

**如何提高Java反射效率：**

1. 缓存class对象
2. 设置`method.setAccessible(true)`
   jdk在设置获取字段，调用方法的时候会执行安全访问检查，而此类操作会比较耗时，所以通过`setAccessible(true)`的方式可以关闭安全检查，从而提升反射效率。



#### 10、Exception 和 Error 的区别？

![img](https://camo.githubusercontent.com/6f4720a573cb252a0e7aa0bd08364fa9752f67e6/687474703a2f2f696d616765732e636e6974626c6f672e636f6d2f626c6f672f3439373633342f3230313430322f3131313232383038353932363232302e6a7067)



**1. Throwable**

Throwable是 Java 语言中所有错误或异常的超类。

Throwable包含两个子类: **Error** 和 **Exception**。它们通常用于指示发生了异常情况。

Throwable包含了其线程创建时线程执行堆栈的快照，它提供了printStackTrace()等接口用于获取堆栈跟踪数据等信息。

**2. Exception**

Exception及其子类是 Throwable 的一种形式，它指出了合理的应用程序想要捕获的条件。

**3. RuntimeException**

RuntimeException是那些可能在 Java 虚拟机正常运行期间抛出的异常的超类。

编译器不会检查RuntimeException异常。例如，除数为零时，抛出ArithmeticException异常。

**4. Error**

和Exception一样，Error也是Throwable的子类。它用于指示合理的应用程序不应该试图捕获的严重问题，大多数这样的错误都是异常条件。

和RuntimeException一样，编译器也不会检查Error。

Java将可抛出(Throwable)的结构分为三种类型：**被检查的异常(Checked Exception)，运行时异常(RuntimeException)和错误(Error)。**

**(01) 运行时异常** **定义**: RuntimeException及其子类都被称为运行时异常。

**特点**: Java编译器不会检查它。也就是说，当程序中可能出现这类异常时，倘若既"没有通过throws声明抛出它"，也"没有用try-catch语句捕获它"，还是会编译通过。例如，数组越界时产生的IndexOutOfBoundsException异常等，都属于运行时异常。

虽然Java编译器不会检查运行时异常，但是我们也可以通过throws进行声明抛出，也可以通过try-catch对它进行捕获处理。

如果产生运行时异常，则需要通过修改代码来进行避免。

**(02) 被检查的异常**

**定义**: Exception类本身，以及Exception的子类中除了"运行时异常"之外的其它子类都属于被检查异常。

**特点**: Java编译器会检查它。此类异常，要么通过throws进行声明抛出，要么通过try-catch进行捕获处理，否则不能通过编译。例如，CloneNotSupportedException就属于被检查异常。

被检查异常通常都是可以恢复的。

**(03) 错误**

**定义**: Error类及其子类。

**特点**: 和运行时异常一样，编译器也不会对错误进行检查。

当资源不足、约束失败、或是其它程序无法继续运行的条件发生时，就产生错误。程序本身无法修复这些错误的。例如，OutOfMemoryError就属于错误。

按照Java惯例，我们是不应该实现任何新的Error子类的！



**异常和错误的区别：异常能被程序本身处理，错误是无法处理。**



#### 11、代理模式？静态代理和动态代理？

##### 1）定义

为其他对象提供一个代理，以控制对这个对象的访问。

##### 2）使用场景

当无法或不想直接访问某个对象或访问某个对象存在困难时可以通过一个代理对象来间接访问，为了保证客户端使用的透明性，委托对象与代理对象需要实现相同的接口。

当一个类需要对不同的调用者提供不同的调用权限时，可以使用代理类来实现。

当我们需要扩展某个类的某个功能时，可以使用代理模式，在代理模式中进行简单扩展。

##### 3）代理模式的参与者

代理模式的角色分为四种：

- Subject ，抽象主题类。该类的主要职责是声明真实主题与代理的共同接口方法，该类既可以是一个抽象类也可以是一个接口。
- RealSubject，真实主题类。该类也被称为委托类或被代理类，该类定义了代理所表示的真实对象，由其执行具体的业务逻辑方法，而客户类则通过代理类间接地调用真实主题类中定义的方法。
- ProxySubject，代理类。该类也被称为委托类或代理类，该类持有一个对真实主题类的引用，在其所实现的接口方法中调用真实主题类中相应的接口方法执行，以此起到代理的作用。
- Client，客户类，即使用代理类的类型。

##### 3）代理模式的分类

- 静态代理，代理类是在编译时就实现好的。也就是说 Java 编译完成后代理类是一个实际的 class 文件。
- 动态代理，代理类是在运行时生成的。也就是说 Java 编译完成之后并没有实际的 class 文件，而是在运行时动态生成的类字节码，并加载到 JVM 中。

##### 4）动态代理

动态代理是指在运行时动态生成代理类。与静态代理相比，动态代理的好处：

1. 不需要为 RealSubject 写一个形式上完全一样的封装类，假如 Subject 中的方法很多，为每一个接口写一个代理方法也很麻烦。如果接口有变动，则目标对象和代理类都要修改，不利于系统维护。
2. 使用一些动态代理的生成方法甚至可以在运行时指定代理类的执行逻辑，从而大大提升系统的灵活性。



主要涉及两个类：

内部主要通过反射来实现的。

- java.lang.reflect.Proxy，这是生成代理类的主类，通过 Proxy 类生成的代理类都继承了 Proxy 类。Proxy 提供了用户创建动态代理类和代理对象的静态方法，它是所有动态代理的父类。
- java.lang.reflect.InvocationHandler，这里称他为“调用处理器”，它是一个接口。当调用动态代理类中的方法时，将会直接转接到执行自定义的 InvocationHandler 中的 invoke() 方法。即我们动态生成的代理类需要完成的具体内容需要自己定义一个类，而这个类必须实现 InvocationHandler 接口，通过重写 invoke() 方法来执行具体内容。

Proxy提供了如下两个方法来创建动态代理类和动态代理实例。

> static Class<?> getProxyClass(ClassLoader loader, Class<?>... interfaces) 返回代理类的java.lang.Class对象。第一个参数是类加载器对象（即哪个类加载器来加载这个代理类到 JVM 的方法区），第二个参数是接口（表明你这个代理类需要实现哪些接口），第三个参数是调用处理器类实例（指定代理类中具体要干什么），该代理类将实现interfaces所指定的所有接口，执行代理对象的每个方法时都会被替换执行InvocationHandler对象的invoke方法。
>
> static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) 返回代理类实例。参数与上述方法一致。



**Retrofit 中动态代理的分析**

> 接口 ApiService
>
> Proxy.newProxyInstance(classloader, interfaces, invocationHandler)
>
> newProxyInstance()方法会调用一个 native 方法生成一个 ApiService 接口的实现类
> 并返回一个该实现类的实例 apiService
>
> 我们在请求某一个接口时，会调用 apiService 对应的方法，即调用的是实现类中对应的方法
>
>
> 该实现类中的方法会调用 super.h.invoke()方法
>
> 每一个动态生成的实现类都继承自 Proxy，所以这里的 super.h 即 Proxy 中的 InvocationHandler 的实例
>
> 即super.h.invoke() 最终会回调到 newProxyInstance() 中传入的 invocationHandler 的 invoke() 方法
>
> 在 invoke() 方法中  我们可以在调用方法前后 做一些处理
>
> 比如在Retrofit 这里  我们通过 method 参数，获取到方法的注解、参数、返回值等信息，最终构建一个 OkHttpCall 发出一个网络请求





### 二、集合框架

![img](https://github.com/Snailclimb/JavaGuide/raw/master/docs/java/collection/images/Java-Collections.jpeg)

注：Deque 其实继承自 Queue



Java集合大致可以分为Set、List、Queue和Map四种体系。

其中Set代表无序、不可重复的集合；List代表有序、重复的集合；而Map则代表具有映射关系的集合。Java 5 又增加了Queue体系集合，代表一种队列集合实现。

Set、List、Queue 均继承自 Collection接口，Collection 继承自 Iterable接口。



#### 1、集合框架底层数据结构总结

##### 1.1 List

- ArrayList：Object[] 数组
- Vector：Object[] 数组
- LinkedList：双向链表

##### 1.2 Set

- HashSet（无序，唯一）：基于 HashMap 实现，底层采用 HashMap 来保存元素
- LinkedHashSet：HashSet 的子类，内部是通过 LinkedHashMap 来实现的
- TreeSet（有序，唯一）：红黑树（自平衡的排序二叉树）

##### 1.3 Map

- HshMap：JDK 1.8之前 是由数组+链表组成的，数组是主体，链表主要是为了解决哈希冲突而存在的。JDK 1.8以后当链表长度大于 8 时，将链表转化为红黑树，以减少搜索时间（将链表转换成红黑树前会判断，如果当前数组的长度小于64，那么会选择先进行数组扩容，而不是转换为红黑树）。
- LinkedHashMap：继承自 HashMap，结构同 HashMap。增加了一条双向链表，使得上面的结构可以保持键值对的插入顺序。同时通过对链表进行相应的操作，实现了访问顺序相关逻辑。
- HashTable：数组+链表组成，数组是主体，链表是为了解决哈希冲突而存在的。
- TreeMap：红黑树



#### 2、为什么使用集合？

数组的缺点是一旦声明之后，长度就不可变了；同时，声明数组时的数据类型也决定了该数组存储的数据的类型；而且，数组存储的数据是有序的、可重复的，特点单一。 但是集合提高了数据存储的灵活性，Java 集合不仅可以用来存储不同类型不同数量的对象，还可以保存具有映射关系的数据



#### 3、线程安全的容器有哪些？

- Vector：相比 ArrayList 使用了 synchronized 同步机制
- Hashtable：相比 HashMap 使用了synchronized 同步机制
- ConcurrentHashMap：JDK 1.7 使用 ReentrantLock 分段加锁，JDK 1.8 使用 synchronized + CAS 实现线程安全
- Stack：栈，继承自 Vector，用数组实现，使用 synchronized 同步机制



#### 4、ArrayList 

- **ArrayList有用过吗？它是一个什么东西？可以用来干嘛？**

  ArrayList就是数组列表，主要用来装载数据，当我们装载的是基本类型的数据int，long，boolean，short，byte…的时候我们只能存储他们对应的包装类，它的主要底层实现是数组Object[] elementData。

  与它类似的是LinkedList，和LinkedList相比，它的查找和访问元素的速度较快，但新增，删除的速度较慢。

  > **特点：查询效率高，增删效率低，线程不安全。使用频率很高。**

- **底层实现是数组，但是数组的大小是定长的，如果我们不断的往里面添加数据的话，不会有问题吗？**

  ArrayList可以通过构造方法在初始化的时候指定底层数组的大小。

  通过无参构造方法的方式ArrayList()初始化，则赋值底层数Object[] elementData为一个默认空数组Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {}所以数组容量为0，只有真正对数据进行添加add时，才分配默认DEFAULT_CAPACITY = 10的初始容量。

  ```java
  public ArrayList(int initialCapacity) {
      if (initialCapacity > 0) {
          this.elementData = new Object[initialCapacity];
      } else if (initialCapacity == 0) {
          this.elementData = EMPTY_ELEMENTDATA;
      } else {
          throw new IllegalArgumentException("Illegal Capacity: "+
                                             initialCapacity);
      }
  }
  ```

- **数组的长度是有限制的，而ArrayList是可以存放任意数量对象，长度不受限制，那么他是怎么实现的呢？**

  - 通过数组扩容的方式去实现的；

  - ArrayList 在添加第一个元素时，创建一个长度为10的数组，之后随着元素的增加，以1.5倍原数组的长度创建一个新数组，即10， 15， 22， 33,。。这样序列建立，将原来的元素拷贝到新数组之中，再把指向原数的地址换到新数组，如果数组长度达到上限，则会以 MAX_ARRAY_SIZE 或者 Integer.MAX_VALUE 作为最大长度，而多余的元素就会被舍弃掉。

    ```java
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);//1.5倍
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
    ```

- **1.7和1.8版本初始化的时候的区别么？**

  初始化的时候，1.7以前会调用this(10)才是真正的容量为10，1.7即本身以后是默认走了空数组，只有第一次add的时候容量会变成10。

- **增删很慢，你能说一下ArrayList在增删的时候是怎么做的么？主要说一下他为啥慢？**

  ArrayList 有指定index新增，也有直接新增的，在这之前他会有一步校验长度的判断 **ensureCapacityInternal**，就是说如果长度不够，是需要扩容的。在扩容的时候，老版本的jdk和8以后的版本是有区别的，8之后的效率更高了，采用了位运算，**右移**一位，其实就是除以2这个操作。

  - 直接新增

    ```java
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
    ```

  - 指定位置新增

    ```java
    public void add(int index, E element) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }
    ```

  > 效率低，主要是新增涉及到数组复制，如果涉及到扩容，那会更慢；

- **ArrayList（int initialCapacity）会不会初始化数组大小？**

  - **会初始化数组大小！但是List的大小没有变，因为list的大小是返回size的。**

  - 将构造函数与initialCapacity结合使用，然后使用 set() 会抛出异常，尽管该数组已创建，但是大小设置不正确。

    set() 方法会比较 index 和 size

    ```java
    private static void arrayListTest() {
        ArrayList<Integer> arrayList = new ArrayList<>(10);
        System.out.println(arrayList.size());
        arrayList.set(5, 1);
        //Exception in thread "main" java.lang.IndexOutOfBoundsException: Index 5 out of bounds for length 0
    }
    //ArrayList.java
    public E set(int index, E element) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }
    ```

- **ArrayList 是怎样实现删除的？删除一定很慢吗？** 

  删除还是用到了 copy 数组

  ```java
  public E remove(int index) {
      if (index >= size)
          throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
  
      modCount++;
      E oldValue = (E) elementData[index];
  
      int numMoved = size - index - 1;
      if (numMoved > 0)
          System.arraycopy(elementData, index+1, elementData, index,
                           numMoved);
      elementData[--size] = null; // clear to let GC do its work
  
      return oldValue;
  }
  ```

  > System.copy() 参数：
  >
  > - Object src : 原数组
  > - int srcPos : 从元数据的起始位置开始
  > - Object dest : 目标数组
  > - int destPos : 目标数组的开始起始位置
  > - int length : 要copy的数组的长度

  比方，我们现在要删除下面这个数组中的 index 为 5 的这个元素：

  ![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/1/8/16f80e37714701d0~tplv-t2oaga2asx-zoom-in-crop-mark:1304:0:0:0.awebp)

  那代码他就复制一个 index 5+1 开始到最后的数组，然后把它放到index开始的位置；index 5 的位置就成功被”删除“了其实就是被覆盖了，给了你被删除的感觉。

  同理他的效率也低，因为数组如果很大的话，一样需要复制和移动的位置就大了。

  > - 删除慢不慢取决于要删除的元素离数组末端有多远。
  > - ArrayList拿来作为堆栈来用还是挺合适的，push和pop操作完全不涉及数据移动操作。

- **是否线程安全？**

  ArrayList 是线程不安全的，线程安全的数组容器是 Vector。

  Vector的实现很简单，就是把所有的方法统统加上synchronized就完事了。

  你也可以不使用Vector，用Collections.synchronizedList把一个普通ArrayList包装成一个线程安全版本的数组容器也可以，原理同Vector是一样的，就是给所有的方法套上一层synchronized。

- **ArrayList 适合用来做队列吗？**

  队列一般是FIFO（先入先出）的，如果用ArrayList做队列，就需要在数组尾部追加数据，数组头部删除数组，反过来也可以。

  但是无论如何总会有一个操作会涉及到数组的数据搬迁，这个是比较耗费性能的。

  **所以不适合做队列。**

- **数组适合做队列吗？**

  数组是非常合适的。

  比如ArrayBlockingQueue内部实现就是一个环形队列，它是一个定长队列，内部是用一个定长数组来实现的。

  另外著名的Disruptor开源Library也是用环形数组来实现的超高性能队列，具体原理不做解释，比较复杂。

  简单点说就是使用两个偏移量来标记数组的读位置和写位置，如果超过长度就折回到数组开头，前提是它们是定长数组。

- **ArrayList 和 LinkedList 遍历性能比较？**

  论遍历ArrayList要比LinkedList快得多，ArrayList遍历最大的优势在于内存的连续性，CPU的内部缓存结构会缓存连续的内存片段，可以大幅降低读取内存的性能开销。



#### 5、HashMap

##### 5.1 HashMap的优势？

- 查询速度快，时间复杂度最快可达到O(1)
- 动态的可变长存储数据

##### 5.2 HashMap 底层原理

**put() 方法：**

![img](https://camo.githubusercontent.com/df1c3077b929873727c9970e3a48c0ef14fd094d/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d372f7075742545362539362542392545362542332539352e706e67)

1. 先根据 key 得到哈希值

2. 第一次调用时，初始化 table 数组，这一步在 resize() 中

3. 根据数组长度和哈希值得到元素在数组中的位置

4. 如果数组中该位置为空，则 newNode 将元素放置到该位置

5. 如果该位置元素不为空，哈希值相等且 key 通过 equals() 比较也相同，就替换掉原来的值

6. 如果是树，插入树中

7. 如果是链表，当前位置元素的 next 为空，即为链表尾端，则 newCode 将新元素添加到链表中，计数加1

   当链表数量超过 8 的时候，转换为红黑树，跳出循环

   如果当前元素的 next 不为空，且与新元素哈希值相等，key 相同，则跳出循环，此处不替换

   如果当前元素的 next 不为空，且与新元素哈希值或者 key 不同，则指针后移，继续遍历

8. 如果最终找到了链表中 key 相同的元素，替换 value，返回原value

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // table未初始化或者长度为0，进行扩容
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // (n - 1) & hash 确定元素存放在哪个桶中，桶为空，新生成结点放入桶中(此时，这个结点是放在数组中)
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    // 桶中已经存在元素
    else {
        Node<K,V> e; K k;
        // 比较桶中第一个元素(数组中的结点)的hash值相等，key相等
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
                // 将第一个元素赋值给e，用e来记录
                e = p;
        // hash值不相等，即key不相等；为红黑树结点
        else if (p instanceof TreeNode)
            // 放入树中
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        // 为链表结点
        else {
            // 在链表最末插入结点
            for (int binCount = 0; ; ++binCount) {
                // 到达链表的尾部
                if ((e = p.next) == null) {
                    // 在尾部插入新结点
                    p.next = newNode(hash, key, value, null);
                    // 结点数量达到阈值，转化为红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    // 跳出循环
                    break;
                }
                // 判断链表中结点的key值与插入的元素的key值是否相等
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    // 相等，跳出循环
                    break;
                // 用于遍历桶中的链表，与前面的e = p.next组合，可以遍历链表
                p = e;
            }
        }
        // 表示在桶中找到key值、hash值与插入元素相等的结点
        if (e != null) { 
            // 记录e的value
            V oldValue = e.value;
            // onlyIfAbsent为false或者旧值为null
            if (!onlyIfAbsent || oldValue == null)
                //用新值替换旧值
                e.value = value;
            // 访问后回调
            afterNodeAccess(e);
            // 返回旧值
            return oldValue;
        }
    }
    // 结构性修改
    ++modCount;
    // 实际大小大于阈值则扩容
    if (++size > threshold)
        resize();
    // 插入后回调
    afterNodeInsertion(evict);
    return null;
} 
```



**get() 方法：**

1. 根据哈希值得到元素在数组中的位置，数组长度大于 0，得到该位置的元素 first
2. 如果 first 不为空，且 key 相同，则返回
3. 如果 key 不相同，且 first.next 不为空，即是一个链表或者 tree
4. 如果是树，从树种获取
5. 如果是链表，遍历链表，找到哈希值和key都相同的元素
6. 如果都没有，返回 null

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        // 数组元素相等
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        // 桶中不止一个节点
        if ((e = first.next) != null) {
            // 在树中get
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            // 在链表中get
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```



##### 5.3 HashMap 扩容机制

- 默认容量为 16，负载因子为 0.75。Map 在使用过程中不断的往里面存放数据，当数量达到了 16 * 0.75 = 12 就需要将当前数组进行扩容，新数组为原来的 2 倍，扩容过程涉及到 rehash、复制数据等操作，所以非常消耗性能。
- 因此通常建议能提前预估 HashMap 的大小最好，尽量减少扩容带来的性能损耗。
- 数组初始化长度 =（需要存储的元素个数 / 负载因子）+ 1（<font color='red'>应该是2的次幂吧</font>）

##### 5.4 哈希值怎样计算的？元素位置怎样计算的？

```java
static final int hash(Object key) {
    int h;
    //主要是从速度，功效和质量来考虑的，减少系统的开销，也不会造成因为高位没有参与下标的计算，从而引起的碰撞。
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

- 先取 key 的 hashcode，记为 h；

- 将 h 与 其无符号右移 16位的结果进行异或运算；

  > 异或运算：两个数对应位置相同为0，相反为1；
  >
  > 按位与运算：两个数对应位置如果都是1，结果为1，否则是0；

- 目的是将高低位二进制特征混合起来；

  > 因为计算元素在数组中的位置 i = (n-1) & hash，比如数组长度为初始的 16，即 (16-1) & hash，15 的 二进制为 0000 0000 0000 0000 0000 0000 0000 1111，进行按位与运算，一般数组长度可能不会太长，高位的大部分是0，与运算后结果也是0，通常参与运算的都是低位，如果不进行移位和异或运算，将会丢失高位的特征，很可能导致两个hash值不同的对象在计算数组位置时，得到相同的值。
  >
  > **计算hash值时使用异或运算**能更好的保留各部分特征，如果采用 & 运算计算出来的值会向 0 靠拢；采用 | 运算计算出来的值会向 1 靠拢；
  >
  > 保证了对象的 hashCode 的 32 位值只要有一位发生改变，整个 hash() 返回值就会改变。尽可能的减少碰撞。

元素位置：

```java
i = (n - 1) & hash
```

n 是数组长度。

为了让元素分布更均匀



##### 5.5 HashMap 的链表是头插法还是尾插法？

JDK 1.8 之前是头插法，1.8 之后是尾插法。



##### 5.6 为什么 HashMap 长度是 2 的次幂？

- 目的是为了让哈希后的结果更均匀的分部，减少哈希碰撞，提升 HashMap 的运行效率；
- 因为计算位置的时候，是按位与运算，都是1得1，否则得0，如果是2的次幂，减1后，二进制低位都是1，通过与哈希值的按位与运算，可以完整的保留哈希值的低位值。
- 如果不是2的次幂，减1后，低位中有0，比如数组长度为7，(7-1)&hash，6的二进制为 0110，在计算数组中的位置时进行按位与运算，不管hash值中低位里是 0 或者是 1，结果都会是0，这样就会造成数组元素分布不均匀，而数组中的某些位置可能永远也用不到。数组利用率会很低。



##### 5.7 如果给定数据多少，设置 HashMap 数组的初始值为多少？

关键点：

- 负载因子 0.75
- 数组长度必须为 2 的次幂

> 因为当容器中的元素个数大于 capacity * loadfactor 时，容器会进行扩容 resize 为 2n；
>
> 比如 3 个数据，可以指定 size 为 3/0.75 = 4，同时 4 也是 2 的次幂
>
> 如果是 5 个数据，5/0.75 = 6.66，找大于且最靠近它的 2 的次幂，即 8；



##### 5.8 HashMap 数据结构

哈希表结构（链表散列：数组+链表）实现，结合数组和链表的优点。当链表长度超过 8 时，链表转换为红黑树。

##### 5.9 jdk 1.8 HashMap 有哪些改变

- 在java 1.8中，如果链表的长度超过了8，那么链表将转换为红黑树。（桶的数量必须大于64，小于64的时候只会扩容）；
- 发生hash碰撞时，java 1.7 会在链表的头部插入，而java 1.8会在链表的尾部插入；
- 在java 1.8中，Entry被Node替代(换了一个马甲）；



##### 5.10 拉链法导致的链表过深问题为什么用红黑树？为什么不一直使用红黑树？红黑树的理解？

- 选择红黑树是为了解决二叉查找树的缺陷，二叉查找树在特殊情况下会变成一条线性结构（这就跟原来使用链表结构一样，造成很深的问题），遍历会非常慢。

- 而红黑树在插入新数据后可能需要通过左旋，右旋、变色这些操作来保持平衡，引入红黑树就是为了查找数据快，解决链表查询深度的问题，我们知道红黑树属于平衡二叉树，但是为了保持“平衡”是需要付出代价的，但是该代价所损耗的资源要比遍历线性链表要少，所以当长度大于8的时候，会使用红黑树，如果链表长度很短的话，根本不需要引入红黑树，引入反而会慢。

  > 链表遍历平均查找的时间复杂度为 O(n)，n 是链表的长度；
  >
  > 红黑树有自平衡的特点，可以始终将查找的时间复杂度控制住 O(log(n))；
  >
  > 默认链表长度达到 8  就转换成红黑树，而当长度降到 6 就转换回去，提现了时间和空间的平衡的思想。
  >
  > **为什么是 8 **：如果 hash 计算的结果离散好的话，各个值均匀分布，很少出现链表很长的情况。当长度为 8  的时候概率仅为 0.00000006，所以通常情况下，并不会发生从链表向红黑树的转换。

- **红黑树**

  - 每个节点非红即黑；
  - 根节点总是黑色的；
  - 如果节点是红色的，则它的子节点必须是黑色的（反之不一定）；
  - 每个叶子节点都是黑色的空节点（NIL节点）；
  - 从根节点到叶节点或空子节点的每条路径，必须包含相同数目的黑色节点。



##### 5.11 HashMap、LinkedHashMap、TreeMap

- 区别
  - LinkedHashMap 保存了记录的插入顺序，在用 Iterator 遍历时，先取到的记录肯定是先插入的；遍历比 HashMap 慢；
  - TreeMap 实现 SortMap 接口，能够把它保存的记录根据键排序（默认按键值升序排序，也可以指定排序的比较器）
- 使用场景
  - HashMap：在 Map 中插入、删除和定位元素时；
  - TreeMap：在需要按自然顺序或自定义顺序遍历键的情况下；
  - LinkedHashMap：在需要输出的顺序和输入的顺序相同的情况下。





#### 6、ConcurrentHashMap 怎样保证线程安全的？

- JDK 1.7 中使用分段锁（ReentrantLock + Segment + HashEntry），相当于把一个 HashMap 分成多个段，每段分配一把锁，这样支持多线程访问。锁粒度：基于 Segment，包含多个 HashEntry。

- JDK 1.8 中使用 CAS + synchronized + Node + 红黑树。锁粒度：Node（首结

  点）（实现 Map.Entry）。锁粒度降低了。

> concurrent 包的通用化实现模式：
>
> 1. 首先，声明共享变量 volatile；
> 2. 然后，使用 CAS 的原子条件更新来实现线程之间的同步；
> 3. 同时，配合以 volatile 的读/写和 CAS 所具有的 volatile 读和写的内存语义来实现线程之间的通信；



> 一、JDK1.7中的ConcurrentHashMap
> 实现线程安全的原理：分段加锁
>
> 将原集合底层的数组拆分成很多个小数组：
> [数组]——>[数组1]，[数组2]，[数组3]，[数组4]…
> 每个数组都会对应一把不同的锁；
> 只有当多个线程操作同一个小数组时，才会发生线程串行的情形。
>
> 二、JDK1.8中的ConcurrentHashMap
> JDK1.8之后，进行了锁粒度的细化
>
> 又变成了一个大数组，假设两个线程同时对数组的第一个位置进行put操作，则会采取CAS策略，保证线程并发的安全性，同一时间只有一个线程成功执行CAS操作；
> 在插入元素的过程中，会给插入的数组元素加上synchronized锁，再基于链表和红黑树插入数据，也就是数组的一个元素对应了一把锁。





#### 7、LinkedList

- LinkedList 是基于链表的数据结构，地址是任意的，其在开辟内存空间的时候不需要等一个连续的地址，对新增和删除操作，LinkedList 比较占优势。
- LinkedList 适用于要头尾操作或插入指定位置的场景。
- 因为 LinkedList 要移动指针，所以查询操作性能比较低。










### gradle

##### 1、gradle Task

Task（任务）可以理解为 gradle 的执行单元，gradle 通过执行一个个 Task 来完成整个项目构建工作。

##### 2、自定义 Task

```groovy
task A {
    println 'this is task A'
}
```

在命令行执行 `gradlew A`

```groovy
D:\WorkSpace\Xht\AndroidNote>gradlew A

> Configure project :app
this is task A

BUILD SUCCESSFUL in 1s
```

打印日志是在 gradle 的配置（Configure）阶段执行的。

gradle 的构建生命周期包含 3 个部分：初始阶段、配置阶段、执行阶段。



在 task A 中添加 doFirst 闭包，再次执行：

```groovy
task A {
    println 'this is task A'
    doFirst {
        println 'this is in execute'
    }
}

> Configure project :app
this is task A

> Task :app:A
this is in execute
```

gradle 在运行期会执行所有 task 的配置语句，然后执行指定的 Task。



##### 3、Task 之间可以存在依赖关系

gradle 中的 Task 可以通过 `dependsOn` 来指定它依赖另一个 Task

```groovy
task B {
    dependsOn 'A'
    println 'this is task B'
    doFirst {
        println 'task B is executing'
    }
}


//执行 task B，A 也会执行
> Configure project :app
this is task A
this is task B

> Task :app:A
this is in execute

> Task :app:B
task B is executing
```

> gradle 会在配置 Configure 阶段，确定依赖关系。对于 Android 项目来说即为执行各个 module 下的 build.gradle 文件，这样各个 build.gradle 文件中的 task 的依赖关系就被确定下来了，而这个依赖关系的确定就是在 Configure 阶段。



##### 3、gradle 自定义方法

def

##### 4、系统预置 task

自定义 task 时，还可以使用系统提供的各种显示 task 来完成相应的任务。具体就是使用关键字 type 来指定使用的是哪一个 task。

##### 5、gradle project

在 Android 中每个 module 就对应着一个 project，gradle 在编译时期会为每一个 project 创建一个 Project 对象用来构建项目。这一过程是在初始化阶段，通过解析 settings.gradle 中的配置来创建相应的 Project。

可以在根 project 中统筹管理所有的子 project，这样写的好处是项目中所有 module 的配置都统一写在一个地方，统筹管理。比如经常会在主项目的 build.gradle 中添加包过滤，解决依赖冲突。



##### 6、buildSrc 统筹依赖管理



##### 7、gradle build 的生命周期

Gradle build 的生命周期主要分为三大部分：初始化阶段、配置阶段和执行阶段。

###### 初始化（Initialization）

Gradle 支持单工程或者多工程构建，初始化阶段的任务是确定有多少工程需要构建，创建整个项目的层次结构，并且为每一个项目创建一个 Project 实例对象。

如果是多工程构建，一般都会在根工程目录下声明一个 setting.gradle 脚本，在脚本中 include 所有需要参与构建的子工程，通过解析 setting.gradle 脚本，读取 include 信息，确定有多少个 Project 需要构建。

###### 配置阶段（Configuration）

配置阶段的主要任务是生成整个构建过程的有向无环图。

确定了所有需要参与构建的工程后，通过读取解析各个工程对应的 build.gradle 脚本，构造 Task 任务，并根据 Task 的依赖关系，生成一个基于 Task 的有向无环图 TaskExecutionGraph。

###### 执行阶段（Execution）

通过读取配置阶段生成有向无环图 TaskExecutionGraph，按顺序依次执行各个 Task，像流水线一样，一步一步构建整个工程，这也是构建过程中最耗时的阶段。



###### 生命周期回调

Gradle build 在各个生命周期执行阶段都提供了回调，我们可以通过 gradle 或者 Project 对象添加一些监听器

```groovy
gradle.addBuildListener(new BuildListener() {
    @Override
    void buildStarted(Gradle gradle) {
				// 开始构建
    }

    @Override
    void settingsEvaluated(Settings settings) {
				// settings.gradle执行解析完毕
    }

    @Override
    void projectsLoaded(Gradle gradle) {
				// Project初始化构造完成
    }

    @Override
    void projectsEvaluated(Gradle gradle) {
				// 所有Project的build.gradle执行解析完毕
    }

    @Override
    void buildFinished(BuildResult result) {
				// 构建结束
    }
})


gradle.taskGraph.whenReady() {
    println "taskGraph build ready"
}

gradle.taskGraph.beforeTask {
    println "$name beforeTask"
}

gradle.taskGraph.afterTask {
    println "$name afterTask"
}
```

通过这些生命周期的回调，我们可以根据实际的需求做一些自定义的操作，例如：

- 为指定的 Task 添加 Action；
- 获取各个阶段的耗时情况；
- 动态改变 Task 的依赖关系，插入一些自定义的 Task；



##### 8、Android 项目的构建流程

- aapt 工具打包资源文件，生成 R.java 文件；

- 处理 aidl 文件，生成相应的 Java 文件；

- 使用 java编译器 javac 编译项目源代码，生成 class文件；

- 使用 dex 工具转换所有的 calss 文件，生成 classes.dex 文件、压缩常量池、消除冗余信息；

- 使用 apkbuilder 打包生成 apk 文件；

- 对 apk 文件进行签名；

  签名之后 apk 文件根目录下会增加 META-INF 目录，包含三个文件：

  - MANIFEST.MF
  - [CERT].RSA
  - [CERT]

- 使用 zipalign 对签名后的 apk 文件进行对齐处理；

  对齐的主要过程是将 apk 包中所有的资源文件距离文件起始偏移为 4 字节整数倍，这样通过内存映射访问 apk 文件时的速度会更快。对齐的作用就是减少运行时内存的使用；

  

新建一个 Android 工程，执行 Android 项目的构建流程，在没有任何修改的情况下大概有30多个 Task 要执行。

关键 Task 说明：

- mergeDebugResources

  收集所有 AAR 中和源码中的资源文件，合并到一个目录下；

- processDebugManifest

  收集所有 AndroidManifest.xml 文件，合并为一个 Manifest 文件；

- processDebugResources

  通过 AAPT 生成 R.java 和资源索引文件及符号表；

- compileDebugJavaWithJavac

  使用 javac 将 Java 文件编译成 class 文件；

- transformClassesWithMultidexlistForDebug

  使用 MultiDex 才会有这个 task；

- transformClassWithDexBuilderForDebug

  合并 class 文件生成 dex 文件；

- packageDebug

  打包生成 apk；


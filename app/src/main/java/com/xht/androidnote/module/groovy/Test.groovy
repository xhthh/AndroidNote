import android.renderscript.ScriptGroup.Closure

//Groovy注释标记和Java一样，//或者/**/
//Groovy语句可以不用分号结尾
//Groovy中支持动态类型，即定义变量的时候可以不指定其类型。变量定义用关键字 def，注意，虽然def不是必须的，但是为了代码清晰，建议还是使用def关键字

def variable1 = 1
def variable2 = "fuck"
def int x = 1//变量定义时可以直接指定参数类型

//函数定义时，参数的类型可以不指定
String testFunction(arg1, arg2) {

}

//出了变量定义可以不指定类型外，函数的返回值也可以时无类型的
def nonReturnTypeFunc() {
    last_line//最后一行代码的执行结果就是本函数的返回值
}

//如果指定了函数返回类型，则可不必加关键字来定义函数
String getString() {
    return "I am a String"
}

//可以不使用return xxx来设置xxx为函数返回值。如果不使用return语句的话，则函数里最后一句代码的执行结果被设置成返回值。

def getSomething() {
    "getSomething return value"
    1000//如果这是最后一行代码，则返回类型为Integer
}

//注意，如果函数定义时候指明了返回值类型的话，函数中则必须返回正确的数据类型，否则运行时报错。如果使用了动态类型的话，你就可以返回任何类型了。

//-------------------------------------------

//单引号''中的内容严格对应Java中的String，不对$符号进行转义
def singleQuote = 'I am $ dollar'//输出就是 I am $ dollar

//双引号""的内容，如果字符中有$符号的话，则它会$表达式先求值
def doubleQuoteWithoutDollar = "I am one dollar"//输出 I am one dollar
def a = 1
def doubleQuoteWithDollar = "I am $a dollar"//输出I am 1 dollar

//三个引号'''xxx'''中的字符串支持随意换行
def multieLines = ''' begin
    line 1
    line 2
    end '''

//函数调用时可以不加括号
println("test") //---> println"test"
//注意，虽然写代码的时候，对于函数调用可以不带括号，但是Groovy经常把属性和函数调用混淆。
def getSomethings() {
    "hello"
}
//getSomethings()如果不加括号的话，Groovy会误认为getSomethings是一个变量。比如
//所以，调用函数要不要带括号：如果这个函数是Groovy API或者Gradle API中比较常用的，比如println，就可以不带括号。否则还是带括号。

//----------------------Groovy 的数据类型----------------------------
//基本数据类型
//Groovy世界中的所有事物都是对象。所以，int，boolean这些在Java中的基本数据类型，在Groovy代码中其实对应的是他们的包装数据类型。
//比如 int 对应 Integer，boolean 对应 Boolean

//容器类
//List：链表，其底层对应Java中的List接口，一般用ArrayList作为真正的实现类。
//Map：键-值表，其底层对应Java中的LinkedHashMap
//Range：范围，它其实是List的一种拓展

//变量定义：List变量由[]定义
def aList = [5, 'string', true]//元素可以是任何对象
//变量存取：可以直接通过索引存取，而且不用担心索引越界。如果索引超过当前链表长度，List会自动往该索引添加元素
assert aList[1] //==> 'string'
assert aList[5] //==> null，第6个元素为空
aList[100] = 100//设置第101个元素的值为100
assert aList[100] //==> 100
println aList.size //--->101个元素

//容器变量定义
//变量定义：Map变量由[:]定义，冒号左边是key，右边是value。key必须是字符串，value可以是任何对象
def aMap = ['key1': 'value1', 'key2': true]
//另外，key可以用''或""包起来，也可以不用引号包起来。比如
def aNewMap = [key1: "value1", key2: true]//其中key1和key2默认被处理成字符串"key1"和"key2"

//如果key不用引号包起来的话，也会带来一定的混淆

def key1 = "wowo"
def aConfusedMap1 = [key1: "who am i?"]
//aConfuseMap中的key1到底是"key1"还是变量key1的值“wowo”？显然，答案是字符串"key1"。
//如果要是"wowo"的话，则aConfusedMap的定义必须设置成：
def aConfusedMap2 = [(key1): "who am i?"]

//Map中的存取
println aMap.key1 //这种表达方式好像key就是aMap中的一个成员变量一样
println aMap['key2']
aMap.key3 = "i am map"//为map添加新元素

//Range
def aRange = 1..5 //Range类型的变量由begin值+两个点+end值表示
//aRange包含1,2,3,4,5这5个值
//如果不想包含最后一个元素，则
def aRangeWithoutEnd = 1..<5 //包含1,2,3,4这4个元素
println aRange.from
println aRange.to

/*
根据Groovy的原则，如果一个类中有名为xxyyzz这样的属性（其实就是成员变量），Groovy会自动为它添加getXxyyzz和setXxyyzz两个函数，用于获取和设置xxyyzz属性值。

注意，get和set后第一个字母是大写的
 */

//闭包，Closure，是一种数据类型，它代表了一段可执行的代码
def aClosure = {
        //闭包是一段代码，所以需要用花括号括起来
    String param1, int param2 ->//箭头前面是参数定义，后面是代码
        println "this is code"//这是代码，最后一句是返回值
        //也可以使用return，和Groovy中普通函数一样
}

//闭包定义格式
def xx = { paramters -> code }
def yy = {
    //无参数，纯code，不需要->符号
}

/*
闭包定义好后，要调用它的方法就是：

    闭包对象.call(参数)

    闭包对象(参数)
 */

//如果闭包没定义参数的话，则隐含有一个参数 it，和this的作用类似。it代表闭包的参数。
def greeting1 = { "Hello, $it!" }
assert greeting1('World')//--->'Hello, World!'
//等同于
def greeting2 = { it -> "Hello, $it!" }
assert greeting2('World')//--->'Hello, World!'

//但是，如果在闭包定义时，采用下面这种写法，则表示闭包没有参数！
def noParamClosure = { -> true }
//此时就不能给noParamClosure传参数了
noParamClosure("test")//--->报错

//-------------------闭包使用中的注意点-------------------------------
//1、省略圆括号
//闭包在Groovy中大量使用，比如很多类都定义了一些函数，这些函数最后一个参数都是一个闭包。
//Groovy中，当函数的最后一个参数是闭包的话，可以省略圆括号。
def testClosure(int a1, String b1, Closure closure) {
    //doSomething
    closure()//调用闭包
}
//没看懂???
testClosure(4, "test", {
    println "I am a  closure"
})
//应该是testClosure是一个函数，第三个参数本来应该加()，但是可以省略
testClosure(4, "test", ({
    println "I am a  closure"
}))

//Gradle中经常出现，闭包的调用
task hello {
    doLast {
        println 'Hello World!'
    }
}

//正常如下，doLast只是把一个Closure对象传了进去，很明显，它不代表这段脚本解析到doLast的时候就会调用print 'Hello World!'
task hello {
    doLast({
        println 'Hello World!'
    })
}


//2、如何确定Closure的参数
//查询API？？？


//--------------------脚本类、文件I/O和XML操作------------------------
//1、脚本中import其他类
//Groovy中可以像Java那样写package，然后写类。比如在文件夹com/xht/groovy/目录中放一个文件，Test.groovy
//Groovy中如果不声明public/private等访问权限的话，类及变量默认都是public

/*
    2、groovy脚本执行的细节
    ①Test.groovy被转换成一个Test类，从script派生。
    ②每一个脚本都会生成static main函数。当我们执行 groovy Test.groovy的时候，其实就是用Java去执行这个main函数。
    ③脚本中的所有代码都会放到run函数中。比如，println 'Groovy world'，这句代码实际上是包含在run函数里的。
    ④如果脚本中定义了函数，则函数会被定义在Test类中。

    注：groovyc是一个比较好的命令，读者要掌握它的用法。然后利用jd-gui来查看对应class的Java源码。

 */

//3、脚本中的变量和作用域
// 在Groovy的脚本中，很重要的一点就是脚本中定义的变量和它的作用域。举例：
def y = 1//--->这个s有def（或者指明类型，比如 int s = 1）
def printy() {
    println y//报错，y找不到
}

/*
  反编译后的class文件中
  printy被定义成test类的成员函数
  def y = 1，这句话是在run中创建的。所以，y = 1 从groovy代码上看好像是在整个脚本中定义的，但实际上printy访问不了它。
  printy 是test成员函数，除非 y 也被定义成test的成员函数，否则printy不能访问它

  只要定义时不加类型和def即可
 */
z = 1
def printz() {
    println z
}
//y也没有被定义成test的成员函数，而是在run的执行过程中，将y作为一个属性添加到Test实例对象中了。然后在printy中，先获取这个属性。

//虽然printy可以访问y变量了，但是假如有其他脚本却无法访问y变量。因为它不是Test的成员变量。

/*
import groovy.transform.Field;
@Field y = 1 <===在y前面加上@Field标注，这样，y就彻彻底底是test的成员变量了
 */


//---------------------------文件I/O操作---------------------------
//1、读文件

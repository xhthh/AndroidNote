package com.xht.androidnote.module.generic.java;

/**
 * PECS法则 (Producer Extends，Consumer Super)
 * <p>
 * 生产者 → extends/out → 协变 → 对象只作为返回值传出
 * 消费者 → super/in → 逆变 → 对象只作为参数传入
 */
public class GenericJavaTest {
    public static void main(String[] args) {
    }

    /**
     * 协变 → 能读不能写 (能用父类型去获取数据，不确定具体类型，不能传)
     *
     * @param response
     */
    public static void testEntityResponse(Response<? extends Entity> response) {
        Entity entity = response.getEntity();
        /*
            不确定response里this.entity的具体类型，但必定是Entity的子类，此时用set()方法是向下转型，存在风险；
            比如，this.entity是Article类型，但传入了Entity对象，类型转换异常
         */
        //response.setEntity(new Entity());
        //response.setEntity(new Article());
    }

    /**
     * 逆变 → 能写不能读 (能传入子类型，不确定具体类型，不能读，但可以用Object读)
     *
     * @param response
     */
    public static void testArticleResponse(Response<? super Article> response) {
        Object object = response.getEntity();
        //Entity entity = response.getEntity();
        //Article article = response.getEntity();
        /*
            this.entity 必定是Article或其父类，传入Article，即使它为Entity，向上转型，安全；
            传入Entity不行，如果它为Article，向下转型，存在风险；
         */
        //response.setEntity(new Entity());
        response.setEntity(new Article());
    }
}

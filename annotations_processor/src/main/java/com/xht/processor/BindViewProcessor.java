package com.xht.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xht.annotations.BindView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private HashMap<String, List<Element>> cacheElements = null;

    private HashMap<String, Element> cacheAllParentElements = null;

    /**
     * 每一个注解处理器类都必须有一个空的构造函数。
     * 这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnvironment参数。
     * ProcessingEnvironment提供很多有用的工具类Elements,Types和Filer。
     * * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementUtils = processingEnv.getElementUtils();
    }

    /**
     * 这相当于每个处理器的主函数main()。
     * 在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnvironment，可以让你查询出包含特定注解的被注解元素。
     * 以自定义注解BindView为例,这里可以查到所有注解了BindView的Activity。
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //扫描所有注解了BindView的Field,因为我们所有注解BindView的地方都是一个Activity的成员
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            //将所有子elements进行过滤
            addElementToCache(element);
        }

        if (cacheElements == null || cacheElements.size() == 0) {
            return true;
        }

        for (String parentElementName : cacheElements.keySet()) {
            //判断一下获取到的parent element是否是类
            try {
                MethodSpec.Builder bindViewMethodSpec = MethodSpec.methodBuilder("bindView")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class)
                        .addParameter(ClassName.get(cacheAllParentElements.get(parentElementName).asType())
                                , "targetActivity");

                List<Element> childElements = cacheElements.get(parentElementName);

                if (childElements != null && childElements.size() > 0) {
                    for (Element childElement : childElements) {
                        BindView bindView = childElement.getAnnotation(BindView.class);
                        //使用JavaPoet对方法内容进行添加
                        bindViewMethodSpec.addStatement(
                                String.format("targetActivity.%s = (%s) targetActivity.findViewById(%s)"
                                        , childElement.getSimpleName()
                                        , ClassName.get(childElement.asType()).toString()
                                        , bindView.id()));
                    }
                }

                //构造一个类,以Bind_开头
                TypeSpec typeElement = TypeSpec.classBuilder("Bind_"
                        + cacheAllParentElements.get(parentElementName).getSimpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(bindViewMethodSpec.build())
                        .build();

                //进行文件写入
                JavaFile javaFile = JavaFile.builder(
                        getPackageName((TypeElement) cacheAllParentElements.get(parentElementName)), typeElement)
                        .build();
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }

        return false;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    /**
     * 这里必须由开发者指定,该方法返回一个Set,作用是这个注解的处理器支持处理哪些注解。
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解类型
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    /**
     * 用来指定你使用的Java版本。通常这里返回SourceVersion.latestSupported()。
     * 然而，如果你有足够的理由只支持Java 7的话，你也可以返回SourceVersion.RELEASE_7。
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 缓存父Element对应的所有子Element
     * 缓存父Element
     *
     * @param childElement
     */
    private void addElementToCache(Element childElement) {
        if (cacheElements == null) {
            cacheElements = new HashMap<>();
        }

        if (cacheAllParentElements == null) {
            cacheAllParentElements = new HashMap<>();
        }

        //父Element类名
        String parentElementName = null;
        parentElementName = ClassName.get(childElement.getEnclosingElement().asType()).toString();

        if (cacheElements.containsKey(parentElementName)) {
            List<Element> childElements = cacheElements.get(parentElementName);
            childElements.add(childElement);
        } else {
            ArrayList<Element> childElements = new ArrayList<>();
            childElements.add(childElement);
            cacheElements.put(parentElementName, childElements);
            cacheAllParentElements.put(parentElementName, childElement.getEnclosingElement());
        }
    }
}

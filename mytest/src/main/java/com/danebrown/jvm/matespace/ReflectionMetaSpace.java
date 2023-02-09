package com.danebrown.jvm.matespace;

import cn.hutool.core.lang.Pair;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Triple;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@SpringBootApplication(scanBasePackages = {"com.danebrown.jvm.matespace"})
public class ReflectionMetaSpace implements CommandLineRunner {

    static Reflections reflections;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MatespaceRunner.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(ReflectionMetaSpace.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


    }

    @PostConstruct
    public void refresh() {
        Set<Class> usages = findAllClassesUsingReflectionsLibrary("com.danebrown.config");
        List<Triple<Class,Object, Method>> list = new ArrayList<>();
        for(Class cls:usages){
             Arrays.stream(cls.getMethods()).collect(Collectors.toSet()).forEach(method -> {
                 ConstructorAccess constructorAccess = ConstructorAccess.get(cls);
                 Object target = constructorAccess.newInstance();
                 if(method.getName().startsWith("get")){
                    list.add(Triple.of(cls,target,method));
                 }
             });
        }
        Scanner scanner = new Scanner(System.in);
        while (true){

            System.out.printf("请输入选项:\n" +
                    "1-reflectasm\n" +
                    "2-jdkreflect\n" +
                    "0-退出\n");
            int  option = scanner.nextInt();
            if(option == 0){
                break;
            }
            else if(option == 1){
                invoke(list);
            }else if(option==2){
                jdkInvoke(list);
            }
            log.info("结果：{}", usages);
        }

    }

    public void invoke(List<Triple<Class,Object, Method>> invoker){
        for (Triple<Class,Object,Method> p:invoker
        ) {
            MethodAccess access = MethodAccess.get(p.getLeft());
            Object targeet = p.getMiddle();
            String mtd = p.getRight().getName();
            try {
                for(int i=0;i<100;i++){
                    access.invoke(targeet, mtd);
                }
            }catch (Exception ex){

            }
        }
    }

    public void jdkInvoke(List<Triple<Class,Object, Method>> invoker){
        for (Triple<Class,Object,Method> p:invoker
        ) {
            Object targeet = p.getMiddle();
            Method mtd = p.getRight();
            try {
                for(int i=0;i<100;i++){
                    mtd.invoke(targeet);
                }
            }catch (Exception ex){

            }
        }
    }

    public Set<Class> findAllClassesUsingReflectionsLibrary(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
    }
}

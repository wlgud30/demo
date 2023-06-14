package com.example.demo.util.responseUtil;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CustomFailResponseAnnotations {
    CustomFailResponseAnnotation[] value() default {};
    // 필요한 다른 속성들을 추가할 수 있습니다.
}
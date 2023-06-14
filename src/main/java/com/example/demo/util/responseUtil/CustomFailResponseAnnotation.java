package com.example.demo.util.responseUtil;

import com.example.demo.enums.ExceptionEnum;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

@Target({ElementType.METHOD, ElementType.TYPE,ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CustomFailResponseAnnotations.class)
public @interface CustomFailResponseAnnotation {
    ExceptionEnum exception();
}

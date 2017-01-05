package com.zhongzhou.Excavator.Annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface BeanComparableProp {
	/** 
	 * If true, BeanComparer will go into the object, and compare all the parameters of it.  
	 * <br> Now only support normal Data Object, List and Map
	 * */
	public boolean isRecursion() default false;
}

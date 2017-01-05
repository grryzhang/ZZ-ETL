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
/** 
 * System will used .equals() to compare the properties which annotated as BeanCompareId
 * <br> If both of BeanCompareId properties are null, then they are difference.
 */
public @interface BeanComparePropId {

}


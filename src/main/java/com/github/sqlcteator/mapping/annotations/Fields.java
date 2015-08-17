package com.github.sqlcteator.mapping.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@comment 针对对象的字段进行标注。 <br/>
 *该注解标注在转换对象的成员变量上。<br/>
 *name参数，表示该成员变量对外对应的名称。不填，默认就是成员变量名；填写了，不论是作为转换对象还是被转换对象，都使用name值来进行转换匹配。<br/>
 *isMapping参数，表示，该对象的该成员变量是否需要填充值，默认为true，需要。如果设置为false，那么，即便Map中有对应的值，也不会被填充到对象中。
 *如果是两个对象进行转换，isMapping只影响转换后的那个对象，对被转换对象无效。
 *@author YangJian
 *@date 2013-7-11 上午11:47:02
 *@version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Fields {

	String name() default "";
	
	String sequenceName() default "";
	
	boolean isMapping() default true;
	
	boolean isPrimaryKey() default false;
	
}

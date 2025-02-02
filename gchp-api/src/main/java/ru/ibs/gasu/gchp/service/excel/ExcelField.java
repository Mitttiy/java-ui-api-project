package ru.ibs.gasu.gchp.service.excel;

import ru.ibs.gasu.dictionaries.Converter;
import ru.ibs.gasu.dictionaries.Dictionary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Для простых полей указывается только name.
 * Для полей которые справочники НСИ, необходимо указать dictionary и  <br>
 * намиенования метода для преобразолвангия в DTO converterMethod ({@link Converter}.
 * Для справочников из базы необходимо указывать converterClass, converterMethod и type != "DICT"
 *
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExcelField {
    String name();
    Class<?> converterClass() default Object.class;
    String converterMethod() default "";
    Dictionary dictionary() default Dictionary.DIC_GASU_GCHP_SPHERE; // whatever
    String type() default "DICT";
    ExcelService.SectionColor color() default ExcelService.SectionColor.DEFAULT;
}

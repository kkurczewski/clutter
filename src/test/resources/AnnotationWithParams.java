package test;

@io.clutter.TestElements.Aggregate(
        intValue = 123,
        stringValue = "abc",
        classValue = java.lang.Object.class,
        enumValue = io.clutter.TestElements.TestEnum.FOO,
        intArray = {456, 789},
        stringArray = {"def", "ghi"},
        classArray = {java.lang.Object.class, java.lang.Integer.class},
        enumArray = {io.clutter.TestElements.TestEnum.BAR, io.clutter.TestElements.TestEnum.FOO}
)
public class GeneratedClass {}
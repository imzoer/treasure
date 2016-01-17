Treasuress是SharedPreference的包装类，使用Java接口代替sp操作
================================
# 使用方法：
* 在继承了Treasure接口的接口中定义需要的操作
* 初始化Treasures.init(context);
* 使用Treasures.of(ConfigWithoutAnnotations.class).setUserName("xxxx");

# 使用要求：
* 接口要继承{@link Treasure}
* 接口函数支持以get和set开头，例如getXXX,setXXX,其中getXXX表示读取xxx的值，setXXX表示设置xxx的值
* 支持根据类名生成sp文件名。如果不使用{@link FileName}指定sp文件名，则根据接口的名称toLowerCase得到sp文件名

# key的生成规则：
* 如果有{@link Key}，用key()的值
* 对于GET函数，所有参数用于拼接key(第一个参数如果是被{@link DefaultValue}注解，则不用作默认值，不用作拼接key)；对于SET函数，如果args长度大于1，那么除最后一个参数，前面所有的参数用于拼接key。每个参数之间以_分隔
* 如果args长度为1，用methodName生成key，比如getXXX，则生成的key是xxx

# 有两种方式可以指定从sp读取时候的default值：
* 通过给get函数添加{@link Default}注解
* 通过给get函数的第一个参数添加{@link DefaultValue}注解

# 实现原理：
* 使用java动态代理，将对接口的调用直接根据接口的定义（包括解析annotation）将解析出来的数据存储到sp中。

# 思路来源：
* 使用接口的思路参考https://github.com/baoyongzhang/Treasure
* 但是没有使用APT，简化了实现方法。但是由于使用了proxy，性能会受到影响。

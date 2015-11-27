Treasuress是SharedPreference的包装类，使用Java接口代替sp操作


使用方法：

1、在继承了Treasure接口的接口中定义需要的操作

2、初始化Treasures.init(context);

3、使用Treasures.of(ConfigWithoutAnnotations.class).setUserName("xxxx");


使用要求：

1、接口要继承{@link Treasure}

2、接口函数支持以get和set开头，例如getXXX,setXXX,其中getXXX表示读取xxx的值，setXXX表示设置xxx的值

3、支持根据类名生成sp文件名。如果不使用{@link FileName}指定sp文件名，则根据接口的名称toLowerCase得到sp文件名


key的生成规则：

1、如果有{@link Key}，用key()的值

2、对于GET函数，所有参数用于拼接key(临时方案；对于SET函数，如果args长度大于1，那么除最后一个参数，前面所有的参数用于拼接key。每个参数之间以_分隔

3、如果args长度为1，用methodName生成key，比如getXXX，则生成的key是xxx


暂未支持的功能：

1、指定sp的读取模式 private / multi process

2、GET函数传入参数作为default值(对应于key的生成规则部分，2的临时方案)

3、指定提交方式 commit/apply


实现原理：

使用java动态代理，将对接口的调用直接根据接口的定义（包括解析annotation）将解析出来的数据存储到sp中。


思路来源：

使用接口的思路参考https://github.com/baoyongzhang/Treasure，但是没有使用APT，简化了实现方法。

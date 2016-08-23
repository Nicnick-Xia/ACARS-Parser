<H1>ACARS报文解析工具</H1>

<H2>工具简述</H2>

本工具用于解析飞机与地面站通信的ACARS报文，是飞行跟踪系统的一个子功能模块。ACARS报文可以根据机型来区分，每种机型都有其固定的格式，本工具的编码思想就在于提取出这些相异的格式信息，将其以java正则表达式的形式，集中存放在几个yaml文件中（类似于配置文件），而主程序则主要关注如何自动利用这些格式信息去取出报文中的有效数据，并将其注入一个AcarsVo实体中，最后返回这些解析完成的实体。

##特性
###优势
* 格式信息与程序主体分离，程序主体较为简单易读
* 利用反射将正则表达式匹配的信息注入实体对应的属性，更加配置化
* 格式信息以正则表达式的形式在yaml文件中存放，添加或修改格式信息时更易于编写
* 格式信息按机型分类，更加契合文档对报文的分类
* 具备一定的错误检测机制，对于未能正常注入的数据，都能将其对应的字段名称打印在日志文件中，用于修正的参考
* 对于一些没有办法直接用一般正则表达式取出的数据，提供了postProcessing的方法，可以自由使用其他方式取出数据

###劣势
* 使用map映射来连接主程序和格式信息，在初始化时需要生成一定数据量的map
* 正则表达时有时并不直观，编写时需要仔细对照相关文档

##使用方法
###解析报文
- AcarsFileClient是此工具与用户程序的交互类。解析报文的步骤如下。
	1. 实例化一个`AcarsFileClient`类
	2. 读取文件，调用该类的`readAcarsFile`方法
		- 该方法接收一个String类型的路径(路径起点为该工程的工程文件夹)，返回一个存放该路径下所有文件的File数组，这一步根据需要自己实现也可以。
	3. 调用该类的`acarsParse`方法
		- 该方法接收一个File数组，返回一个AcarVo实体的List，即解析结果。


	```
	AcarsFileClient afc = new AcarsFileClient();
	File[] acarsFiles = afc.readAcarsFile(EsbConst.ACARS_ALL_PATH);
	List<AcarsVo> newAcarsVoList = afc.acarsParse(acarsFiles);
	```	

###增加／修改格式信息
####关于格式信息
- 格式信息存放在res目录下的patternYaml目录中，以正则表达式的形式存放在一个个分机型和报文类型的yaml文件中，命名为 “ 飞机型号＋报文类型＋Pattern ” ，例如A32X系列的Pattern命名为A32XOOOIPattern.yaml。以下为其具体内容示例


	```
acarsFltTypes:
  - 319_OOOI
  - 320_OOOI
  - 321_OOOI
  - 32E_OOOI
  - 32N_OOOI
regexBeans:
    - field: lastArinc
      regex: "\\.(\\w{7})\\s+\\d{6}"
      type: 1
    - field: dspDt
      regex: "\\.\\w{7}\\s+(\\d{6})"
      type: 1
    - field: telexDt
      regex: "(\\d{6})([A-Z]{4}|----)([A-Z]{4}|----)"
      type: 1
    - field: tailNr
      regex:"AN\\s+B-(\\d{4})"
      type: 1
    - field: fltNr
      regex: "FI\\s+([A-Z]{2}\\d+[A-Z]?)"
      type: 1
    - field: curLat
      regex: "LAT\\s*((N|S){0,1}\\s*\\d+\\.\\d+)"
      type: 1
    - field: curLon
      regex: "LON\\s*((E|W){0,1}\\s*\\d+\\.\\d+)"
      type: 1
    - field: depArpCd
      regex: "([A-Z]{4}|----)([A-Z]{4}|----)(\\r\\n)*\\d{4}"
      type: 1
    - field: arvArpCd
      regex: "([A-Z]{4}|----)([A-Z]{4}|----)(\\r\\n)*\\d{4}"
      type: 2
    - field: curFob
      regex: "(\\r\\n)*\\d{4}(\\s*\\d{1,4})(\\r\\n)*LON"
      type: 2
	```

- 第一部分`acarsFltTypes`，其子项就是适用于此格式信息的机型的一个汇总，表示这些型号的飞机的Acars报文格式是完全相同的。
- 第二部分`regexBeans`,用于存放一个**AcarsVo实体**的所有**字段**的格式信息。每一个子项配有3个属性：
	- `field`对应字段名称
	- `regex`即该字段对应报文数据的正则表达式
	- `type`应为int类型，此变量用于配置`RegExpUtil.find(regex,content,type)`的第三个参数，即**决定了取第几个括号里匹配的内容**，参考jdk正则表达式部分 `matcher.group(groupIndex)`，此处的`groupIndex`的意义与`type`完全相同
	
####修改之前的格式信息
- 直接操作这些yaml文件中对应字段的正则表达式或type值即可

####添加新机型的格式信息
- 步骤如下(只需要增加yaml文件)

1. 新建并编写新机型的yaml文件，如何命名和编写参考上文和源码，建议直接复制一个已有Pattern.yaml，在固定格式中修改。

	> 例如，新增B787的POS类型报文，在res的patternYaml目录中新建一个`B787POSPattern.yaml`的yams文件。
	
	
		acarsFltTypes:
 			- 787POS
		regexBeans:
			
	
	> 已知该机型报文中的`lastArinc`字段的格式为`“\\.(\\w{7})\\s+(\\d{6})”`取第一个括号对应的数据，则应在`regexBeans:`下添加如此一条

	```
	- field: lastArinc
      regex: "\\.(\\w{7})\\s+(\\d{6})"
      type: 1	
	```
2. 完成所有字段的`regexBeans`子项即可

###关于报错机制
如果想测试每个字段是否被反射正常注入，可以直接运行测试，如果该字段的正则表达式未匹配到任何字符，则在日志文件中会显示这个字段未找到匹配项，并同时提供报文类型和机型的信息，方便针对性排错。


###后续处理方法 postProcessing
这个方法位于`com.csair.newparse.parser.arc`的`AcarsPaser`类中。
这个方法的用处在于，对于一些无法通过正则表达式直接取出的数据，或者取出后需要进一步处理的数据，可以在这个方法中添加编码处理。
> 例如：从报文中获取到机尾号后需要后续处理，则将下面代码添加其中。

```
	if(acarsVo.getTailNr()!=null){
		acarsVo.setTailNr("B"+acarsVo.getTailNr().replace(" ", ""));
	}
```
##应用总结

本工具最大的特点就是解析的核心代码与格式信息分离，从而使得格式信息的增删改变得十分方便，便于维护和项目交接。其实这次的实践也是为以后做类似的解析组件提供了一个很好地参考，以后做大数据爬虫，或者其他运用正则表达式解析的工具时都可以使用类似的结构。
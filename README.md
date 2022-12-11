# 食用教程
## 修改配置
在icu.cming.service.impl.AoLanApiServiceImpl中配置url信息  
在icu.cming.AllTest中配置数据源信息
## dat文件
通过icu.cming.AllTest#generateDat1产生DaKaBoot.dat文件  
**该文件应该与DaKaBoot.jar可执行文件在同一目录下**
应用会根据该文件所序列化的对象执行任务
## 打包启动
cmd命令行:`` mvn clean;cp ./target/DaKaBoot.jar ./DaKaBoot.jar;mvn clean;java -jar DakaBoot.jar ``
## 运行查看
可通过控制台的输出或者工作目录下的logs文件夹下的.log文件查看运行过程  
运行结果可以在[这里](http://127.0.0.1:8086/)查看

***如果您有更好的idea欢迎email:cming0420@gmail.com或者直接Issuse***
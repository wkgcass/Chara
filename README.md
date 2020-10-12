# Chara

Chara是一款基于JavaFX的桌面人物软件。

目前做的角色支持了如下功能：

1. 随机动画：例如头发飘动、眨眼等
2. 点击人物的某些部位，人物给出对应的反馈
3. 眼睛跟随鼠标
4. 聊天功能：需要接入第三方chatbot
5. 互动好感度系统
6. 少量meta元素
7. 管理员指令
8. 模型文件封装

# 目前实现的角色

## 心璃

<img src="readme-bundle/kokori.png" width="128px" height="128px">

中文名：心璃  
英文名：Kokori  
日文名：心(ココ)璃(リ)  

设定：wkgcass  
原画：wkgcass  
模型：wkgcass  
编码：wkgcass

一句话人物概述：神社的巫女，外表柔弱内心坚强的小姐姐，少量病娇属性。

# 如何开发一个新角色

## 写代码前的准备工作

1. 做好设定和原画
2. 按人物组件绘图，并将各组件保存为透明底色的PNG文件
3. 使用Live2D Cubism之类的软件做出需要的形变样式，中间帧全部保存为透明底色的PNG文件
4. 输出的PNG文件不要做切割，保持原来的大小和位置即可，图片大小就是画布大小

## 模型文件

模型文件是一个zip包。按如下要求打zip包后将后缀名改为`.model`即可。

其中zip的根目录需要放置：`model.json`，`icon.png`，`code`目录，`words`目录，以及一个与模型名称同名的目录。

|                  | 说明
|------------------|-------------------------------
| `model.json`     | 模型配置文件
| `icon.png`       | 用于配置窗口图标
| `code`           | 存放模型相关的代码，目录下可以存放多个`.jar`文件
| `words`          | 可选。用于扩充代码中没有包含的对话文本
| 模型名称同名目录 | 存放模型相关资源，例如图片资源等

### model.json

模型配置文件，其内容如下：

```js
{
  "name": "模型的名字，例如kokori",
  "compatibleMinCodeVersion": 1000000, // 代码最低版本号，比方说版本号是x.y.z，那么这里填 x * 1_000_000 + y * 1000 + z
  "compatibleMaxCodeVersion": -1, // 代码最高版本号，写-1表示没有要求
  "modelClass": "模型实现的类名" // 该类需要有一个无参public的构造函数，并且必须在module-info中export其所在的包
}
```

### icon.png

该文件会被作为图标使用。建议使用正方形透明底色的PNG文件作为图标。一般使用人物的头部作为图标。

### words

words目录中的文件用于在模型中扩充角色所说的语句。

文件没有扩展名要求，推荐不设置扩展名，在代码读取的时候，文件名整体会作为key，在`interactionWordsSelectors`中可以获取到。

一个文件对应一个`WordsSelector`对象。文件中的一组`BEGIN/END WORDS`对应一个`Words`对象。在`BEGIN/END WORDS`内部，还有`BEGIN/END ${lang}`，表示某种语言。

文件格式如下：

```
-----BEGIN WORDS-----
-----BEGIN ${lang}-----
对应语言的文本写在这里。
每一行都会作为一个聊天气泡出现。
-----END ${lang}-----
-----END WORDS-----
在BEGIN/END之外，不会被程序读取，可以当作注释使用。
```

例如：

文件`opening`

```
-----BEGIN WORDS-----
-----BEGIN ZH-----
很高兴又和你见面了
-----END ZH-----
-----BEGIN EN-----
Glad to see you
-----END EN-----
-----END WORDS-----

-----BEGIN WORDS-----
-----BEGIN ZH-----
我就在这里哦～
-----END ZH-----
-----BEGIN EN-----
I'm right here~
-----END EN-----
-----END WORDS-----
```

加载后，相当于

```java
new WordsSelector(
    new WordsBuilder
        ("很高兴又和你见面了")
        .setEn("Glad to see you")
        .build(),
    new WordsBuilder
        ("我就在这里哦～")
        .setEn("I'm right here~")
        .build()
)
```

### code

code目录中存放模型代码的`.jar`文件。需要将所有依赖的jar也一并放入其中，加载模型文件时会加载这里所有的`.jar`文件。

注意，这里的`.jar`文件需要包含`model.json`中配置的`modelClass`，并且`module-info.java`中需要将对应的包export出来。

### 模型名称同名目录

该目录名需要和`model.json`中配置的`name`字段一致。图片资源文件都需要放置在该目录中。基础代码中的`ImageManager.load(...)`会加载这个目录中的资源文件。  
该目录内部的结构没有特定要求，按照自己喜好组织即可。

### 参考目录

kokori的目录结构是这样的：

```
kokori.model +
             |
             +-- model.json
             +-- icon.png
             +-- words +
             |         |
             |         +-- opening
             |         +-- normal
             |         |
             |       .....
             |
             +-- code +
             |        |
             |        +-- kokori.jar
             |
             +-- kokori +
                        |
                        +-- animation +
                        |             |
                        |             +-- dress_front +
                        |             |               |
                        |           .....             +-- dress_front_000.png
                        |                             +-- dress_front_001.png
                        |                             |
                        +-- static +                .....
                                   |
                                 .....
                                   |
                                   +-- 041_arm_fore_left.PNG
                                   +-- 042_arm_upper_left.PNG
                                   |
                                 .....
```

## 开发流程

开发过程中因为需要经常调试和修改源码，不适合将代码打包入模型文件。  
这里给出一套开发流程供参考。描述比较简略，具体可以看`kokori`工程的代码。此外推荐使用IDEA开发，可以使用免费的社区版。

### 1. clone

`git clone`本repo

### 2. 创建工程根目录

在`characters/`目录下创建一个你的角色的子目录，作为工程根目录，例如`characters/kokori/`

后文会多次出现“人物名字”，这里统一用变量符号`${name}`代替。

### 3. 使用gradle初始化

推荐使用`gradle wrapper`。  
可以直接从`kokori`工程目录中，将这几个文件拷贝到你的目录里，然后做一些修改。

* `gradle`: gradle wrapper相关文件
* `gradlew`: linux/macos通过这个脚本使用gradlew
* `gradlew.bat`: windows通过这个脚本使用gradlew
* `build.gradle`: gradle配置文件1
* `settings.gradle`: gradle配置文件2

拷贝后，修改`settings.gradle`，将项目名称改为`${name}`。修改`build.gradle`中的`archiveName`为`${name}.jar`。文件中其他参数视情况自行增删。

### 4. 基础依赖

在`build.gradle`配置的`dependencies`中，需要确保有如下配置：

1. `compile files('../../core/build/libs/chara.jar')`，用于加载基础框架依赖。
2. `compile group: 'com.1stleg', name: 'jnativehook', version: '2.1.0'`，这是基础框架依赖的一部分，为了调试方便可以添加，打包时忽略即可

在`build.gradle`配置的`javafx`中，需要确保有如下配置：

1. `modules = ['javafx.controls', 'javafx.swing']`，这也是基础框架的依赖，为了调试方便可以添加

### 5. 编译基础框架

进入`core`目录，运行`./gradlew clean jar`即可。

### 6. 开发

在你的新工程中创建一个类，命名为`${Name}Model`，例如`KokoriModel`，并`implements Model`（实现Model接口）。  
实现这个类，并实现与之相对应的`Chara`接口

在源代码根目录（一般来说是`src/main/java`）中，创建一个`module-info.java`，在其中`exports`你的`${Name}Model`所在的包。

### 7. 运行和调试

在你的新工程中创建一个包：`run.${name}`（例如`run.kokori`）。  
创建一个类`Run`。写入如下内容：

```java
package run.${name};

import javafx.application.Application;
import javafx.stage.Stage;
import net.cassite.desktop.chara.Main;

public class Run extends Application {
    @Override
    public void start(Stage primaryStage) {
        new Main().start(primaryStage);
    }
}
```

然后在`module-info.java`中，把这个包也`exports`出来：`exports run.${name}`

通过`Run`类，即可对整个App进行调试。

### 8. 打包

在你的工程目录下，`./gradlew clean jar`，jar包会在`build/libs/`目录中生成。

### 9. 修改基础代码

有时候基础代码无法满足模型文件的需要，这时可以修改基础代码。  
在修改完毕后，在基础代码的工程根目录（也就是`core/`目录）中，运行`./gradlew jar`即可。

注意，尽量不要`clean`基础代码，如果编译失败，jar文件消失，恢复后，可能需要重启IDEA才能重新找回依赖。

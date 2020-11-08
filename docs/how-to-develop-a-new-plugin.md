# 如何开发一个新插件

## 插件工作过程

插件需要放置在`~/.chara/plugin/`目录下。在启动时，程序会自动读取并加载该目录下所有以`.plugin`结尾的文件，并且插件的加载总是优先于角色模型的加载。

插件可以同时存在多个，并且可以同时工作。

## 插件常见实现方式

当插件加载时，插件的`init(...)`和`launch()`函数会被调用，你可以在插件中实现任何代码逻辑，比方说在角色模型上添加一些额外元素，或者向`ChatbotManager`注册一个`Chatbot`实现，或者开启一个额外的窗口，甚至可以实现一个毫不相关的应用。

注意，在`init(...)`函数中，你可以放置资源读取代码，以及一些不可被回滚的逻辑，而在`launch()`函数中，你需要放置一些可回滚的逻辑。

与`launch()`函数对应的是`release()`函数，它用来释放插件依赖的资源，一般来说就是回滚`launch()`函数的内容。如果有其他动态添加的资源，也需要一并回滚。

已加载的插件会在右键菜单中展示，并且可以通过点击进行交互。当菜单中的插件对象被点击时，`clicked(...)`函数会被调用，一般来说可以用于“启用/禁用”插件，或者展示插件提供的额外窗口等。

插件接口本身非常简单，但是可以通过`EventBus`功能watch各类事件。

```java
var registration = EventBus.watch(event, v -> { });
```

watch函数会返回一个`registration`，你需要在插件中保存这些registration，并且在release函数中释放这些registration。

core模块公开提供的事件，可以在`Events`类中找到。

## .plugin文件结构

插件的扩展名为`.plugin`，其本身是一个zip压缩包。

其目录结构如下：

|                  | 说明
|------------------|--------------------------------
| `plugin.json`    | 插件配置文件
| `code`           | 存放插件相关的代码，目录下可以存放多个`.jar`文件
| 插件名称同名目录 | 可选。存放插件相关资源，例如图片、音乐、影像、字体等资源

### plugin.json

插件配置文件，其内容如下：

```js
{
  "name": "插件的名字，例如dev",
  "version": 1000000, // 插件的版本号。格式是这样：比方说版本号是x.y.z，那么这里填 x * 1_000_000 + y * 1000 + z
  "compatibleMinCodeVersion": 1000000, // 基础代码的最低版本号
  "compatibleMaxCodeVersion": -1, // 基础代码的最高版本号，写-1表示没有要求
  "pluginClass": "插件实现的类名",
  "extra": { } // 该字段可选。这个字段的内容没有限制。可以存入各类自定义信息，比如作者、网站、git等信息。
}
```

### code

code目录中存放插件代码的`.jar`文件。需要将所有依赖的jar也一并放入其中，加载插件时会加载这里的所有`.jar`文件。

注意，这里的`.jar`文件需要包含`plugin.json`中配置的`pluginClass`，并且`module-info.java`中需要将对应的包export出来。

### 插件名称同名目录

该目录需要和`plugin.json`中配置的`name`字段一致。资源文件都需要放置在该目录中。`Plugin`接口的`resourceHandlers()`方法会加载这个目录中的资源文件。  
该目录内部的结构没有特定要求，按照自己喜好组织即可。

### 其他

在插件文件中还可以放置一些其他文件，例如LICENSE、README等。

### 参考目录

dev插件的目录结构是这样的：

```
dev.plugin +
           |
           +-- plugin.json
           +-- code +
                    |
                    +-- dev.jar
```

wqy-font插件的目录结构是这样的：

```
wqy-font.plugin +
                         |
                         +-- plugin.json
                         +-- code +
                         |        |
                         |        +-- wqy-font.jar
                         |
                         +-- wqy-font +
                         |                     |
                         |                     +-- font +
                         +-- wqy +                      |
                                 |                      +-- wqy-microhei.ttc
                                 +-- LICENSE_Apache2.txt
                                 +-- LICENSE_GPLv3.txt
                                 +-- AUTHORS.txt
                                ...
```

## 开发流程

开发过程中因为需要经常调试和修改源码，不适合将代码打包入插件文件。  
这里给出一套开发流程供参考。描述比较简略，具体可以看`dev`插件工程的代码。此外推荐使用IDEA开发，可以使用免费的社区版。

### 1. clone

`git clone`本repo

### 2. 初始化工程

在`plugins/`目录下执行`NEW_PLUGIN_PACKAGE_NAME="包名" NEW_PLUGIN_CLASS_PREFIX="类前缀" NEW_PLUGIN_NAME="插件名" ./new-plugin.sh`创建一个你的插件。例如`NEW_PLUGIN_PACKAGE_NAME="notofont" NEW_PLUGIN_CLASS_PREFIX="NotoFont" NEW_PLUGIN_NAME="noto-font" ./new-plugin.sh`。其中有如下规范：包名为全小写连续的字母，类前缀使用驼峰命名不包含Plugin后缀，插件名最好使用全小写并以中横线分割。

### 3. 基础依赖

从release页面下载最新版的[vproxy.jar](https://github.com/wkgcass/vproxy)，放置在仓库根目录，命名为`vproxy.jar`。

### 4. 编译基础框架

进入`core`目录，运行`./gradlew clean jar`即可。

### 5. 开发

在生成的`${Prefix}Plugin`中编写插件代码。可参考core模块的javadoc。

### 6. 运行和调试

通过自动生成的`Run`类，即可对整个App进行调试。

### 7. 打包

在你的插件工程目录下，运行`./build-plugin.sh`即可打包插件，插件会在工程的`plugin/`目录中生成。

### 8. 修改基础代码

有时候基础代码无法满足插件文件的需要，这时可以修改基础代码。
在修改完毕后，在基础代码的工程根目录（也就是`core/`目录）中，运行`./gradlew jar`即可。

注意，尽量不要`clean`基础代码，如果编译失败，jar文件消失，恢复后，可能需要重启IDEA才能重新找回依赖。

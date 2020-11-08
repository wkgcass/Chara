# Chara

<img src="docs/readme-bundle/chara.png" width="128px" height="128px">

> “Ch”图标来源：[Icons8](https://icons8.com)

Chara是一款基于JavaFX的桌面人物软件。

目前支持如下功能：

1. 随机动画：例如头发飘动、眨眼等
2. 点击人物的某些部位，人物给出对应的反馈
3. 眼睛跟随鼠标
4. 聊天功能：需要接入第三方chatbot
5. 互动好感度系统
6. 少量meta元素
7. 管理员指令
8. 模型文件封装
9. 插件功能

# 框架可以用来

制作：

1. 桌宠（桌面人物）
2. Widget
3. 动态桌面

# 演示

请见这里：[http://blog.cassite.net/Chara/](http://blog.cassite.net/Chara/)。

# 目前实现的角色

<table>
<thead>
  <tr>
   <th>形象</th>
   <th>简述</th>
  </tr>
</thead>
<tbody>
  <tr>
   <td>
<a href="https://github.com/wkgcass/Chara/tree/master/characters/kokori"><img src="docs/readme-bundle/kokori.png" width="128px" height="128px"></a>
   </td>
   <td>
<table>
<tbody>
  <tr><td> 人物 </td></tr>
  <tr><td> 中文名 </td> <td> 心璃 </td></tr>
  <tr><td> 英文名 </td> <td> Kokori </td></tr>
  <tr><td> 日文名 </td> <td> 心(ココ)璃(リ) </td></tr>
  <tr><td> 关键字 </td> <td> 巫女 温柔 病娇 </td></tr>
  <tr><td> 作者 </td></tr>
  <tr><td> 设定 </td> <td> wkgcass </td></tr>
  <tr><td><a href="https://www.pixiv.net/artworks/85094809"> 原画 </a></td> <td> wkgcass </td></tr>
  <tr><td> 模型 </td> <td> wkgcass </td></tr>
  <tr><td> 编码 </td> <td> wkgcass </td></tr>
</tbody>
</table>
   </td>
  </tr> <!-- end kokori -->
</tbody>
</table>

# 目前实现的插件

将插件放置在`~/.chara/plugin/`（`%userprofile%\.chara\plugin`）目录中即可启用。

## dev

提供一些在开发模型中常常能用到的功能。例如：鼠标点击时显示坐标、连线、计算角度等。

点击右键菜单中的按钮启用或关闭标记功能。

## console

启用一个控制台展示程序的所有日志。主要考虑在使用打包后的无命令行程序时方便查看日志。

点击右键菜单中的按钮启用或关闭日志窗口。

## wqy-font

使用文泉驿字体。

## noto-font

使用Noto字体。

注意，你可能需要自行根据语言/地区添加相应的字体文件和加载配置。

## tianxing-chatbot

使用[天行机器人](https://www.tianapi.com/search/机器人)作为聊天机器人。

默认使用天行机器人，点击右键菜单中的按钮可切换天行图灵。（天行图灵免费额度较低）

## debug

启用debug特性。将该插件放置在`~/.chara/plugin/`（`%userprofile%\.chara\plugin`）目录中并在启动时勾选加载才可启用debug特性。

## r18

启用r18特性。将该插件放置在`~/.chara/plugin/`（`%userprofile%\.chara\plugin`）目录中并在启动时勾选加载才可启用r18特性。

# 开发文档

* [基础模块javadoc生成脚本](docs/generate-javadoc.sh)
* [如何开发一个新角色](docs/how-to-develop-a-new-character.md)
* [如何开发一个插件](docs/how-to-develop-a-new-plugin.md)

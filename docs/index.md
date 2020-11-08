# Chara

<img src="readme-bundle/chara.png" width="128px" height="128px">

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

## 眼睛跟踪鼠标

请使用Chrome浏览器查看。

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/kokori-eye-track.webm" type="video/webm" height="354">
</video>

## 触摸反馈

请使用Chrome浏览器查看。

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/kokori-touch.webm" type="video/webm" height="450">
</video>

触摸后的反应会根据羁绊值的高低而不同哦～

## 菜单交互

请使用Chrome浏览器查看。

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/kokori-menu.webm" type="video/webm" height="632">
</video>

## 羁绊值

请使用Chrome浏览器查看。

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/kokori-bond-bar.webm" type="video/webm" height="398">
</video>

通过增加羁绊值和▇▇值解锁更多新交互吧～

<details><summary>...</summary>
减少羁绊值也会发生变化～
</details>

## 聊天交互

请使用Chrome浏览器查看。

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/kokori-chat-feature.webm" type="video/webm" height="650">
</video>

聊天交互需要事先配置chatbot

## dev插件演示

<p id="dev-plugin"></p>

请使用Chrome浏览器查看。

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/dev-plugin.webm" type="video/webm" height="650">
</video>

## R18特性

<details><summary>别点我</summary>

需要将r18插件放置在`~/.chara/plugin/`（`%userprofile%\.chara\plugin`）目录中，并在启动时勾选加载才可启用r18特性。<br>

<details><summary>别点我</summary>

请使用Chrome浏览器查看。<br>

<video autoplay loop muted src="https://github.com/wkgcass/Chara-bundle/raw/main/readme-bundle/kokori-r18.webm" type="video/webm" height="352">
</video>

</details>

</details>

# 目前实现的角色

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
<a href="https://github.com/wkgcass/Chara/tree/master/characters/kokori"><img src="readme-bundle/kokori.png" width="128px" height="128px"></a>
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

* [基础模块javadoc生成脚本](https://github.com/wkgcass/Chara/blob/master/docs/generate-javadoc.sh)
* [如何开发一个新角色](how-to-develop-a-new-character.md)
* [如何开发一个插件](how-to-develop-a-new-plugin.md)

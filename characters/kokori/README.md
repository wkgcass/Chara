# 心璃

![](../../docs/readme-bundle/kokori.png)

## 背景设定

在一个有「灵」存在的世界的一名「普通」巫女，和同伴们守护世界上最大的「灵脉」。  
在十年前出现的世界性变故「冲击」中存活。「冲击」产生的「模因」让她不得不独自一人进行对「灵脉」的观测和守护。  
长期独处而积存的孤独感被她尽数埋藏在心底。而和「你」的接触却让这感情流出，使她病娇的一面开始显现...

## 功能

这是在Chara框架下实现的第一个桌面人物，接入了初始版本`core`模块提供的大部分特性。

1. 眼睛跟随鼠标
2. 触摸不同部位给出不同反馈
3. 依据羁绊值等条件处理触摸事件
4. 右键菜单
5. 少量meta元素
6. 少量R18功能
7. 通过接入第三方chatbot进行对话

## debug命令

在聊天框中可以输入如下debug命令：

| 命令                               | 功能                                     |
|------------------------------------|------------------------------------------|
| `::repeat:{}`                      | 让心璃说出你输入的内容                   |
| `::get:bond_point`                 | 输出当前羁绊值                           |
| `::get:desire_point`               | 输出当前欲望值                           |
| `::set:bond_point:{}`              | 设置羁绊值                               |
| `::set:desire_point:{}`            | 设置欲望值                               |
| `::animate:rune:show`              | 显示符咒                                 |
| `::animate:red_cheek:show`         | 脸红                                     |
| `::animate:red_cheek:hide`         | 取消脸红                                 |
| `::animate:mouth:happy`            | 设置嘴巴为“开心”(笑)状态                 |
| `::animate:mouth:sad`              | 设置嘴巴为“伤心”状态                     |
| `::animate:mouth:open`             | 设置嘴巴为“张开”状态                     |
| `::animate:mouth:default`          | 重置嘴巴                                 |
| `::animate:mouth:left`             | 向左歪嘴                                 |
| `::animate:mouth:right`            | 向右歪嘴                                 |
| `::animate:breathe:start`          | 开始呼吸动画                             |
| `::animate:breathe:stop`           | 停止呼吸动画                             |
| `::animate:highlight:show`         | 显示眼睛高光                             |
| `::animate:highlight:hide`         | 隐藏眼睛高光                             |
| `::animate:eye:zoom:{}`            | 配置眼睛缩放比例                         |
| `::animate:eye:close`              | 闭眼                                     |
| `::animate:eye:open`               | 睁眼                                     |
| `::animate:head:right`             | 向右歪头                                 |
| `::animate:head:left`              | 向左歪头                                 |
| `::animate:head:default`           | 头部回到原处                             |
| `::animate:arm_right:tighten`      | 收紧右臂                                 |
| `::animate:arm_right:down`         | 右臂向下                                 |
| `::animate:arm_right:default`      | 右臂回到原处                             |
| `::animate:leg_left:tighten`       | 左腿收紧                                 |
| `::animate:leg_left:loose`         | 左腿放松                                 |
| `::action:sex`                     | 执行一次sex动作                          |
| `::sys:love_potion:add`            | 添加一瓶媚药                             |

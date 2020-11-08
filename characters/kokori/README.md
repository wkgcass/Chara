# 心璃

<img src="../../docs/readme-bundle/kokori.png" width="300px" height="300px">

## 背景设定

在一个有「灵」存在的世界的一名「普通」巫女，和同伴们守护世界上最大的「灵脉」。  
在世界性灾难「冲击」中存活。「冲击」产生的「模因」让她不得不独自一人进行对「灵脉」的观测和守护。  
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

## 表情快捷键

可以在`右键菜单 -> 角色 -> 表情`中查看可用的表情快捷键：

> 注意，做出表情时并不会重置上一个表情，可以使用“重置”快捷键进行表情的重置。

| 快捷键                  | 表情 |
| Ctrl+Shift+Alt+ H       | 开心 |
| Ctrl+Shift+Alt+ S       | 害羞 |
| Ctrl+Shift+Alt+ A       | 伤心 |
| Ctrl+Shift+Alt+ D       | 厌恶 |
| Ctrl+Shift+Alt+ R       | 重置 |
| Ctrl+Shift+Alt+ P       | 惊讶 |
| Ctrl+Shift+Alt+ Y (R18) | 病娇 |
| Ctrl+Shift+Alt+ O (R18) | 高潮 |

## debug命令

在聊天框中可以输入如下debug命令：

> 注意，你必须启用debug插件才能使用debug命令。

| 命令                               | 功能                                     |
|------------------------------------|------------------------------------------|
| `::repeat:{}`                      | 让心璃说出你输入的内容                   |
| `::get:bond_point`                 | 输出当前羁绊值                           |
| `::get:desire_point`               | 输出当前欲望值                           |
| `::get:proposing_accepted`         | 获取当前是否接受了表白                   |
| `::get:proposing_count`            | 获取表白次数                             |
| `::set:bond_point:{}`              | 设置羁绊值                               |
| `::set:desire_point:{}`            | 设置欲望值                               |
| `::set:proposing_count:{}`         | 设置表白次数                             |
| `::set:proposing_accepted:{}`      | 设置当前是否接受了表白 (true/false)      |
| `::set:propose_menu_item:{}`       | 设置表白按钮是否可用 (enabled/disabled)  |
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
| `::animate:arm_right:stretch`      | 右臂伸展                                 |
| `::animate:arm_right:front`        | 右臂置于身前                             |
| `::animate:arm_right:back`         | 右臂置于身后                             |
| `::animate:leg_left:tighten`       | 左腿收紧                                 |
| `::animate:leg_left:loose`         | 左腿放松                                 |
| `::action:sex`                     | 执行一次sex动作                          |
| `::sys:love_potion:add`            | 添加一瓶媚药                             |
| `::sys:get:sex_count`              | 获取sex次数                              |
| `::sys:get:bad_sex_count`          | 获取负面的sex次数                        |
| `::sys:get:normal_sex_count`       | 获取普通sex次数                          |
| `::sys:get:orgasm_count`           | 获取高潮次数                             |
| `::sys:get:sex_total_time`         | 获取sex总时长(ms)                        |
| `::sys:get:love_potion_used`       | 当前是否使用了媚药                       |

## 事件列表

| id                      | 前置条件                                 | 触发事件              | 概率           | 类型            | 动作                                                     |
|-------------------------|------------------------------------------|-----------------------|----------------|-----------------|----------------------------------------------------------|
| `random-event`          | 无                                       | 每3秒一次随机触发     | -              | 触发器          | 触发`hair-swing cloth-swing eye-blink rune-flow rune-show love-potion-add look-at-left-or-right enable-propose` |
| `hair-swing`            | 无                                       | -                     | 0.7            | 动画            | 头发飘动                                                 |
| `cloth-swing`           | 无                                       | -                     | 0.7            | 动画            | 衣服飘动                                                 |
| `eye-blink`             | 无                                       | -                     | 0.8            | 动画            | 眨眼                                                     |
| `eye-double-blink`      | 无                                       | eye-blink触发         | 0.4            | 动画            | 再次眨眼                                                 |
| `rune-flow`             | 无                                       | -                     | 0.6            | 动画            | 符咒飘动                                                 |
| `rune-show`             | 无                                       | -                     | 0.006          | 动画            | 显示符咒                                                 |
| `love-potion-add`       | R18                                      | -                     | 0.001          | 系统            | 增加一瓶媚药                                             |
| `look-at-left-or-right` | R18 && 欲望值 == 1                       | -                     | 0.5            | 动画            | 眼睛随机看向左边/右边                                    |
| `enable-propose`        | 羁绊值 >= 0.85                           | -                     | 0.006          | 系统            | 表白按钮设为可点击                                       |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-points-related`  | 无                                       | 羁绊值/欲望值发生变化 | -              | 触发器          | 触发`reset-mouth reset-cheek reset-highlight reset-like-menu reset-bond-stories reset-propose-menu reset-eye-position`
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-mouth`           | R18 && 欲望值 == 1                       | -                     | -              | 动画            | 嘴巴一张一合（呼吸状）                                   |
| `reset-mouth`           | else if 羁绊值 < badMood                 | -                     | -              | 动画            | 嘴巴变为伤心状态                                         |
| `reset-mouth`           | else                                     | -                     | -              | 动画            | 嘴巴变为默认状态                                         |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-cheek`           | R18 && 欲望值 >= veryHighDesirePoint     | -                     | -              | 动画            | 显示红晕                                                 |
| `reset-cheek`           | else                                     | -                     | -              | 动画            | 隐藏红晕                                                 |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-highlight`       | R18 && 欲望值 == 1                       | -                     | -              | 动画            | 高光消失                                                 |
| `reset-highlight`       | else if 欲望值 < reallyBadMood           | -                     | -              | 动画            | 高光消失                                                 |
| `reset-highlight`       | else                                     | -                     | -              | 动画            | 高光恢复                                                 |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-like-menu`       | 羁绊值 < 0.7                             | -                     | -              | 系统            | 喜欢的东西按钮不可点击                                   |
| `reset-like-menu`       | else                                     | -                     | -              | 系统            | 喜欢的东西按钮可点击                                     |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-bond-stories`    | ...                                      | -                     | -              | 系统            | 根据羁绊值从0.6~1开启不同的羁绊故事                      |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-propose-menu`    | 羁绊值 < 0.85                            | -                     | -              | 系统            | 移除表白按钮                                             |
| `reset-propose-menu`    | else                                     | -                     | -              | 系统            | 增加表白按钮                                             |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `reset-eye-position`    | R18 && 欲望值 == 1                       | 鼠标移出指定范围      | -              | 动画            | 眼睛随机看向左边/右边                                    |
| `reset-eye-position`    | else                                     | 鼠标移出指定范围      | -              | 动画            | 眼睛看向前方                                             |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `opening`               | 羁绊值 < reallyReallyBadMood             | 模型显示时            | -              | 对话            | 显示reallyReallyBadMoodOpening                           |
| `opening`               | else if 羁绊值 < badMood                 | 模型显示时            | -              | 对话            | 显示badMoodOpening                                       |
| `opening`               | else                                     | 模型显示时            | -              | 对话            | 显示opening                                              |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `eye-track`             | 无                                       | 鼠标在指定范围内移动  | -              | 动画            | 眼睛追踪鼠标                                             |
| `dragged`               | 无                                       | 模型被拖动            | -              | 动画            | 头发飘动 衣服飘动 箭袋震动                               |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `pre-i-check`           | 羁绊值 < badMood                         | -                     | -              | 对话            | 显示dontWantToSeeYou                                     |
| `pre-i-check`           | 羁绊值 < reallyBadMood                   | -                     | 0.3            | 对话            | 抛出异常，程序退出                                       |
| `pre-i-check`           | 羁绊值 < reallyReallyBadMood             | -                     | 0.3            | 对话            | 删除（实际上是移动）模型文件，并且程序退出               |
| `pre-i-check`           | 羁绊值 < reallyBadMood                   | -                     | 1              | 流程            | return                                                   |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `positive-i` `(bad)`    | 触发过快                                 | -                     | -              | 系统            | 减少羁绊值                                               |
| `positive-i` `(none)`   | 未超过触发cooldown                       | -                     | -              | 无动作          | -                                                        |
| `positive-i` `(good)`   | else                                     | -                     | -              | 系统            | 增加羁绊值、欲望值                                       |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `normal-i`   `(good)`   | 无                                       | -                     | 0.0005 * 羁绊值 + 0.0015 | 系统  | 增加少量羁绊值、少量欲望值                               |
| `normal-i`  ` (bad)`    | 无                                       | -                     | else 0.001     | 系统            | 减少少量羁绊值、少量欲望值                               |
| `normal-i`   `(none)`   | 无                                       | -                     | else 1         | 无动作          | -                                                        |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `negative-i` `(none)`   | 触发过快                                 | -                     | -              | 无动作          | -                                                        |
| `negative-i` `(bad)`    | else                                     | -                     | -              | 系统            | 减少羁绊值、欲望值                                       |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `normal-message`        | 触发过快                                 | -                     | -              | 无动作          | -                                                        |
| `normal-message`        | else if 羁绊值 >= 0.85                   | -                     | 0.3            | 对话            | 显示highIntimacyConversations                            |
| `normal-message`        | else                                     | -                     | else           | 对话            | 显示normalConversations                                  |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-hair`            | pre-i-check                              | 点击头发时            | -              | 动画            | 头发飘动                                                 |
| `touch-hair`            | pre-i-check                              | 点击头发时            | -              | 流程            | 执行`positive-i`                                         |
| `touch-hair`            | positive-i good                          | 点击头发时            | 0.5            | 对话            | 显示happy                                                |
| `touch-hair`            | positive-i good                          | 点击头发时            | else           | 对话            | 显示normalMessage                                        |
| `touch-hair`            | positive-i good                          | 点击头发时            | 0.2            | 动画            | 嘴巴变为开心状态3秒                                      |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-eye`             | pre-i-check                              | 点击眼睛时            | -              | 动画            | 被点击的眼睛眨眼                                         |
| `touch-eye`             | pre-i-check                              | 点击眼睛时            | -              | 流程            | 执行`normal-i`                                           |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-face` `(cheek)`  | pre-i-check                              | 点击脸部时            | 0.5            | 动画            | 显示红晕1.5秒                                            |
| `touch-face`            | pre-i-check && 当前头处于默认位置        | 点击脸部时            | !cheek?1:0.15  | 动画            | 头向左或者右倾斜                                         |
| `touch-face`            | pre-i-check && 当前头不处于默认位置      | 点击脸部时            | -              | 动画            | 头回到默认位置                                           |
| `touch-face`            | pre-i-check && 羁绊值 >= 0.7             | 点击脸部时            | -              | 流程            | 执行`positive-i`                                         |
| `touch-face`            | pre-i-check && else                      | 点击脸部时            | -              | 流程            | 执行`normal-i`                                           |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-rune`            | pre-i-check                              | 点击符咒时            | -              | 动画            | 符咒飘动、1秒后隐藏                                      |
| `touch-rune`            | pre-i-check                              | 点击符咒时            | -              | 流程            | 执行`normal-i`                                           |
| `touch-rune`            | normal-i good or none                    | 点击符咒时            | -              | 对话            | 显示aboutRune                                            |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-cloth`           | pre-i-check                              | 点击衣服时            | -              | 动画            | 被点击部分的衣服飘动                                     |
| `touch-cloth`           | pre-i-check                              | 点击衣服时            | -              | 流程            | 执行`normal-i`                                           |
| `touch-cloth`           | normal-i good or none                    | 点击衣服时            | -              | 流程            | 触发`normal-message`                                     |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-breast`          | pre-i-check                              | 点击胸部时            | -              | 动画            | 嘴巴和红晕变为害羞状态、右臂收紧(1.5秒)，隐藏符咒、隐藏手里的箭 |
| `touch-breast`          | pre-i-check && 羁绊值 > 0.8              | 点击胸部时            | 0.2            | 流程            | 执行`positive-i`                                         |
| `touch-breast`          | pre-i-check && 羁绊值 > 0.8              | 点击胸部时            | else 0.1       | 流程            | 执行`negative-i`                                         |
| `touch-breast`          | pre-i-check && 羁绊值 > 0.8              | 点击胸部时            | else 0.1       | 对话            | 显示doNotTouchThere                                      |
| `touch-breast`          | pre-i-check && 羁绊值 > 0.8              | 点击胸部时            | else           | 流程            | 执行`normal-i`且一定不会减少欲望值                       |
| `touch-breast`          | pre-i-check && else if 羁绊值 > 0.45     | 点击胸部时            | -              | 流程            | 执行`negative-i`                                         |
| `touch-breast`          | pre-i-check && else if 羁绊值 > 0.45     | 点击胸部时            | -              | 对话            | 显示doNotTouchThere                                      |
| `touch-breast`          | pre-i-check && else                      | 点击胸部时            | -              | 流程            | 执行`negative-i`且大幅减少羁绊值和欲望值                 |
| `touch-breast`          | pre-i-check && else                      | 点击胸部时            | -              | 对话            | 显示doNotTouchThere                                      |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-arm-left`        | pre-i-check                              | 点击左臂时            | -              | 动画            | 展示或者隐藏弓和箭、当要展示弓时且右手没有弓箭时箭袋震动 |
| `touch-arm-left`        | pre-i-check && 处于没有展示弓的状态      | 点击左臂时            | -              | 对话            | 显示showBow                                              |
| `touch-arm-left`        | pre-i-check                              | 点击左臂时            | -              | 流程            | 执行`normal-i`                                           |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-crotch` `(good|normal|bad)` | ...较为复杂...                | 点击胯部时            | ...            | ...             | 简述：涉及羁绊值0.9、0.6，欲望值veryHighDesirePoint，羁绊值欲望值变化幅度大，如果结果为good则欲望值清零 |
| `touch-crotch`          | R18 && good && 欲望值 == 1               | 点击胯部时            | -              | 对话            | 显示cannotRestrain                                       |
| `touch-crotch`          | R18 && good && else                      | 点击胯部时            | -              | 对话            | 显示wantSex                                              |
| `touch-crotch`          | !R18 && good                             | 点击胯部时            | -              | 对话            | 显示happyWords                                           |
| `touch-crotch`          | normal                                   | 点击胯部时            | -              | 对话            | 显示doNotTouchThere                                      |
| `touch-crotch`          | bad                                      | 点击胯部时            | -              | 对话            | 显示doNotTouchThere                                      |
| `touch-crotch`          | good                                     | 点击胯部时            | -              | 动画            | 嘴巴和红晕变为害羞状态1.5秒、左腿收紧并放松、裙子飘动    |
| `touch-crotch`          | R18 && good                              | 点击胯部时            | -              | 系统            | 进入sex模式                                             |
| `touch-crotch`          | normal or bad                            | 点击胯部时            | -              | 动画            | 左腿收紧并放松、右手伸到胯部1.5秒、隐藏手里的弓箭和符咒、裙子飘动 |
| `touch-crotch`          | normal                                   | 点击胯部时            | -              | 动画            | 嘴巴和红晕变为害羞状态1.5秒                              |
| `touch-crotch`          | bad                                      | 点击胯部时            | 0.3            | 动画            | 移除高光1.5秒                                            |
| `touch-crotch`          | bad                                      | 点击胯部时            | else           | 动画            | 嘴巴和红晕变为害羞状态1.5秒                              |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-leg` `(good)`    | pre-i-check && 羁绊值 >= 0.85            | 点击腿时              | -              | 流程            | 触发`positive-i`                                         |
| `touch-leg` `(good)`    | pre-i-check && else if 羁绊值 >= 0.65    | 点击腿时              | 0.1            | 流程            | 触发`positive-i`                                         |
| `touch-leg` `(bad)`     | pre-i-check && else if 羁绊值 >= 0.65    | 点击腿时              | else 0.4       | 流程            | 触发`negative-i`                                         |
| `touch-leg` `(bad)`     | pre-i-check && else if 羁绊值 >= 0.65    | 点击腿时              | else 0.4       | 流程            | 显示doNotTouchLeg                                        |
| `touch-leg` `(normal)`  | pre-i-check && else if 羁绊值 >= 0.65    | 点击腿时              | else           | 流程            | 触发`normal-i`                                           |
| `touch-leg` `(normal)`  | pre-i-check && else if 羁绊值 >= 0.65    | 点击腿时              | else           | 对话            | 显示flirt                                                |
| `touch-leg` `(bad)`     | pre-i-check && else                      | 点击腿时              | -              | 流程            | 触发`negative-i`                                         |
| `touch-leg` `(bad)`     | pre-i-check && else                      | 点击腿时              | -              | 对话            | 显示doNotTouchLeg                                        |
| `touch-leg-left`        | pre-i-check                              | 点击左腿时            | -              | 动画            | 左腿收紧并放松、裙子飘动、红晕显示1.5秒                  |
| `touch-leg-left`        | normal or bad                            | 点击左腿时            | -              | 动画            | 嘴巴变为伤心1.5秒                                        |
| `touch-leg-right`       | pre-i-check                              | 点击右腿时            | -              | 动画            | 红晕显示1.5秒                                            |
| `touch-leg-right`       | normal or bad                            | 点击右腿时            | -              | 动画            | 嘴巴变为伤心1.5秒                                        |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `touch-other`           | pre-i-check                              | 点击其他人物区域时    | -              | 动画            | 双眼眨眼                                                 |
| `touch-other`           | pre-i-check                              | 点击其他人物区域时    | 0.5            | 动画            | 双眼再次眨眼                                             |
| `touch-other`           | pre-i-check                              | 点击其他人物区域时    | -              | 流程            | 执行`normal-i`                                           |
| `touch-other`           | normal-i good                            | 点击其他人物区域时    | -              | 流程            | 触发`normal-message`                                     |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `exp-happy`             | 无                                       | ctrl+shift+alt+H      | -              | 动画            | 开心表情                                                 |
| `exp-shy`               | 无                                       | ctrl+shift+alt+S      | -              | 动画            | 害羞表情                                                 |
| `exp-sad`               | 无                                       | ctrl+shift+alt+A      | -              | 动画            | 伤心表情                                                 |
| `exp-disgust`           | 无                                       | ctrl+shift+alt+D      | -              | 动画            | 厌恶表情                                                 |
| `exp-reset`             | 无                                       | ctrl+shift+alt+R      | -              | 动画            | 重置表情                                                 |
| `exp-surprised`         | 无                                       | ctrl+shift+alt+P      | -              | 动画            | 惊讶表情                                                 |
| `exp-yandere`           | R18                                      | ctrl+shift+alt+Y      | -              | 动画            | 病娇表情                                                 |
| `exp-orgasm`            | R18                                      | ctrl+shift+alt+O      | -              | 动画            | 高潮表情                                                 |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `about-name`            | 无                                       | 点击关于名字菜单项    | -              | 对话            | 显示aboutName                                            |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `conversation`          | pre-i-check                              | 点击对话菜单项        | -              | 对话            | 显示menuConversations                                    |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `things-she-likes`      | pre-i-check && 羁绊值 < 0.7              | 点击喜欢的东西菜单项  | -              | 无              | -                                                        |
| `things-she-likes`      | pre-i-check && else if 羁绊值 < 0.8      | 点击喜欢的东西菜单项  | -              | 对话            | 显示thingsLikes1                                         |
| `things-she-likes`      | pre-i-check && else if 羁绊值 < 0.85     | 点击喜欢的东西菜单项  | -              | 对话            | 显示thingsLikes2                                         |
| `things-she-likes`      | pre-i-check && else && 非恋爱状态        | 点击喜欢的东西菜单项  | -              | 对话            | 显示thingsLikes3                                         |
| `things-she-likes`      | pre-i-check && else && 恋爱状态          | 点击喜欢的东西菜单项  | -              | 动画            | 病娇表情20秒                                             |
| `things-she-likes`      | pre-i-check && else && 恋爱状态 && R18   | 点击喜欢的东西菜单项  | -              | 动画            | 右手放到胯部                                             |
| `things-she-likes`      | pre-i-check && else && 恋爱状态 && R18   | 点击喜欢的东西菜单项  | -              | 对话            | 显示thingsLikesR18                                       |
| `things-she-likes`      | pre-i-check && else all                  | 点击喜欢的东西菜单项  | -              | 动画            | 右臂收紧                                                 |
| `things-she-likes`      | pre-i-check && else all                  | 点击喜欢的东西菜单项  | -              | 对话            | 显示thingsLikes4                                         |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `things-she-hates`      | 羁绊值 < reallyBadMood                   | 点击讨厌的东西菜单项  | -              | 对话            | 显示thingsSheHates1                                      |
| `things-she-hates`      | else 羁绊值 < badMood                    | 点击讨厌的东西菜单项  | -              | 对话            | 显示thingsSheHates2                                      |
| `things-she-hates`      | else 羁绊值 < 0.7                        | 点击讨厌的东西菜单项  | -              | 对话            | 显示thingsSheHates3                                      |
| `things-she-hates`      | else && 非恋爱状态                       | 点击讨厌的东西菜单项  | -              | 对话            | 显示thingsSheHates4                                      |
| `things-she-hates`      | else && 恋爱状态 && R18                  | 点击讨厌的东西菜单项  | 0.5            | 对话            | 显示thingsSheHatesR18                                    |
| `things-she-hates`      | else all                                 | 点击讨厌的东西菜单项  | -              | 对话            | 显示thingsSheHates5                                      |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `bond-story`            | ...                                      | 点击各项羁绊故事      | -              | 对话            | 根据羁绊值从0.6-1展示对应可展示的羁绊故事                |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `propose`               | pre-i-check && 非恋爱状态 && 羁绊值 >= 0.85 | 点击表白菜单项     | (羁绊值 - 0.75) * 表白次数 | 系统 | 转为恋爱状态                                            |
| `propose`               | pre-i-check && 非恋爱状态 && 羁绊值 >= 0.85 | 点击表白菜单项     | (羁绊值 - 0.75) * 表白次数 | 对话 | 显示acceptProposal                                      |
| `propose`               | pre-i-check && 非恋爱状态 && 羁绊值 >= 0.85 | 点击表白菜单项     | else           | 对话            | 显示rejectProposal                                       |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `love-potion`           | R18 && 媚药存量 >= 1                     | 点击媚药菜单项        | 根据羁绊值计算 | 系统            | 进入使用媚药状态、欲望值设置为1                          |
| `love-potion`           | R18 && 媚药存量 >= 1                     | 点击媚药菜单项        | 根据羁绊值计算 | 对话            | 显示succeededUsingLovePotion                             |
| `love-potion`           | R18 && 媚药存量 >= 1                     | 点击媚药菜单项        | else           | 系统            | 羁绊值大幅减少、欲望值清零                               |
| `love-potion`           | R18 && 媚药存量 >= 1                     | 点击媚药菜单项        | else           | 对话            | 显示failedUsingLovePotion                                |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `want-h`                | R18 && ...较为复杂...                    | 点击想要H菜单项       | ...            | ...             | 简述：涉及羁绊值0.3、0.6、0.85，欲望值0.8、1、是否处于恋爱状态，可能增加或者减少羁绊值和欲望值，可以进入sex模式 |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `periodic-point-event`  | 处于恋爱状态                             | 长时间没有交互        | -              | 系统            | 增加少量羁绊值、增加少量欲望值                           |
| `periodic-point-event`  | else && 羁绊值 > 0.8                     | 长时间没有交互        | -              | 系统            | 减少少量羁绊值                                           |
| `periodic-point-event`  | else && else                             | 长时间没有交互        | -              | 系统            | 增加少量羁绊值                                           |
| `periodic-point-event`  | else && 高潮次数 > 0                     | 长时间没有交互        | -              | 系统            | 增加少量欲望值                                           |
| `periodic-point-event`  | else && else && 0.2 < 欲望值 < 0.8       | 长时间没有交互        | -              | 系统            | 增加少量欲望值                                           |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `exit-duration-point`   | 羁绊值 > 初始羁绊                        | 程序退出了很长时间    | -              | 系统            | 按时间减少羁绊值直到等于初始羁绊                         |
| `exit-duration-point`   | 欲望值 > veryHighDesirePoint             | 程序退出了较长时间    | -              | 系统            | 欲望值大幅减少                                           |
| -                       | -                                        | -                     | -              | -               | -                                                        |
| `finish-sex`            | R18                                      | 在sex模式中达到高潮  | -              | ...             | 简述：涉及羁绊值0.85、当次sex模式中的用时、是否使用媚药，可能增加或减少羁绊值和欲望值，可能立即充满欲望值 |

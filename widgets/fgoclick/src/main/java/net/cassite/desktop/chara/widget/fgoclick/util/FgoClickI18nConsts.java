// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick.util;

import net.cassite.desktop.chara.i18n.Words;
import net.cassite.desktop.chara.i18n.WordsBuilder;

public class FgoClickI18nConsts {
    private FgoClickI18nConsts() {
    }

    public static final Words sleepIndicatorMenuItem = new WordsBuilder
        ("延时指示器")
        .setEn("Sleep indicator")
        .build();
    public static final Words snapshotMenuItem = new WordsBuilder
        ("自动截图")
        .setEn("Auto Snapshot")
        .build();
    public static final Words htmlRobotMenuItem = new WordsBuilder
        ("网页远程控制")
        .setEn("WebPage Remote Control")
        .build();
    public static final Words click100TimesMenuItem = new WordsBuilder
        ("重复点击鼠标100次")
        .setEn("Click mouse 100 times")
        .build();
    public static final Words click50TimesMenuItem = new WordsBuilder
        ("重复点击鼠标50次")
        .setEn("Click mouse 50 times")
        .build();

    public static final Words htmlRobotStartedOn59080 = new WordsBuilder
        ("网页远程控制运行于59080端口")
        .setEn("WebPage remote control now running on port 59080")
        .build();
    public static final Words waitForTwoSecondsBeforeClicking = new WordsBuilder
        ("等待2秒后开始点击")
        .setEn("Wait for 2 seconds before clicking")
        .build();
    public static final Words clickingFinishes = new WordsBuilder
        ("点击结束")
        .setEn("Clicking finishes")
        .build();

    public static final Words prepareButton = new WordsBuilder
        ("准备")
        .setEn("Prepare")
        .build();
    public static final Words rePrepareButton = new WordsBuilder
        ("重新准备")
        .setEn("Re-Prepare")
        .build();
    public static final Words runButton = new WordsBuilder
        ("启动")
        .setEn("Run")
        .build();
    public static final Words cancelButton = new WordsBuilder
        ("取消")
        .setEn("Cancel")
        .build();
    public static final Words okButton = new WordsBuilder
        ("确定")
        .setEn("Ok")
        .build();
    public static final Words stopButton = new WordsBuilder
        ("停止")
        .setEn("Stop")
        .build();
    public static final Words stoppingButton = new WordsBuilder
        ("停止中...")
        .setEn("Stopping...")
        .build();
    public static final Words pauseButton = new WordsBuilder
        ("暂停")
        .setEn("Pause")
        .build();
    public static final Words pausingButton = new WordsBuilder
        ("暂停中...")
        .setEn("Pausing...")
        .build();
    public static final Words resumeButton = new WordsBuilder
        ("继续执行")
        .setEn("Resume")
        .build();
    public static final Words skipButton = new WordsBuilder
        ("下一指令")
        .setEn("Next Op")
        .build();
    public static final Words skippingButton = new WordsBuilder
        ("快进中...")
        .setEn("Skipping...")
        .build();

    public static final Words chooseScriptFile = new WordsBuilder
        ("选择脚本文件")
        .setEn("Choose a script file")
        .build();
    public static final Words noFileSelected = new WordsBuilder
        ("没有选择脚本文件")
        .setEn("No script file selected")
        .build();
    public static final Words invalidScriptFile = new WordsBuilder
        ("脚本文件错误")
        .setEn("Invalid script file")
        .build();
    public static final Words invalidAnchors = new WordsBuilder
        ("锚点坐标错误")
        .setEn("Invalid anchor coordinates")
        .build();
    public static final Words allOpsDone = new WordsBuilder
        ("所有指令均已执行完成")
        .setEn("All operations are done")
        .build();
    public static final Words noOpYet = new WordsBuilder
        ("当前未执行指令")
        .setEn("No op yet")
        .build();

    public static final Words selectBoundsHelpMessage = new WordsBuilder
        ("请拖动并拉伸半透明选框，使其覆盖FGO的窗口（包括标题栏）")
        .setEn("Please drag and stretch the translucent dialog, to make it cover the FGO window (including the title bar)")
        .build();
    public static final Words ensureGameScene = new WordsBuilder
        ("请保证目前游戏处于刚进入[战斗1]的场景。如果不是请点击取消，然后重新执行[准备]操作")
        .setEn("Please ensure the game is at scene [battle 1]. If not, please click the 'Cancel' button and re-run the 'Prepare' process")
        .build();
    public static final Words clickTitleBarHelpMessage = new WordsBuilder
        ("请透过半透明窗口点击FGO的[标题栏]")
        .setEn("Please click [title bar] of FGO through the translucent dialog")
        .build();
    public static final Words clickServant1Skill1HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[一号位从者]的[1技能]")
        .setEn("Please click [the First skill] of [the First servant] through the translucent dialog")
        .build();
    public static final Words clickServant1Skill2HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[一号位从者]的[2技能]")
        .setEn("Please click [the Second skill] of [the First servant] through the translucent dialog")
        .build();
    public static final Words clickServant1Skill3HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[一号位从者]的[3技能]")
        .setEn("Please click [the Third skill] of [the First servant] through the translucent dialog")
        .build();
    public static final Words clickServant2Skill2HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[二号位从者]的[2技能]")
        .setEn("Please click [the Second skill] of [the Second servant] through the translucent dialog")
        .build();
    public static final Words clickServant3Skill3HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[三号位从者]的[3技能]")
        .setEn("Please click [the Third skill] of [the Third servant] through the translucent dialog")
        .build();
    public static final Words clickMasterItemHelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[御主技能]按钮")
        .setEn("Please click [master skill] button through the translucent dialog")
        .build();
    public static final Words clickMasterSkill3HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[御主]的[3技能]")
        .setEn("Please click [the Third skill] of [Master] through the translucent dialog")
        .build();
    public static final Words clickMasterSkill1HelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[御主]的[1技能]")
        .setEn("Please click [the First skill] of [Master] through the translucent dialog")
        .build();
    public static final Words clickTrophyHelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[战利品]图标（顶部偏右的小箱子图标）")
        .setEn("Please click [Trophy] icon (an icon looks like a box at top right) through the translucent dialog")
        .build();
    public static final Words clickAttackHelpMessage = new WordsBuilder
        ("请透过半透明窗口点击[攻击]按钮")
        .setEn("Please click [Attack] button through the translucent dialog")
        .build();
    public static final Words clickBackHelpMessage = new WordsBuilder
        ("请透过半透明窗口点击游戏右下角的[返回]按钮")
        .setEn("Please click [back] button at right-bottom corner of the game through the translucent dialog")
        .build();
    public static final Words readyToGoMessage = new WordsBuilder
        ("已准备就绪")
        .setEn("Ready to go")
        .build();
    public static final Words clickConfirmStageToSwitchCoordinatesInDifferentScenes = new WordsBuilder
        ("点击半透明窗口查看不同场景中的坐标")
        .setEn("Click the translucent window to check coordinates in different scenes")
        .build();

    public static final Words titlePoint = new WordsBuilder
        ("标题")
        .setEn("Title")
        .build();
    public static final Words skillPoint = new WordsBuilder
        ("技能")
        .setEn("Skill ")
        .build();
    public static final Words attackPoint = new WordsBuilder
        ("攻击")
        .setEn("Attack")
        .build();
    public static final Words backPoint = new WordsBuilder
        ("back")
        .setEn("back")
        .build();
    public static final Words masterItemPoint = new WordsBuilder
        ("御主技能按钮")
        .setEn("Master Skill Button")
        .build();
    public static final Words masterSkillPoint = new WordsBuilder
        ("御主技能")
        .setEn("Master Skill ")
        .build();
    public static final Words trophyPoint = new WordsBuilder
        ("战利品")
        .setEn("Trophy")
        .build();
    public static final Words originPoint = new WordsBuilder
        ("原点")
        .setEn("Origin")
        .build();
    public static final Words maxXYPoint = new WordsBuilder
        ("最大范围")
        .setEn("Max bounds")
        .build();
    public static final Words orderCardPoint = new WordsBuilder
        ("指令卡")
        .setEn("Order Card ")
        .build();
    public static final Words noblePhantasmPoint = new WordsBuilder
        ("宝具")
        .setEn("Noble Phantasm ")
        .build();
    public static final Words selectPoint = new WordsBuilder
        ("选择")
        .setEn("Select ")
        .build();
    public static final Words confirmPoint = new WordsBuilder
        ("确认")
        .setEn("Confirm")
        .build();
    public static final Words dummyClickPoint = new WordsBuilder
        ("随意点击")
        .setEn("Dummy click")
        .build();
    public static final Words nextPoint = new WordsBuilder
        ("下一步")
        .setEn("Next")
        .build();
    public static final Words apCostRec = new WordsBuilder
        ("消耗AP")
        .setEn("AP Cost")
        .build();
    public static final Words noDirectNextPoint = new WordsBuilder
        ("退出")
        .setEn("Exit")
        .build();
    public static final Words directNextGamePoint = new WordsBuilder
        ("连续出击")
        .setEn("Next game")
        .build();
    public static final Words apStonePoint = new WordsBuilder
        ("石头恢复体力")
        .setEn("Use stone to restore ap")
        .build();
    public static final Words appleGoldenPoint = new WordsBuilder
        ("金苹果")
        .setEn("Golden apple")
        .build();
    public static final Words appleSilverPoint = new WordsBuilder
        ("银苹果")
        .setEn("Silver apple")
        .build();
    public static final Words appleCopperPoint = new WordsBuilder
        ("铜苹果")
        .setEn("Copper apple")
        .build();
    public static final Words confirmToUseApplePoint = new WordsBuilder
        ("确认使用苹果")
        .setEn("Confirm to use apple")
        .build();
    public static final Words casterSupportPoint = new WordsBuilder
        ("术阶")
        .setEn("Caster")
        .build();
    public static final Words refreshListPoint = new WordsBuilder
        ("刷新列表")
        .setEn("Refresh list")
        .build();
    public static final Words supportSkillPoint = new WordsBuilder
        ("技能")
        .setEn("Skill ")
        .build();
    public static final Words chooseSupportPoint = new WordsBuilder
        ("选择助战")
        .setEn("Choose support ")
        .build();
    public static final Words confirmRefreshingSupportPoint = new WordsBuilder
        ("确认刷新助战")
        .setEn("Confirm refreshing support")
        .build();
    public static final Words lastGamePoint = new WordsBuilder
        ("上次游戏")
        .setEn("Last game")
        .build();
    public static final Words cancelApplePoint = new WordsBuilder
        ("取消使用苹果")
        .setEn("Cancel using apple")
        .build();
    public static final Words goldenAppleIconPoint = new WordsBuilder
        ("金苹果")
        .setEn("Golden apple")
        .build();
    public static final Words silverAppleIconPoint = new WordsBuilder
        ("银苹果")
        .setEn("Silver apple")
        .build();
    public static final Words startPoint = new WordsBuilder
        ("开始")
        .setEn("Start")
        .build();

    public static final Words ScathachSkathi = new WordsBuilder
        ("斯卡哈·斯卡蒂")
        .build();
    public static final Words ZhugeKongming = new WordsBuilder
        ("诸葛孔明")
        .build();
}

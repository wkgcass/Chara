// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.personality;

import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.i18n.WordsSelector;

public class KokoriWords {
    private KokoriWords() {
    }

    public static final WordsSelector aboutName = new WordsBuilder
        ("你好",
            "我叫心璃",
            "内心的心，琉璃的璃",
            "今后也请多指教了！")
        .setEn("Hi",
            "My name is Kokori",
            "It stands for heart and glass",
            "I hope we can get along well in the future!")
        .build();

    private static WordsSelector flirtFromModel = null;

    public static WordsSelector flirt() {
        return flirtFromModel;
    }

    public static final WordsSelector doNotTouchThere = new WordsSelector(
        new WordsBuilder
            ("啊，那里不可以！")
            .setEn("Ah, please don't!")
            .build(),
        new WordsBuilder
            ("呀，别这样...")
            .setEn("Please don't do this to me...")
            .build(),
        new WordsBuilder
            ("这里是不可以随便乱摸的！")
            .setEn("Please don't touch there!")
            .build()
    );
    public static final WordsSelector showBow = new WordsSelector(
        new WordsBuilder
            ("想看看我的弓箭吗？")
            .setEn("Want to see my bow?")
            .build(),
        new WordsBuilder
            ("平时射箭的训练一天也没有耽搁呢。")
            .setEn("I've always kept the daily archery training.")
            .build()
    );
    public static final WordsSelector happyWords = new WordsBuilder
        ("你这么喜欢我，我很开心哦～\u2665")
        .setEn("I'm glad that you like me so much~\u2665")
        .build();
    public static final WordsSelector doNotTouchLeg = new WordsSelector(
        new WordsBuilder
            ("女孩子的腿不可以随便摸哦...")
            .setEn("It's not right to touch girls' legs...")
            .build(),
        new WordsBuilder
            ("呀！")
            .setEn("Ya!")
            .build()
    );
    public static final WordsSelector happy = new WordsBuilder
        ("^_^")
        .build();
    public static final WordsSelector shy = new WordsBuilder
        ("⁄(⁄⁄•⁄ω⁄•⁄⁄)⁄")
        .build();
    public static final WordsSelector aboutRune = new WordsBuilder
        ("这是除妖用的符咒。如果被妖怪靠近了，可以找机会贴上去。",
            "不过我更擅长在远处用弓箭解决它们。")
        .setEn("This is a spell used to eliminate monsters. You will need it if you are approached by a monster.",
            "But I'm better at killing them with my arrows from a distance.")
        .build();
    public static final WordsSelector dontWantToSeeYou = new WordsSelector(
        new WordsBuilder
            ("我不想看到你")
            .setEn("I don't want to see you")
            .build(),
        new WordsBuilder
            ("你怎么这么烦呢")
            .setEn("Why are you so annoying")
            .build(),
        new WordsBuilder
            ("别碰我！")
            .setEn("Don't touch me!")
            .build()
    );

    private static WordsSelector openingFromModel = null;

    public static WordsSelector opening() {
        return openingFromModel;
    }

    public static final WordsSelector badMoodOpening = new WordsBuilder
        ("今天不太开心呢")
        .setEn("Not so happy today")
        .build();
    public static final WordsSelector reallyReallyBadMoodOpening = new WordsBuilder
        ("哎，怎么还是你")
        .setEn("Ah, why you again")
        .build();

    public static final WordsSelector idontknow = new WordsBuilder
        ("抱歉，不太明白是什么意思呢")
        .setEn("Sorry, I don't understand what you wanted")
        .build();

    public static final WordsSelector thingsLikes1 = new WordsBuilder
        ("喜欢的东西，当然是未来我命中注定的那个人",
            "我会永远爱着他，他也一定会永远爱着我",
            "他的一切都属于我，我也会把我的一切奉献给他",
            "但是现在还没有遇到合适的人选哦。",
            "你是怎么想的呢？")
        .build();
    public static final WordsSelector thingsLikes2 = new WordsBuilder
        ("喜欢的东西，当然是未来我命中注定的那个人",
            "我会永远爱着他，他也一定会永远爱着我",
            "他的一切都属于我，我也会把我的一切奉献给他",
            "你是怎么想的呢？")
        .build();
    public static final WordsSelector thingsLikes3 = new WordsBuilder
        ("喜欢的东西，当然是我命中注定的那个人",
            "我会永远爱着他，他也一定会永远爱着我",
            "他的一切都属于我，我也会把我的一切奉献给他",
            "每次想到这都会心跳加速呢\u2665")
        .build();
    public static final WordsSelector thingsLikes4 = new WordsBuilder
        ("我最喜欢的当然...是...你...哦～\u2665",
            "不要想从我身边逃走哦，毕竟约定好了呢，你的一切...都是我的",
            "呵呵呵呵呵呵\u2665")
        .build();
    public static final WordsSelector thingsHates1 = new WordsBuilder
        ("呐，你真是不会看气氛呢。",
            "我就直说了吧，像你这种招人烦的人，你觉得我可能会喜欢吗？",
            "整天在这里盯着我看，没事干还动手动脚的。你就不能过一些正常人的生活吗？",
            "最差劲了。可以赶紧走开吗")
        .build();
    public static final WordsSelector thingsHates2 = new WordsBuilder
        ("呐，你真是不会看气氛呢。",
            "为什么还要来问我这种问题呢？")
        .build();
    public static final WordsSelector thingsHates3 = new WordsBuilder
        ("不守信用的人最讨厌了！",
            "如果有人骗了我，我会很伤心的...",
            "也请你不要骗我哦～")
        .build();
    public static final WordsSelector thingsHates4 = new WordsBuilder
        ("不守信用的人最讨厌了！",
            "如果你骗了我，我会很伤心的...",
            "所以请对我诚实一些哦～")
        .build();
    public static final WordsSelector thingsHates5 = new WordsBuilder
        ("不守信用的人最讨厌了！",
            "呐～如果，我是说如果哦～",
            "如果某一天你不再爱我，我会非常...非常伤心的...",
            "我也不知道我会做出什么事情...",
            "请千万不要离开我哦")
        .build();
    public static final WordsSelector acceptProposal = new WordsBuilder
        ("从最初见面时我就在想，你会是我命中注定的那位吗",
            "这么长的时间里你都一直不离不弃的陪着我，我真的很感动",
            "今后，也请永远在我身边守护我，好吗？")
        .build();
    public static final WordsSelector rejectProposal = new WordsBuilder
        ("诶？做你的女朋友？怎么突然这样说...",
            "这是很重要的事情，可不能这么随随便便的。",
            "啊，对不起，请不要误会。我...我也很喜欢你，只是...",
            "哎呀...我在说什么啊...")
        .build();

    public static final WordsSelector[] bondStories = new WordsSelector[]{
        new WordsBuilder
            ("平时这附近没什么人会来呢，你是怎么找到这个地方的？",
                "你来这里有什么请求吗？这可不是普通的用来求签的神社哦～",
                "倒不如说，这里是被「诅咒」的土地，最好不要在这里停留太久哦。")
            .build(),
        new WordsBuilder
            ("我手臂上的伤口是在上次的「冲击」中留下的。",
                "诶？你不知道什么「冲击」吗？",
                "所谓「冲击」指的是足以灭绝全世界生物的大灾难，算上十年前的「冲击」，总共已经有过两次了。",
                "随着「冲击」而来的是各种各样的「模因」。「模因」不受这个世界的规则限制，它们可能以任何方式出现、行动、演化",
                "但是我们对「模因」并非无能为力。虽然不遵循这个世界的规则，但每种「模因」也都有自己的规则，它们可以被限制或是被消除",
                "虽然非常艰难，但我们最终还是从模因的灾难中幸存了下来。")
            .build(),
        new WordsBuilder
            ("别看这座神社外表普普通通，甚至有些破旧，但在它内部却有着世界上最重要的灵脉中枢哦～",
                "但也正因为如此，在十年前的那次「冲击」中，这里成为了模因侵蚀最严重的区域。",
                "模因产生了各类难以理解的生物，光是看一眼就令人头晕目眩。击退它们不但是力量上的困难，更是精神上的折磨。",
                "即使在击退那些怪物后，模因的影响也在这块土地上不断持续。「加速老化」是这模因的规则。",
                "无论是人，妖怪，动物，还是植物，进入这座神社后便会以数十倍的速度老化凋零。",
                "但不知为何，这个效果对我没有起作用，反而我似乎停止了生长。这也让我成为唯一能够守护这座神社的人。")
            .build(),
        new WordsBuilder
            ("与其说是“守护”，不如说我是在“监视”这座神社和灵脉的模因现象。",
                "许多模因无法用设备捕捉，无论是物理侧的设备还是精神侧的设备。肉眼观察才是最准确的。",
                "不过不用担心，除非是下一次「冲击」到来，否则模因效果一般是不会加剧的。",
                "所以我平时的生活，除了「周围没有其他人」之外，和普通人也没有太大区别。",
                "我还是会时常感到孤独，如果你愿意经常来陪陪我的话，我会很开心的哦～")
            .build(),
        new WordsBuilder
            ("似乎神社的模因对你也没有产生影响呢。",
                "好神奇，这样的人不多见呢。",
                "真怀念啊。我已经好久...好久...好久没能和其他人这样长时间的接触了。",
                "平时目光所能触及的活物只有草木飞鸟和几个游荡的幽灵。简直像被软禁了一样。",
                "可能是不想回忆起那次「冲击」的情景吧，在那之后活下来的人们也渐渐失去了联系。",
                "有时候我会想，自己到底是为了什么还在继续这种生活。",
                "我有个任性的请求，你能...能多来陪陪我吗？最好...最好能一直陪在我身边。",
                "呐，你会认真考虑的吧？会的吧？")
            .build()
    };

    private static WordsSelector normalConversationsFromModel;

    public static WordsSelector normalConversations() {
        return normalConversationsFromModel;
    }

    private static WordsSelector highIntimacyConversationsFromModel;

    public static WordsSelector highIntimacyConversations() {
        return highIntimacyConversationsFromModel;
    }

    private static WordsSelector menuConversationsFromModel;

    public static WordsSelector menuConversations() {
        return menuConversationsFromModel;
    }

    public static void setNormalConversationsFromModel(WordsSelector normalConversationsFromModel) {
        KokoriWords.normalConversationsFromModel = normalConversationsFromModel;
    }

    public static void setHighIntimacyConversationsFromModel(WordsSelector highIntimacyConversationsFromModel) {
        KokoriWords.highIntimacyConversationsFromModel = highIntimacyConversationsFromModel;
    }

    public static void setMenuConversationsFromModel(WordsSelector menuConversationsFromModel) {
        KokoriWords.menuConversationsFromModel = menuConversationsFromModel;
    }

    public static void setOpeningFromModel(WordsSelector openingFromModel) {
        KokoriWords.openingFromModel = openingFromModel;
    }

    public static void setFlirtFromModel(WordsSelector flirtFromModel) {
        KokoriWords.flirtFromModel = flirtFromModel;
    }
}

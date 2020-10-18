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
            ("平时射箭的训练一天也没有耽搁呢。",
                "你也想看看吗？")
            .setEn("I've always kept the daily archery training.",
                "Do you want to see?")
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

    public static final WordsSelector thingsLikes = new WordsBuilder
        ("我最喜欢吃青团子",
            "这是江南地区的特色小吃",
            "用艾草的汁拌进糯米粉里，再包裹进豆沙馅儿或者莲蓉，不甜不腻，带有清淡却悠长的青草香气",
            "怎么样？你有没有尝过？")
        .build();
    public static final WordsSelector thingsHates = new WordsBuilder
        ("我最讨厌蛊惑人心的妖魔",
            "它们只为了自己的一点点利益，就可以断章取义，捏造是非，颠倒黑白",
            "它们能够加深人们相互之间的隔阂，扭曲人心，甚至让他人为自己所支配",
            "让我更加害怕的是，这样的妖魔无处不在，也难以侦辨",
            "这样的妖魔往往会利用人的欲望来蛊惑人心，但即使我可以控制自己的欲望，我也没办法让身边的人也控制住",
            "呐，你见到过这样的妖魔吗？或者说，你会成为这样的妖魔吗？")
        .build();

    public static final WordsSelector[] bondStories = new WordsSelector[]{
        new WordsBuilder
            ("诶？小心一点哦。有什么事我们到神社外面说吧。",
                "你身上的「灵」不太一样呢。你是从其他的世界来的吧？",
                "的确听说过一些传闻，上次「冲击」时有其他世界的人也被送来了这里。",
                "你来这里有什么请求吗？这可不是普通的用来求签的神社哦～")
            .build(),
        new WordsBuilder
            ("十年前的「冲击」，你应该也知道把？",
                "那次「冲击」中产生的怪物在我左臂上留下了一道可怕的伤口。",
                "即使早已愈合，却也留下了丑陋的印记",
                "现在习惯性得用绷带绑住藏起来，也能稍微好看一点吧，嘿嘿～")
            .build(),
        new WordsBuilder
            ("别看这座神社外表普普通通，甚至有些破旧，但在它内部却有着世界上最重要的灵脉中枢哦～",
                "但也正因为如此，在十年前的那次「冲击」中，这里成为了模因侵蚀最严重的区域。",
                "模因产生了各类难以理解的生物，光是看一眼就令人头晕目眩。击退它们不但是力量上的困难，更是精神上的折磨。",
                "即使在击退那些怪物后，模因的影响也在这块土地上不断持续。「无法生存」便是最显著的影响。",
                "无论是人，妖怪，动物，还是植物，进入这座神社后便会以数十倍的速度老化凋零。",
                "但不知为何，这个效果对我没有起作用。这也让我成为唯一能够守护这片神社的人。")
            .build(),
        new WordsBuilder
            ("与其说是“守护”，不如说我是在“监视”这座神社和灵脉。",
                "许多模因无法用设备捕捉，无论是物理侧的设备还是精神侧的设备。肉眼观察才是最准确的。",
                "不过不用担心，除非是下一次「冲击」到来，否则模因效果一般是不会加剧的。而「冲击」最少也得几百年才会出现一次吧。",
                "所以我平时的生活，除了“周围没有其他人”之外，和普通人也没有太大区别。",
                "因为只有我自己在嘛，所以也只能自己做东西吃啦",
                "以前老师和伙伴们还在的时候，我也是经常下厨的哦～",
                "呐，如果有机会的话，你愿意尝一尝我的手艺吗？")
            .build(),
        new WordsBuilder
            ("似乎神社的模因对你也没有产生影响呢。",
                "好神奇，这样的人不多见呢。",
                "真怀念啊。我已经好久...好久...好久没能和其他人这样长时间的接触了。",
                "每周只能用几个小时外出采购生活必需品，平时目光所能触及的活物只有草木飞鸟和几个游荡的幽灵。简直像被软禁了一样。",
                "可能是不想脱离那次「冲击」的影响吧，在那之后活下来的曾经的熟人也渐渐失去了联系。",
                "有时候我会想，自己到底是为了什么还在继续这种生活。",
                "我有个任性的请求，你能...能多来陪陪我吗？最好能一直...一直陪在我身边。",
                "呐，你会认真考虑的吧？会的吧？",
                "呐？",
                "呐？")
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

    public static void setNormalConversationsFromModel(WordsSelector normalConversationsFromModel) {
        KokoriWords.normalConversationsFromModel = normalConversationsFromModel;
    }

    public static void setHighIntimacyConversationsFromModel(WordsSelector highIntimacyConversationsFromModel) {
        KokoriWords.highIntimacyConversationsFromModel = highIntimacyConversationsFromModel;
    }

    public static void setOpeningFromModel(WordsSelector openingFromModel) {
        KokoriWords.openingFromModel = openingFromModel;
    }

    public static void setFlirtFromModel(WordsSelector flirtFromModel) {
        KokoriWords.flirtFromModel = flirtFromModel;
    }
}

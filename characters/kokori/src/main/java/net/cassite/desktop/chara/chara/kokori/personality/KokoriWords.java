// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.personality;

import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.i18n.WordsSelector;
import net.cassite.desktop.chara.util.Utils;

public class KokoriWords {
    private KokoriWords() {
    }

    public static final WordsSelector aboutName = new WordsBuilder
        ("你好",
            "我叫心璃",
            "内心的心，琉璃的璃")
        .setEn("Hi",
            "My name is XinLi (Kokori)",
            "It stands for heart and glass")
        .build();

    private static final WordsSelector flirt = new WordsSelector(
        new WordsBuilder
            ("这...",
                "好吧...")
            .setEn("Ah, this is a little bit...",
                "Fine then...")
            .build(),
        new WordsBuilder
            ("我会害羞的...")
            .setEn("I may become so shy...")
            .build()
    );
    private static WordsSelector flirtFromModel = null;

    public static WordsSelector flirt() {
        return chooseSelector(flirt, flirtFromModel);
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
            .setEn("I always keep the daily archery training.",
                "Do you want to see?")
            .build()
    );
    public static final WordsSelector wantSex = new WordsSelector(
        new WordsBuilder
            ("如果你也想要的话...",
                "可以哦～♡")
            .setEn("If that's what you want...",
                "I'm all yours~♡")
            .build(),
        new WordsBuilder
            ("在这里就要...吗？",
                "呵呵呵呵，你还真是急不可耐呢～")
            .setEn("You want to ... here?",
                "Hehehehe, you are really impatient~")
            .build(),
        new WordsBuilder
            ("你把我弄得有点奇怪了...")
            .setEn("You made me a little weird...")
            .build()
    );
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
            "不过我更喜欢在远处用弓箭解决它们。")
        .setEn("This is a spell used to eliminate monsters. You will need it if you are approached by a monster.",
            "But I prefer to use my arrow to do the work from a distance.")
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
    private static final WordsSelector opening = new WordsSelector(
        new WordsBuilder
            ("嗨，又见面啦")
            .setEn("Hey, how are you")
            .build()
    );
    private static WordsSelector openingFromModel = null;

    public static WordsSelector opening() {
        return chooseSelector(opening, openingFromModel);
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

    private static final WordsSelector normalConversations = new WordsBuilder
        ("这件衣服...啊，会不会有点太暴露了",
            "本来应该是普通的巫女服的，不知道为什么变成了现在这个样子",
            "诶？你说很合适？",
            "这...哈哈，稍微有点开心呢")
        .setEn("This cloth... Ah, Is it a bit too exposed",
            "It was supposed to be a standard mikofuku, I don't know why it becomes like this",
            "Eh? You said it looks pretty on me?",
            "Hah, you are sweet")
        .build();
    private static WordsSelector normalConversationsFromModel;

    public static WordsSelector normalConversations() {
        return chooseSelector(normalConversations, normalConversationsFromModel);
    }

    private static final WordsSelector highIntimacyConversations = new WordsBuilder
        ("诶？你-你说...接...接吻？有点...太突然了...我还没准备好",
            "但...如果是你的话...可...可以哦...")
        .setEn("Eh? D-Did you say...k...kiss? This suddenly...it's a little embarrassing... ",
            "But...if it's with you...I-I might be okay with it...")
        .build();
    private static WordsSelector highIntimacyConversationsFromModel;

    public static WordsSelector highIntimacyConversations() {
        return chooseSelector(highIntimacyConversations, highIntimacyConversationsFromModel);
    }

    private static WordsSelector chooseSelector(WordsSelector a, WordsSelector nullableB) {
        if (nullableB == null) {
            return a;
        }
        //noinspection UnnecessaryLocalVariable
        final var b = nullableB;

        double countA = a.count();
        double countB = b.count();
        if (Utils.random(countA / (countA + countB))) {
            return a;
        } else {
            return b;
        }
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

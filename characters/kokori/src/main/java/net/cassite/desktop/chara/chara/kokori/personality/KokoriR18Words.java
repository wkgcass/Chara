// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chara.kokori.personality;

import net.cassite.desktop.chara.i18n.WordsBuilder;
import net.cassite.desktop.chara.i18n.WordsSelector;

public class KokoriR18Words {
    private KokoriR18Words() {
    }

    public static final WordsSelector reject1 = new WordsBuilder
        ("你真是个变态")
        .setEn("")
        .build();
    public static final WordsSelector reject2 = new WordsBuilder
        ("诶？你在说什么啊！？")
        .setEn("Eh? What are you talking about!?")
        .build();
    public static final WordsSelector reject3 = new WordsBuilder
        ("我们还没到那种关系呢")
        .setEn("We haven't got that kind of relationship yet")
        .build();
    public static final WordsSelector reject4 = new WordsBuilder
        ("我现在还不是很想要...")
        .setEn("I don't want that for now...")
        .build();
    public static final WordsSelector wantSex = new WordsSelector(
        new WordsBuilder
            ("如果你也想要的话...",
                "可以哦～\u2665")
            .setEn("If that's what you want...",
                "It's ok with me~\u2665")
            .build(),
        new WordsBuilder
            ("在这里就要\u2665吗？",
                "呵呵呵呵，你还真是急不可耐呢～\u2665")
            .setEn("You want to \u2665 here?",
                "Hehehehe, you are really impatient~\u2665")
            .build(),
        new WordsBuilder
            ("你把我弄得有点奇怪了～\u2665")
            .setEn("You made me a little weird~\u2665")
            .build(),
        new WordsBuilder
            ("你想要了么？",
                "请对我温柔一点哦～\u2665")
            .setEn("You want sex?",
                "Please be gentle with me~\u2665")
            .build()
    );
    public static final WordsSelector cannotRestrain = new WordsSelector(
        new WordsBuilder
            ("我真的控制不住我自己了！")
            .setEn("I really can't control myself anymore!")
            .build()
    );
    public static final WordsSelector succeededUsingLovePotion = new WordsSelector(
        new WordsBuilder
            ("身体突然变得...好敏感",
                "好想要...",
                "啊，没有，我什么都没说！")
            .setEn("My body suddenly becomes ... so sensitive",
                "I want to make ...",
                "Ah, nothing, I didn't say anything!")
            .build()
    );
    public static final WordsSelector failedUsingLovePotion = new WordsSelector(
        new WordsBuilder
            ("你在干什么！？",
                "你居然想给我喂那种东西！")
            .setEn("What are you doing!?",
                "How dare you feed me such a thing!")
            .build()
    );

    public static final WordsSelector scream1 = new WordsSelector(
        new WordsBuilder
            ("啊～\u2665")
            .build(),
        new WordsBuilder
            ("嗯～啊～\u2665")
            .build(),
        new WordsBuilder
            ("这样很舒服哦～\u2665")
            .build()
    );
    public static final WordsSelector scream2 = new WordsSelector(
        new WordsBuilder
            ("里面...好烫...\u2665")
            .build(),
        new WordsBuilder
            ("太...啊\u2665太激烈了～")
            .build(),
        new WordsBuilder
            ("要坏掉了！\u2665")
            .build()
    );
    public static final WordsSelector scream3 = new WordsSelector(
        new WordsBuilder
            ("快一点...再快一点！！！\u2665")
            .build(),
        new WordsBuilder
            ("好棒啊！啊～啊啊～\u2665")
            .build(),
        new WordsBuilder
            ("太舒服了，没办法思考了\u2665")
            .build()
    );
    public static final WordsSelector scream4 = new WordsSelector(
        new WordsBuilder
            ("要...要去了！\u2665")
            .build(),
        new WordsBuilder
            ("啊～啊，我要不行了～\u2665")
            .build(),
        new WordsBuilder
            ("感觉有...有什么东西要...要出来了～\u2665")
            .build()
    );
    public static final WordsSelector finish = new WordsSelector(
        new WordsBuilder
            ("啊\u2665我好幸福\u2665")
            .build(),
        new WordsBuilder
            ("感觉要...要怀孕了呢...\u2665")
            .build(),
        new WordsBuilder
            ("要...要对我负责哦\u2665")
            .build()
    );
    public static final WordsSelector finishBadWithLovePotion = new WordsSelector(
        new WordsBuilder
            ("呜...呜呜...",
                "我的身体被玷污了...")
            .setEn("Wu ... wu wu ...",
                "My body is tarnished...")
            .build()
    );
    public static final WordsSelector finishWantMore = new WordsSelector(
        new WordsBuilder
            ("虽然很害羞，但是...",
                "但是...",
                "我...我还想要...")
            .build(),
        new WordsBuilder
            ("好厉害...已经高潮了好几次了",
                "但是我还...还没有满足...",
                "要...要变成...RBQ了")
            .build()
    );
}

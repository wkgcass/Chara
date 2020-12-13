// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.widget.fgoclick;

import net.cassite.desktop.chara.widget.fgoclick.statem.Statem;
import net.cassite.desktop.chara.widget.fgoclick.util.FgoClickUtils;
import org.junit.Test;
import vjson.cs.CharArrayCharStream;

public class TestDeserialize {
    private static final String json = "" +
        "{\n" +
        "  \"preset\": {\n" +
        "    \"servants\": [\n" +
        "      {\n" +
        "        \"name\": \"太空伊什塔尔\",\n" +
        "        \"skills\": [\n" +
        "          {\n" +
        "            \"name\": \"恶魔的砂糖\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"normal\"\n" +
        "            }\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"金星驱动\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"select\",\n" +
        "              \"options\": 3\n" +
        "            }\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"多重星环\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"normal\"\n" +
        "            }\n" +
        "          }\n" +
        "        ],\n" +
        "        \"noblePhantasm\": {\n" +
        "          \"name\": \"闪耀于原始宇宙的王冠\",\n" +
        "          \"action\": {\n" +
        "            \"time\": 13\n" +
        "          }\n" +
        "        }\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"斯卡哈·斯卡蒂\",\n" +
        "        \"skills\": [\n" +
        "          {\n" +
        "            \"name\": \"原初之卢恩\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"select\",\n" +
        "              \"options\": 3\n" +
        "            }\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"冰冻暴风雪\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"normal\"\n" +
        "            }\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"大地的睿智\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"select\",\n" +
        "              \"options\": 3\n" +
        "            }\n" +
        "          }\n" +
        "        ]\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"尼禄·克劳狄乌斯[新娘]\",\n" +
        "        \"skills\": [\n" +
        "          {\n" +
        "            \"name\": \"予天以星\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"select\",\n" +
        "              \"options\": 3\n" +
        "            }\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"予地以花\",\n" +
        "            \"action\": {\n" +
        "              \"type\": \"select\",\n" +
        "              \"options\": 3\n" +
        "            }\n" +
        "          }\n" +
        "        ]\n" +
        "      }\n" +
        "    ],\n" +
        "    \"masterSkills\": [\n" +
        "      {\n" +
        "        \"name\": \"全体回复\",\n" +
        "        \"action\": {\n" +
        "          \"type\": \"normal\"\n" +
        "        }\n" +
        "      },\n" +
        "      {\n" +
        "        \"name\": \"灵子转让\",\n" +
        "        \"action\": {\n" +
        "          \"type\": \"select\",\n" +
        "          \"options\": 3\n" +
        "        }\n" +
        "      }\n" +
        "    ],\n" +
        "    \"settings\": {\n" +
        "      \"totalBattles\": 3,\n" +
        "      \"confirmDialog\": true\n" +
        "    }\n" +
        "  },\n" +
        "  \"operations\": [\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 1,\n" +
        "        \"skill\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 3,\n" +
        "        \"skill\": 2,\n" +
        "        \"select\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"noblePhantasm\",\n" +
        "      \"noblePhantasm\": {\n" +
        "        \"servant\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"nextBattle\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 3,\n" +
        "        \"skill\": 1,\n" +
        "        \"select\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 1,\n" +
        "        \"skill\": 3\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"noblePhantasm\",\n" +
        "      \"noblePhantasm\": {\n" +
        "        \"servant\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"nextBattle\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 2,\n" +
        "        \"skill\": 1,\n" +
        "        \"select\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 2,\n" +
        "        \"skill\": 2\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 2,\n" +
        "        \"skill\": 3,\n" +
        "        \"select\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"skill\",\n" +
        "      \"skill\": {\n" +
        "        \"servant\": 1,\n" +
        "        \"skill\": 2,\n" +
        "        \"select\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"masterSkill\",\n" +
        "      \"masterSkill\": {\n" +
        "        \"skill\": 2,\n" +
        "        \"select\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"noblePhantasm\",\n" +
        "      \"noblePhantasm\": {\n" +
        "        \"servant\": 1\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"nextBattle\"\n" +
        "    }\n" +
        "  ]\n" +
        "}\n" +
        "";

    @Test
    public void deserialize() {
        var o = FgoClickUtils.deserialize(new CharArrayCharStream(json.toCharArray()));
        System.out.println(o);
    }

    @Test
    public void compile() {
        var ls = new Statem(FgoClickUtils.deserialize(new CharArrayCharStream(json.toCharArray()))).compile();
        for (var e : ls) {
            System.out.println(e);
        }
    }
}

// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.model.vpwsagent;

import javafx.scene.image.Image;
import net.cassite.desktop.chara.chara.Chara;
import net.cassite.desktop.chara.model.Model;
import net.cassite.desktop.chara.model.ModelInitConfig;
import net.cassite.desktop.chara.util.ResourceHandler;
import net.cassite.desktop.chara.widget.vpwsagent.Agent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WebSocksProxyAgentModel implements Model {
    private AgentConsts consts;

    @Override
    public String name() {
        return "vpws-agent";
    }

    @Override
    public int version() {
        return 1000000; // _THE_VERSION_
    }

    private static final Data data = new DataBuilder()
        .setAboutMessage("" +
            "author: wkgcass\n" +
            "code license: GPLv2 with classpath exception"
        )
        .setActiveInteractionSupported(false)
        .setDefaultAllowActiveInteraction(false)
        .setDefaultAlwaysOnTop(true)
        .setDefaultMessageEnabled(true)
        .setDefaultMouseIndicatorEnabled(false)
        .setDefaultShowIconOnTaskbar(false)
        .setMessageSupported(false)
        .setModelMenuItemText(AgentConsts.proxyMenuItem)
        .build();

    @Override
    public Data data() {
        return data;
    }

    @Override
    public Chara construct(ConstructParams params) {
        return new Agent(params, new Resources(idleImage, launchingImage, runningImage), consts);
    }

    @Override
    public List<String> requiredImages() {
        return Collections.emptyList();
    }

    private Image idleImage;
    private Image launchingImage;
    private Image runningImage;

    @Override
    public List<ResourceHandler> resourceHandlers() {
        return Arrays.asList(
            new ResourceHandler("idle.png", (input, cb) -> {
                idleImage = new Image(input);
                cb.succeeded();
            }),
            new ResourceHandler("launching.png", (input, cb) -> {
                launchingImage = new Image(input);
                cb.succeeded();
            }),
            new ResourceHandler("running.png", (input, cb) -> {
                runningImage = new Image(input);
                cb.succeeded();
            })
        );
    }

    @Override
    public void init(ModelInitConfig config) {
        consts = new AgentConsts(config);
    }
}

package net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ;

import com.germ.germplugin.api.dynamic.gui.GermGuiLabel;
import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.util.UtilGerm2K;

import java.util.UUID;

public class GAudioGermStateGui extends GermGuiScreen {
    public final AudioState state;

    public GAudioGermStateGui(AudioState state) {
        super(UUID.randomUUID().toString(), true);
        this.state = state;
        init();
    }

    protected void init() {
        getOptions().setStartX("w/2*-1").setStartY("h*2*-1");

        addGuiPart(UtilGerm2K.createColor("color", 0x88FF8000, 0, 0, 2560, 400));
        addGuiPart(UtilGerm2K.createLabel("name", state.name + "[" + state.id + "]", 1280, 720-90, 25F, GermGuiLabel.Align.CENTER));
        if("DEFAULT".equals(state.audio.type))
            addGuiPart(UtilGerm2K.createTexture("tex", "effect/audio_range.png")
                    .setWidth("tw1/2560*w").setHeight("th1/1440*h")
                    .setLocationX("w/2-(tw1/2/2560*w)").setLocationY("h/2-(th1/2/1440*h)")
            );
    }
}

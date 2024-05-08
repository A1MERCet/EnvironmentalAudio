package net.mcbbs.a1mercet.environmentalaudio.util;

import com.germ.germplugin.api.dynamic.DynamicBase;
import com.germ.germplugin.api.dynamic.animation.GermAnimationScale;
import com.germ.germplugin.api.dynamic.gui.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UtilGerm2K
{
    public static GermGuiCanvas setLocation(GermGuiCanvas part , int x , int y)
    {
        return part.setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h");
    }
    public static GermGuiScroll setLocation(GermGuiScroll part , int x , int y)
    {
        return part.setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h");
    }
    public static GermGuiTexture setLocation(GermGuiTexture part , int x , int y)
    {
        return part.setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h");
    }
    public static GermGuiPart<? extends DynamicBase> setLocation(GermGuiPart<? extends DynamicBase> part , int x , int y)
    {
        return part.setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h");
    }
    public static IGuiPartResizeable<?> setSize(IGuiPartResizeable<?> part , int w , int h)
    {
        part.setWidth(w/2560F+"*w");
        part.setHeight(h/1440F+"*h");
        return part;
    }
    public GermGuiButton createGuideButton(int w, int h,int x, int y)
    {
        return new GermGuiButton(UUID.randomUUID().toString())
                .setHoverPath("aestus/gui/guide/button.png").setDefaultPath("aestus/gui/guide/button.png")
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(w/2560F+"*w").setHeight(h/1440F+"*h");
    }
    public static class GMask extends GermGuiCanvas
    {
        protected final boolean hasSkip;
        protected final GermGuiCanvas mask = new GermGuiCanvas("mask");
        protected GermGuiButton skip;

        public GMask(String indexName , boolean hasSkip)
        {
            super(indexName);
            this.hasSkip=hasSkip;
            init();
        }

        protected void init()
        {
            if(hasSkip) {skip=createSkip();addGuiPart(skip);}
        }

        protected GermGuiButton createSkip()
        {
            return createButton("skip","",0,0)
                    .registerCallbackHandler(this::callbackSkip, GermGuiButton.EventType.LEFT_CLICK);
        }

        protected void callbackSkip(Player p , GermGuiButton b)
        {

        }
        public GMask updateMask(int w, int h, int x, int y){return updateMask(w,h,x,y,-1,-1);}

        public GMask updateMask(int w, int h, int x, int y , int skipX , int skipY)
        {
            if(skip!=null)
            {
                if(skipX>-1)skip.setLocationX(skipX/2560F+"*w");
                if(skipY>-1)skip.setLocationY(skipY/1440F+"*h");
            }
            mask.clearGuiPart();
            mask.addGuiPart(new GermGuiButton("top")
                    .setHoverPath("aestus/gui/guide/button.png").setDefaultPath("aestus/gui/guide/button.png")
                    .setWidth("w").setHeight(y/2560F+"*h"));
            mask.addGuiPart(new GermGuiButton("under")
                    .setHoverPath("aestus/gui/guide/button.png").setDefaultPath("aestus/gui/guide/button.png")
                    .setLocationX(x/2560F+"*w").setLocationY(y+h/1440F+"*h")
                    .setWidth("w").setHeight("h"));
            mask.addGuiPart(new GermGuiButton("left")
                    .setHoverPath("aestus/gui/guide/button.png").setDefaultPath("aestus/gui/guide/button.png")
                    .setLocationX("0").setLocationY(y/2560F+"*h")
                    .setWidth(x/1440F+"*w").setHeight(y+h/1440F+"*h"));
            mask.addGuiPart(new GermGuiButton("right")
                    .setHoverPath("aestus/gui/guide/button.png").setDefaultPath("aestus/gui/guide/button.png")
                    .setLocationX(x+w+"/2560*w").setLocationY(y/2560F+"*h")
                    .setWidth("w").setHeight(y+h/1440F+"*h"));
            return this;
        }
    }

    public GMask createGuideButtonMask(String name ,int w, int h, int x, int y , boolean hasSkip)
    {
        return new GMask(name,hasSkip).updateMask(w, h, x, y);
    }
    public static GermGuiTexture setSize(GermGuiTexture part , int w , int h)
    {
        return part.setWidth(w/2560F+"*w").setHeight(h/1440F+"*h");
    }
    public static GermGuiButton setLocation(GermGuiButton part , int x , int y)
    {
        return part.setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h");
    }
    public static GermGuiButton setSize(GermGuiButton part , int w , int h)
    {
        return part.setWidth(w/2560F+"*w").setHeight(h/1440F+"*h");
    }
    public static GermGuiTexture createBackground(String name , String path)
    {
        return new GermGuiTexture(name).setPath(path).setWidth("w").setHeight("h");
    }
    public static GermGuiTexture createTexture(String name , String path , int x , int y , int width , int height)
    {
        return new GermGuiTexture(name).setPath(path)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(width/2560F+"*w").setHeight(height/1440F+"*h");
    }
    public static GermGuiCanvas createCanvas(String name , int x , int y , int width , int height)
    {
        return new GermGuiCanvas(name)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(width/2560F+"*w").setHeight(height/1440F+"*h");
    }
    public static GermGuiLabel createLabel(String name , String text , int x , int y)
    {
        return new GermGuiLabel(name).setShadow(true).setText(text).setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h").setFont("font").setFontSize(48);
    }
    public static GermGuiLabel createLabel(String name , String text , int x , int y , float scale)
    {
        return new GermGuiLabel(name).setShadow(true).setText(text).setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h").setFont("font").setFontSize(48).setScale(scale+"/2560*w");
    }
    public static GermGuiLabel createLabel(String name , String text , int x , int y , float scale, GermGuiLabel.Align align)
    {
        return new GermGuiLabel(name).setShadow(true).setText(text).setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h").setFont("font").setFontSize(48).setScale(scale+"/2560*w").setAlign(align);
    }
    public static GermGuiTexture createTexture(String name , String path)
    {
        return new GermGuiTexture(name).setPath(path).setWidth("tw1/2560*w").setHeight("th1/1440*h");
    }
    public static GermGuiTexture createTexture(String name , String path , int x , int y )
    {
        return new GermGuiTexture(name).setPath(path)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth("tw1/2560*w").setHeight("th1/1440*h");
    }

    public static GermGuiColor createColor(String name , long color , int x , int y , int width , int height)
    {
        return new GermGuiColor(name)
                .setColor(color).setEndColor(color)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(width/2560F+"*w").setHeight(height/1440F+"*h");
    }

    public static GermGuiButton createButton(String name , String path , String path2 , int x , int y , int width , int height)
    {
        return new GermGuiButton(name).setDefaultPath(path).setHoverPath(path2)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(width/2560F+"*w").setHeight(height/1440F+"*h")
                .setClickSound("tarz:menu.button_click2").setHoverSound("tarz:menu.button_hov2");
    }

    public static GermGuiButton createButton(String name , String path , int x , int y , int width , int height)
    {
        return new GermGuiButton(name).setDefaultPath(path).setHoverColor(UtilColor.GREEN)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(width/2560F+"*w").setHeight(height/1440F+"*h")
                .setClickSound("tarz:menu.button_click2").setHoverSound("tarz:menu.button_hov2");
    }

    public static GermGuiButton createButton(String name , String path , String path2 , int x , int y)
    {
        return new GermGuiButton(name).setDefaultPath(path).setHoverPath(path2)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth("tw1/2560*w").setHeight("th1/1440*h")
                .setClickSound("tarz:menu.button_click2").setHoverSound("tarz:menu.button_hov2");
    }

    public static GermGuiButton createButtonAnimation(String name , String path , int x , int y){return createButtonAnimation(name, path, x, y ,0.9F);}
    public static GermGuiButton createButtonAnimation(String name , String path , int x , int y , float scale)
    {
        return new GermGuiButton(name){
            @Override public void onCallback(Player player, Enum<?> anEnum) {
                super.onCallback(player, anEnum);
                if(anEnum==EventType.BEGIN_HOVER)
                    setAnimation(new GermAnimationScale("but_in").setCycle(1).setPermanent(true).setFrom(1).setTo(scale).setDuration(100));
                else if(anEnum==EventType.LEAVE_HOVER)
                    setAnimation(new GermAnimationScale("but_out").setCycle(1).setPermanent(true).setFrom(scale).setTo(1).setDuration(100));
            }
        }
                .setDefaultPath(path).setHoverColor(UtilColor.GREEN)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth("tw1/2560*w").setHeight("th1/1440*h")
                .setClickSound("tarz:menu.button_click2").setHoverSound("tarz:menu.button_hov2");
    }

    public static GermGuiButton createMain(String name , String path)
    {
        return new GermGuiButton(name).setDefaultPath(path).setSwallow(true)
                .setWidth("tw1/2560*w").setHeight("th1/1440*h");
    }
    public static GermGuiButton createMain(String name , String path , int x , int y)
    {
        return new GermGuiButton(name).setDefaultPath(path).setSwallow(true)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth("tw1/2560*w").setHeight("th1/1440*h");
    }
    public static GermGuiButton createMain(String name , String path , int x , int y,int w ,int h)
    {
        return new GermGuiButton(name).setDefaultPath(path).setSwallow(true)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth(w/2560F+"*w").setHeight(h/1440F+"*h");
    }

    public static GermGuiButton createButton(String name , String path , int x , int y)
    {
        return new GermGuiButton(name).setDefaultPath(path).setHoverColor(UtilColor.GREEN)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth("tw1/2560*w").setHeight("th1/1440*h")
                .setClickSound("tarz:menu.button_click2").setHoverSound("tarz:menu.button_hov2");
    }
    public static GermGuiButton createButtonBG(String name , String path , int x , int y)
    {
        return new GermGuiButton(name).setDefaultPath(path).setHoverPath(path)
                .setLocationX(x/2560F+"*w").setLocationY(y/1440F+"*h")
                .setWidth("tw1/2560*w").setHeight("th1/1440*h").setSwallow(true).setInvalid(true);
    }
    public static GermGuiButton createButtonBG(String name , String path)
    {
        return new GermGuiButton(name).setDefaultPath(path).setHoverPath(path)
                .setWidth("tw1/2560*w").setHeight("th1/1440*h").setSwallow(true).setInvalid(true);
    }
}

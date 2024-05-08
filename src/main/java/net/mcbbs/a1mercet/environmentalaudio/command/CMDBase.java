package net.mcbbs.a1mercet.environmentalaudio.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public abstract class CMDBase implements CommandExecutor
{
    public enum ArgType
    {
        DEPEND("前置"){
            @Override public Object formart(String v) {return null;}},
        INTEGER("整型"){
            @Override public Object formart(String v) {try {return Integer.parseInt(v);}catch (Exception e){return null;}}},
        STRING("字符"){
            @Override public Object formart(String v) {try {return v;}catch (Exception e){return null;}}},
        FLOAT("浮点"){
            @Override public Object formart(String v) {try {return Float.parseFloat(v);}catch (Exception e){return null;}}},
        BOOLEAN("布尔"){
            @Override public Object formart(String v) {try {return Boolean.valueOf(v);}catch (Exception e){return null;}}},
        ;
        public final String name;
        ArgType(String name) {this.name = name;}
        public Object formart(String v){return null;}
    }


    @Retention      (value = RetentionPolicy.RUNTIME)
    @Target         (value = ElementType.METHOD)
    @Documented
    public @interface CommandArgs {
        String describe()  default "无";
        String[] args();
        ArgType[] types();
        boolean playerOnly() default false;
    }

    public final String name;
    public final Class<?> clazz;
    public Method[] methods;

    public CMDBase(String name,Class<?> clazz) {
        this.name = name;
        this.clazz=clazz;
        this.methods=clazz.getMethods();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
    {
        if (command.getName().equalsIgnoreCase(name))
        {
            if(methods==null)methods=clazz.getMethods();

            boolean executed = false;
            for(Method method : methods)
                if(canInvoke(method,args))
                {
                    invoke(sender,method,args);
                    executed=true;
                }
            if(!executed)sender.sendMessage("指令不存在");
            return true;
        }
        return true;
    }

    public boolean canInvoke(Method method,String[] args)
    {
        try {

            CommandArgs annotation = method.isAnnotationPresent(CommandArgs.class)?method.getAnnotation(CommandArgs.class):null;
            if(annotation==null)return false;

            ArgType[] anTypes = annotation.types();
            String[]  anArgs  = annotation.args();

            if(args.length!=anTypes.length)return false;
            
            for(int i = 0;i<args.length;i++)
            {
                ArgType type  = anTypes[i];
                String  arg   = anArgs[i];
                String  input = args[i];
                
                if(type==ArgType.DEPEND && !arg.equals(input))return false;
            }
            
            return true;
        }catch (Exception e){e.printStackTrace();return false;}
    }

    public Object invoke(CommandSender sender , Method method , String[] args)
    {
        try {
            CommandArgs annotation = method.isAnnotationPresent(CommandArgs.class)?method.getAnnotation(CommandArgs.class):null;
            if(annotation==null)return null;

            if(annotation.playerOnly() && !(sender instanceof Player)){sender.sendMessage("只有玩家才可以执行该指令");return null;}

            ArgType[] anTypes = annotation.types();
            int argLength = 0;
            for(ArgType t : anTypes) if(!(t==ArgType.DEPEND))argLength++;

            Object[] objects = new Object[argLength+1];
            objects[0]=sender;

            int argIndex = 0;
            for (int i = 0;i<anTypes.length;i++) {
                ArgType type = anTypes[i];
                if (type == ArgType.DEPEND) continue;

                Object formart = type.formart(args[i]);
                if(formart==null){sender.sendMessage("指令第"+i+"参数出错 > \""+args[argIndex]+"\" 需求 "+type.name);return null;}

                objects[argIndex + 1] = formart;
                argIndex++;
            }

            return method.invoke(this,objects);
        }catch (Exception e){e.printStackTrace();return null;}
    }



    @CommandArgs(
            describe = "查看全部指令",
            args     = {"help"} ,
            types    = {ArgType.DEPEND}
    )
    public void listCommand(CommandSender sender)
    {
        sender.sendMessage("[全部指令]");
        for(Method method : methods)
        {
            CommandArgs annotation = method.isAnnotationPresent(CommandArgs.class)?method.getAnnotation(CommandArgs.class):null;
            if(annotation==null)continue;

            String[] anArgs = annotation.args();
            ArgType[] anTypes = annotation.types();

            StringBuilder builder = new StringBuilder();
            builder.append("/").append("audio").append(" ");
            for(int i = 0;i<anArgs.length;i++)
                if(anTypes[i]==ArgType.DEPEND)  builder.append(anArgs[i]).append("  ");
                else                            builder.append("[").append(anArgs[i]).append("]  ");


            builder.append(annotation.describe());

            sender.sendMessage(builder.toString());
        }
    }

}

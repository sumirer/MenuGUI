package sole.memory.guishop.shop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindow;
import sole.memory.guishop.windows.Custom;
import sole.memory.guishop.windows.Simple;
import sole.memory.guishop.windows.button.ButtonInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SoleMemory
 * on 2017/11/3.
 */
public class CommandSend {

    public static void getAllCommandInfoPage(Player player){
        Map<String,Command> command = Server.getInstance().getCommandMap().getCommands();
        HashMap<Integer,Command> map = new HashMap<>();
        //pick up command
        command.forEach(((plugin,cmd)->{
            if (player.hasPermission(cmd.getPermission())){
                map.put(map.size(),cmd);
            }
        }));
        HashMap<Integer,ButtonInfo> commandButton = new HashMap<>();
        map.forEach((index,cmd)->{
            ButtonInfo info = new ButtonInfo();
            info.text = cmd.getName();
            info.haveImg = false;
            commandButton.put(index,info);
        });
        Simple simple = new Simple();
        simple.text = "点击命令界面可打开命令选项";
        simple.inputData(commandButton);
        simple.changeDataToGUI();
        player.showFormWindow(simple.getGUI());
    }

    public static FormWindow getCommandInputPage(Command command){
        StringBuilder m = new StringBuilder();
        CommandParameter[] parameters = command.getCommandParameters().get("default");
        for (CommandParameter parameter : parameters) {
            m.append("<").append(parameter.name).append(":").append(parameter.type).append("> ");
        }
        ElementInput input = new ElementInput("请输入命令",new String(m)," ");
        ElementLabel label = new ElementLabel(Server.getInstance().getLanguage().translateString(command.getDescription())+"\n\n\n");
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(map.size(),input);
        map.put(map.size(),label);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.title = "请输入命令";
        custom.changeDataToGUI();
        return custom.getGUI();
    }
}

package sole.memory.guishop.menu;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;
import money.Money;
import sole.memory.guishop.utils.StringUtils;
import sole.memory.guishop.windows.Custom;
import sole.memory.guishop.windows.Simple;
import sole.memory.guishop.windows.button.ButtonInfo;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/11/3.
 */
public class AdminChangePlayerInfo {

    public static void showAllOnlinePlayerList(Player player){
        HashMap<Integer,ButtonInfo> map = new HashMap<>();
        for (Player player1: Server.getInstance().getOnlinePlayers().values()) {
            ButtonInfo info = new ButtonInfo();
            info.text = player1.getName();
            info.haveImg = false;
            map.put(map.size(),info);
        }
        Simple simple = new Simple();
        simple.text = "玩家管理系统";
        simple.info = "点击管理在线玩家";
        simple.inputData(map);
        simple.changeDataToGUI();
        player.showFormWindow(simple.getGUI());
    }

    // gameMode money  effect health handItem Inventory(item count change) ban kick teleport kill showInfo
    public static FormWindow getPlayerInfoPage(Player player){
        ElementLabel info = new ElementLabel(StringUtils.getPlayerInfo(player));
        ElementToggle isOP = new ElementToggle("管理员身份",player.isOp());
        ElementSlider health = new ElementSlider("生命",1,player.getMaxHealth(),1,player.getHealth());
        ElementStepSlider gameMode = new ElementStepSlider("模式");
        gameMode.addStep("生存",player.getGamemode()==0);
        gameMode.addStep("创造",player.getGamemode()==1);
        gameMode.addStep("冒险",player.getGamemode()==2);
        gameMode.addStep("观察",player.getGamemode()==3);
        ElementInput money = new ElementInput("金币","<留空表示不更改>", Money.getInstance().getMoney(player)+"");
        ElementDropdown effect = new ElementDropdown("药水效果");
        effect.addOption("还未完工");
        ElementToggle kill = new ElementToggle("杀死玩家",false);
        ElementToggle ban = new ElementToggle("禁止加入",false);
        ElementToggle kick = new ElementToggle("踢出服务器",false);
        ElementToggle teapot = new ElementToggle("传送到身边",false);
        ElementToggle inventory = new ElementToggle("背包管理(未完工)",false);
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(map.size(),info);
        map.put(map.size(),isOP);
        map.put(map.size(),gameMode);
        map.put(map.size(),health);
        map.put(map.size(),effect);
        map.put(map.size(),inventory);
        map.put(map.size(),teapot);
        map.put(map.size(),kill);
        map.put(map.size(),kick);
        map.put(map.size(),ban);
        map.put(map.size(),money);
        Custom custom = new Custom();
        custom.title = player.getName()+" 的信息";
        custom.inputData(map);
        custom.changeDataToGUI();
        return custom.getGUI();
    }

    public static FormWindowModal getPlayerNotOnlinePage(String player){
        return new FormWindowModal("玩家不在线","玩家："+player+" 已经不在线了！！！！","返回主页","退出");
    }

    public static FormWindowModal getBanPlayerPage(String player){
        return new FormWindowModal("禁止玩家加入","玩家："+player+" 已经被加入禁止列表！！！！","返回主页","退出");
    }
}

package sole.memory.menugui.menu;

import cn.nukkit.Player;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.utils.TextFormat;
import sole.memory.menugui.menu.data.LevelData;
import sole.memory.menugui.windows.Custom;
import sole.memory.menugui.windows.Simple;
import sole.memory.menugui.windows.button.ButtonInfo;

import java.util.HashMap;
import java.util.Map;



public class ManagerLevelSend {

    public static void sendAllLevel(Player player){
        HashMap<Integer,Object> list = new HashMap<>();
        for (String level: LevelData.levelList.keySet()) {
            ButtonInfo buttonInfo = new ButtonInfo();
            buttonInfo.text = level;
            list.put(list.size(),buttonInfo);
        }
        Simple simple = new Simple();
        simple.text = TextFormat.BOLD+"LevelManager";
        simple.info = "点击管理地图";
        simple.inputData(list);
        simple.changeDataToGUI();
        player.showFormWindow(simple.getGUI());
    }

    public static void reloadLevel(Player player,HashMap<String,Boolean> listt){
        StringBuilder re = new StringBuilder("重载结果如下:\n");
        for (Map.Entry<String,Boolean> l:listt.entrySet()) {
                re.append(l.getKey()).append("  加载成功\n");

        }
        player.showFormWindow(new FormWindowModal("加载结果",re.toString(),"返回主页","退出"));
    }

    public static void sendLevelInfo(Player player,String level){

        LevelData levelData = LevelData.levelList.get(level);
        ElementInput levelName = new ElementInput("地图名字(请勿修改):","请勿修改" ,level);
        ElementToggle isLoad = new ElementToggle("是否加载(慎重操作此项):",levelData.isLoad);
        ElementToggle canBreak = new ElementToggle("允许破坏方块",levelData.isCanBreak());
        ElementToggle canPlace = new ElementToggle("允许放置方块",levelData.isCanPlace());
        ElementToggle canUpdate = new ElementToggle("允许方块更新(流水、植物生长)",levelData.isCanUpdate());
        ElementToggle canPVP = new ElementToggle("允许PVP",levelData.isCanPVP());
        ElementToggle canPVE = new ElementToggle("允许PVE",levelData.isCanPVE());
        ElementToggle canChangeMode = new ElementToggle("允许改变模式",levelData.isCanChangeMode());
        ElementToggle canFly = new ElementToggle("允许飞行",levelData.isCanFly());
        ElementToggle canPhysicalDamage = new ElementToggle("坠落伤害",levelData.isCanPhysicalDamage());
        ElementToggle notAdminCanTP = new ElementToggle("非OP允许加入",levelData.notAdminCanTP);
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(map.size(),levelName);
        map.put(map.size(),isLoad);
        map.put(map.size(),canBreak);
        map.put(map.size(),canPlace);
        map.put(map.size(),canUpdate);
        map.put(map.size(),canPVP);
        map.put(map.size(),canPVE);
        map.put(map.size(),canChangeMode);
        map.put(map.size(),canFly);
        map.put(map.size(),canPhysicalDamage);
        map.put(map.size(),notAdminCanTP);
        Custom custom = new Custom();
        custom.title = level+" 信息";
        custom.inputData(map);
        custom.changeDataToGUI();
        player.showFormWindow(custom.getGUI());
    }

    public static void getSuccessPage(Player player) {
        player.showFormWindow( new FormWindowModal("修改成功", "成功修改地图信息，请关闭此界面","返回主页","退出"));
    }

    public static void getFliedPage(Player player) {
        player.showFormWindow( new FormWindowModal("修改失败", "请检查是否改动地图名字","返回主页","退出"));
    }

    public static void getLoadPage(Player player) {
        player.showFormWindow( new FormWindowModal("加载失败", "加载或卸载地图失败，可能是地图数量不足以传送此地图的人，或者未被保护的世界太少","返回主页","退出"));
    }
}

package sole.memory.menugui.menu;

import cn.nukkit.Player;
import sole.memory.menugui.menu.data.LevelData;
import sole.memory.menugui.windows.Simple;
import sole.memory.menugui.windows.button.ButtonInfo;

import java.util.HashMap;

public class PlayerTeleportSend {

    public static void sendAllCanTPLevel(Player player){
        Simple simple = new Simple();
        simple.text = "请选择要前往的世界";
        simple.info = "不可加入的世界默认不显示";
        HashMap<Integer,Object> map = new HashMap<>();
        for (LevelData lv:LevelData.levelList.values()) {
            if (lv.isLoad()){
                if (lv.isNotAdminCanTP()){
                    ButtonInfo buttonInfo = new ButtonInfo();
                    buttonInfo.haveImg = false;
                    buttonInfo.text = lv.levelName;
                    map.put(map.size(),buttonInfo);
                }else if (player.isOp()){
                    ButtonInfo buttonInfo = new ButtonInfo();
                    buttonInfo.haveImg = false;
                    buttonInfo.text = lv.levelName;
                    map.put(map.size(),buttonInfo);
                }
            }
        }
        simple.inputData(map);
        simple.changeDataToGUI();
        player.showFormWindow(simple.getGUI());
    }
}

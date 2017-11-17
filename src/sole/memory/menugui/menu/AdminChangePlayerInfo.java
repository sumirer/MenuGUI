package sole.memory.menugui.menu;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.item.Item;
import money.Money;
import sole.memory.menugui.lang.Lang;
import sole.memory.menugui.utils.StringUtils;
import sole.memory.menugui.windows.Custom;
import sole.memory.menugui.windows.Simple;
import sole.memory.menugui.windows.button.ButtonInfo;

import java.util.HashMap;
import java.util.Map;

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
        simple.text = Lang.translate("admin-change-1");
        simple.info = Lang.translate("admin-change-2");
        simple.inputData(map);
        simple.changeDataToGUI();
        player.showFormWindow(simple.getGUI());
    }

    // gameMode money  effect health handItem Inventory(item count change) ban kick teleport kill showInfo
    public static FormWindow getPlayerInfoPage(Player player){
        ElementLabel info = new ElementLabel(StringUtils.getPlayerInfo(player));
        ElementToggle isOP = new ElementToggle(Lang.translate("admin-change-admin"),player.isOp());
        ElementSlider health = new ElementSlider(Lang.translate("admin-change-health"),1,player.getMaxHealth(),1,player.getHealth());
        ElementStepSlider gameMode = new ElementStepSlider(Lang.translate("admin-change-mode"));
        gameMode.addStep(Lang.translate("admin-change-mode-0"),player.getGamemode()==0);
        gameMode.addStep(Lang.translate("admin-change-mode-1"),player.getGamemode()==1);
        gameMode.addStep(Lang.translate("admin-change-mode-2"),player.getGamemode()==2);
        gameMode.addStep(Lang.translate("admin-change-mode-3"),player.getGamemode()==3);
        ElementInput money = new ElementInput(Lang.translate("admin-change-money"),Lang.translate("admin-change-money-msg"), Money.getInstance().getMoney(player)+"");
        ElementDropdown effect = new ElementDropdown(Lang.translate("admin-change-effect"));
        effect.addOption("还未完工");
        ElementToggle kill = new ElementToggle(Lang.translate("admin-change-kill"),false);
        ElementToggle ban = new ElementToggle(Lang.translate("admin-change-ban"),false);
        ElementToggle kick = new ElementToggle(Lang.translate("admin-change-kick"),false);
        ElementToggle teleport = new ElementToggle(Lang.translate("admin-change-teleport"),false);
        ElementToggle inventory = new ElementToggle(Lang.translate("admin-change-inventory"),false);
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(map.size(),info);
        map.put(map.size(),isOP);
        map.put(map.size(),gameMode);
        map.put(map.size(),health);
        map.put(map.size(),effect);
        map.put(map.size(),inventory);
        map.put(map.size(),teleport);
        map.put(map.size(),kill);
        map.put(map.size(),kick);
        map.put(map.size(),ban);
        map.put(map.size(),money);
        Custom custom = new Custom();
        custom.title = Lang.translate("admin-change-player-info",player.getName());
        custom.inputData(map);
        custom.changeDataToGUI();
        return custom.getGUI();
    }

    public static FormWindow getPlayerInventoryInfoPage(Player player){
        HashMap<Integer,ButtonInfo> infoHashMap = new HashMap<>();
        Map<Integer,Item> itemMap = player.getInventory().getContents();
        if (itemMap.size()<1){
            return new FormWindowModal(Lang.translate("admin-change-inventory-null"),Lang.translate("admin-change-inventory-null-msg"),Lang.translate("gui-button-back-main-page"),Lang.translate("gui-button-quite"));
        }
        for (Map.Entry<Integer,Item> itemEntry:itemMap.entrySet()) {
            ButtonInfo info = new ButtonInfo();
            info.haveImg = false;
            info.text =StringUtils.getItemInfo(itemEntry.getValue(),itemEntry.getKey());
            infoHashMap.put(infoHashMap.size(),info);
        }
        Simple simple = new Simple();
        simple.text = Lang.translate("admin-change-inventory-info",player.getName());
        simple.info = Lang.translate("admin-change-kick-button-edit");
        simple.inputData(infoHashMap);
        simple.changeDataToGUI();
        return simple.getGUI();
    }

    public static FormWindow getItemEditPage(String player,float count){
        ElementSlider slider = new ElementSlider(Lang.translate("admin-change-set-item-count"),0,64,1,count);
        ElementLabel label = new ElementLabel(Lang.translate("admin-change-set-item-count-msg"));
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(0,slider);
        map.put(1,label);
        Custom custom = new Custom();
        custom.title = Lang.translate("admin-change-player-inventory",player);
        custom.inputData(map);
        custom.changeDataToGUI();
        return custom.getGUI();
    }

    public static FormWindow getEditBackPage(String player,float count){
        return new FormWindowModal(Lang.translate("admin-change-player-inventory",player),Lang.translate("admin-change-item-count-change",count+""),Lang.translate("gui-button-back-inventory"),Lang.translate("gui-button-back-main-page"));
    }

    public static FormWindow getUnKnowErrorPage(String player){
        return new FormWindowModal(Lang.translate("admin-change-player-inventory",player),Lang.translate("admin-change-unknown-error"),Lang.translate("gui-button-back-inventory"),Lang.translate("gui-button-back-main-page"));
    }

    public static FormWindowModal getPlayerNotOnlinePage(String player){
        return new FormWindowModal(Lang.translate("admin-change-player-not-online"),Lang.translate("admin-change-player-not-online-msg",player),Lang.translate("gui-button-back-main-page"),Lang.translate("gui-button-quite"));
    }

    public static FormWindowModal getBanPlayerPage(String player){
        return new FormWindowModal(Lang.translate("admin-change-ban"),Lang.translate("admin-change-ban-player-msg",player),Lang.translate("gui-button-back-main-page"),Lang.translate("gui-button-quite"));
    }

    public static FormWindowModal getCountErrorPage(String price){
        return new FormWindowModal(Lang.translate("admin-change-data-error"),Lang.translate("admin-change-data-error-msg",price),Lang.translate("gui-button-re-edit"),Lang.translate("gui-button-back-main-page"));
    }

}

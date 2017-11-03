package sole.memory.guishop.menu;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import sole.memory.guishop.database.ConfigDataBase;
import sole.memory.guishop.menu.data.SellData;
import sole.memory.guishop.utils.StringUtils;
import sole.memory.guishop.windows.Custom;

import java.util.HashMap;

/**
 * Created by SoleMemory
 * on 2017/11/2.
 */
public class PlayerSellShop {

    public static void getSellPage(Player player){

        ElementLabel label = new ElementLabel(TextFormat.AQUA+"请选择要出售的物品");
        ElementDropdown stepSlider = new ElementDropdown("可出售物品");
        HashMap<Integer,SellData> maps = ConfigDataBase.getPlayerHaveItemList(player);
        for (SellData date: maps.values()) {
            stepSlider.addOption(StringUtils.getSellInfoStepText(date));
        }
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(0,label);
        map.put(1,stepSlider);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        custom.title = "出售商店";
        if (maps.size()<1){
            player.showFormWindow(PlayerBuyShop.getNullPage());
            return;
        }
        player.showFormWindow(custom.getGUI());
    }


    public static FormWindow getSellShopChoseCountPage(int count) {
        HashMap<Integer, Object> map = new HashMap<>();
        ElementSlider stepSlider = new ElementSlider("请选择出售数量", 1, count, 1, 1);
        ElementLabel label1 = new ElementLabel(TextFormat.GREEN + "选择好数量后请点击提交按钮");
        map.put(0, stepSlider);
        map.put(1, label1);
        Custom custom = new Custom();
        custom.title = "选择页面";
        custom.inputData(map);
        custom.changeDataToGUI();
        return custom.getGUI();
    }

    public static FormWindowModal getSellCheckPage(SellData data, int chooseCount){
        return new FormWindowModal("请确认信息",StringUtils.getSellShopButtonInfo(data)+"\n本次交易将获得: "+data.price*chooseCount +"金币","确认","取消");
    }

    public static FormWindowModal getSellCancelPage(){
        return new FormWindowModal("取消交易","你已经取消了此次交易，请关闭此页面","返回主页","退出");
    }

    public static FormWindowModal getSellSuccessPage(float price){
        return new FormWindowModal("交易成功","此次交易成功\n获得金币:"+price,"返回主页","退出");
    }


    public static int getMaxCount(Player player,String id){

        Integer[] lj = StringUtils.getItemInfo(id);
        int count = 0;
        for (Item item:player.getInventory().getContents().values()) {
            if (item.getId()==lj[0] && lj[1] == item.getDamage()){
                count+=item.getCount();
            }
        }
        return count;
    }

}

package sole.memory.menugui.menu;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import money.Money;
import sole.memory.menugui.database.ConfigDataBase;
import sole.memory.menugui.menu.data.ShopData;
import sole.memory.menugui.utils.StringUtils;
import sole.memory.menugui.windows.Custom;
import sole.memory.menugui.windows.Simple;
import sole.memory.menugui.windows.button.ButtonInfo;

import java.util.HashMap;

/**
 * @author SoleMemory
 *  on 2017/10/31.
 */
public class PlayerBuyShop {

    public static FormWindow getAllShopPage(){
        ElementLabel label = new ElementLabel(TextFormat.AQUA+"请选择要购买的物品");
        ElementDropdown stepSlider = new ElementDropdown("可购买物品");
        for (ShopData date: ConfigDataBase.data.values()) {
            stepSlider.addOption(StringUtils.getShopButtonInfo(date));
        }
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(0,label);
        map.put(1,stepSlider);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        custom.title = "出售商店";
        if (ConfigDataBase.data.size()<1){
            return PlayerBuyShop.getNullPage();
        }
        return custom.getGUI();
    }


    public static FormWindowSimple getMainPage(){
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = "打开出售商店";
        ButtonInfo buttonInfo1 = new ButtonInfo();
        buttonInfo1.text = "打开回收商店";
        ButtonInfo buttonInfo2 = new ButtonInfo();
        buttonInfo2.text = "打开执行命令";
        HashMap<Integer,ButtonInfo> map = new HashMap<>();
        map.put(0,buttonInfo);
        map.put(1,buttonInfo1);
        map.put(2,buttonInfo2);
        Simple simple = new Simple();
        simple.inputData(map);
        simple.changeDataToGUI();
        return simple.getGUI();
    }

    public static FormWindow getBuyPage(ShopData data){
        HashMap<Integer,Object> map = new HashMap<>();
        ElementLabel label = new ElementLabel(StringUtils.getShopButtonInfo(data));
        ElementSlider stepSlider = new ElementSlider("请选择购买数量",1,100,1,16);
        ElementLabel label1 = new ElementLabel(TextFormat.GREEN+"选择好数量后请点击提交按钮");
        map.put(0,label);
        map.put(1,stepSlider);
        map.put(2,label1);
        Custom custom = new Custom();
        custom.title = "购买页面";
        custom.inputData(map);
        custom.changeDataToGUI();
        return custom.getGUI();
    }

    public static FormWindowModal getBuyCheckPage(ShopData data, int count){
        return new FormWindowModal("购买确认",StringUtils.getShopButtonInfo(data)+"\n\n"+"本次需要花费金币: "+count*data.price+"\n购买数量: "+count,"确认","取消");
    }

    public static FormWindowModal getNoMoneyPage(ShopData data,int count){
        return new FormWindowModal("金币不足","你的金币不足以购买 "+count+" 个 "+data.name,"返回主页","退出");
    }

    public static FormWindowModal getFoolPage(ShopData data,int count){
        return new FormWindowModal("空间不足","你的空间不足以存放 "+count+" 个 "+data.name,"返回主页","退出");
    }

    public static FormWindowModal getBuySuccessPage(ShopData data,int count){
        return new FormWindowModal("购买成功","你成功购买 "+count+" 个 "+data.name+"\n"+"花费金币: "+data.price*count,"返回主页","退出");
    }

    public static boolean playerHaveMoney(Player player,float price){
        return Money.getInstance().getMoney(player)>price;
    }


    public static FormWindowModal getNullPage(){
        return new FormWindowModal("无商品","商店空空如也.. ","返回主页","退出");
    }

}

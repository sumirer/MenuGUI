package sole.memory.guishop.shop;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import sole.memory.guishop.database.ConfigDataBase;
import sole.memory.guishop.shop.data.SellData;
import sole.memory.guishop.shop.data.ShopData;
import sole.memory.guishop.shop.item.ItemName;
import sole.memory.guishop.utils.StringUtils;
import sole.memory.guishop.windows.Custom;
import sole.memory.guishop.windows.Simple;
import sole.memory.guishop.windows.button.ButtonInfo;

import java.util.HashMap;

/**
 * @author SoleMeory
 * on 2017/10/31.
 */
public class AdminSetShop {

    //TODO: del model
    public String model = "add";

    //shop and sell
    public String type = "shop";

    public float price = 0;

    public String name = "";

    public String id = "";

    public SellData sellData = null;

    public ShopData shopData = null;


    public static FormWindowSimple getMainPage() {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = "设置出售商店";
        ButtonInfo buttonInfo1 = new ButtonInfo();
        buttonInfo1.text = "设置回收商店";
        HashMap<Integer, ButtonInfo> map = new HashMap<>();
        map.put(0, buttonInfo);
        map.put(1, buttonInfo1);
        Simple simple = new Simple();
        simple.inputData(map);
        simple.changeDataToGUI();
        return simple.getGUI();
    }


    public boolean isCheckData() {
        return price != 0 && !name.equals("") && model.equals("add");
    }

    public boolean checkDelModel() {
        return price != 0 && !name.equals("") && model.equals("del");
    }

    public static FormWindowCustom getSearchPage() {
        FormWindowCustom custom = new FormWindowCustom("请输入要添加物品的ID");
        ElementLabel label = new ElementLabel(TextFormat.GOLD + "请输入要添加物品的ID\n 格式为(ID:特殊值(无特殊值不需要:))");
        custom.addElement(label);
        ElementInput input = new ElementInput("请输入ID", TextFormat.DARK_GRAY + "(ID:特殊值(无特殊值不需要:)");
        custom.addElement(input);
        return custom;
    }

    public static FormWindowModal getSearchNullPage(String input) {
        return new FormWindowModal("未搜索到结果", "未搜索到ID为: " + input + " 的物品，请重新输入", "重新输入", "取消添加");
    }

    public FormWindowCustom getInputPricePage() {
        FormWindowCustom custom = new FormWindowCustom("请输入要添加物品的价格");
        ElementLabel label = new ElementLabel(TextFormat.AQUA + "你正在添加物品： " + name + "\n" + TextFormat.GOLD + "请输入要添加物品的ID\n 格式为(price:int)");
        custom.addElement(label);
        ElementInput input = new ElementInput("请输入价格", TextFormat.DARK_GRAY + "(价格:数字)");
        custom.addElement(input);
        return custom;
    }

    public static FormWindowSimple getTypeChoosePage() {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = "添加物品";
        ButtonInfo buttonInfo1 = new ButtonInfo();
        buttonInfo1.text = "删除物品";
        HashMap<Integer, ButtonInfo> map = new HashMap<>();
        map.put(0, buttonInfo);
        map.put(1, buttonInfo1);
        Simple simple = new Simple();
        simple.inputData(map);
        simple.changeDataToGUI();
        return simple.getGUI();
    }


    public FormWindowModal getCheckPage() {
        String info = TextFormat.GOLD + "物品名字: " + name + "\n" + TextFormat.AQUA + "单价: " + price + "\n\n\n" + TextFormat.GREEN + "请确认信息...";
        return new FormWindowModal("请确认信息", info, "确认", "取消");
    }

    public FormWindowModal getSuccessPage() {
        return new FormWindowModal("出售添加成功", "成功添加物品到出售商店，请关闭此界面","退出","退出");
    }

    public FormWindowModal getSuccessSellPage() {
        return new FormWindowModal("回收添加成功", "成功添加物品到回收商店，请关闭此界面","退出","退出");
    }

    public FormWindowModal getFailedPage() {
        return new FormWindowModal("添加失败", TextFormat.RED + "添加物品到商店失败，请关闭此界面，并检查输入数据是否正确","退出","退出");
    }


    //sell
    public static void getSellShopAllPage(Player player){

        ElementLabel label = new ElementLabel(TextFormat.AQUA+"请选择要编辑的物品");
        ElementDropdown stepSlider = new ElementDropdown("可编辑出售物品");
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
        custom.title = "编辑出售商店";
        if (maps.size()<1){
            player.showFormWindow(PlayerBuyShop.getNullPage());
            return;
        }
        player.showFormWindow(custom.getGUI());
    }

    public static void getAllShopPage(Player player){
        ElementLabel label = new ElementLabel(TextFormat.AQUA+"请选择要编辑的物品");
        ElementDropdown stepSlider = new ElementDropdown("可编辑物品");
        for (ShopData date: ConfigDataBase.data.values()) {
            stepSlider.addOption(StringUtils.getShopButtonInfo(date));
        }
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(0,label);
        map.put(1,stepSlider);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        custom.title = "编辑商店";
        if (ConfigDataBase.data.size()<1){
            player.showFormWindow(PlayerBuyShop.getNullPage());
            return;
        }
        player.showFormWindow(custom.getGUI());
    }

    private static FormWindow getEditShopPage(float price, String name,String id){
        ElementToggle toggle = new ElementToggle("删除(无视修改)",false);
        ElementInput input = new ElementInput("价格",TextFormat.DARK_GRAY+""+price,price+"");
        ElementInput input1 = new ElementInput("名字",TextFormat.DARK_GRAY+name,name);
        ElementInput input2 = new ElementInput("ID",TextFormat.DARK_GRAY+id,id);
        HashMap<Integer,Object> map = new HashMap<>();
        map.put(0,input);
        map.put(1,input1);
        map.put(2,input2);
        map.put(3,toggle);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        return custom.getGUI();
    }

    public static FormWindow getEditSellShopPage(SellData data){
        return getEditShopPage(data.price,data.name,data.id);
    }

    public static FormWindow getEditShopPage(ShopData data){
        return getEditShopPage(data.price,data.name,data.id);
    }

    public static boolean checkInputID(String id){
        return !ItemName.getName(id).equals("不存在此物品");
    }

    public static FormWindowModal getIDErrorPage(String id){
        return new FormWindowModal("ID错误","未找到ID为: "+id+" 的物品","重新编辑","取消编辑");
    }

    public static FormWindowModal getPriceErrorPage(String price){
        return new FormWindowModal("数据错误","输入数据: "+price+" 不正确","重新编辑","取消编辑");
    }

    public static FormWindowModal getEditSuccessPage(){
        return new FormWindowModal("修改成功","物品信息更新成功","退出","退出");
    }

    public static FormWindowModal getEditFailedPage(){
        return new FormWindowModal("修改失败","物品信息更新失败","退出","退出");
    }

    public static FormWindowModal getDeletePage(String type,String index){
        return new FormWindowModal("成功删除","成功删除类型为: "+type+" 的物品,商店代号: "+index,"退出","退出");
    }
}

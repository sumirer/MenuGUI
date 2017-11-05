package sole.memory.menugui.database;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import sole.memory.menugui.MenuGUI;
import sole.memory.menugui.menu.AdminSetShop;
import sole.memory.menugui.menu.data.SellData;
import sole.memory.menugui.menu.data.ShopData;
import sole.memory.menugui.utils.StringUtils;
import sole.memory.menugui.windows.button.ButtonInfo;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SoleMemory
 * on 2017/10/31.
 */
public class ConfigDataBase {
    private static String path = MenuGUI.getInstance().getDataFolder()+"/menu.yml";

    private static String sellPath = MenuGUI.getInstance().getDataFolder()+"/sell.yml";

    public static HashMap<String,ShopData> data = new HashMap<>();

    public static HashMap<String,SellData> sellData = new HashMap<>();



    @SuppressWarnings("unchecked")
    public static void initData(){
        if (!new File(ConfigDataBase.path).exists()){
            Config config = new Config(path,Config.YAML);
            config.save();
        }
        if (!new File(ConfigDataBase.sellPath).exists()){
            Config config = new Config(sellPath,Config.YAML);
            config.save();
        }
        Map<String,Object> map= new Config(path,Config.YAML).getAll();

        Map<String,Object> sellmap= new Config(sellPath,Config.YAML).getAll();

        map.forEach((index,info)->{
           HashMap map1 =  (HashMap<String, Object>) info;
           ShopData data1 = new ShopData();
           data1.index = index;
           data1.id = map1.get("id").toString();
           data1.name = map1.get("name").toString();
           data1.price = Float.valueOf(map1.get("price").toString());
           ConfigDataBase.data.put(index,data1);
        });

        sellmap.forEach((index,info)->{
            HashMap map1 =  (HashMap<String, Object>) info;
            SellData data1 = new SellData();
            data1.index = index;
            data1.id = map1.get("id").toString();
            data1.name = map1.get("name").toString();
            data1.price = Float.valueOf(map1.get("price").toString());
            ConfigDataBase.sellData.put(index,data1);
        });
        MenuGUI.getInstance().getLogger().info("imgPath is load...");
    }

    public static void deleteData(String index){
        if (data.containsKey(index)){
            data.remove(index);
            save();
        }
    }

    public static void deleteSellData(String index){
        if (sellData.containsKey(index)){
            sellData.remove(index);
            saveSell();
        }
    }

    public static void updateShopData(String index,ShopData data){
        ConfigDataBase.data.put(index,data);
        save();
    }

    public static void updateSellData(String index,SellData data){
        ConfigDataBase.sellData.put(index,data);
        saveSell();
    }

    public static void addNewData(AdminSetShop adminSetShop){
        ShopData data1 = new ShopData();
        data1.index = data.size()+"";
        data1.name = adminSetShop.name;
        data1.id = adminSetShop.id;
        data1.price = adminSetShop.price;
        ConfigDataBase.data.put(data1.index,data1);
        save();
    }


    public static void addNewSellData(AdminSetShop adminSetShop){
        SellData data1 = new SellData();
        data1.index = data.size()+"";
        data1.name = adminSetShop.name;
        data1.id = adminSetShop.id;
        data1.price = adminSetShop.price;
        ConfigDataBase.sellData.put(data1.index,data1);
        saveSell();
    }



    //if player inventory have sell info item ,pick this to map
    public static HashMap<Integer,SellData>  getPlayerHaveItemList(Player player){
        HashMap<Integer,SellData> pick = new HashMap<>();
        sellData.forEach((index,info)->{
            Integer[] lk = StringUtils.getItemInfo(info.id);
            int count = 0;
            for (Item item:player.getInventory().getContents().values()) {
                if (item.getId()==lk[0] && lk[1] == item.getDamage()){
                    count+=item.getCount();
                }
            }
            if (count>0){
                pick.put(pick.size(),info);
            }
        });
        return pick;
    }


    @SuppressWarnings("unchecked")
    private static void save(){
        HashMap<String,HashMap<String,Object>> map = new HashMap<>();
        data.forEach((index,info)->{
            map.put(index,info.toMap());
        });

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.putAll(map);
        Config config = new Config(path,Config.YAML);
        config.setAll(linkedHashMap);
        config.save();
    }

    @SuppressWarnings("unchecked")
    private static void saveSell(){
        HashMap<String,HashMap<String,Object>> map = new HashMap<>();
        sellData.forEach((index,info)->{
            map.put(index,info.toMap());
        });

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.putAll(map);
        Config config = new Config(sellPath,Config.YAML);
        config.setAll(linkedHashMap);
        config.save();
    }

    public static HashMap<Integer,ButtonInfo> getShopChooseButton(){
        HashMap<Integer,ButtonInfo> map = new HashMap<>();
        data.forEach((index,info)->{
            ButtonInfo buttonInfo = new ButtonInfo();
            buttonInfo.haveImg = false;
            buttonInfo.text = StringUtils.getShopButtonInfo(info);
            map.put(map.size(),buttonInfo);
        });
        return map;
    }

    public static ShopData getShopDataByIndex(String index){
        if (data.containsKey(index)) return data.get(index);
        return new ShopData();
    }

    public static SellData getSellDataByIndex(String index){
        if (sellData.containsKey(index)) return sellData.get(index);
        return new SellData();
    }

    public static boolean isDataIndex(String string){
        return data.containsKey(string);
    }
}

package sole.memory.menugui.menu.data;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import sole.memory.menugui.MenuGUI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class LevelData {

    public static HashMap<String,LevelData> levelList = new HashMap<>();
    public static ArrayList<String> levelNameArray = new ArrayList<>();
    public static Config config = null;
    public static void init(){
        reloadLevelList();
        loadConfig();
        checkNewLevel();
        for (Object info:config.getAll().values()) {
            LinkedHashMap ld = (LinkedHashMap) info;
            LevelData data = initLevelData(ld);
            if (data == null) continue;
            levelList.put(data.levelName,data);
        }
    }

    private static LevelData initLevelData(LinkedHashMap<String, Object> ld){
        LevelData data = new LevelData();
        data.levelName = ld.get("levelName").toString();
        data.isLoad = (boolean) ld.get("isLoad");
        data.canBreak = (boolean) ld.get("canBreak");
        data.canPlace = (boolean) ld.get("canPlace");
        data.canUpdate = (boolean) ld.get("canUpdate");
        data.canPVP = (boolean)ld.get("canPVP");
        data.canPVE = (boolean)ld.get("canPVE");
        data.canChangeMode = (boolean) ld.get("canChangeMode");
        data.canFly = (boolean) ld.get("canFly");
        data.canPhysicalDamage = (boolean) ld.get("canPhysicalDamage");
        data.notAdminCanTP = (boolean) ld.get("notAdminCanTP");
        if (data.isLoad){
            if (Server.getInstance().loadLevel(data.levelName)){
                Server.getInstance().getLogger().info("Level: "+ TextFormat.AQUA +data.levelName+TextFormat.RESET+" isLoad...");
            }else {
                //地图文件不存在，直接加载新地图
                Server.getInstance().generateLevel(data.levelName);
                Server.getInstance().getLogger().info("Level: "+ TextFormat.AQUA +data.levelName+TextFormat.RESET+" Not exist, generate a New Map...");
            }
            return data;
        }
        return null;
    }

    public static HashMap<String, Boolean> loadNewMap(){
        HashMap<String, Boolean> count = new HashMap<>();
        File file = new File(Server.getInstance().getDataPath()+"/worlds");
        File[] files = file.listFiles();
        if (files!=null &&files.length>1) {
            for (File lv : files) {
                if (lv.exists() && lv.isDirectory()){
                    reloadLevelList();
                    String name = lv.getName();
                    if (!config.exists(name)){
                        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
                        data.put("levelName",name);
                        data.put("isLoad",true);
                        data.put("canBreak",true);
                        data.put("canPlace",true);
                        data.put("canUpdate",true);
                        data.put("canPVP",true);
                        data.put("canPVE",true);
                        data.put("canChangeMode",true);
                        data.put("canFly",false);
                        data.put("canPhysicalDamage",true);
                        data.put("notAdminCanTP",true);
                        config.set(name,data);
                        config.save();
                        LevelData datas = initLevelData(data);
                        datas.setLoad(true);
                            levelList.put(datas.levelName, datas);
                            count.put(name,true);
                    }
                }
            }
        }
        return count;
    }

    public static void reloadLevelList(){
        File file = new File(Server.getInstance().getDataPath()+"/worlds");
        File[] list = file.listFiles();
        levelNameArray.clear();
        if (list != null) {
            for (File fl:list) {
                if (fl.isDirectory()) {
                    levelNameArray.add(fl.getName());
                }
            }
        }
    }

    public static void checkNewLevel(){
        for (String name:levelNameArray) {
            if (!config.exists(name)){
                LinkedHashMap<String,Object> data = new LinkedHashMap<>();
                data.put("levelName",name);
                data.put("isLoad",true);
                data.put("canBreak",true);
                data.put("canPlace",true);
                data.put("canUpdate",true);
                data.put("canPVP",true);
                data.put("canPVE",true);
                data.put("canChangeMode",true);
                data.put("canFly",false);
                data.put("canPhysicalDamage",true);
                data.put("notAdminCanTP",true);
                config.set(name,data);
            }
        }
        for (String key:config.getAll().keySet()) {
            if (!levelNameArray.contains(key)){
                config.remove(key);
            }
        }
        config.save();
    }

    public static void loadConfig(){
        config = new Config(MenuGUI.getInstance().getDataFolder()+"/levelConfig.yml",Config.YAML);
    }

    public LevelData(String levelName){
        this.levelName = levelName;
        getLevel();
    }

    public LevelData(){

    }
    public Level level = null;

    public String levelName = "";

    public boolean isLoad = false;

    public boolean canBreak = true;

    public boolean canPlace = true;

    public boolean canUpdate = true;

    public boolean canPVP = true;

    public boolean canPVE = true;

    public boolean canChangeMode = true;

    public boolean canFly = false;

    public boolean canPhysicalDamage = true;

    public boolean notAdminCanTP = true;

    public boolean setLoad(boolean load){
        if (levelName.isEmpty()) {
            return false;
        }
        if (load){
            if (isLoad){
            return false;
        }
            return Server.getInstance().loadLevel(levelName);
        }
        Level lv = null;
        if ((lv = Server.getInstance().getLevelByName(levelName)) == null) {
            return false;
        }
        if (teleportPlayer(lv)){
            lv.close();
            return true;
        }
        return false;
    }
    private void getLevel(){
        if (levelName.isEmpty()) return;
        level = Server.getInstance().getLevelByName(levelName);
    }

    private boolean teleportPlayer(Level level){
        //判断是否有地图可以传送
        LevelData levelData = null;
        for (LevelData lv:levelList.values()) {
            if (lv.isLoad && lv.level!=null && !lv.levelName.equals(level.getFolderName()) && lv.isNotAdminCanTP()){
              levelData = lv;
              break;
            }
        }
        if (levelData==null) return false;
        for (Player p:level.getPlayers().values()) {
                p.teleport(levelData.level.getSpawnLocation());
                p.sendMessage(TextFormat.RED+"地图: "+levelData.levelName+" 即将被管理员卸载，已经将你传送到附近地图");
        }
        return true;
    }

    public boolean isCanBreak(){
        return canBreak;
    }

    public boolean isCanPlace() {
        return canPlace;
    }

    public boolean isCanUpdate() {
        return canUpdate;
    }

    public boolean isCanChangeMode() {
        return canChangeMode;
    }

    public boolean isNotAdminCanTP() {
        return notAdminCanTP;
    }

    public boolean isCanFly() {
        return canFly;
    }

    public boolean isCanPhysicalDamage() {
        return canPhysicalDamage;
    }

    public boolean isCanPVE() {
        return canPVE;
    }

    public boolean isCanPVP() {
        return canPVP;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void save(){
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        data.put("levelName",levelName);
        data.put("isLoad",isLoad);
        data.put("canBreak",canBreak);
        data.put("canPlace",canPlace);
        data.put("canUpdate",canUpdate);
        data.put("canPVP",canPVP);
        data.put("canPVE",canPVE);
        data.put("canChangeMode",canChangeMode);
        data.put("canFly",canFly);
        data.put("canPhysicalDamage",canPhysicalDamage);
        data.put("notAdminCanTP",notAdminCanTP);
        config.set(levelName,data);
        config.save();
    }

    public boolean update(HashMap map){

        isLoad = (boolean)map.get(1);
        canBreak = (boolean)map.get(2);
        canPlace = (boolean)map.get(3);
        canUpdate = (boolean)map.get(4);
        canPVP = (boolean)map.get(5);
        canPVE = (boolean)map.get(6);
        canChangeMode = (boolean)map.get(7);
        canFly = (boolean)map.get(8);
        canPhysicalDamage = (boolean)map.get(9);
        notAdminCanTP = (boolean)map.get(10);
        save();
        if (!isLoad()){
            return setLoad(false);
        }
        return true;
    }
}

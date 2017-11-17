package sole.memory.menugui.database;

import cn.nukkit.utils.Config;
import sole.memory.menugui.MenuGUI;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by SoleMemory
 * on 2017/11/7.
 */
public class PlayerDataBase {

    public MenuGUI plugin = MenuGUI.getInstance();

    public PlayerDataBase(String name){
        File file = new File(plugin.getDataFolder()+"/player/"+name+".yml");
        if (!file.exists()||!file.isFile()){
            this.isNewPlayer = true;
            return;
        }
        Map<String,Object> map = new Config(plugin.getDataFolder()+"/player/"+name+".yml",Config.YAML).getAll();
        try {
            this.messages = (HashMap) map.get("message");
            this.answer = map.get("answer").toString();
            this.email = map.get("email").toString();
            this.passWorld = map.get("passWorld").toString();
            this.question = map.get("question").toString();
            this.ip = map.get("ip").toString();
            this.name = map.get("name").toString();
            this.lastDate = map.get("lastDate").toString();
        }catch (NullPointerException e){
            this.plugin.getLogger().warning("Please check file: MenuGUI/player/"+name+".yml ,There may be errors");
        }
    }
    public String name = "";

    public HashMap messages;

    public boolean isNewPlayer = false;

    public String passWorld = "";

    public String question = "";

    public String answer = "";

    public String ip = "";

    public String lastDate = "";

    public String email = "";


    public void putInfo(HashMap<String,String> map){
        this.name = map.get("name");
        this.passWorld = map.get("passWord");
        this.email = map.get("email");
        this.question = map.get("question");
        this.answer = map.get("answer");
        this.ip = map.get("ip");
    }


    private HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",this.name);
        map.put("passWord",passWorld);
        map.put("question",question);
        map.put("answer",answer);
        map.put("email",email);
        map.put("ip",ip);
        map.put("lastDate",lastDate);
        map.put("message",messages);
        return map;
    }

    @SuppressWarnings("unchecked")
    public void save(){
        Config config = new Config(plugin.getDataFolder()+"/player/"+name+".yml",Config.YAML);
        LinkedHashMap map = new LinkedHashMap();
        map.putAll(toMap());
        config.setAll(map);
        config.save();
    }

    public boolean isTowHourNoCheck(){
        if (lastDate.equals("")) return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setLenient(false);
        Date date1 = null;
        try {
            date1 = format.parse(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = new Date();
        long time = 0;
        if (date1 != null) {
            time = (date2.getTime() - date1.getTime()) / (1000 * 60*60);
        }
        return time <2L;
    }
}

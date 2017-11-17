package sole.memory.menugui.menu;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.utils.TextFormat;
import sole.memory.menugui.windows.Custom;

import java.util.HashMap;


/**
 * Created by SoleMemory
 * on 2017/11/7.
 */
public class PlayerLoginSend {

    public static void getPlayerLoginPage(Player player){
        HashMap<Integer,Object> map = new HashMap<>();
        ElementLabel label = new ElementLabel("用户名: "+ TextFormat.BOLD+player.getName());
        ElementInput input = new ElementInput("请输入密码");
        ElementToggle toggle = new ElementToggle("忘记密码?",false);
        map.put(0,label);
        map.put(1,input);
        map.put(2,toggle);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        player.showFormWindow(custom.getGUI());
    }

    public static void getPlayerRegisterPage(Player player){
        HashMap<Integer,Object> map = new HashMap<>();
        ElementLabel label = new ElementLabel("用户名校验(注意大小写): "+ TextFormat.BOLD+player.getName());
        ElementInput input = new ElementInput("请输入密码");
        ElementToggle toggle = new ElementToggle("忘记密码?",false);
        map.put(0,label);
        map.put(1,input);
        map.put(2,toggle);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        player.showFormWindow(custom.getGUI());
    }

    public static void getPlayerBondMailPage(Player player){
        HashMap<Integer,Object> map = new HashMap<>();
        ElementLabel label = new ElementLabel("绑定邮箱支持QQ和网易163邮箱");
        ElementInput input = new ElementInput("请输入邮箱");
        map.put(0,label);
        map.put(1,input);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        player.showFormWindow(custom.getGUI());
    }

    public static void getInputVerificationPage(Player player,String mail){
        HashMap<Integer,Object> map = new HashMap<>();
        ElementLabel label = new ElementLabel("验证码已经发送到邮箱:"+mail+"\n 请不要关闭此页面，等待邮件发送\n请输入 6 位数的邮箱验证码");
        ElementInput input = new ElementInput("请输入验证码");
        map.put(0,label);
        map.put(1,input);
        Custom custom = new Custom();
        custom.inputData(map);
        custom.changeDataToGUI();
        player.showFormWindow(custom.getGUI());
    }

    public static void getSendEmailErrorPage(Player player){
        FormWindowModal modal = new FormWindowModal("邮箱错误","发送验证码失败，请联系管理员","返回主页","退出");
    }

    public static void getInputEmaillErrorPage(Player player){
        FormWindowModal modal = new FormWindowModal("邮箱错误","你输入的邮箱格式不对，请检查后输入","重新输入邮箱","返回主页");
    }
}

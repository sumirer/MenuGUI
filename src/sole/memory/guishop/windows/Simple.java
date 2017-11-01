package sole.memory.guishop.windows;

import cn.nukkit.form.window.FormWindowSimple;
import sole.memory.guishop.windows.button.ButtonInfo;

import java.util.HashMap;

/**
 * @author SOleMemory
 * on 2017/10/31.
 */
public class Simple extends Windows{

    public String text = "";

    public String info = "";

    private FormWindowSimple simple = null;

    private HashMap<Integer,ButtonInfo> data = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void inputData(HashMap map) {
        this.data = map;
    }

    @Override
    public FormWindowSimple getGUI() {

       if (this.simple != null) return this.simple;
       return new FormWindowSimple("错误","未出现任何控件，数据为空");
    }

    @Override
    public void changeDataToGUI() {
        if (data.isEmpty()) return;
        FormWindowSimple formWindowSimple = new FormWindowSimple(this.text,this.info);
        for (ButtonInfo button:this.data.values()) {
            formWindowSimple.addButton(button.getButton());
        }
        this.simple = formWindowSimple;
    }
}

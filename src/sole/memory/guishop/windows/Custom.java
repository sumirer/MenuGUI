package sole.memory.guishop.windows;

import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.HashMap;

/**
 * @author SoleMemory
 * on 2017/10/31.
 */
public class Custom extends Windows {

    public String title = "";

    private HashMap<Integer,Object> data = new HashMap<>();

    private FormWindowCustom custom = null;


    @Override
    @SuppressWarnings("unchecked")
    public void inputData(HashMap map) {
        this.data = map;
    }

    @Override
    public FormWindow getGUI() {
        if (custom != null) return custom;
        return new FormWindowSimple("错误","未读取到任何数据，请添加");
    }

    @Override
    public void changeDataToGUI(){
        if (data.isEmpty()) return;
        FormWindowCustom formWindowCustom = new FormWindowCustom(this.title);
        for (Object element:data.values()) {
            if (element instanceof ElementDropdown){
                formWindowCustom.addElement((Element) element);
            }
            if (element instanceof ElementInput){
                formWindowCustom.addElement((Element) element);
            }
            if (element instanceof ElementLabel){
                formWindowCustom.addElement((Element) element);
            }
            if (element instanceof ElementSlider){
                formWindowCustom.addElement((Element) element);
            }
            if (element instanceof ElementStepSlider){
                formWindowCustom.addElement((Element) element);
            }
            if (element instanceof ElementToggle){
                formWindowCustom.addElement((Element) element);
            }
        }
        this.custom = formWindowCustom;
    }

}

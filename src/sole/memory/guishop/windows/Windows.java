package sole.memory.guishop.windows;

import cn.nukkit.form.window.FormWindow;

import java.util.HashMap;

/**
 *
 *on 2017/10/31.
 * @author SoleMemory
 */
public abstract class Windows {




    public abstract void inputData(HashMap map);

    public abstract FormWindow getGUI();

    public abstract void changeDataToGUI();
}

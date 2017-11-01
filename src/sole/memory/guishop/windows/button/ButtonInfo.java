package sole.memory.guishop.windows.button;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;

import java.io.File;

/**
 * @author SOleMemory
 *  on 2017/10/31.
 */
public class ButtonInfo {

    public boolean haveImg = false;

    public String imgPath = "";

    public String text = "";

    public ElementButton getButton(){
        if (this.haveImg && new File(this.imgPath).exists()){
            ElementButtonImageData path = new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH,this.imgPath);
            return new ElementButton(this.text,path);
        }
        return new ElementButton(this.text);
    }
}

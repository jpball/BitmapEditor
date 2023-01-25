import java.awt.Color;
import java.util.Random;
public class RandomColorBitmapEdit extends BaseModifier{
    Random rng;
    //--
    public RandomColorBitmapEdit(Modifier m){
        super(m);
        rng = new Random();
    }
    //--
    @Override
    public void modify(BitmapEdit bEdit) {
        bEdit.setColor(new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256)));
        super.modify(bEdit);
    }
}

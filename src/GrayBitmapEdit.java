import java.awt.Color;
public class GrayBitmapEdit extends BaseModifier{
    public GrayBitmapEdit(Modifier m){
        super(m);
    }
    //--
    @Override
    public void modify(BitmapEdit bEdit) {
        Color col = bEdit.getColor();

        int avg = (col.getRed() + col.getGreen() + col.getBlue())/3;
        bEdit.setColor(new Color(avg,avg,avg));
        super.modify(bEdit);
    }
}

public class HorizontalInvertBitmapEdit extends BaseModifier{
    //--
    public HorizontalInvertBitmapEdit(Modifier m){
        super(m);
    }
    //--
    @Override
    public void modify(BitmapEdit bEdit) {
        bEdit.setXVal(BitmapGUI.getBitmapWidth() - bEdit.getXVal() - 1);
        super.modify(bEdit);
    }
}

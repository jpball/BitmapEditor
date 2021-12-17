public class VerticalInvertBitmapEdit extends BaseModifier{
    //--
    public VerticalInvertBitmapEdit(Modifier m){
        super(m);
    }
    //--
    @Override
    public void modify(BitmapEdit bEdit) {
        bEdit.setYVal(BitmapGUI.getBitmapHeight() - bEdit.getYVal() - 1);
        super.modify(bEdit);
    }
}

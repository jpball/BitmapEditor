public class BaseModifier implements Modifier{
    private Modifier modifiedComponent;
    
    public BaseModifier(Modifier md){
        modifiedComponent = md;
    }
    
    @Override
    public void modify(BitmapEdit bEdit) {
        modifiedComponent.modify(bEdit);
    }
}

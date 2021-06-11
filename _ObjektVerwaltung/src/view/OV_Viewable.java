package view;

public interface OV_Viewable {

	public abstract int getOriginalWidth();
	
	public abstract int getOriginalHeight();
	
	public abstract int[] getOffset(int screenWidth, int screenHeight);
	
}

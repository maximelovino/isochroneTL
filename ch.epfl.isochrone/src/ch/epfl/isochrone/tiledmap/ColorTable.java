package ch.epfl.isochrone.tiledmap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class ColorTable {
    private final List<Color> colors;
    private final int sliceLength;
    
    
    /**
     * @param sliceLength 
     * 		Time (sec)
     * @param colors 
     * 		A list of colors
     */
    public ColorTable(int sliceLength, List<Color> colors){
        if(colors.isEmpty()){
            throw new IllegalArgumentException("the color table is empty");
        }
        
        this.sliceLength=sliceLength;
        this.colors=new ArrayList<Color>(colors);
    }
    
    
    /**
     * @param sliceNum 
     * 		A slice number
     * @return  the color associated to a slice (null if it doesn't exist)
     */
    public Color colorForSlice(int sliceNum){
        if(sliceNum<colors.size()-1){
            return colors.get(sliceNum);
        }else{
            return null;
        }
    }
    
    
    /**
     * @return The number of slices
     */
    public int slicesNumber(){
        return colors.size();
    }
    
    /**
     * @return The length of the slice
     */
    public int slicesLength(){
        return sliceLength;        
    }

}

package ch.epfl.isochrone.tiledmap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class ColorTable {
    private final List<Color> colors;
    private final int sliceLength;
    
    
    public ColorTable(int sliceLength, List<Color> colors){
        this.sliceLength=sliceLength;
        this.colors=new ArrayList<Color>(colors);
    }
    
    
    public Color colorForSlice(int sliceNum){
        if(sliceNum<colors.size()-1){
            return colors.get(sliceNum);
        }else{
            return null;
        }
    }
    
    public int slicesNumber(){
        return colors.size();
    }
    
    public int slicesLength(){
        return sliceLength;        
    }

}

package ch.epfl.isochrone.tiledmap;

public final class TransparentTileProvider extends FilteringTileProvider {
    private TileProvider tp;
    private double alphaChannel;
    
    public TransparentTileProvider(double alphaChannel, TileProvider tp){
        if(alphaChannel<0||alphaChannel>1){
            throw new IllegalArgumentException("the alpha value is not valid");
        }
        this.tp=tp;
        this.alphaChannel=alphaChannel;
    }

    @Override
    public Tile tileAt(int zoom, int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int transformARGB(int colorARGB) {
        // TODO Auto-generated method stub
        return 0;
    }

}

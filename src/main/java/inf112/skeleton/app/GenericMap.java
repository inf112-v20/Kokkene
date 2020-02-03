package inf112.skeleton.app;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Generic map for making construction easier.
 */
public abstract class GenericMap {

    public abstract void render (OrthographicCamera camera);
    public abstract void update (float delta);
    public abstract void dispose ();

    /**
     * Gets a tile by its coordinate on the Map at a specified layer.
     * @param layer tiles will be placed in layers.
     * @param x horisontal value
     * @param y vertical value
     * @return TileType
     */
    public abstract TileType getTileTypeByCoordinate(int layer, float x, float y);

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getLayers();
}

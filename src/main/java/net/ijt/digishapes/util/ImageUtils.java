/**
 * 
 */
package net.ijt.digishapes.util;

import java.util.function.BiFunction;

import ij.ImageStack;
import ij.process.ImageProcessor;

/**
 * 
 */
public class ImageUtils
{
    /**
     * Fills the content of the specified ImageProcessor using a function of the
     * coordinates within the image.
     * 
     * Example:
     * 
     * <pre>{@code
     * ImageProcessor image = new ByteProcessor(8, 6);
     * ImageUtils.fill(image, (x, y) -> (x + y * 10.0));
     * }</pre>
     * 
     * @param image
     *            the image to fill
     * @param fun
     *            the function to use for filling
     */
    public static final void fill(ImageProcessor image, BiFunction<Integer, Integer, Boolean> fun, float fillValue)
    {
        int sizeX = image.getWidth();
        int sizeY = image.getHeight();
        
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                if (fun.apply(x, y))
                {
                    image.setf(x, y, fillValue);
                }
            }
        }
    }
    
    /**
     * Fills the content of the specified ImageStack using a function of the
     * coordinates within the stack.
     * 
     * 
     * <pre>{@code
     * ImageStack image = ImageStack.create(7, 5, 3, 8);
     * ImageUtils.fill(image, (x, y, z) -> (x + y * 10.0 + z * 100.0));
     * }</pre>
     * 
     * @param image
     *            the image to fill
     * @param fun
     *            the function to use for filling
     */
    public static final void fill(ImageStack image, TriFunction<Integer, Integer, Integer, Boolean> fun, double fillValue)
    {
        int sizeX = image.getWidth();
        int sizeY = image.getHeight();
        int sizeZ = image.getSize();
        
        for (int z = 0; z < sizeZ; z++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    if (fun.apply(x, y, z))
                    {
                        image.setVoxel(x, y, z, fillValue);
                    }
                }
            }
        }
    }
    

}

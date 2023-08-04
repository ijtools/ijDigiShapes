/**
 * 
 */
package net.ijt.digishapes.plugins;

import java.util.Locale;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import net.ijt.digishapes.shapes3d.Cuboid3D;
import net.ijt.geom3d.Bounds3D;

/**
 * Fills a 3D oriented Box ("cuboid") defined by user parameters on the current
 * 3D image.
 * 
 * @author dlegland
 *
 */
public class FillOrientedBox3D implements PlugIn
{
    @Override
    public void run(String arg)
    {
        ImagePlus imagePlus = IJ.getImage();
        if (imagePlus.getStackSize() == 1) 
        {
            IJ.error("Requires a Stack");
            return;
        }
        
        // create the dialog
        GenericDialog gd = new GenericDialog("Fill Oriented Box 3D");
        gd.addNumericField("Center_X", 100, 2);
        gd.addNumericField("Center_Y", 100, 2);
        gd.addNumericField("Center_Z", 100, 2);
        gd.addNumericField("Side Length 1", 80, 2);
        gd.addNumericField("Side Length 2", 40, 2);
        gd.addNumericField("Side Length 3", 20, 2);
        gd.addNumericField("Euler Angle X (degrees)", 0, 2);
        gd.addNumericField("Euler Angle Y (degrees)", 0, 2);
        gd.addNumericField("Euler Angle Z (degrees)", 0, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double centerZ = gd.getNextNumber();
        double sideLength1 = gd.getNextNumber();
        double sideLength2 = gd.getNextNumber();
        double sideLength3 = gd.getNextNumber();
        double eulerAngleX = gd.getNextNumber();
        double eulerAngleY = gd.getNextNumber();
        double eulerAngleZ = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
        long t0 = System.currentTimeMillis();

        // retrieve image data
        ImageStack array = imagePlus.getStack();
        int sizeX = array.getWidth();
        int sizeY = array.getHeight();
        int sizeZ = array.getSize();
        
        // generate geometric shape
        Cuboid3D cuboid = new Cuboid3D(centerX, centerY, centerZ, sideLength1, sideLength2, sideLength3, eulerAngleX, eulerAngleY, eulerAngleZ);
        
        // computation bounds
        Bounds3D bounds = cuboid.bounds();
        int x0 = Math.max((int) Math.floor(bounds.minX()), 0);
        int x1 = Math.min((int) Math.ceil(bounds.maxX()), sizeX - 1);
        int y0 = Math.max((int) Math.floor(bounds.minY()), 0);
        int y1 = Math.min((int) Math.ceil(bounds.maxY()), sizeY - 1);
        int z0 = Math.max((int) Math.floor(bounds.minZ()), 0);
        int z1 = Math.min((int) Math.ceil(bounds.maxZ()), sizeZ - 1);
        
        // iterate over image voxels
        for (int z = z0; z <= z1; z++)
        {
            for (int y = y0; y <= y1; y++)
            {
                for (int x = x0; x <= x1; x++)
                {
                    if (cuboid.isInside(x + 0.5, y + 0.5, z + 0.5))
                    {
                        array.setVoxel(x, y, z, fillValue);
                    }
                }
            }
        }

        
        // elapsed time in seconds
        long t1 = System.currentTimeMillis();
        double timeInSecs = (t1 - t0) / 1_000.0;
        
        // refresh display
        imagePlus.updateAndDraw();
        
        // show elapsed time
        String pattern = "%s: %.3f seconds";
        String status = String.format(Locale.ENGLISH, pattern, "Fill Cuboid", timeInSecs);
        IJ.showStatus(status);
    }
}

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
import net.ijt.geometry.geom3d.Bounds3D;
import net.ijt.geometry.geom3d.Ellipsoid3D;

/**
 * Fills an ellipsoid defined by user parameters on the current 3D image.
 * 
 * @author dlegland
 *
 */
public class FillEllipsoid3D implements PlugIn
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
        
        // retrieve image data
        ImageStack array = imagePlus.getStack();
        int sizeX = array.getWidth();
        int sizeY = array.getHeight();
        int sizeZ = array.getSize();
        
        // create the dialog
        GenericDialog gd = new GenericDialog("Fill Ellipsoid");
        gd.addNumericField("Center_X", Math.floor(sizeX / 2), 2);
        gd.addNumericField("Center_Y", Math.floor(sizeY / 2), 2);
        gd.addNumericField("Center_Z", Math.floor(sizeZ / 2), 2);
        gd.addNumericField("Semi-Axis_1", 40, 2);
        gd.addNumericField("Semi-Axis_2", 20, 2);
        gd.addNumericField("Semi-Axis_3", 10, 2);
        gd.addNumericField("Euler_Angle_X (degrees)", 0, 2);
        gd.addNumericField("Euler_Angle_Y (degrees)", 0, 2);
        gd.addNumericField("Euler_Angle_Z (degrees)", 0, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double centerZ = gd.getNextNumber();
        double semiAxis1 = gd.getNextNumber();
        double semiAxis2 = gd.getNextNumber();
        double semiAxis3 = gd.getNextNumber();
        double eulerAngleX = gd.getNextNumber();
        double eulerAngleY = gd.getNextNumber();
        double eulerAngleZ = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
        long t0 = System.currentTimeMillis();

        // generate geometric shape
        Ellipsoid3D elli = new Ellipsoid3D(centerX, centerY, centerZ, semiAxis1, semiAxis2, semiAxis3, eulerAngleX, eulerAngleY, eulerAngleZ);
        
        // computation bounds
        Bounds3D bounds = elli.bounds();
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
                    if (elli.isInside(x + 0.5, y + 0.5, z + 0.5))
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
        String status = String.format(Locale.ENGLISH, pattern, "Fill Ellipsoid", timeInSecs);
        IJ.showStatus(status);
    }
}

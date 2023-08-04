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
import net.ijt.digishapes.shapes3d.Cylinder3D;
import net.ijt.geom3d.Point3D;

/**
 * Fills a 3D cylinder defined by user parameters on the current 3D image.
 * 
 * @author dlegland
 *
 */
public class FillCylinder3D implements PlugIn
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
        GenericDialog gd = new GenericDialog("Fill Cylinder");
        gd.addNumericField("Point 1 X", 20, 2);
        gd.addNumericField("Point 1 Y", 20, 2);
        gd.addNumericField("Point 1 Z", 20, 2);
        gd.addNumericField("Point 2 X", 80, 2);
        gd.addNumericField("Point 2 Y", 80, 2);
        gd.addNumericField("Point 2 Z", 80, 2);
        gd.addNumericField("Radius", 10, 2);
        gd.addNumericField("Fill Value", 255, 0);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        double p1x = gd.getNextNumber();
        double p1y = gd.getNextNumber();
        double p1z = gd.getNextNumber();
        double p2x = gd.getNextNumber();
        double p2y = gd.getNextNumber();
        double p2z = gd.getNextNumber();
        double radius = gd.getNextNumber();
        float fillValue = (float) gd.getNextNumber();
        
        long t0 = System.currentTimeMillis();

        // retrieve image data
        ImageStack array = imagePlus.getStack();
        int sizeX = array.getWidth();
        int sizeY = array.getHeight();
        int sizeZ = array.getSize();
        
        // generate geometric shape
        Point3D p1 = new Point3D(p1x, p1y, p1z);
        Point3D p2 = new Point3D(p2x, p2y, p2z);
        Cylinder3D cyl = new Cylinder3D(p1, p2, radius);
        
//        // computation bounds
//        Bounds3D bounds = cyl.bounds();
//        int x0 = Math.max((int) Math.floor(bounds.minX()), 0);
//        int x1 = Math.min((int) Math.ceil(bounds.maxX()), sizeX - 1);
//        int y0 = Math.max((int) Math.floor(bounds.minY()), 0);
//        int y1 = Math.min((int) Math.ceil(bounds.maxY()), sizeY - 1);
//        int z0 = Math.max((int) Math.floor(bounds.minZ()), 0);
//        int z1 = Math.min((int) Math.ceil(bounds.maxZ()), sizeZ - 1);
        
        // iterate over image voxels
        for (int z = 0; z < sizeZ; z++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    if (cyl.isInside(x + 0.5, y + 0.5, z + 0.5))
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
        String status = String.format(Locale.ENGLISH, pattern, "Fill Cylinder", timeInSecs);
        IJ.showStatus(status);
    }
}

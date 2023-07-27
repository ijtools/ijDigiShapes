/**
 * 
 */
package net.ijt.digishapes;

import java.util.Locale;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import net.ijt.geom3d.surface.Ellipsoid3D;

/**
 * @author dlegland
 *
 */
public class DiscretizedEllipsoid3D implements PlugIn
{
    static int count = 0;
    
    @Override
    public void run(String arg)
    {
        // create the dialog
        GenericDialog gd = new GenericDialog("Discretized Ellipsoid");
        gd.addNumericField("Image Size X", 100, 0);
        gd.addNumericField("Image Size Y", 100, 0);
        gd.addNumericField("Image Size Z", 100, 0);
        gd.addNumericField("Center X", 50, 2);
        gd.addNumericField("Center Y", 50, 2);
        gd.addNumericField("Center Z", 50, 2);
        gd.addNumericField("Semi-Axis 1", 40, 2);
        gd.addNumericField("Semi-Axis 2", 20, 2);
        gd.addNumericField("Semi-Axis 3", 10, 2);
        gd.addNumericField("Euler Angle X (degrees)", 0, 2);
        gd.addNumericField("Euler Angle Y (degrees)", 0, 2);
        gd.addNumericField("Euler Angle Z (degrees)", 0, 2);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        int sizeX = (int) gd.getNextNumber();
        int sizeY = (int) gd.getNextNumber();
        int sizeZ = (int) gd.getNextNumber();
        double centerX = gd.getNextNumber();
        double centerY = gd.getNextNumber();
        double centerZ = gd.getNextNumber();
        double semiAxis1 = gd.getNextNumber();
        double semiAxis2 = gd.getNextNumber();
        double semiAxis3 = gd.getNextNumber();
        double eulerAngleX = gd.getNextNumber();
        double eulerAngleY = gd.getNextNumber();
        double eulerAngleZ = gd.getNextNumber();
        
        // allocate image array
        ImageStack stack = ImageStack.create(sizeX, sizeX, sizeY, 8);
        
        // generate geometric shape
        Ellipsoid3D elli = new Ellipsoid3D(centerX, centerY, centerZ, semiAxis1, semiAxis2, semiAxis3, eulerAngleX, eulerAngleY, eulerAngleZ);
        
        // iterate over image voxels
        for (int z = 0; z < sizeZ; z++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    if (elli.isInside(x + 0.5, y + 0.5, z + 0.5))
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }

        // compute image name
        String newName = "Ellipsoid";
        if (count > 0)
        {
            newName = String.format(Locale.ENGLISH, "Ellipsoid-%02d", count);
        }
        count++;

        // create Image
        ImagePlus image = new ImagePlus(newName, stack);
        image.show();
    }
}

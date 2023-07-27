/**
 * 
 */
package net.ijt.digishapes;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import net.ijt.digishapes.shapes3d.Cylinder3D;
import net.ijt.geom3d.Point3D;

/**
 * @author dlegland
 *
 */
public class DiscretizedCylinder3D implements PlugIn
{
    static int count = 0;
    
    @Override
    public void run(String arg)
    {
        // create the dialog
        GenericDialog gd = new GenericDialog("Discretized Cylinder");
        gd.addNumericField("Image Size X", 100, 0);
        gd.addNumericField("Image Size Y", 100, 0);
        gd.addNumericField("Image Size Z", 100, 0);
        gd.addNumericField("Point 1 X", 20, 2);
        gd.addNumericField("Point 1 Y", 20, 2);
        gd.addNumericField("Point 1 Z", 20, 2);
        gd.addNumericField("Point 2 X", 80, 2);
        gd.addNumericField("Point 2 Y", 80, 2);
        gd.addNumericField("Point 2 Z", 80, 2);
        gd.addNumericField("Radius", 10, 2);

        gd.showDialog();
        if (gd.wasCanceled())
            return;
        
        // retrieve user inputs
        int sizeX = (int) gd.getNextNumber();
        int sizeY = (int) gd.getNextNumber();
        int sizeZ = (int) gd.getNextNumber();
        double p1x = gd.getNextNumber();
        double p1y = gd.getNextNumber();
        double p1z = gd.getNextNumber();
        double p2x = gd.getNextNumber();
        double p2y = gd.getNextNumber();
        double p2z = gd.getNextNumber();
        double radius = gd.getNextNumber();
        
        // allocate image array
        ImageStack stack = ImageStack.create(sizeX, sizeX, sizeY, 8);
        
        // generate geometric shape
        Point3D p1 = new Point3D(p1x, p1y, p1z);
        Point3D p2 = new Point3D(p2x, p2y, p2z);
        Cylinder3D cyl = new Cylinder3D(p1, p2, radius);
        
        // iterate over image voxels
        for (int z = 0; z < sizeZ; z++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                for (int x = 0; x < sizeX; x++)
                {
                    if (cyl.isInside(x + 0.5, y + 0.5, z + 0.5))
                    {
                        stack.setVoxel(x, y, z, 255);
                    }
                }
            }
        }

        // compute image name
        String newName = createName("Cylinder", count++);

        // create Image
        ImagePlus image = new ImagePlus(newName, stack);
        image.show();
    }
    
    private String createName(String baseName, int count)
    {
        String name = baseName;
        if (count > 0)
        {
            name += String.format("-%02d", count);
        }
        return name;
    }
}

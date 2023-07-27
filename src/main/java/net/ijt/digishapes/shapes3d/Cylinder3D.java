/**
 * 
 */
package net.ijt.digishapes.shapes3d;

import net.ijt.geom3d.AffineTransform3D;
import net.ijt.geom3d.Point3D;
import net.ijt.geom3d.Vector3D;

/**
 * @author dlegland
 *
 */
public class Cylinder3D
{
    // ===================================================================
    // Class variables

    /**
     * The first extremity point of the cylinder.
     */
    final Point3D p1;

    /**
     * The second extremity point of the cylinder.
     */
    final Point3D p2;
    
    final double radius;
    
    
    // ===================================================================
    // Constructors
    
    public Cylinder3D(Point3D p1, Point3D p2, double radius)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.radius = radius;
    }

    /**
     * Checks if the specified point is contained within the domain bounded by
     * this boundary.
     * 
     * @param point
     *            the point to test
     * @return true is the point is within the domain corresponding to this
     *         boundary.
     */
    public boolean isInside(Point3D point)
    {
        Point3D pt = globalToLocalTransform().transform(point);
        double r = Math.hypot(pt.getX(), pt.getY());
        if (r > 1) return false;
        if (pt.getZ() > 1) return false;
        if (pt.getZ() < 0) return false;
        return true;
    }

    /**
     * Checks if the specified point is contained within the domain bounded by
     * this boundary.
     * 
     * @param x
     *            the x-coordinate of the point to test
     * @param y
     *            the y-coordinate of the point to test
     * @return true is the point is within the domain corresponding to this
     *         boundary.
     */
    public boolean isInside(double x, double y, double z)
    {
        return isInside(new Point3D(x, y, z));
    }
    
    /**
     * Creates the affine transform that will map a centered unit cube to this
     * cylinder instance.
     * 
     * @return the affine transform that will map a centered unit cube to this
     *cylindercuboid instance.
     */
    private AffineTransform3D localToGlobalTransform()
    {
        // compute scaling
        double height = p1.distance(p2);
        AffineTransform3D sca = AffineTransform3D.createScaling(radius, radius, height);
        
        // convert pair of points to spherical coordinates
        Vector3D vect = new Vector3D(p1, p2);
        double hxy = Math.hypot(vect.getX(), vect.getY());
        double theta = Math.PI/2 - Math.atan2(vect.getZ(), hxy);
        double phi   = Math.atan2(vect.getY(), vect.getX());
        AffineTransform3D rot1 = AffineTransform3D.createRotationOy(theta);
        AffineTransform3D rot2 = AffineTransform3D.createRotationOz(phi);
        
        // concatenate and translate
        AffineTransform3D tra = AffineTransform3D.createTranslation(p1);
        return tra.concatenate(rot2).concatenate(rot1).concatenate(sca);
    }
    
    private AffineTransform3D globalToLocalTransform()
    {
        return localToGlobalTransform().inverse();
    }

}

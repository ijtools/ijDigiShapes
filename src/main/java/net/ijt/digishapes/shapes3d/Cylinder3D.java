/**
 * 
 */
package net.ijt.digishapes.shapes3d;

import net.ijt.geom3d.AffineTransform3D;
import net.ijt.geom3d.Bounds3D;
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
        double r = Math.hypot(pt.x(), pt.y());
        if (r > 1) return false;
        if (pt.z() > 1) return false;
        if (pt.z() < 0) return false;
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
     * Returns upper bounds by computing extremity points and adding a margin
     * equal to the radius.
     * 
     * @return the approximated bounds of this cylinder.
     */
    public Bounds3D bounds()
    {
        double xmin = Math.min(p1.x(), p2.x()) - radius;
        double xmax = Math.max(p1.x(), p2.x()) + radius;
        double ymin = Math.min(p1.y(), p2.y()) - radius;
        double ymax = Math.max(p1.y(), p2.y()) + radius;
        double zmin = Math.min(p1.z(), p2.z()) - radius;
        double zmax = Math.max(p1.z(), p2.z()) + radius;
        return new Bounds3D(xmin, xmax, ymin, ymax, zmin, zmax);
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
        double hxy = Math.hypot(vect.x(), vect.y());
        double theta = Math.PI/2 - Math.atan2(vect.z(), hxy);
        double phi   = Math.atan2(vect.y(), vect.x());
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

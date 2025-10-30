/**
 * 
 */
package net.ijt.digishapes.shapes3d;

import net.ijt.geometry.geom3d.AffineTransform3D;
import net.ijt.geometry.geom3d.Bounds3D;
import net.ijt.geometry.geom3d.Point3D;
import net.ijt.geometry.geom3d.Rotation3D;

/**
 * @author dlegland
 *
 */
public class CenteredCylinder3D
{
    // ===================================================================
    // Class variables

    /**
     * The center of the cylinder.
     */
    final Point3D center;
    
    final double length;
    
    final double radius;
    
    /**
     * The first rotation applied to the cylinder, around the X-axis, in degrees.
     */
    final double eulerAngleX; 
    /**
     * The second rotation applied to the cylinder, around the Y-axis, in degrees.
     */
    final double eulerAngleY; 
    /**
     * The third rotation applied to the cylinder, around the Z-axis, in degrees.
     */
    final double eulerAngleZ;
    
    
    // ===================================================================
    // Constructors
    
    public CenteredCylinder3D(Point3D center, double length, double radius)
    {
        this(center, length, radius, 0, 0, 0);
    }

    public CenteredCylinder3D(Point3D center, double length, double radius, double eulerX, double eulerY, double eulerZ)
    {
        this.center = center;
        this.length = length;
        this.radius = radius;
        this.eulerAngleX = eulerX;
        this.eulerAngleY = eulerY;
        this.eulerAngleZ = eulerZ;
    }
    
    /**
     * Returns the first reference point of this cylinder, corresponding to one
     * of the extremities.
     * 
     * @return the first reference point.
     */
    public Point3D point1()
    {
        return localToGlobalTransform().transform(new Point3D(0, 0, -0.5));
    }
    
    /**
     * Returns the second reference point of this cylinder, corresponding to one
     * of the extremities.
     * 
     * @return the second reference point.
     */
    public Point3D point2()
    {
        return localToGlobalTransform().transform(new Point3D(0, 0, +0.5));
    }
    
    
    /**
     * Returns the orientation of this cylinder, as a Rotation3D object. 
     * 
     * @return the orientation of the cylinder.
     */
    public Rotation3D orientation()
    {
        return Rotation3D.fromEulerAngles(Math.toRadians(eulerAngleX), Math.toRadians(eulerAngleY), Math.toRadians(eulerAngleZ));
    }
    

    /**
     * Checks if the specified point is contained within this cylinder.
     * 
     * @param point
     *            the point to test
     * @return true is the point is within the domain corresponding to this
     *         cylinder.
     */
    public boolean isInside(Point3D point)
    {
        Point3D pt = globalToLocalTransform().transform(point);
        double r = Math.hypot(pt.x(), pt.y());
        if (r > 1) return false;
        if (Math.abs(pt.z()) > 0.5) return false;
        return true;
    }

    /**
     * Checks if the specified point is contained within this cylinder.
     * 
     * @param x
     *            the x-coordinate of the point to test
     * @param y
     *            the y-coordinate of the point to test
     * @return true is the point is within the domain corresponding to this
     *         cylinder.
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
        Point3D p1 = point1();
        Point3D p2 = point2();
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
     *         cylinder instance.
     */
    private AffineTransform3D localToGlobalTransform()
    {
        // compute scaling
        AffineTransform3D sca = AffineTransform3D.createScaling(radius, radius, length);
        
        // rotation
        AffineTransform3D rot = orientation().asTransform();
        
        // concatenate and translate
        AffineTransform3D tra = AffineTransform3D.createTranslation(center);
        return tra.concatenate(rot).concatenate(sca);
    }
    
    private AffineTransform3D globalToLocalTransform()
    {
        return localToGlobalTransform().inverse();
    }

}

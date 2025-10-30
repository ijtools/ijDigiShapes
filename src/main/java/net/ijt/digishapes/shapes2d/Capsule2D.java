/**
 * 
 */
package net.ijt.digishapes.shapes2d;

import net.ijt.digishapes.shapes3d.CenteredCylinder3D;
import net.ijt.digishapes.shapes3d.Cylinder3D;
import net.ijt.geometry.geom2d.AffineTransform2D;
import net.ijt.geometry.geom2d.Bounds2D;
import net.ijt.geometry.geom2d.Point2D;

/**
 * A 2D "capsule" shape, corresponding to an oriented box terminated by two disk
 * at both extremities.
 * 
 * @see Cylinder3D
 * @see CenteredCylinder3D
 * 
 * @author dlegland
 */
public class Capsule2D
{
    // ===================================================================
    // Class variables

    /**
     * The x-coordinate of the center of the capsule.
     */
    final double centerX;
    
    /**
     * The x-coordinate of the center of the capsule.
     */
    final double centerY;
    
    /**
     * The length of the capsule, without including the two spherical caps.
     */
    final double length;
    
    final double radius;
    
    /**
     * Rotation angle of the capsule, in degrees, starting from horizontal.
     */
    final double orientation;
    
    
    // ===================================================================
    // Constructors
    
    /**
     * Creates an horizontal capsule.
     * 
     * @param center
     *            the center of the capsule
     * @param length
     *            the length of the capsule, without including end caps
     * @param radius
     *            the radius of the capsule (distance to medial axis)
     */
    public Capsule2D(Point2D center, double length, double radius)
    {
        this(center.x(), center.y(), length, radius, 0);
    }

    public Capsule2D(double centerX, double centerY, double length, double radius, double rotAngle)
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.length = length;
        this.radius = radius;
        this.orientation = rotAngle;
    }
    
    /**
     * Returns the first reference point, extremity of the center cylinder and
     * center of one of the two spherical caps.
     * 
     * @return the first reference point.
     */
    public Point2D point1()
    {
        return localToGlobalTransform().transform(new Point2D(-0.5, 0));
    }
    
    /**
     * Returns the second reference point, extremity of the center cylinder and
     * center of one of the two spherical caps.
     * 
     * @return the second reference point.
     */
    public Point2D point2()
    {
        return localToGlobalTransform().transform(new Point2D(0.5, 0));
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
    public boolean isInside(Point2D point)
    {
        // first check if point is within cylinder 
        Point2D pt = globalToLocalTransform().transform(point);
        if (Math.abs(pt.y()) > 1) return false;
        if (pt.x() >= -0.5 && pt.x() <= 0.5) return true;
        // if not, check within end caps 
        if (point.distance(point1()) <= radius) return true;
        if (point.distance(point2()) <= radius) return true;
        return false;
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
    public boolean isInside(double x, double y)
    {
        return isInside(new Point2D(x, y));
    }
    
    /**
     * Returns bounds by computing extremity points and adding a margin
     * equal to the radius.
     * 
     * @return the approximated bounds of this cylinder.
     */
    public Bounds2D bounds()
    {
        Point2D p1 = point1();
        Point2D p2 = point2();
        double xmin = Math.min(p1.x(), p2.x()) - radius;
        double xmax = Math.max(p1.x(), p2.x()) + radius;
        double ymin = Math.min(p1.y(), p2.y()) - radius;
        double ymax = Math.max(p1.y(), p2.y()) + radius;
        return new Bounds2D(xmin, xmax, ymin, ymax);
    }
    
    /**
     * Creates the affine transform that will map a centered unit square to this
     * capsule instance.
     * 
     * @return the affine transform that will map a centered unit square to this
     *         capsule instance.
     */
    private AffineTransform2D localToGlobalTransform()
    {
        // compute scaling
        AffineTransform2D sca = AffineTransform2D.createScaling(length, radius);
        
        // rotation
        AffineTransform2D rot = AffineTransform2D.createRotation(Math.toRadians(orientation));
        
        // concatenate and translate
        AffineTransform2D tra = AffineTransform2D.createTranslation(centerX, centerY);
        return tra.concatenate(rot).concatenate(sca);
    }
    
    private AffineTransform2D globalToLocalTransform()
    {
        return localToGlobalTransform().inverse();
    }

}

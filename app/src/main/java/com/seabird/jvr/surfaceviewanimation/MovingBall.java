package com.seabird.jvr.surfaceviewanimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.List;

/**
 * Created by jvr on 29.07.2016.
 */
public class MovingBall
{
    PointF prCenter;           // pixels on canvas
    float  rRadius;            // pixels
    float  rMass;              // kg
    PointF prVelocity;         // pixels per ms
    PointF prAccel;            // px per ms per ms
    int    nColor = Color.RED;

    public PointF getCenter()   {return prCenter;}
    public float  getRadius()   {return rRadius;}
    public float  getMass()     {return rMass;}
    public PointF getVelocity() {return prVelocity;}
    public PointF getAccel()    {return prAccel;}
    public int    getColor()    {return nColor;}

    public void setCenter( PointF prC )     {prCenter.set(prC);}
    public void setRadius( float rR )       {rRadius = rR;}
    public void setMass( float rM )         {rMass = rM;}
    public void setVelocity( PointF prV )   {prVelocity.set(prV);}
    public void setAccel( PointF prA )      {prAccel.set(prA);}
    public void setColor( int nC )          {nColor = nC;}

    // Defaults
    MovingBall()
    {
        prCenter    = new PointF( 0, 0 );
        rRadius     = 50;               // these are pixels !?
        rMass       = 0.1F;             // Kg nominally
        prVelocity  = new PointF( 0.2F, 0.2F );
        prAccel     = new PointF( 0, 0 );
        nColor      = Color.RED;
    }
    // Explicit initial values
    MovingBall( PointF prC, float rR, float rM, PointF prV, PointF prA, int nC )
    {
        prCenter    = new PointF( prC.x, prC.y );
        rRadius     = rR;
        rMass       = rM;
        prVelocity  = new PointF( prV.x, prV.y );
        prAccel     = new PointF( prA.x, prA.y );
        nColor      = nC;
    }

    /**
     * Center and velocity are corrected for
     * Milliseconds elapsed
     *
     * @param rMS
     */
    public void stepTime( double rMS )
    {
        prCenter.x += prVelocity.x * rMS;
        prCenter.y += prVelocity.y * rMS;

        prVelocity.x += prAccel.x * rMS;
        prVelocity.y += prAccel.y * rMS;
    }

    /**
     * Increments the center coordinates
     *
     * @param prC increment (x,y)
     */
    public void stepCenter( PointF prC )
    {
        prCenter.x += prC.x;
        prCenter.y += prC.y;
    }

    public void draw( Canvas canvas )
    {
        Paint paint = new Paint();
        paint.setColor( nColor );
        canvas.drawCircle( prCenter.x, prCenter.y, rRadius, paint );
    }

    /**
     * Reflection of the ball against rectangular borders
     * Both position af the center and velocity are corrected
     *
     * @param rTop
     * @param rBot
     * @param rLeft
     * @param rRight
     */
    public void reflect( float rTop, float rBot, float rLeft, float rRight )
    {
        if( prCenter.y <= (rRadius + rTop) )
        {
            prCenter.y = 2 * rRadius + 2 * rTop - prCenter.y;
            prVelocity.y = -prVelocity.y;
        }
        if( prCenter.y >= (rBot - rRadius) )
        {
            prCenter.y = -2 * rRadius + 2 * rBot - prCenter.y;
            prVelocity.y = -prVelocity.y;
        }
        if( prCenter.x <= (rRadius + rLeft) )
        {
            prCenter.x = 2 * rRadius + 2 * rLeft - prCenter.x;
            prVelocity.x = -prVelocity.x;
        }
        if( prCenter.x >= (rRight - rRadius) )
        {
            prCenter.x = -2 * rRadius + 2 * rRight - prCenter.x;
            prVelocity.x = -prVelocity.x;
        }
    }

    // Checks whether ball[nBix] has collided (i.e. overlaps)
    // any of the other balls in the list. If so position and
    // velocity of both is corrected
    public void collide( int nBix, List<MovingBall> balls )
    {
        float rCx = balls.get(nBix).getCenter().x;
        float rCy = balls.get(nBix).getCenter().y;
        float rR  = balls.get(nBix).getRadius();

        for( int i = 0; i < balls.size(); i++ )
        {
            if( i != nBix )
            {
                // if overlap
                if( Math.pow( (balls.get(i).getCenter().x - rCx), 2)
                    + Math.pow( (balls.get(i).getCenter().y - rCy), 2)
                        < (Math.pow( balls.get(i).getRadius() + rR, 2)))
                {
                    undoOverlap( i, nBix, balls );
                    reflect( i, nBix, balls );
                }
            }
        }
    }

    // The two balls touch. The velocity components in the
    // direction of the line joining the centers are interchanged
    // This is correct only for equal masses
    private void reflect( int i, int nBix, List<MovingBall> balls )
    {
        float rC1x = balls.get(nBix).getCenter().x;
        float rC1y = balls.get(nBix).getCenter().y;
        float rC2x = balls.get(i).getCenter().x;
        float rC2y = balls.get(i).getCenter().y;

        float rD  = (float) Math.sqrt( Math.pow( rC2x - rC1x, 2 ) + Math.pow( rC2y - rC1y, 2 ));
        float rDx = (rC2x - rC1x)/rD;
        float rDy = (rC2y - rC1y)/rD;

        PointF prV1 = balls.get(nBix).prVelocity;
        PointF prV2 = balls.get(i).prVelocity;

        float rProj1 = prV1.x * rDx + prV1.y * rDy;
        float rProj2 = prV2.x * rDx + prV2.y * rDy;

        prV1.x = prV1.x - rProj1*rDx + rProj2*rDx;
        prV1.y = prV1.y - rProj1*rDy + rProj2*rDy;
        prV2.x = prV2.x - rProj2*rDx + rProj1*rDx;
        prV2.y = prV2.y - rProj2*rDy + rProj1*rDy;

        balls.get(nBix).setVelocity(prV1);
        balls.get(i).setVelocity(prV2);
    }

    // Two balls overlap (or might overla). Step back
    // to correct so they just touch.
    private void undoOverlap( int i, int nBix, List<MovingBall> balls )
    {
        float rC1x = balls.get(nBix).getCenter().x;
        float rC1y = balls.get(nBix).getCenter().y;
        float rC2x = balls.get(i).getCenter().x;
        float rC2y = balls.get(i).getCenter().y;
        float rR1  = balls.get(nBix).getRadius();
        float rR2  = balls.get(i).getRadius();
        float rDelta, rDeltaX, rDeltaY;
        float rD;
        PointF prDelta1, prDelta2;

        rD = (float) Math.sqrt( Math.pow( rC2x - rC1x, 2 ) + Math.pow( rC2y - rC1y, 2 ));
        rDelta = rR2 + rR1 - rD;

        rDeltaX = rDelta*(rC2x - rC1x)/rD;
        rDeltaY = rDelta*(rC2y - rC1y)/rD;

        prDelta1 = new PointF( -rDeltaX/2, -rDeltaY/2);
        prDelta2 = new PointF( rDeltaX/2, rDeltaY/2 );

        balls.get(nBix).stepCenter(prDelta1);
        balls.get(i).stepCenter(prDelta2);
    }
}

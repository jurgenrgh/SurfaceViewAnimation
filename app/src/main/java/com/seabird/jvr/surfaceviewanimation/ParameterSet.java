package com.seabird.jvr.surfaceviewanimation;

/**
 * Created by jvr on 20.08.2016.
 */
// Data controlling the display: Nbr of balls, velocity
// Radii etc.
// Used synchronously to sent new paameter values to the animation
// thread.
//
public class ParameterSet
{
    public int nNbrBalls;
    public int nVelocity;
    public int nRadius;

    public ParameterSet()  {}

    // Regular Constructor
    public ParameterSet( int nBalls, int nVelocity, int nRadius )
    {
        this.nNbrBalls = nBalls;
        this.nVelocity = nVelocity;
        this.nRadius = nRadius;
    }

    // Copy Constructor
    public ParameterSet( ParameterSet inData )
    {
        this( inData.nNbrBalls,inData.nVelocity,inData.nRadius );
    }

    public int getBalls()       { return nNbrBalls; }
    public int getVelocity()    { return nVelocity; }
    public int getRadius()      { return nRadius; }

    public int setBalls( int newB )
    {
        int nB = nNbrBalls;
        nNbrBalls = newB;
        return nB;
    }
    public int setVelocity( int newV )
    {
        int nV = nVelocity;
        nVelocity = newV;
        return nV;
    }
    public int setRadius( int newR )
    {
        int nR = nRadius;
        nRadius = newR;
        return nR;
    }

    public void SyncUpdate(ParameterSet psOther)
    {
        synchronized( psOther )
        {
            psOther.setBalls( nNbrBalls );
            psOther.setVelocity( nVelocity );
            psOther.setRadius( nRadius );
        }
    }
}

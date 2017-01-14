package com.seabird.jvr.surfaceviewanimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jvr on 27.07.2016.
 */
public class AnimThread extends Thread
{
    public List<MovingBall> balls = new ArrayList<MovingBall>();

    private SurfaceHolder holder;
    private boolean running = true;
    private Point pnC = new Point(100,100);
    private long nStepMs = 30;
    private Point pnDeltaC = new Point(5,5);
    private PointF prV0 = new PointF(0.2f,0.2f);
    private int nBorder = 4;
    public ParameterSet paramSetAnimThread = new ParameterSet(0,0,0);
    public Handler mainHandler;

    public AnimThread(SurfaceHolder holder, Handler handler)
    {
        this.holder = holder;
        mainHandler = handler;
    }

    @Override
    public void run()
    {
        // this is where the animation will occur
        // Send message back to UIthread that we are running
        Message msg = Message.obtain();
        msg.arg1=9;
        msg.arg2=9;
        msg.what = 999;
        mainHandler.sendMessage(msg);

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        long nTimeMs0 = SystemClock.elapsedRealtime();
        long nTimeMsD = 0;

        synchronized(paramSetAnimThread)
        {
            int nB = paramSetAnimThread.getBalls();
            int nR = paramSetAnimThread.getRadius();

            PointF prC = new PointF();
            for( int i = 0; i < nB/2; i++ )
            {
                MovingBall b = new MovingBall();
                prC.set( i * 3*nR, 200 );
                b.setCenter( prC );
                balls.add( b );
            }
            for( int i = 0; i < nB/2; i++ )
            {
                MovingBall b = new MovingBall();
                b.setColor( Color.BLUE );
                prC.set( i * 3*nR, 1000 );
                b.setCenter( prC );
                balls.add( b );
            }
        }

        while(running)
        {
            long nTimeMs1 = SystemClock.elapsedRealtime();

            if( (nTimeMs1 - nTimeMs0) > nStepMs )
            {
                nTimeMsD = nTimeMs1 - nTimeMs0;
                nTimeMs0 = nTimeMs1;

                Canvas canvas = null;
                try
                {
                    canvas = holder.lockCanvas();
                    if( canvas != null )
                    {
                        synchronized( holder )
                        {
                            int nB = paramSetAnimThread.getBalls();
                            int nR = paramSetAnimThread.getRadius();
                            int nV = paramSetAnimThread.getVelocity();

                            for( int i = 0; i < balls.size(); i++ )
                            {
                                balls.get(i).setRadius(nR);
                                PointF pV = balls.get(i).getVelocity();
                                float absV =  (float) Math.sqrt(pV.x*pV.x + pV.y*pV.y);
                                pV.x = 0.1f * pV.x * nV/absV; //Scaled down so the units in the UI mean
                                pV.y = 0.1f * pV.y * nV/absV; //roughly sp/ms
                                balls.get(i).setVelocity( pV );
                            }

                            if(nB < balls.size() )
                            {
                                while(balls.size() > nB)
                                {
                                    int k = balls.size()/2;
                                    balls.remove(k);
                                }
                            }

                            if(nB > balls.size())
                            {
                                MovingBall newBall = new MovingBall();
                                balls.add(newBall);
                            }


                            // draw
                            float w = canvas.getWidth();
                            float h = canvas.getHeight();

                            canvas.drawColor( Color.rgb( 0x5d, 0x94, 0x51 ) );

                            Paint paint = new Paint();
                            paint.setColor( Color.DKGRAY );
                            paint.setStrokeWidth( 4 );
                            canvas.drawLine( 4, 4, w - 4, 4, paint );
                            canvas.drawLine( w - 4, 4, w - 4, h - 4, paint );
                            canvas.drawLine( w - 4, h - 4, 4, h - 4, paint );
                            canvas.drawLine( 4, h - 4, 4, 4, paint );

                            for( int i = 0; i < balls.size(); i++ )
                            {
                                balls.get(i).stepTime( nTimeMsD );
                                balls.get(i).reflect( 0, h, 0, w );
                                balls.get(i).collide( i, balls );
                                balls.get(i).draw( canvas );
                            }
                        }
                    }
                }
                finally
                {
                    if (canvas != null)
                    {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    public void setRunning(boolean b)
    {
        running = b;
    }
}

package com.seabird.jvr.surfaceviewanimation;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jvr on 27.07.2016.
 */
public class AnimView extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder holder;
    public AnimThread animThread;
    private int nWidth, nHeight, nPxFormat;
    private Handler threadHandler;

    public AnimView(Context context)
    {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        threadHandler = ((MainActivity) getContext()).animThreadHandler;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        nWidth  = width;
        nHeight = height;
        nPxFormat = format;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        threadHandler = ((MainActivity) getContext()).animThreadHandler;
        animThread = new AnimThread(holder,threadHandler);
        animThread.setRunning(true);
        animThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        animThread.setRunning(false);
        while (retry)
        {
            try
            {
                animThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public AnimView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public AnimView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        holder = getHolder();
        holder.addCallback(this);
    }
}


package com.seabird.jvr.surfaceviewanimation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Created by jvr on 03.08.2016.
 */
public class Slider extends SeekBar implements OnSeekBarChangeListener
{
    SeekBar     sBar;
    TextView    tvMin, tvMax, tvValue;
    float       rMin, rMax, rValue;

    public Slider( Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // TODO Auto-generated constructor stub
    }

    public Slider( Context context, AttributeSet attrs,
                   int nIdMin, int nIdMax, int nIdValue, int nIdsBar,
                   float rMin, float rMax )
    {
        super(context, attrs);

        tvMin   = (TextView) findViewById( nIdMin );
        tvMax   = (TextView) findViewById( nIdMax );
        tvValue = (TextView) findViewById( nIdValue );

        sBar    = (SeekBar)  findViewById(nIdsBar);

        this.rMin = rMin;
        this.rMax = rMax;
        this.rValue = rMin;

        tvMin.setText( Float.toString(rMin) );
        tvMax.setText( Float.toString(rMax) );
        tvValue.setText( Float.toString(rMin));

        sBar.setOnSeekBarChangeListener( this );
    }
    @Override
    public void onProgressChanged( SeekBar seekBar, int nValue, boolean b )
    {
        rValue = rMin + rMax * nValue / 100.0F;
        tvValue.setText(Float.toString(rValue));
    }

    @Override
    public void onStartTrackingTouch( SeekBar seekBar )
    {
        //Here would be a reaction to touch recognition
    }

    @Override
    public void onStopTrackingTouch( SeekBar seekBar )
    {
        //Undo any visual change
        tvValue.setText(Float.toString(rValue));
    }
}

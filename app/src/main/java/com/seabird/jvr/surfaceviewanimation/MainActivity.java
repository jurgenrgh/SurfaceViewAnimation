package com.seabird.jvr.surfaceviewanimation;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements ParamDialog.OnParametersListener
{
    private static final String KEY_BALLS = "nbrBalls";
    private static final String KEY_VELOCITY = "nVelocity";
    private static final String KEY_RADIUS = "nRadius";
    private static final int NBR_BALLS_DEFAULT = 16;
    private static final int VELOCITY_DEFAULT = 10;
    private static final int RADIUS_DEFAULT = 40;
    private static final String SAVED_PARAMS = "preferences";
    private ParameterSet animThreadParams;

    // This class always holds current parameter values,
    // even after change via DialogFragment
    public ParameterSet paramSet = new ParameterSet();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        
        if( savedInstanceState != null )
        {
            paramSet.setBalls( savedInstanceState.getInt( KEY_BALLS, NBR_BALLS_DEFAULT ) );
            paramSet.setVelocity( savedInstanceState.getInt( KEY_VELOCITY, VELOCITY_DEFAULT ) );
            paramSet.setRadius( savedInstanceState.getInt( KEY_RADIUS, RADIUS_DEFAULT ) );
        }
        else
        {
            paramSet.setBalls( NBR_BALLS_DEFAULT );
            paramSet.setVelocity( VELOCITY_DEFAULT );
            paramSet.setRadius( RADIUS_DEFAULT );
        }

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(SAVED_PARAMS, 0);
        paramSet.setBalls( settings.getInt( KEY_BALLS, NBR_BALLS_DEFAULT ) );
        paramSet.setVelocity( settings.getInt( KEY_VELOCITY, VELOCITY_DEFAULT ) );
        paramSet.setRadius( settings.getInt( KEY_RADIUS, RADIUS_DEFAULT ) );

        setContentView( R.layout.activity_main );

        TextView tv = (TextView) findViewById( R.id.tvBallsValue );
        tv.setText( Integer.toString( paramSet.getBalls() ) );

        tv = (TextView) findViewById( R.id.tvVelocityValue );
        tv.setText( Integer.toString( paramSet.getVelocity() ) );


        tv = (TextView) findViewById( R.id.tvRadiusValue );
        tv.setText( Integer.toString( paramSet.getRadius() ) );

        //Replace action bar by toolbar (auto generated)
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        // Handlers for the increment/decrement buttons at top of page
        // + and - for each of 3 variables
        //
        ImageView ivBallsPlus = (ImageView) findViewById( R.id.ivBallsPlus );
        ImageView ivBallsMinus = (ImageView) findViewById( R.id.ivBallsMinus );
        ImageView ivVelocityPlus = (ImageView) findViewById( R.id.ivVelocityPlus );
        ImageView ivVelocityMinus = (ImageView) findViewById( R.id.ivVelocityMinus );
        ImageView ivRadiusPlus = (ImageView) findViewById( R.id.ivRadiusPlus );
        ImageView ivRadiusMinus = (ImageView) findViewById( R.id.ivRadiusMinus );

        ivBallsPlus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nBalls;
                TextView tvBallsValue = (TextView) findViewById( R.id.tvBallsValue );

                nBalls = Integer.parseInt( tvBallsValue.getText().toString() );
                nBalls += 1;
                tvBallsValue.setText( String.valueOf( nBalls ) );
                paramSet.setBalls(nBalls);
                paramSet.SyncUpdate(animThreadParams);
            }
        } );
        ivBallsMinus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nBalls;
                TextView tvBallsValue = (TextView) findViewById( R.id.tvBallsValue );

                nBalls = Integer.parseInt( tvBallsValue.getText().toString() );
                if( nBalls > 1 ) nBalls -= 1;
                tvBallsValue.setText( String.valueOf( nBalls ) );
                paramSet.setBalls(nBalls);
                paramSet.SyncUpdate(animThreadParams);
            }
        } );
        ivVelocityPlus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nVelocity;
                TextView tvVelocityValue = (TextView) findViewById( R.id.tvVelocityValue );

                nVelocity = Integer.parseInt( tvVelocityValue.getText().toString() );
                nVelocity += 1;
                tvVelocityValue.setText( String.valueOf( nVelocity ) );
                paramSet.setVelocity(nVelocity);
                paramSet.SyncUpdate(animThreadParams);
            }
        } );
        ivVelocityMinus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nVelocity;
                TextView tvVelocityValue = (TextView) findViewById( R.id.tvVelocityValue );

                nVelocity = Integer.parseInt( tvVelocityValue.getText().toString() );
                if(nVelocity > 1) nVelocity -= 1;
                tvVelocityValue.setText( String.valueOf( nVelocity ) );
                paramSet.setVelocity(nVelocity);
                paramSet.SyncUpdate(animThreadParams);
            }
        } );
        ivRadiusPlus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nRadius;
                TextView tvRadiusValue = (TextView) findViewById( R.id.tvRadiusValue );

                nRadius = Integer.parseInt( tvRadiusValue.getText().toString() );
                nRadius += 1;
                tvRadiusValue.setText( String.valueOf( nRadius ) );
                paramSet.setRadius(nRadius);
                paramSet.SyncUpdate(animThreadParams);
            }
        } );
        ivRadiusMinus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nRadius;
                TextView tvRadiusValue = (TextView) findViewById( R.id.tvRadiusValue );

                nRadius = Integer.parseInt( tvRadiusValue.getText().toString() );
                if(nRadius > 1) nRadius -= 1;
                tvRadiusValue.setText( String.valueOf( nRadius ) );
                paramSet.setRadius(nRadius);
                paramSet.SyncUpdate(animThreadParams);
            }
        } );
    }

    // Define the Handler that receives messages from the thread
    public final Handler animThreadHandler = new Handler()
    {
        // This is the callback, the entry point when a new msg arrives
        public void handleMessage(Message msg)
        {
            // The what item is an arbitrary identifier invented by the sender
            if ( msg.what == 999 )
            {
                animThreadParams = ((AnimView) findViewById( R.id.animview )).animThread.paramSetAnimThread;
                paramSet.SyncUpdate( animThreadParams );
            }
        }
    };



    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Prepare and call the dialog for parameter change
        // Current values are read from the display
        if( id == R.id.action_settings )
        {
            int nBalls;
            TextView tvBallsValue = (TextView) findViewById( R.id.tvBallsValue );
            nBalls = Integer.parseInt( tvBallsValue.getText().toString() );

            int nVelocity;
            TextView tvVelocityValue = (TextView) findViewById( R.id.tvVelocityValue );
            nVelocity = Integer.parseInt( tvVelocityValue.getText().toString() );

            int nRadius;
            TextView tvRadiusValue = (TextView) findViewById( R.id.tvRadiusValue );
            nRadius = Integer.parseInt( tvRadiusValue.getText().toString() );

            // This is all there is to the call of the dialog
            // The parameters are custom
            ParamDialog myDiag = ParamDialog.newInstance( nBalls, nVelocity, nRadius );
            myDiag.show( getSupportFragmentManager(), "Params" );
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        // Save parameter values using preferences
        // SavedInstance doesn't do it in all cases
        // Editor object needed to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(SAVED_PARAMS, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(KEY_BALLS, paramSet.getBalls());
        editor.putInt(KEY_VELOCITY, paramSet.getVelocity());
        editor.putInt(KEY_RADIUS, paramSet.getRadius());

        // Commit the edits!
        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save the user's current state

        savedInstanceState.putInt(KEY_BALLS, paramSet.getBalls());
        savedInstanceState.putInt(KEY_VELOCITY, paramSet.getVelocity());
        savedInstanceState.putInt(KEY_RADIUS, paramSet.getRadius());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    // Callback from ParamDialog. The parameter values may have changed
    // So change displayed values and store in paramSet
    public void onDoneParams( int nNbrBalls, int nVelocity, int nRadius )
    {
        TextView tv = (TextView) findViewById( R.id.tvBallsValue );
        tv.setText( Integer.toString( nNbrBalls ) );
        paramSet.setBalls(nNbrBalls);

        tv = (TextView) findViewById( R.id.tvVelocityValue );
        tv.setText( Integer.toString( nVelocity ) );
        paramSet.setVelocity(nVelocity);

        tv = (TextView) findViewById( R.id.tvRadiusValue );
        tv.setText( Integer.toString( nRadius ) );
        paramSet.setRadius(nRadius);

        //Transfer Changes to worker thread
        paramSet.SyncUpdate(animThreadParams);
    }
}

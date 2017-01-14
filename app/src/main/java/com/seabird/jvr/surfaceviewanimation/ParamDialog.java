package com.seabird.jvr.surfaceviewanimation;



import android.app.Activity;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jvr on 18.08.2016.
 */
//
// The UI objects are created in xml file
// parameters_dialogfragment.
// The XML file is simply be inflated
// in the onCreateView method.
//
public class ParamDialog extends DialogFragment
{
    // ID's for arguments in Bundle
    private static final String ARG_nbrBalls = "nbrBalls";
    private static final String ARG_velocity = "velocity";
    private static final String ARG_radius = "radius";

    // Parameter values local
    private int nNbrBalls;
    private int nVelocity;
    private int nRadius;

    private OnParametersListener mListener;

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nNbrBalls Parameter 1.
     * @param nVelocity Parameter 2.
     * @param nRadius   Parameter 3.
     * @return A new instance of fragment ParamDialog.
     */

    // Called from Main when selected by menu
    // In the real case these values have to be obtained from
    // the worker thread.
    //
    public static ParamDialog newInstance( int nNbrBalls, int nVelocity, int nRadius )
    {
        ParamDialog paramdialog = new ParamDialog();
        Bundle args = new Bundle();
        args.putInt( ARG_nbrBalls, nNbrBalls );
        args.putInt( ARG_velocity, nVelocity );
        args.putInt( ARG_radius, nRadius );
        paramdialog.setArguments( args );
        return paramdialog;
    }

    public ParamDialog()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        if( getArguments() != null )
        {
            nNbrBalls = getArguments().getInt( ARG_nbrBalls );
            nVelocity = getArguments().getInt( ARG_velocity );
            nRadius = getArguments().getInt( ARG_radius );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState )
    {
        final View parent = inflater.inflate( R.layout.parameter_dialog, container );

        // Watch for done button clicks.
        Button okButton = (Button) parent.findViewById( R.id.okButton );
        okButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                getNewParams();
                if( mListener != null )
                {
                    mListener.onDoneParams( nNbrBalls, nVelocity, nRadius );
                }
                dismiss();
            }
        } );

        // Watch for cancel button clicks.
        Button cancelButton = (Button) parent.findViewById( R.id.cancelButton );
        cancelButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                dismiss();
            }
        } );

        ImageView ivBallsPlus = (ImageView) parent.findViewById( R.id.ivPlusBalls );
        ImageView ivBallsMinus = (ImageView) parent.findViewById( R.id.ivMinusBalls );
        ImageView ivVelocityPlus = (ImageView) parent.findViewById( R.id.ivPlusVelocity );
        ImageView ivVelocityMinus = (ImageView) parent.findViewById( R.id.ivMinusVelocity );
        ImageView ivRadiusPlus = (ImageView) parent.findViewById( R.id.ivPlusRadius );
        ImageView ivRadiusMinus = (ImageView) parent.findViewById( R.id.ivMinusRadius );

        ivBallsPlus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nBalls;
                TextView tvBallsValue = (TextView) parent.findViewById( R.id.tvBallsNbr );

                nBalls = Integer.parseInt( tvBallsValue.getText().toString() );
                nBalls += 1;
                tvBallsValue.setText( String.valueOf( nBalls ) );
            }
        } );
        ivBallsMinus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nBalls;
                TextView tvBallsValue = (TextView) parent.findViewById( R.id.tvBallsNbr );

                nBalls = Integer.parseInt( tvBallsValue.getText().toString() );
                if(nBalls > 1) nBalls -= 1;
                tvBallsValue.setText( String.valueOf( nBalls ) );
            }
        } );
        ivVelocityPlus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nVelocity;
                TextView tvVelocityValue = (TextView) parent.findViewById( R.id.tvVelocityVal );

                nVelocity = Integer.parseInt( tvVelocityValue.getText().toString() );
                nVelocity += 1;
                tvVelocityValue.setText( String.valueOf( nVelocity ) );
            }
        } );
        ivVelocityMinus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nVelocity;
                TextView tvVelocityValue = (TextView) parent.findViewById( R.id.tvVelocityVal );

                nVelocity = Integer.parseInt( tvVelocityValue.getText().toString() );
                if(nVelocity > 1) nVelocity -= 1;
                tvVelocityValue.setText( String.valueOf( nVelocity ) );
            }
        } );
        ivRadiusPlus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nRadius;
                TextView tvRadiusValue = (TextView) parent.findViewById( R.id.tvRadiusVal );

                nRadius = Integer.parseInt( tvRadiusValue.getText().toString() );
                nRadius += 1;
                tvRadiusValue.setText( String.valueOf( nRadius ) );
            }
        } );
        ivRadiusMinus.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                int nRadius;
                TextView tvRadiusValue = (TextView) parent.findViewById( R.id.tvRadiusVal );

                nRadius = Integer.parseInt( tvRadiusValue.getText().toString() );
                if(nRadius > 1) nRadius -= 1;
                tvRadiusValue.setText( String.valueOf( nRadius ) );
            }
        } );

        return parent;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        setDialogParams( nNbrBalls, nVelocity, nRadius );

        Window win = getDialog().getWindow();
        Point size = new Point();

        Display display = win.getWindowManager().getDefaultDisplay();
        display.getSize( size );

        int w = size.x;
        int h = size.y;

        // win.setLayout( (int) (w * 0.75), (int) (h * 0.50));

        win.setGravity( Gravity.CENTER );
    }

    private void getNewParams()
    {
        TextView tv = (TextView) getView().findViewById( R.id.tvBallsNbr );
        nNbrBalls = Integer.parseInt( tv.getText().toString() );
        tv = (TextView) getView().findViewById( R.id.tvVelocityVal );
        nVelocity = Integer.parseInt( tv.getText().toString() );
        tv = (TextView) getView().findViewById( R.id.tvRadiusVal );
        nRadius = Integer.parseInt( tv.getText().toString() );
    }

    private void setDialogParams( int nB, int nV, int nR )
    {
        TextView tv = (TextView) getView().findViewById( R.id.tvBallsNbr );
        tv.setText(Integer.toString( nNbrBalls ));
        tv = (TextView) getView().findViewById( R.id.tvVelocityVal );
        tv.setText(Integer.toString( nVelocity ));
        tv = (TextView) getView().findViewById( R.id.tvRadiusVal );
        tv.setText(Integer.toString( nRadius ));
    }

    @Override
      public void onAttach( Context context )
//    public void onAttach(Activity activity)
    {
        super.onAttach( context );
        try
        {
            mListener = (OnParametersListener) context;
        }
        catch( ClassCastException e )
        {
            throw new ClassCastException( context.toString()
                                          + " must implement OnParametersListener" );
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState( Bundle outState )
    {
        super.onSaveInstanceState( outState );
        getNewParams();
        outState.putInt( "NbrBalls", nNbrBalls );
        outState.putInt( "Velocity", nVelocity );
        outState.putInt( "Radius", nRadius );
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnParametersListener
    {
        public void onDoneParams( int nNbrBalls, int nVelocity, int nRadius );
    }
}



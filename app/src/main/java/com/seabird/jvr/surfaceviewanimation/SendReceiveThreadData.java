package com.seabird.jvr.surfaceviewanimation;

import java.util.ArrayList;

/**
 * Created by jvr on 13.08.2016.
 */
public class SendReceiveThreadData
{
    public static boolean bWR  = false; //WriteRequest
    public static boolean bRR  = false; //RReadRequest
    public static boolean bWF  = false; //WriteFlag
    public static boolean bRF  = false; //ReadFlag
    public static boolean bDR  = false; //DataReady

    public static ArrayList<Float> rArrData = new ArrayList<Float>( );

    public String strSenderName = "";
    public String strReceiverName = "";

    public SendReceiveThreadData( String strSenderName, String strReceiverName )
    {
        this.strSenderName = strSenderName;
        this.strReceiverName = strReceiverName;
    }

    public boolean write( ArrayList<Float> rArrInData )
    {
        boolean bOK = false;

        if( bRR || bRF )
        {
            bWR = false;
            bWF = false;
            bOK = false;
            return bOK;
        }
        else if( !bWR )
        {
            bWR = true;
            bWF = false;
            bDR = false;
            bOK = false;
            return bOK;
        }
        else
        {
            bWF = true;
            rArrData.clear();
            rArrData.addAll( rArrInData );
            bDR = true;
            bWF = false;
            bWR = false;
            bOK = true;
            return bOK;
        }
    }

    public boolean read( ArrayList<Float> rArrOutData )
    {
        boolean bOK = false;

        if( bWR || bWF )
        {
            bRR = false;
            bRF = false;
            bOK = false;
            return bOK;
        }
        else if( !bRR )
        {
            bRR = true;
            bRF = false;
            bDR = false;
            bOK = false;
            return bOK;
        }
        else
        {
            bRF = true;
            rArrOutData.clear();
            rArrOutData.addAll( rArrData );
            bDR = false;
            bRF = false;
            bRR = false;
            bOK = true;
            return bOK;
        }
    }
}

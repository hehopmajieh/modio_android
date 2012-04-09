package axon.test.testJNI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestJNIActivity extends Activity {
    private static final String TAG = "I2C-Test";
    axonI2C i2c;
    int slaveAddr = 0x6C;
    int channel = 0;
    TextView myTextField;
	int bits;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        myTextField = (TextView)findViewById(R.id.myTextField);

        myTextField.setText("0");
        
        i2c = new axonI2C();

        new Thread() {
        	@Override
        	public void run() {
        		while(true)
        		{
					bits = ReadADC(slaveAddr, channel);
			        
					TestJNIActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							String temp = Integer.toString(bits);
								
							myTextField.setText(temp);
						}
					});
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        }.start();
    }
    
    int ReadADC(int slaveAddr, int channel)
    {
        int fileHandle = 0;
        int Bits = 0;
        int[] buf = new int[4];

        try
        {
        	fileHandle = i2c.open("/dev/i2c-0");
        }
        catch(Exception e)
        {
        	Log.w(TAG, "Could not open I2C interface");
        }
        i2c.read(fileHandle, slaveAddr, buf, 3);

    	if((buf[2] & 0x80) == 0)			// Not doing a conversion?
    	{
    		Bits = ((int) buf[0] << 8) + buf[1];
    		//
    		// Start a conversion going
    		//
    		buf[0] = 0x88 | channel << 5;
    		
    		i2c.write(fileHandle, slaveAddr, 0, buf, 1);    	
    	}
        i2c.close(fileHandle);
		return Bits;
    }
}

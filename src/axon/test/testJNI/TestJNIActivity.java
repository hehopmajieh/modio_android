package axon.test.testJNI;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TestJNIActivity extends Activity {
    private static final String TAG = "I2C-Test";
    axonI2C i2c;
    int slaveAddr=0x58;
    int relay = 0;
    int[] state = new int[4];
	int bits;
	double adc1,adc2,adc3,adc4;
	TextView DPort1,DPort2,DPort3,DPort4,APort1,APort2,APort3,APort4;
	EditText AddrInput;
	private Spinner spinner1;
	/** Called when the activity is first created. */
   
	public void addItemsOnSpinner() {
		 
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("0x58");
		list.add("0x59");
		list.add("0x5A");
		list.add("0x5B");
		list.add("0x5C");
		list.add("0x5D");
		list.add("0x5E");
		list.add("0x5F");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
	  }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        i2c = new axonI2C();
        state[0] = 0;
		state[1] = 0;
		state[2] = 0;
		state[3] = 0; //save restore/state	
        //toDo : States @!
		addItemsOnSpinner();
		
		new Thread() {
        	@Override
        	public void run() {
        		while(true)
        		{
					
        			
					TestJNIActivity.this.runOnUiThread(new Runnable() {
						public void run() {
						
							DPort1 = (TextView)findViewById(R.id.textView2); //Analog Port Labels
							AddrInput = (EditText)findViewById(R.id.editText1);
							
							DPort2 = (TextView)findViewById(R.id.textView3);
							DPort3 = (TextView)findViewById(R.id.textView4);
							DPort4 = (TextView)findViewById(R.id.textView5);
							//Analog Port Labels
							APort1 = (TextView)findViewById(R.id.textView7);
							APort2 = (TextView)findViewById(R.id.textView8);
							APort3 = (TextView)findViewById(R.id.textView9);
							APort4 = (TextView)findViewById(R.id.textView10);
							
							Log.i(TAG, String.valueOf(spinner1.getSelectedItem()));
							slaveAddr = Integer.decode(String.valueOf(spinner1.getSelectedItem()));
							String[] hin1 = { "su", "-c","chmod 777 /dev/i2c-2" };  //gain root access
						        try {
						                Runtime.getRuntime().exec(hin1);
						            } catch (IOException e) {
						        // TODO Auto-generated catch block
						        e.printStackTrace();
						      }
						        ;
						       
						        final Button button = (Button) findViewById(R.id.button1);
					        button.setOnClickListener(new View.OnClickListener() {
					        	
					 		 
					        	public void onClick(View v) {
					            
					        		
					            	WriteI2C(slaveAddr, 1); //ToDo: check port 
					           
					            }
					        });		
					        final Button button2 = (Button) findViewById(R.id.button2);
					        button2.setOnClickListener(new View.OnClickListener() {
					            public void onClick(View v) {
					            
					            	WriteI2C(slaveAddr, 2);
					            
					           
					           
					            }
					        });	
					        final Button button3 = (Button) findViewById(R.id.button3);
					        button3.setOnClickListener(new View.OnClickListener() {
					            public void onClick(View v) {
					            
					            	WriteI2C(slaveAddr, 4);
					            
					           
					           
					            }
					        });
					        final Button button4 = (Button) findViewById(R.id.button4);
					        button4.setOnClickListener(new View.OnClickListener() {
					            public void onClick(View v) {
					            
					            	WriteI2C(slaveAddr, 8);
					            
					           
					           
					            }
					        });							       
					        final Button button6 = (Button) findViewById(R.id.button6);
					        button6.setOnClickListener(new View.OnClickListener() {
					            public void onClick(View v) {
					            
					            	//Log.i(TAG,)
					            DPort1.setText(AddrInput.getText().toString());
					           
					           
					            }
					        });			      
					        
					        
					        switch   (ReadI2CDigital(slaveAddr,0x20)) {
					        case 1 : //DPort1.setText("Digital Port1: High"); 
					        	break;
					        case 2 : DPort2.setText("Digital Port2: High"); break;
					        case 4 : DPort3.setText("Digital Port3: High"); break;
					        case 8 : DPort4.setText("Digital Port4: High"); break;
					        default: //DPort1.setText("Digital Port1: Low"); 
					        		 DPort2.setText("Digital Port2: Low");
					        		 DPort3.setText("Digital Port3: Low");
					        		 DPort4.setText("Digital Port4: Low"); break;
					        } 
					        adc1 = (3.3 / 1024)*(ReadI2CAnalog(slaveAddr,0x30));
					        adc2 = (3.3 / 1024)*(ReadI2CAnalog(slaveAddr,0x31));
					        adc3 = (3.3 / 1024)*(ReadI2CAnalog(slaveAddr,0x32));
					        adc4 = (3.3 / 1024)*(ReadI2CAnalog(slaveAddr,0x33));
					        //APort1.setText("ADC1:"+  Integer.toString(ReadI2CAnalog(slaveAddr,0x30)));
					        APort1.setText("AN1:"+  Double.toString(adc1) + "Volts");
					        APort2.setText("AN2:"+  Double.toString(adc2) + "Volts");
					        APort3.setText("AN3:"+  Double.toString(adc3) + "Volts");
					        APort4.setText("AN4:"+  Double.toString(adc4) + "Volts");
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
    
    void WriteI2C(int slaveAddr, int relay)
    {
    	 
		 
    	int fileHandle = 0;
        int[] buf = new int[4];
        
        try
        {
        	
        	fileHandle = i2c.open("/dev/i2c-2");
        }
        catch(Exception e)
        {
        	Log.w(TAG, "Could not open I2C interface");
        }
    	
    		buf[0] = 0x10; //set reg
    		buf[1] = relay;	 //set out
   
    		i2c.write(fileHandle, slaveAddr, 0, buf, 2);    	
    
        i2c.close(fileHandle);
		
        //return Bits;
    }
    int ReadI2CDigital(int slaveAddr, int register)
    {
        int fileHandle = 0;
        int[] buf = new int[4];
        
        try
        {
        	fileHandle = i2c.open("/dev/i2c-2");
        }
        catch(Exception e)
        {
        	Log.w(TAG, "Could not open I2C interface");
        }
    		buf[0] = register;
    		i2c.write(fileHandle, slaveAddr, 0, buf, 1);    	
        

    		i2c.read(fileHandle, slaveAddr, buf, 4);
    		Log.w(TAG,
                    "buf0= " + Integer.toHexString(buf[0]) + " buf1= "
                            + Integer.toHexString(buf[1]) + " buf2= "
                            + Integer.toHexString(buf[2]) + " buf=3 "
                            + Integer.toHexString(buf[3]));
 
    		i2c.close(fileHandle);
			return buf[0];
    }  
    int ReadI2CAnalog(int slaveAddr, int register)
    {
        int fileHandle = 0;
        int[] buf = new int[4];
        
        try
        {
        	fileHandle = i2c.open("/dev/i2c-2");
        }
        catch(Exception e)
        {
        	Log.w(TAG, "Could not open I2C interface");
        }
    		buf[0] = register;
    		
    		i2c.write(fileHandle, slaveAddr, 0, buf, 1);    	
        

    		i2c.read(fileHandle, slaveAddr, buf, 4);
    		Log.w(TAG,
                    "buf0= " + Integer.toHexString(buf[0]) + " buf1= "
                             + Integer.toHexString(buf[1]) + " buf2= "
                             + Integer.toHexString(buf[2]) + " buf=3 "
                             + Integer.toHexString(buf[3]));
 
    		i2c.close(fileHandle);
			return buf[0];
    }  

    void ChangeSlaveAddr(int CurrentAddr, int NewAddr)
    {
        int fileHandle = 0;
        int[] buf = new int[4];
        
        try
        {
        	fileHandle = i2c.open("/dev/i2c-2");
        }
        catch(Exception e)
        {
        	Log.w(TAG, "Could not open I2C interface");
        }
    		buf[0] = 0xF0;
    		buf[1] = NewAddr;
    		i2c.write(fileHandle, CurrentAddr, 0, buf, 2);    	
        

    		
    		/*Log.w(TAG,
                    "buf0= " + Integer.toHexString(buf[0]) + " buf1= "
                             + Integer.toHexString(buf[1]) + " buf2= "
                             + Integer.toHexString(buf[2]) + " buf=3 "
                             + Integer.toHexString(buf[3])); */
 
    		i2c.close(fileHandle);
    }  
}

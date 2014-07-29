package com.example.networking;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String DEBUG_TAG = "HttpExample";
	private static final String ff_TAG = "kk";
	private static final String f_TAG = "secondissssyffhg";
	ProgressDialog mProgressDialog;
	 XmlPullParserFactory xmlfc;
	 XmlPullParser parser;
	 ProgressBar prbar;
	 
    
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0; 
	TextView text;
	public String url1="http://api.worldweatheronline.com/free/v1/weather.ashx?q=";
	public String url2="&format=xml&num_of_days=3&key=bdne422qsustfnjh7rhxmxkj";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prbar=(ProgressBar)findViewById(R.id.progressBar1);
		Button button=(Button)findViewById(R.id.button1);
		prbar.setVisibility(View.INVISIBLE);
		button.setOnClickListener(listener);
		
		
	}
    private OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View arg0) {
		      ConnectivityManager connMgr = (ConnectivityManager) 
			        getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			    if (networkInfo != null && networkInfo.isConnected()) {
			    	TextView txt=(TextView)findViewById(R.id.textView1);
			    	txt.setText("Getting informaton...Please wait...");
			    	EditText location=(EditText)findViewById(R.id.editText1);
			    	String loc=location.getText().toString();
			    	String finalurl=url1+loc+url2;
			    	prbar.setVisibility(View.VISIBLE);
				        
			    	new DownloadWebpageTask().execute(finalurl);
			    	
				      
			    } else {
			        // display error
			    }
		}
    	
    };


    private class DownloadWebpageTask extends AsyncTask<String,Integer,String[]>{
    	
    	
    	protected void onPreExecute()
    	{
    		super.onPreExecute();

    	}
    	
		@Override
		protected String[] doInBackground(String... urls) {
			try {
				 
				 
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	


		private String[] downloadUrl(String myurl) throws IOException {
			
			// TODO Auto-generated method stub
			InputStream is = null;
			String[] xml;
		    // Only display the first 500 characters of the retrieved
		    // web page content.
		    
		  
		    try {
		        URL url = new URL(myurl);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setReadTimeout(10000 /* milliseconds */);
		        conn.setConnectTimeout(15000 /* milliseconds */);
		        conn.setRequestMethod("GET");
		        conn.setDoInput(true);
		        // Starts the query
		        int lengthoffile = conn.getContentLength();
		        int response = conn.getResponseCode();
		        is=conn.getInputStream();
		        
		       
		        
		        if(is==null)
		        {
		        	/*text=(TextView)findViewById(R.id.textView2);
		        text.setText("WEb page is not loaded");*/
	            String message="web pge not loaded";
	           // return message;
		        }
		        try {
					xmlfc=XmlPullParserFactory.newInstance();
					parser=xmlfc.newPullParser();
					parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
					        , false);
					parser.setInput(is,null);
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       
		         xml=parsexml(parser);
		      
		      
		      
		       is.close();
		        // Convert the InputStream into a string
		        if(xml!=null)
		        return xml;
		       
		    
		    } finally {
		        if (is != null) {
		            is.close();
		        } 
		    }
			return xml;
			
		
			
		}
		
 private String[] parsexml(XmlPullParser parser) {
			// TODO Auto-generated method stub
	     
	        int i=0;
	        
	        
			String[] s = new String[7];
			
	
			
			int event;
		      String text=null;
		      try {
		         event = parser.getEventType();
		         while (event != XmlPullParser.END_DOCUMENT) {
		            String name=parser.getName();
		            switch (event){
		               case XmlPullParser.START_TAG:
		            	  
		               break;
		               case XmlPullParser.TEXT:
		               text = parser.getText();
		               break;
		              
		               case XmlPullParser.END_TAG:
		              
		            	   if(name.equalsIgnoreCase("temp_c"))
		            	   {
		            		   s[i]=text;
		            		   i++;
		            	   }
		            	   if(name.equalsIgnoreCase("tempmaxc"))
		            	   {
		            		   s[i]=text;
		            		   i++;
		            	   }
		            	   if(name.equalsIgnoreCase("tempminc"))
		            	   {
		            		   s[i]=text;
		            		   i++;
		            	   }
		                   Log.d(DEBUG_TAG, "the value is"+s[0]+" "+s[1]+" "+s[2]);
		                	  break;
	     		          }		 
		                  event = parser.next(); 

		              }
		         
		      }
		         catch (Exception e) {
		             e.printStackTrace();
		         }  
   return s;		
 }

		 protected void onProgressUpdate(Integer... values)
  {
			 ProgressBar prbar2=(ProgressBar)findViewById(R.id.progressBar2);
	  prbar2.setProgress(values[0]);
  }
		 protected void onPostExecute(String[] result) {
			 TextView message=(TextView)findViewById(R.id.textView1);
			 message.setText("");
			 
			 prbar.setVisibility(View.INVISIBLE);
			 TextView text1=(TextView)findViewById(R.id.textView2);
			 TextView text2=(TextView)findViewById(R.id.textView6);
			 TextView text3=(TextView)findViewById(R.id.textView8);
			
			 if(result!=null)
			 {
				 Calendar cl=Calendar.getInstance();
			        int day=cl.get(Calendar.DAY_OF_WEEK);
			    int i=0;
			    String s[]=new String[2];
					for(i=0;i<2;i++)
					{
						if(day==1)
							s[i]="SUNDAY";
						if(day==2)
							s[i]="MONDAY";
						if(day==3)
							s[i]="TUESDAY";
							if(day==4)
								s[i]="WEDNESDAY";
							if(day==5)
								s[i]="THURSDAY";
							if(day==6)
								s[i]="FRIDAY";
							if(day==7)
								s[i]="SATURDAY";
						    cl.add(Calendar.DATE, 1);
						    day=cl.get(Calendar.DAY_OF_WEEK);
								
					}
					 TextView text4=(TextView)findViewById(R.id.textView4);
					 TextView text5=(TextView)findViewById(R.id.textView5);
					 TextView text6=(TextView)findViewById(R.id.textView7);

					 TextView text9=(TextView)findViewById(R.id.textView9);
					 TextView text11=(TextView)findViewById(R.id.textView11);
					 TextView text12=(TextView)findViewById(R.id.textView12);
					 TextView text10=(TextView)findViewById(R.id.textView10);
					 text4.setText("Today");
						text5.setText(s[0]);
						text6.setText(s[1]);
						text9.setText("Current Conditions");
						text10.setText("Forecast");
			text1.setText(result[0]+(char) 0x00B0);
			text2.setText("Max "+result[3]+" "+(char) 0x00B0);
			text3.setText("Max "+result[5]+" "+(char) 0x00B0);
			text11.setText("Min "+result[4]+" "+(char) 0x00B0);
			text12.setText("Min"+result[6]+" "+(char) 0x00B0);
			 }
			 else
				 text.setText("notfound");
			 //mProgressDialog.dismiss();
		/*	 ImageView imageView = (ImageView) findViewById(R.id.imageView1);
			 imageView.setImageBitmap(result);*/
	       }
		/*public Bitmap readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
			Bitmap bitmap = BitmapFactory.decodeStream(stream);
			
			return bitmap;
		}*/
    	
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

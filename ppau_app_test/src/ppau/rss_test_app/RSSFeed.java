package ppau.rss_test_app;

import java.io.IOException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class RSSFeed extends Activity
{
	LinearLayout mainLayout;
	String rssResult = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
		
		try
		{
			URL rssUrl = new URL("http://pirateparty.org.au/feed/");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			
			RSSHandler rssHandler = new RSSHandler();
			xmlReader.setContentHandler(rssHandler);
			
			InputSource inputSource = new InputSource(rssUrl.openStream());
			
			xmlReader.parse(inputSource);
			
			TextView newText = new TextView(this);
			newText.setText(rssResult);
			mainLayout.addView(newText);
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	int itemType = 0;
	boolean item = false;
	private class RSSHandler extends DefaultHandler 
	{
		public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException
        {
            if(localName.equals("item"))
            {
                item = true;
            }
            
            if(item)
            {
	        	if(localName.equals("title"))
	        	{
	        		itemType = 1;
	        		//rssResult = rssResult + localName + "\n";
	        	}
	        	else if(localName.equals("description"))
	        	{
	        		itemType = 2;
	        		//rssResult = rssResult + localName + "\n";
	        	}
	        	else
	        	{
	        		itemType = 0;
	        	}
            }
        }
		
		@Override
		public void characters(char[] ch, int start, int length)
		{
			String cdata = new String(ch, start, length);
		    if(itemType == 1)
		    {
		    	//title
		    	rssResult = rssResult + (cdata.trim()).replaceAll("\\s+", " ") + "\n\n";
		    }
		    else if(itemType == 2)
		    {
		    	//item text
		    	rssResult = rssResult + (cdata.trim()).replaceAll("\\s+", " ") + "\n";
		    }
		}
	}
}

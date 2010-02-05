import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import org.antlr.stringtemplate.StringTemplate;
//import org.antlr.stringtemplate.StringTemplateGroup;
//import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;

import target.TargetClass;
import target.TargetClassTop;

/**
 * 
 */

/**
 * @author qiying.wang
 *
 */
public class T {

	public static class Element
	{
		public String name = "ele";
	}
	
	public static class TList
	{
		public String name = "list";
		public List<Element> list = new LinkedList<Element>();
	}

	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//String value = "SID=EXPIRED;Domain=.google.com;Path=/;Expires=Mon, 01-Jan-1990 00:00:00 GMT, HSID=EXPIRED;Domain=.google.com;Path=/;Expires=Mon, 01-Jan-1990 00:00:00 GMT, SSID=EXPIRED;Domain=.google.com;Path=/;Expires=Mon, 01-Jan-1990 00:00:00 GMT;Secure, LSID=EXPIRED;Domain=www.google.com;Path=/accounts;Expires=Mon, 01-Jan-1990 00:00:00 GMT, LSID=EXPIRED;Path=/accounts;Expires=Mon, 01-Jan-1990 00:00:00 GMT, LSID=EXPIRED;Domain=www.google.com;Path=/accounts;Expires=Mon, 01-Jan-1990 00:00:00 GMT, LSID=EXPIRED;Path=/accounts;Expires=Mon, 01-Jan-1990 00:00:00 GMT, GAUSR=EXPIRED;Path=/accounts;Expires=Mon, 01-Jan-1990 00:00:00 GMT, GAUSR=EXPIRED;Path=/accounts;Expires=Mon, 01-Jan-1990 00:00:00 GMT";
		String value = "N_T=sess%3D12c7936ba27cf481%26v%3D2%26c%3D6aa3723d%26s%3D4b6831ce%26t%3DA%3A1%3A32050%26sessref%3D%2523code; Expires=Tue, 02-Feb-2010 14:38:15 GMT; Path=/support; HttpOnly";
		LinkedList<String> save = new LinkedList<String>();
		String[] headerValues = value.split(",");
		for(String v:headerValues)
		{
			if(v.indexOf("=") == -1
					|| (v.indexOf("=") > v.indexOf(";")))
			{
				save.add(save.removeLast() + "," + v);
			}
			else
			{
				save.add(v);
			}
		}
		for(String v:save)
		{
			System.out.println("####" + v);
		}
		
	}

}

import java.io.FileNotFoundException;
import java.io.FileReader;
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
		//StringTemplateGroup stg = new StringTemplateGroup();
//		String template = "$aMap.keys:{k| $k$ maps to $aMap.(k)$}$.";
//		String template2 = "int sum = 0;$numbers:{ n | sum += $n$;}$";
//
//		StringTemplate st = new StringTemplate(template);
//		Map m = new HashMap();
//		m.put(1, "hello");
//		m.put(2, "world!");
//		st.setAttribute("aMap",m);
//		
//		List list = new LinkedList();
//		list.add(1);
//		list.add(102);
//		st = new StringTemplate(template2);
//		st.setAttribute("numbers",list);
//		System.out.println(st);
//		
//		StringTemplateGroup group =
//            new StringTemplateGroup(new FileReader("template/test.stg"),
//                AngleBracketTemplateLexer.class);
//		TList tlist = new TList();
//		tlist.list.add(new Element());
//		StringTemplate st3 = group.getInstanceOf("test");
//		st3.setAttribute("value", tlist);
//		System.out.println(st3.toString());
	}

}

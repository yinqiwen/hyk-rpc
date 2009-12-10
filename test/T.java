import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

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

	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//StringTemplateGroup stg = new StringTemplateGroup();
		String template = "$aMap.keys:{k| $k$ maps to $aMap.(k)$}$.";
		String template2 = "int sum = 0;$numbers:{ n | sum += $n$;}$";

		StringTemplate st = new StringTemplate(template);
		Map m = new HashMap();
		m.put(1, "hello");
		m.put(2, "world!");
		st.setAttribute("aMap",m);
		
		List list = new LinkedList();
		list.add(1);
		list.add(102);
		st = new StringTemplate(template2);
		st.setAttribute("numbers",list);
		System.out.println(st);
		
		String template3 = "$if(obj.hasType())$ waht a fuck!$endif$";
		//TargetClassTop stl = new TargetClassTop();
		TargetClass target = new TargetClass();
		//target.setName("ghjk");
		//stl.setTarget(target);
		StringTemplate st2 = new StringTemplate(template3);
		st2.setAttribute("obj", target);
		System.out.println(st2);
		System.out.println(T.class.getPackage());
	}

}

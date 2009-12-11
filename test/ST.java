import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.antlr.stringtemplate.PropertyRetriver;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;

import target.TargetClass;
import target.TargetClassTop;

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.hyk.protobuf.mapping.util.ClassAnalyzer;
import com.hyk.protobuf.mapping.util.ObjectRender;

/**
 * 
 */

/**
 * @author qiying.wang
 *
 */
public class ST {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		StringTemplateGroup group =
            new StringTemplateGroup(new FileReader("template/protobufgen.stg"),
                AngleBracketTemplateLexer.class);
		
		StringTemplate st = group.getInstanceOf("file");
		
		
		ClassAnalyzer analyzer = new ClassAnalyzer();
//		List<MessageInfo> infos = analyzer.analyze(TargetClassTop.class);
//
//		st.setAttribute("messages", infos);
//		System.out.println(st);
		
		List<FileDescriptorProto.Builder> buffer = new LinkedList<FileDescriptorProto.Builder>();
		analyzer.parse(TargetClassTop.class, buffer);
		st.setAttribute("desc", buffer.get(0));
		st.registerRenderer(Type.class, new ObjectRender());
		st.registerRenderer(Label.class, new ObjectRender());
		st.registerPropertyRetriever(FieldDescriptorProto.class, new PropertyRetriver() {
			
			@Override
			public Method getRetriever(Class type,String propertyName) {
				// TODO Auto-generated method stub
				try {
					return type.getMethod(propertyName, null);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
		System.out.println(st);
		
		FileOutputStream fos = new FileOutputStream("test.proto");
		try {
			fos.write(st.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fos.close();
		}
	}

}

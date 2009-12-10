/**
 * 
 */
package com.hyk.protobuf.mapping.util;

import org.antlr.stringtemplate.AttributeRenderer;

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;

/**
 * @author qiying.wang
 *
 */
public class ObjectRender implements AttributeRenderer {

	
	private String renderLable(Label label)
	{
		switch (label) {
		case LABEL_OPTIONAL:
			return "optional";
		case LABEL_REPEATED:
			return "repeated";
		case LABEL_REQUIRED:
			return "required";
		default:
			return "unknown";
		}
	}
	
	public static String renderType(Type type)
	{
		switch (type) {
		case TYPE_INT32:
			return "int32";
		case TYPE_BOOL:
			return "bool";
		case TYPE_DOUBLE:
			return "double";
		case TYPE_FIXED32:
			return "fixed32";
		case TYPE_FLOAT:
			return "float";
		case TYPE_INT64:
			return "int64";
		case TYPE_STRING:
			return "string";
		case TYPE_BYTES:
			return "bytes";
		case TYPE_FIXED64:
			return "fixed64";
		case TYPE_SFIXED32:
			return "sfixed32";
		case TYPE_SFIXED64:
			return "sfixed64";
		case TYPE_SINT32:
			return "sint32";
		case TYPE_SINT64:
			return "sint64";
		case TYPE_UINT32:
			return "uint32";
		case TYPE_UINT64:
			return "uint64";
		case TYPE_ENUM:
		case TYPE_MESSAGE:
		{
			return "null";
		}
		default:
			return "message";
		}
	}
	
	/* (non-Javadoc)
	 * @see org.antlr.stringtemplate.AttributeRenderer#toString(java.lang.Object)
	 */
	@Override
	public String toString(Object o) {
		if(o instanceof Type)
		{
			return renderType((Type) o);
		}
		else if(o instanceof Label)
		{
			return renderLable((Label) o);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.antlr.stringtemplate.AttributeRenderer#toString(java.lang.Object, java.lang.String)
	 */
	@Override
	public String toString(Object o, String formatName) {
		// TODO Auto-generated method stub
		return null;
	}

}

/**
 * 
 */
package target;

import java.io.IOException;
import java.io.Serializable;


/**
 * @author qiying.wang
 *
 */
public class TargetClass implements Serializable
//                                      ,Externalizable 
{

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public boolean hasType()
	{
		return true;
	}
	public long getTag() {
		return tag;
	}
	public void setTag(long tag) {
		this.tag = tag;
	}
	private String name = "helloaksldjsklafjksdakjlfgjsdkalgjkdsfjklgjklsdjgksdk" +
			"gklsdakgfsdjkgjkdsklgjksdkgjksdagjksdkagjklasfgsdaggdfagsdgasdgtfwetfgw" +
			"etgwegtwertgrwegregreagaergregraegert34543654754765856865846586544=84=36" +
			"53245346534643643546y45xzcsadfsafsadfsdafsdfdsfdsfsdgfsdgdfgfdgfdhfdjhfdjfgh";
	private int number;
	private long tag;
	private Boolean isOK = true;

}

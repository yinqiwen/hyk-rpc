package common;

import com.hyk.rpc.annotation.Remote;

@Remote
public interface DelegateInter {

	public String doSth(int i);
}

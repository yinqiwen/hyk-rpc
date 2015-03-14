# About #
  * hyk-rpc is a light-weight RPC framework.
  * hyk-rpc allow programmer use different protocol as the transport protocol, use different serialize mechanism to serialize/deserialize java objects.
  * hyk-rpc implemented an effective serialize mechanism which beat the standard Java serialize mechanism & other serialize mechanism in most situation.

# Architecture #
  * //TODO

# Example #
  * Common
    * Define an remote interface, the interface should be annotated by @com.hyk.rpc.core.annotation.Remote, eg:
```
       @Remote
       public interface HelloIntf
       {
	 public String sayHello(String name);
       }
```
  * Server Side:
    * Implement the remote interface, eg:
```
     public class HelloImpl implements HelloIntf
     {
	@Override
	public String sayHello(String name)
	{
	   return "Hello," + name;
	}
     }
```
    * Bind the instance into Naming service
```
       UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 2000);
       RPC rpc = new RPC(transport);
       rpc.getLocalNaming().bind("hello", new HelloImpl());
```
  * Client Side:
    * Connect server & retrieve remote object reference by naming service
```
      UDPRpcChannel transport = new UDPRpcChannel(Executors.newFixedThreadPool(10), 3000);
      RPC rpc = new RPC(transport);
      NameService serv = rpc.getRemoteNaming(new SimpleSockAddress(InetAddress.getLocalHost().getHostAddress(), 2000));
      HelloIntf hello = (HelloIntf)serv.lookup("hello");
      String ret = hello.sayHello("hyk-rpc");
```

# Status #
  * Not stable enough to release, but project [hyk-proxy](http://hyk-proxy.googlecode.com/) already use it as the low substructure & it seems that hyk-rpc work fine.
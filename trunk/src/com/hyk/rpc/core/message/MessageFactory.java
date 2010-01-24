/**
 * 
 */
package com.hyk.rpc.core.message;

import com.hyk.rpc.core.util.ID;

/**
 * @author qiying.wang
 *
 */
public class MessageFactory {

	public static MessageFactory instance = new MessageFactory();
	
	
	
	public Message createRequest(long objID, int opertionID, Object[] paras)
	{
		Request req = new Request();
		req.args = paras;
		req.objID = objID;
		req.operationID = opertionID;
		Message message = new Message();
		message.sessionID = ID.generateSessionID();
		message.type = MessageType.Request;
		message.value = req;
		return message;
	}
	
	public Message createResponse(Message request, Object returnValue)
	{
		Response res = new Response();
		res.reply = returnValue;
		Message message = new Message();
		message.type = MessageType.Response;
		message.sessionID = request.sessionID;
		message.value = res;
		return message;
	}
}

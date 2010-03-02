/**
 * This file is part of the hyk-rpc project.
 * Copyright (c) 2010 Yin QiWen <yinqiwen@gmail.com>
 *
 * Description: Timer.java 
 *
 * @author qiying.wang [ Mar 2, 2010 | 5:09:14 PM ]
 *
 */
package com.hyk.timer;

import java.util.TimerTask;

/**
 *
 */
public interface Timer
{
	public void schedule(TimerTask task, long delay);
}

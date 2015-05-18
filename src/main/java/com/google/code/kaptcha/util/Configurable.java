package com.google.code.kaptcha.util;


/**
 * This interface determines if a class can be configured by properties handled
 * by config manager.
 */
public abstract class Configurable
{
	private Config config = null;

	public Config getConfig()
	{
		return this.config;
	}

	public void setConfig(Config config)
	{
		this.config = config;
	}
}

package com.google.code.kaptcha.util;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;


/**
 * This class provides helper methods in parsing configuration values.
 */
public class ConfigHelper
{
	/** */
	public Color getColor(String paramName, String paramValue,
			Color defaultColor)
	{
		Color color;
		if ("".equals(paramValue) || paramValue == null)
		{
			color = defaultColor;
		}
		else if (paramValue.indexOf(",") > 0)
		{
			color = createColorFromCommaSeparatedValues(paramName, paramValue);
		}
		else
		{
			color = createColorFromFieldValue(paramName, paramValue);
		}
		return color;
	}

	/** */
	public Color createColorFromCommaSeparatedValues(String paramName,
			String paramValue)
	{
		Color color;
		String[] colorValues = paramValue.split(",");
		try
		{
			int r = Integer.parseInt(colorValues[0]);
			int g = Integer.parseInt(colorValues[1]);
			int b = Integer.parseInt(colorValues[2]);
			if (colorValues.length == 4)
			{
				int a = Integer.parseInt(colorValues[3]);
				color = new Color(r, g, b, a);
			}
			else if (colorValues.length == 3)
			{
				color = new Color(r, g, b);
			}
			else
			{
				throw new ConfigException(paramName, paramValue,
						"Color can only have 3 (RGB) or 4 (RGB with Alpha) values.");
			}
		}
		catch (NumberFormatException nfe)
		{
			throw new ConfigException(paramName, paramValue, nfe);
		}
		catch (ArrayIndexOutOfBoundsException aie)
		{
			throw new ConfigException(paramName, paramValue, aie);
		}
		catch (IllegalArgumentException iae)
		{
			throw new ConfigException(paramName, paramValue, iae);
		}
		return color;
	}

	/** */
	public Color createColorFromFieldValue(String paramName, String paramValue)
	{
		Color color;
		try
		{
			Field field = Class.forName("java.awt.Color").getField(paramValue);
			color = (Color) field.get(null);
		}
		catch (NoSuchFieldException nsfe)
		{
			throw new ConfigException(paramName, paramValue, nsfe);
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new ConfigException(paramName, paramValue, cnfe);
		}
		catch (IllegalAccessException iae)
		{
			throw new ConfigException(paramName, paramValue, iae);
		}
		return color;
	}

	/** */
	public Object getClassInstance(String paramName, String paramValue,
			Object defaultInstance, Config config)
	{
		Object instance;
		if ("".equals(paramValue) || paramValue == null)
		{
			instance = defaultInstance;
		}
		else
		{
			try
			{
				instance = Class.forName(paramValue).newInstance();
			}
			catch (IllegalAccessException iae)
			{
				throw new ConfigException(paramName, paramValue, iae);
			}
			catch (ClassNotFoundException cnfe)
			{
				throw new ConfigException(paramName, paramValue, cnfe);
			}
			catch (InstantiationException ie)
			{
				throw new ConfigException(paramName, paramValue, ie);
			}
		}

		setConfigurable(instance, config);

		return instance;
	}

	/** */
	public Font[] getFonts(String paramName, String paramValue, int fontSize,
			Font[] defaultFonts)
	{
		Font[] fonts;
		if ("".equals(paramValue) || paramValue == null)
		{
			fonts = defaultFonts;
		}
		else
		{
			String[] fontNames = paramValue.split(",");
			fonts = new Font[fontNames.length];
			for (int i = 0; i < fontNames.length; i++)
			{
				fonts[i] = new Font(fontNames[i], Font.BOLD, fontSize);
			}
		}
		return fonts;
	}

	/** */
	public int getPositiveInt(String paramName, String paramValue,
			int defaultInt)
	{
		int intValue;
		if ("".equals(paramValue) || paramValue == null)
		{
			intValue = defaultInt;
		}
		else
		{
			try
			{
				intValue = Integer.parseInt(paramValue);
				if (intValue < 1)
				{
					throw new ConfigException(paramName, paramValue,
							"Value must be greater than or equals to 1.");
				}
			}
			catch (NumberFormatException nfe)
			{
				throw new ConfigException(paramName, paramValue, nfe);
			}
		}
		return intValue;
	}

	/** */
	public char[] getChars(String paramName, String paramValue,
			char[] defaultChars)
	{
		char[] chars;
		if ("".equals(paramValue) || paramValue == null)
		{
			chars = defaultChars;
		}
		else
		{
			chars = paramValue.toCharArray();
		}
		return chars;
	}

	/** */
	public boolean getBoolean(String paramName, String paramValue,
			boolean defaultValue)
	{
		boolean booleanValue;
		if ("yes".equals(paramValue) || "".equals(paramValue)
				|| paramValue == null)
		{
			booleanValue = defaultValue;
		}
		else if ("no".equals(paramValue))
		{
			booleanValue = false;
		}
		else
		{
			throw new ConfigException(paramName, paramValue,
					"Value must be either yes or no.");
		}
		return booleanValue;
	}

	/** */
	private void setConfigurable(Object object, Config config)
	{
		if (object instanceof Configurable)
		{
			((Configurable) object).setConfig(config);
		}
	}
}

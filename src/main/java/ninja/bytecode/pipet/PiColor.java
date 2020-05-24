package ninja.bytecode.pipet;

import java.awt.Color;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class PiColor
{
	public static void setRGBState(boolean r, boolean g, boolean b, GpioPinDigitalOutput... pins)
	{
		pins[0].setState(r);
		pins[1].setState(g);
		pins[2].setState(b);
	}

	public static Color hex2Rgb(String colorStr)
	{
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public static void setRGBHex(String hex, int tick, GpioPinDigitalOutput... pins)
	{
		setRGB(hex2Rgb(hex), tick, pins);
	}

	public static void setRGB(Color color, int tick, GpioPinDigitalOutput... pins)
	{
		int rc = color.getRed();
		int gc = color.getGreen();
		int bc = color.getBlue();
		int sector = 0;

		while(rc > 0 || gc > 0 || bc > 0 || sector < 256)
		{
			sector++;

			if(sector % 3 == 0 && rc > 0 && gc > 0)
			{
				setRGBState(false, false, true, pins);
				rc -= 1;
				gc -= 1;
				continue;
			}

			else if(sector % 3 == 0 && rc > 0 && bc > 0)
			{
				setRGBState(false, true, false, pins);
				rc -= 1;
				bc -= 1;
				continue;
			}

			else if(sector % 3 == 1 && gc > 0 && bc > 0)
			{
				setRGBState(true, false, false, pins);
				gc -= 1;
				bc -= 1;
				continue;
			}

			else if(sector % 3 == 1 && gc > 0 && rc > 0)
			{
				setRGBState(false, false, true, pins);
				gc -= 1;
				rc -= 1;
				continue;
			}

			else if(sector % 3 == 2 && bc > 0 && rc > 0)
			{
				setRGBState(false, true, false, pins);
				bc -= 1;
				rc -= 1;
				continue;
			}

			else if(sector % 3 == 2 && bc > 0 && gc > 0)
			{
				setRGBState(true, false, false, pins);
				bc -= 1;
				gc -= 1;
				continue;
			}

			else if(sector % 3 == 0 && rc > 0)
			{
				setRGBState(false, true, true, pins);
				rc -= 1;
				continue;
			}

			else if(sector % 3 == 1 && gc > 0)
			{
				setRGBState(true, false, true, pins);
				gc -= 1;
				continue;
			}

			else if(sector % 3 == 2 && bc > 0)
			{
				setRGBState(true, true, false, pins);
				bc -= 1;
				continue;
			}

			else
			{
				setRGBState(true, true, true, pins);
				continue;
			}
		}
	}
}

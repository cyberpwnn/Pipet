package ninja.bytecode.pipet;

import java.awt.Color;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import ninja.bytecode.shuriken.execution.ChronoLatch;
import ninja.bytecode.shuriken.logging.L;

public class Pipet
{
	public static long work = 0;
	public static final PipetSystem system = new PipetSystem();

	public static void main(String[] a)
	{
		L.i("Starting Pipet");
		int sxx = 888;

		try
		{
			sxx = Integer.valueOf(a[0]);
		}
		
		catch(Throwable e)
		{

		}

		GpioPinDigitalOutput rPin = system.registerOutput(RaspiPin.GPIO_00, "BUTT_R", PinState.LOW);
		GpioPinDigitalOutput gPin = system.registerOutput(RaspiPin.GPIO_02, "BUTT_G", PinState.LOW);
		GpioPinDigitalOutput bPin = system.registerOutput(RaspiPin.GPIO_03, "BUTT_B", PinState.LOW);
		GpioPinDigitalInput uPin = system.registerInput(RaspiPin.GPIO_07, "BUTT", PinPullResistance.PULL_DOWN);
		int t = 0;
		int tx = 0;

		ChronoLatch cl = new ChronoLatch(4, true);

		float r = 1f;
		float g = 1f;
		float b = 1f;
		Color clx = Color.getHSBColor(r, g, b);

		while(true)
		{
			if(cl.flip())
			{
				tx++;
				r = (tx % sxx) / (float)(sxx);
				clx = Color.getHSBColor(r, g, b);
			}

			setRGB(clx, t++, rPin, gPin, bPin);
		}
	}

	public static Color hex2Rgb(String colorStr)
	{
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public static void setRGB(Color color, int tick, GpioPinDigitalOutput... pins)
	{
		int rc = color.getRed();
		int gc = color.getGreen();
		int bc = color.getBlue();
		int sector = 0;

		while(rc > 0 || gc > 0 || bc > 0 || sector < 255 * 3)
		{
			sector++;

			if(sector % 3 == 0 && rc > 0 && gc > 0)
			{
				pins[0].low();
				pins[1].low();
				pins[2].high();
				rc -= 1;
				gc -= 1;
				work += 2;
				continue;
			}

			else if(sector % 3 == 0 && rc > 0 && bc > 0)
			{
				pins[0].low();
				pins[1].high();
				pins[2].low();
				rc -= 1;
				bc -= 1;
				work += 2;
				continue;
			}

			else if(sector % 3 == 1 && gc > 0 && bc > 0)
			{
				pins[0].high();
				pins[1].low();
				pins[2].low();
				gc -= 1;
				bc -= 1;
				work += 2;
				continue;
			}

			else if(sector % 3 == 1 && gc > 0 && rc > 0)
			{
				pins[0].low();
				pins[1].low();
				pins[2].high();
				gc -= 1;
				rc -= 1;
				work += 2;
				continue;
			}

			else if(sector % 3 == 2 && bc > 0 && rc > 0)
			{
				pins[0].low();
				pins[1].high();
				pins[2].low();
				bc -= 1;
				rc -= 1;
				work += 2;
				continue;
			}

			else if(sector % 3 == 2 && bc > 0 && gc > 0)
			{
				pins[0].high();
				pins[1].low();
				pins[2].low();
				bc -= 1;
				gc -= 1;
				work += 2;
				continue;
			}

			else if(sector % 3 == 0 && rc > 0)
			{
				pins[0].low();
				pins[1].high();
				pins[2].high();
				rc -= 1;
				work++;
				continue;
			}

			else if(sector % 3 == 1 && gc > 0)
			{
				pins[0].high();
				pins[1].low();
				pins[2].high();
				gc -= 1;
				work++;
				continue;
			}

			else if(sector % 3 == 2 && bc > 0)
			{
				pins[0].high();
				pins[1].high();
				pins[2].low();
				bc -= 1;
				work++;
				continue;
			}

			else
			{
				pins[0].high();
				pins[1].high();
				pins[2].high();
				work++;
				continue;
			}
		}
	}
}

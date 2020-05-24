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

			PiColor.setRGB(clx, t++, rPin, gPin, bPin);
		}
	}
}

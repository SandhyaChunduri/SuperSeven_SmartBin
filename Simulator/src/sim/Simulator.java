package sim;

import java.util.concurrent.ThreadLocalRandom;

import com.smartbin.data.Bin;
import com.smartbin.data.BinHandler;
import com.smartbin.service.ServiceMgmt;

public class Simulator {

	static int fillLvlStart = 0;
	static int fillLvlEnd = 2;
	static int binIDStart = 1;
	static int binIDEnd = 1000;
	static int tempMin = 0;
	static int tempMax = 100;
	static int humidMin = 0;
	static int humidMax = 100;
	static Float latMin = 17.4968f;
	//static Float latMax = 19.4968f;
	static Float lngMin = 78.3614f;
	//static Float lngMax = 80.3614f;
	public static void main(String[] args) {
		
		Bin bin = new Bin();
		while(true)
		{
			// nextInt is normally exclusive of the top value,
			// so add 1 to make it inclusive
			int binID = ThreadLocalRandom.current().nextInt(binIDStart, binIDEnd + 1);
			bin.setBinId(binID);
			int temperature = ThreadLocalRandom.current().nextInt(tempMin, tempMax + 1);
			bin.setTemperature(temperature);
			int humidity = ThreadLocalRandom.current().nextInt(humidMin, humidMax + 1);
			bin.setHumidity(humidity);
			Float latitude = new Float(latMin + ThreadLocalRandom.current().nextFloat());
			bin.setLatitude(latitude);
			Float longitude = new Float(lngMin + ThreadLocalRandom.current().nextFloat());
			bin.setLongitude(longitude);
			bin.convertLatLongToLocation(bin.getLatitude().toString()+","+bin.getLongitude().toString());
			int fillLevel = ThreadLocalRandom.current().nextInt(fillLvlStart, fillLvlEnd + 1);
			bin.setFillLevel(fillLevel);
			new ServiceMgmt();
			BinHandler handler = new BinHandler();
			handler.insertOrUpdateBin("BinStore",bin);
			try {
				Thread.sleep(5000);
			} catch(Exception e)
			{
				return;
			}
		}

	}

}

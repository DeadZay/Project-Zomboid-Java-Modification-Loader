package zombie.javamods;

import java.io.IOException;


public class Main implements Runnable {

	private final static String debugArg     = "-debug";
	private final static String serverArg    = "-server";
	private final static String bootstrapArg = "-bootstrap";

	private Main(String[] args) throws IOException {

		for (String arg : args) {

			if (arg.equals(bootstrapArg)) {
				Loader.bootstrapJavaMods();
				System.out.println(Loader.getClassPath());
				System.exit(0);
			}

			else if (arg.equals(debugArg))
				Core.debug =  true;

			else if (arg.equals(serverArg))
				Core.server = true;

		}

		Log.init();
		Log.info("Starting JavaMods");
		Log.info("Running with debug mode = " + Core.debug);

		new Thread(this).start();
	}

	public void run() {
		Core.waitLuaManagerReady();
		Core.init();
		Core.loop();
	}

	private static void startZomboid() {

		try {
			final Object[] arg = new Object[]{ new String[] {} };
			Class<?> zomboidMainClass = Core.getZomboidMainClass();
			zomboidMainClass.getDeclaredMethod("main", String[].class).invoke(null, arg);
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

	private static void startZomboidServer() {

		try {
			final Object[] arg = new Object[]{ new String[] { "-statistic" }};
			Class<?> zomboidMainClass = Core.getZomboidMainClass();
			zomboidMainClass.getDeclaredMethod("main", String[].class).invoke(null, arg);
		}
		catch (Exception error) {
			Log.error(error);
		}
	}

	public static void main(String[] args) throws IOException {

		new Main(args);

		if (Core.server)
			startZomboidServer();
		else
			startZomboid();

		Log.exit();
	}
}

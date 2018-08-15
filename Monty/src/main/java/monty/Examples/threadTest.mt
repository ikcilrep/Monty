import sml.io.println;
import sml.casts.toString;
import sml.threading.sleep;
import sml.threading.join;


func int infinity int a;
	while true;
		println("Creating thread:\t" toString(a) + );
		sleep(1000);
		thread infinity(a 1 +);
		println("Created thread:\t" toString(a) +);
		join(0);
	end;
end;

thread infinity(0);

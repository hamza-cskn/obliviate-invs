package mc.obliviate.inventory;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;

public class Scheduler {


    public static TaskScheduler getScheduler(){

        return UniversalScheduler.getScheduler(InventoryAPI.getInstance().getPlugin());

    }

}

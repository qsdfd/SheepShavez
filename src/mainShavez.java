import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;



import org.osbot.rs07.utility.Area;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


@ScriptManifest(name = "SheepShavez", author = "Dokato", version = 1.0, info = "", logo = "") 
public class mainShavez extends Script {
	
	//private ArrayList<Entity> sheeps;
	
	private final Area SHEEP_AREA = new Area(3193,3276,3211,3257);
	private final Area STILE_AREA = new Area(3196,3277,3198,3278);
	private final Area SHEEP_MID_AREA = new Area(3200,3261,3203,3271);
	private final Area GATE_AREA_INSIDE = new Area(3211,3260,3212,3263);
	private final Area GATE_AREA_OUTSIDE = new Area(3213,3260,3215,3263);
	private final Area ALL_GATE_AREA = new Area(3208,3258,3217,3266);
	private final Area BANK_AREA = new Area(3207,3215,3210,3222);
	private String status;
	
	private final static int woolPrice=395;
	private int woolColected;
	private int moneyMade;
	
	 private long timeBegan;
	 private long timeRan;
	 private int banktrips;
	 private int k;
	

	@Override
    public void onStart() {
		timeBegan = System.currentTimeMillis();
		banktrips=0;
		woolColected=0;
		moneyMade=0;
		//sheeps = new ArrayList<Entity>();
	}
    
    @Override
    public void onExit() {
    }
    
    public void walkToBank() throws InterruptedException{
    	status="walking to bank";
	    		if(map.canReach(new Position(3208,3210,0))){
	    			localWalker.walk(new Area(3206,3209,3208,3211), true);
	    		}else if(map.canReach(new Position(3207,3214,0))){
	    			localWalker.walk(new Area(3207,3213,3205,3214), true);
	    		}else if(map.canReach(new Position(3200,3218,0))){
	    			localWalker.walk(new Area(3203,3215,3201,3217), true);
	    		}else if(map.canReach(new Position(3199,3228,0))){
	    			localWalker.walk(new Area(3200,3226,3199,3230), true);
	    		}else if(map.canReach(new Position(3202,3239,0))){
	    			localWalker.walk(new Area(3201,3237,3203,3239), true);
	    		}else if(map.canReach(new Position(3206,3248,0))){
	    			localWalker.walk(new Area(3205,3245,3207,3248), true);
	    		}else if(map.canReach(new Position(3209,3253,0))){
	    			localWalker.walk(new Area(3209,3252,3211,3256), true);
	    		}
    	
    }
    
    public boolean banked(){
    	if((inventory.getItems().length==1)&&(getInventory().contains("Shears"))){
    		return true;
    	}else{
    		return false;
    	}
    }

    public void bank() throws InterruptedException{
    	status="walking to bank";
    	status="banking1";
    	if(!banked()){
    		status="banking2";
    		if(bank.isOpen()){
    			status="open bank";
    			if(inventory.isEmpty()){
    				do{
	    				bank.withdraw("Shears", 1);
	    				sleep(random(600,900));
    				}while(!inventory.contains("Shears"));
    				bank.close();
    				sleep(random(600,900));
    			}else{
    				bank.depositAll();
    				banktrips=banktrips+1;
    				sleep(random(600,900));
    			}
    		}else{
    			status="opening the bank";
    			objects.closest("Bank booth").interact("Bank");
    			sleep(random(600,900));
    		}
    	}
    }
    
    public void walkToSheeps() throws InterruptedException{
    	if(!SHEEP_AREA.contains(myPlayer())){
    		status="walking to sheeps";
    		if(map.canReach(new Position(3210,3262,0))){
    			localWalker.walk(SHEEP_MID_AREA,true);
    		}else if(ALL_GATE_AREA.contains(myPlayer())&&(!map.canReach(new Position(3210,3262,0)))){
	    		objects.closest("Gate").interact("Open");
	    		sleep(random(300,900));
	    	}else if(map.canReach(new Position(3214,3261,0))){
    			localWalker.walk(GATE_AREA_OUTSIDE, true);
    		}else if(map.canReach(new Position(3209,3253,0))){
    			localWalker.walk(new Area(3209,3252,3211,3255), true);
    		}else if(map.canReach(new Position(3206,3248,0))){
    			localWalker.walk(new Area(3205,3245,3207,3248), true);
    		}else if(map.canReach(new Position(3202,3239,0))){
    			localWalker.walk(new Area(3201,3237,3203,3239), true);
    		}else if(map.canReach(new Position(3199,3228,0))){
    			localWalker.walk(new Area(3200,3226,3199,3230), true);
    		}else if(map.canReach(new Position(3200,3218,0))){
    			localWalker.walk(new Area(3203,3215,3201,3217), true);
    		}else if(map.canReach(new Position(3207,3214,0))){
    			localWalker.walk(new Area(3207,3213,3205,3214), true);
    		}
    	}
    }
    
    Random randomGenerator = new Random();
	public int getRandomNumber(int min,int max){
        int randomNumber = randomGenerator.nextInt(max+1);
        while(randomNumber<min){
            randomNumber = randomGenerator.nextInt(max+1);
        }
        return randomNumber;
    }
    
    @Override
    public int onLoop() throws InterruptedException {
    	if(STILE_AREA.contains(myPlayer())){
    		objects.closest("Stile").interact("Climb-over");
    		sleep(random(1000,1500));
    	}
    	if((!inventory.isFull())&&(getInventory().contains("Shears"))){
	    	if(SHEEP_AREA.contains(myPlayer())){
	    		if(myPlayer().isUnderAttack()){
	    				objects.closest("Stile").interact("Climb-over");
	    				sleep(random(1900,2300));
	    				if(!myPlayer().isMoving()&&(!myPlayer().isAnimating())){
		    				while(myPlayer().isOnScreen()){
		    					logoutTab.logOut();
		    				}
	    				}
	        	}
	    		status="About to share a sheep";
	    			settings.setRunning(true);
	    			sleep(random(500,800));
	    			List<NPC>  sheeps = getNpcs().filter(new AreaFilter<NPC>(SHEEP_AREA));
	    			for(NPC npcees : sheeps ){
	    				int npcID=npcees.getId();
		    				if(map.realDistance(npcees.getPosition())<=10){
			    				if((((((npcID==2794)||(npcID==2795))||(npcID==2796))||(npcID==2800))||(npcID==2801))||(npcID==2802)){
			    					if(!myPlayer().isMoving()){
			    		    			status="Shearing sheep";
			    						npcees.interact("Shear");
			    						sleep(random(600,900));
			    					}
			    				}
		    				}
	    			}
	    			sheeps.clear();	
	    	}else if((BANK_AREA.contains(myPlayer()))&&(myPosition().getZ()==2)){
	    		status="Going downstairs";
	    		localWalker.walk(new Area(3205,3209,3206,3210),true);
	    		sleep(random(2400,2900));
	    	}else if((!BANK_AREA.contains(myPlayer()))&&(myPosition().getZ()==2)){
	    		objects.closest("Staircase").interact("Climb-down");
	    		sleep(random(1000,1500));
	    	}else if(myPosition().getZ()==1){
	    		objects.closest("Staircase").interact("Climb-down");
	    		sleep(random(1000,1500));
	    	}else{
	    		walkToSheeps();
	    	}
    	}else{
    		if(ALL_GATE_AREA.contains(myPlayer())){
    			if(!map.canReach(new Position(3214,3261,0))){
    				objects.closest("Gate").interact("Open");
    				sleep(random(1500,1800));
    			}else{
    				walkToBank();
    			}
    		}else if((SHEEP_AREA.contains(myPlayer()))&&(!GATE_AREA_INSIDE.contains(myPlayer()))){
    			localWalker.walk(GATE_AREA_INSIDE,true);
    		}else if((BANK_AREA.contains(myPlayer()))&&(myPosition().getZ()==2)){
    				bank();
    		}else if((new Area(3206,3209,3208,3211).contains(myPlayer()))&&(myPosition().getZ()==0)){
				objects.closest("Staircase").interact("Climb-up");
				sleep(random(600,900));
			}else if(myPosition().getZ()==1){
				objects.closest("Staircase").interact("Climb-up");
				sleep(random(600,900));
			}else if((!BANK_AREA.contains(myPlayer()))&&(myPosition().getZ()==2)){
				localWalker.walk(BANK_AREA,true);
				sleep(random(600,900));
			}else{
				walkToBank();
			}
    	}
        return 100;
    }
    
    public void onMessage(Message message) throws java.lang.InterruptedException {
    	String woolTxt = message.getMessage().toLowerCase();
    	
    	if(woolTxt.contains("some wool")&&(getInventory().contains("Wool"))){
    		woolColected++;
    	}
    }
    

    @Override
    public void onPaint(Graphics2D g1) {
    	timeRan = System.currentTimeMillis() - this.timeBegan;
    	moneyMade=woolColected*woolPrice;
    	Graphics2D g = (Graphics2D)g1;
    	
    	 g.setFont(new Font("Arial", 0, 13));
	     g.setColor(new Color(255, 255, 255));
	     g.drawString("Runtime: "+ft(timeRan), 20, 185);
	     g.drawString("Wool colected: "+woolColected, 20, 200);
	     g.drawString("Wool price: "+woolPrice, 20, 215);
	     g.drawString("Money made: "+moneyMade, 20, 230);
	     g.drawString("Banktrips: "+banktrips, 20, 245);
	     g.drawString("Status: "+status, 20, 260);
    }
    
    private String ft(long duration){ //the method will format seconds, minutes & hours into seconds. Otherwise the time ran would show in milliseconds and that would get majorly confusing.
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
		- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
		- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
		.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
		- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
		.toMinutes(duration));
		if (days == 0){
		res = (hours + ":" + minutes + ":" + seconds);
		}else{
		res = (days + ":" + hours + ":" + minutes + ":" + seconds);
		}
		return res;
	} 


}
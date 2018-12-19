package time;

import engineTester.MainGameLoop;


public class Time {
	
	public String time;
	public int minute = 0;
	public int hour = 2;
	public boolean AM = true;
	
	public int calculateMinute(int frameSecs){
		
		if(frameSecs%6000 == 0){
			minute = frameSecs / 6000;
			if(minute < 10){
				if(AM){
				time = hour + ":0" + minute + " AM";
				}else{
					time = hour + ":0" + minute + " PM";
				}
			}else{
				if(AM){
					time = hour + ":" + minute + " AM";
					}else{
						time = hour + ":" + minute + " PM";
					}
			}
		}
		
		return minute;
	}
	
	public void formatTime(){
		if(minute == 59){
			MainGameLoop.frameSecs = 0;
			minute = 0;
			if(hour != 12){
				hour += 1;
			}else{
				hour = 1;
				if(AM){
					AM = false;
				}else{
					AM = true;
				}
			}
				
		}
		
		
	}

	public String getTime() {
		return time;
	}

	public int getMinute() {
		return minute;
	}

	public int getHour() {
		return hour;
	}
	
	
	
	
	
}

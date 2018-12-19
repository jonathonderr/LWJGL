package collision;

public class collisionDetection {
	
	
	public static boolean isColliding(
			 float Ax, float Ay, float Az,
			 float AX, float AY, float AZ,
			 float ax, float ay, float az,
			 float Bx, float By, float Bz,
			 float BX, float BY, float BZ,
			 float bx, float by, float bz){
		
		if (AX < Bx || BX < Ax || AY < By || BY < Ay ){
			System.out.println("Isn't colliding");
			return false;
		}
		else{
			System.out.println("Collided");
			return true;
		}
		
		
		
	}
}

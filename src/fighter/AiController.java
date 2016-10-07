package fighter;

public class AiController {
	Character connectedCharacter;
	Game myGame;
	Character followedPerson;
	int distanceToX;
	int distanceToY;
	int attackWindow = 0;

	public AiController(Character character, Game gameinstance) {
		connectedCharacter = character;
		myGame = gameinstance;
	}

	public void update() {
		FollowCharacterWithHighestPercent();
	}

	public void getDistanceToX() {
		distanceToX = (int) Math.abs(followedPerson.getX() - connectedCharacter.getX());
	}

	public void getDistanceToY() {
		distanceToY = (int) Math.abs(followedPerson.getY() - connectedCharacter.getY());
	}

	// makes the ai walk towards the player who has the highest percent
	public void FollowCharacterWithHighestPercent() {
		// chooses the character to follow
		for (Character person : myGame.characters) {
			if (person != connectedCharacter) {

				if (followedPerson == null)
					followedPerson = person;
				else {
					if (person.percent > followedPerson.percent) {
						followedPerson = person;
					}
				}
			}

		}
		getDistanceToX();
		getDistanceToY();
		if (distanceToX > 50) {
			if (followedPerson.getX() - 1 < connectedCharacter.getX()) {
				connectedCharacter.direction = Character.DIRECTION_LEFT;
				connectedCharacter.runLeft();
				connectedCharacter.rotateRunArray();
			} else if (followedPerson.getX() + 1 > connectedCharacter.getX()) {
				connectedCharacter.direction = Character.DIRECTION_RIGHT;
				connectedCharacter.runRight();
				connectedCharacter.rotateRunArray();
			}
		} else {
			chooseRandomAttack();
		}
		if (distanceToY > 30) {
			if (followedPerson.getY() < connectedCharacter.getY())
				connectedCharacter.jump();
			else if(connectedCharacter.isGrounded)
				connectedCharacter.state = Character.STATE_CROUCH;
		}
	}

	public void chooseRandomAttack() {
		if (attackWindow <= 0) {
			if (connectedCharacter.canAttack()) {
				if (connectedCharacter.isGrounded) {
					if (distanceToX < 30) {
						connectedCharacter.jab();
						attackWindow = 20;
					}else if(distanceToX < 50){
						connectedCharacter.dTilt();
						attackWindow = 20;
					}

				}

			}
		} else {
			attackWindow--;
		}
	}

}

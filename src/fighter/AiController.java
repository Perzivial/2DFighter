package fighter;

import java.util.Random;

public class AiController {
	Character connectedCharacter;
	Game myGame;
	Character followedCharacter;
	int distanceToX;
	int distanceToY;
	int attackWindow = 0;
	int dashDanceCounter = 0;

	public AiController(Character character, Game gameinstance) {
		connectedCharacter = character;
		myGame = gameinstance;
	}

	public void update() {
		FollowCharacterWithHighestPercent();
	}

	public void getDistanceToX() {
		distanceToX = (int) Math.abs(followedCharacter.getX() - connectedCharacter.getX());
	}

	public void getDistanceToY() {
		distanceToY = (int) Math.abs(followedCharacter.getY() - connectedCharacter.getY());
	}

	// makes the ai walk towards the player who has the highest percent
	public void FollowCharacterWithHighestPercent() {
		// chooses the character to follow
		for (Character person : myGame.characters) {
			if (person != connectedCharacter) {

				if (followedCharacter == null)
					followedCharacter = person;
				else {
					if (person.percent > followedCharacter.percent) {
						followedCharacter = person;
					}
				}
			}

		}
		getDistanceToX();
		getDistanceToY();
		if (distanceToX > 50) {
			if (followedCharacter.getX() - 1 < connectedCharacter.getX()) {
				connectedCharacter.runLeft();
			} else if (followedCharacter.getX() + 1 > connectedCharacter.getX()) {
				connectedCharacter.runRight();
			}
		} else {
			chooseRandomAttack();
		}
		if (distanceToY > 30) {
			if (followedCharacter.getY() < connectedCharacter.getY())
				connectedCharacter.jump();
			else if (connectedCharacter.isGrounded) {
				connectedCharacter.jump();
				connectedCharacter.velY += 5;
			}
		}
	}

	public void chooseRandomAttack() {
		if (attackWindow <= 0) {
			if (connectedCharacter.canAttack()) {
				Random rand = new Random();
				int randomChoice = rand.nextInt(6) - 3;
				if (connectedCharacter.isGrounded) {
					switch (randomChoice) {
					case (1):
						if (followedCharacter.state != Character.STATE_SHIELD) {
							chooseBestGroundAttack();
						}
						break;
					case (2):
						if (followedCharacter.state != Character.STATE_SHIELD
								|| followedCharacter.state != Character.STATE_JUMP) {
							connectedCharacter.jump();
							attackWindow = 10;
						}
						break;
					}
					if (followedCharacter.state == Character.STATE_SHIELD) {
						if (connectedCharacter.getX() < followedCharacter.getX())
							connectedCharacter.direction = Character.DIRECTION_RIGHT;
						else
							connectedCharacter.direction = Character.DIRECTION_LEFT;
						connectedCharacter.grab();
					}
				} else {
					if ((followedCharacter.getY() < connectedCharacter.getY())) {
						connectedCharacter.upSpecial();
						connectedCharacter.state = Character.STATE_UPSPECIAL;
						attackWindow = 10;
					}
					chooseBestAirAttack();
				}

			}
		} else {
			attackWindow--;
		}
	}

	public void chooseBestAirAttack() {
		if (connectedCharacter.state == Character.STATE_NEUTRAL) {
			if (distanceToX < 10) {
				if (distanceToY > 5 && distanceToY < 15) {
					connectedCharacter.uair();
					attackWindow = 20;
				}
			}
			if (distanceToY < 20) {
				if (distanceToX < 10) {
					connectedCharacter.dair();
					connectedCharacter.state = Character.STATE_ATTACK_DAIR;
					attackWindow = 20;
				} else if (distanceToX < 40) {
					connectedCharacter.fair();
					connectedCharacter.state = Character.STATE_ATTACK_FAIR;
					attackWindow = 10;
				} else {
					if (followedCharacter.getX() - 1 < connectedCharacter.getX()) {
						connectedCharacter.runLeft();
					} else if (followedCharacter.getX() + 1 > connectedCharacter.getX()) {
						connectedCharacter.runRight();
					}
				}
			}
		}
	}

	public void chooseBestGroundAttack() {
		Random rand = new Random();
		int randomChoice = rand.nextInt(2);
		switch (randomChoice) {
		case (0):
			if (connectedCharacter.state == Character.STATE_NEUTRAL) {
				if (distanceToX < 20) {
					connectedCharacter.fTilt();
					attackWindow = 20;
				} else if (distanceToX < 30) {
					connectedCharacter.jab();
					attackWindow = 20;
				} else if (distanceToX < 50) {
					connectedCharacter.dTilt();
					attackWindow = 20;
				}
				if (distanceToY > 5 && distanceToY < 15 && followedCharacter.getY() < connectedCharacter.getY()) {
					connectedCharacter.uTilt();
					attackWindow = 20;
				}
			}
			break;
		case (1):
			if (connectedCharacter.state == Character.STATE_NEUTRAL) {
				if (distanceToX < 20) {
					connectedCharacter.downSpecial();
					connectedCharacter.state = Character.STATE_DOWNSPECIAL;
					System.out.println("derp");
					attackWindow = 60;
				} else {
					connectedCharacter.forwardSpecial();
					attackWindow = 60;
				}
			}
			break;
		case (2):
			if (connectedCharacter.state == Character.STATE_NEUTRAL) {
				if (distanceToX < 20) {
					connectedCharacter.downSmash();
					connectedCharacter.state = Character.STATE_SMASH_ATTACK_DOWN;
					attackWindow = 20;
				} else {
					connectedCharacter.forwardSmash();
					connectedCharacter.state = Character.STATE_SMASH_ATTACK_FORWARD;
					attackWindow = 20;
				}
			}
			break;
		}
	}
}

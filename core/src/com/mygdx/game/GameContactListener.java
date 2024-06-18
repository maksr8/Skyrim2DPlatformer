package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import objects.*;

public class GameContactListener implements ContactListener {
    GameScreen gameScreen;

    public GameContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        handleFootBeginContact(fixtureA);
        handleFootBeginContact(fixtureB);

        handleFallingPlatform(fixtureA, fixtureB);
        handleFallingPlatform(fixtureB, fixtureA);

        handleRatSensorBeginContact(fixtureA, fixtureB);
        handleRatSensorBeginContact(fixtureB, fixtureA);

        handleVikingSensorBeginContact(fixtureA, fixtureB);
        handleVikingSensorBeginContact(fixtureB, fixtureA);

        handleDragonSensorBeginContact(fixtureA, fixtureB);
        handleDragonSensorBeginContact(fixtureB, fixtureA);

        handleRatHitByPlayerBeginContact(fixtureA, fixtureB);
        handleRatHitByPlayerBeginContact(fixtureB, fixtureA);

        handleVikingHitByPlayerBeginContact(fixtureA, fixtureB);
        handleVikingHitByPlayerBeginContact(fixtureB, fixtureA);

        handleDragonHitByPlayerBeginContact(fixtureA, fixtureB);
        handleDragonHitByPlayerBeginContact(fixtureB, fixtureA);

        handlePlayerHitBySmthBeginContact(fixtureA, fixtureB);
        handlePlayerHitBySmthBeginContact(fixtureB, fixtureA);

        handleVikingTriggerBeginContact(fixtureA, fixtureB);
        handleVikingTriggerBeginContact(fixtureB, fixtureA);

        handleEntranceBeginContact(fixtureA, fixtureB);
        handleEntranceBeginContact(fixtureB, fixtureA);

        handleStartBossfight(fixtureA, fixtureB);
        handleStartBossfight(fixtureB, fixtureA);

        handleDragonInCenterBeginContact(fixtureA, fixtureB);
        handleDragonInCenterBeginContact(fixtureB, fixtureA);
    }

    private void handleDragonInCenterBeginContact(Fixture fixtureB, Fixture fixtureA) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Dragon && fixtureB.getUserData() instanceof StartBossfightSensor) {
                ((Dragon) fixtureA.getUserData()).setInCenter(true);
            }
        }
    }

    private void handleDragonHitByPlayerBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Dragon && (fixtureB.getUserData().equals("rightAttack")
                    || fixtureB.getUserData().equals("leftAttack"))) {
                if (((Dragon) fixtureA.getUserData()).getBody().getFixtureList().get(0) == fixtureA) {
                    if (fixtureB.getUserData().equals("rightAttack")) {
                        gameScreen.getPlayer().addEntityToHitTowardsRight((Dragon) fixtureA.getUserData());
                    } else {
                        gameScreen.getPlayer().addEntityToHitTowardsLeft((Dragon) fixtureA.getUserData());
                    }
                }
            }
        }
    }

    private void handleDragonSensorBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Dragon
                && fixtureB.getUserData() != null && fixtureB.getUserData() instanceof DragonPlatform) {
            Dragon dragon = (Dragon) fixtureA.getUserData();
            if (dragon.getBody().getFixtureList().get(1) == fixtureA) {
                dragon.addLeftSensorContact();
            } else if (dragon.getBody().getFixtureList().get(2) == fixtureA) {
                dragon.addRightSensorContact();
            }
        }
    }

    private void handleStartBossfight(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof StartBossfightSensor && fixtureB.getUserData() instanceof Player) {
                if (((Player) fixtureB.getUserData()).getBody().getFixtureList().get(1) == fixtureB) {
                    gameScreen.startBossfight();
                }
            }
        }
    }

    private void handleEntranceBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Entrance && fixtureB.getUserData() instanceof Player) {
                if (((Player) fixtureB.getUserData()).getBody().getFixtureList().get(1) == fixtureB) {
                    ((Player) fixtureB.getUserData()).setTouchingEntrance(true);
                }
            }
        }
    }

    private void handleVikingTriggerBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Viking && fixtureB.getUserData() instanceof Player
                    && ((Player) fixtureB.getUserData()).getBody().getFixtureList().get(1) == fixtureB
                    && ((Viking) fixtureA.getUserData()).getBody().getFixtureList().get(3) == fixtureA) {
                ((Viking) fixtureA.getUserData()).setTriggered(true);
            }
        }
    }

    private void handleVikingHitByPlayerBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Viking && (fixtureB.getUserData().equals("rightAttack")
                    || fixtureB.getUserData().equals("leftAttack"))) {
                if (((Viking) fixtureA.getUserData()).getBody().getFixtureList().get(0) == fixtureA) {
                    if (fixtureB.getUserData().equals("rightAttack")) {
                        gameScreen.getPlayer().addEntityToHitTowardsRight((Viking) fixtureA.getUserData());
                    } else {
                        gameScreen.getPlayer().addEntityToHitTowardsLeft((Viking) fixtureA.getUserData());
                    }
                }
            }
        }
    }

    private void handleVikingSensorBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Viking
                && !fixtureB.isSensor()) {
            Viking viking = (Viking) fixtureA.getUserData();
            if (viking.getBody().getFixtureList().get(1) == fixtureA) {
                viking.addLeftSensorContact();
            } else if (viking.getBody().getFixtureList().get(2) == fixtureA) {
                viking.addRightSensorContact();
            }
        }
    }

    private void handlePlayerHitBySmthBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null && fixtureA.getUserData() instanceof Player) {
            if ((fixtureB.getUserData() instanceof Rat && !fixtureB.isSensor())
                    || (fixtureB.getUserData() instanceof Viking
                    && (fixtureB.getBody().getFixtureList().get(0) == fixtureB
                    || fixtureB.getBody().getFixtureList().get(4) == fixtureB)
                    || fixtureB.getUserData() instanceof Fireball) ){
                gameScreen.getPlayer().addFixtureToBeHitBy(fixtureB);
            }
        }
    }

    private void handleRatHitByPlayerBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Rat && (fixtureB.getUserData().equals("rightAttack")
                    || fixtureB.getUserData().equals("leftAttack")) ) {
                if (((Rat) fixtureA.getUserData()).getBody().getFixtureList().get(0) == fixtureA) {
                    if (fixtureB.getUserData().equals("rightAttack")) {
                        gameScreen.getPlayer().addEntityToHitTowardsRight((Rat) fixtureA.getUserData());
                    } else {
                        gameScreen.getPlayer().addEntityToHitTowardsLeft((Rat) fixtureA.getUserData());
                    }
                }
            }
        }
    }

    private void handleRatSensorBeginContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Rat
                && !fixtureB.isSensor()) {
            Rat rat = (Rat) fixtureA.getUserData();
            if (rat.getBody().getFixtureList().get(1) == fixtureA) {
                rat.addLeftSensorContact();
            } else if (rat.getBody().getFixtureList().get(2) == fixtureA) {
                rat.addRightSensorContact();
            }
        }
    }

    private void handleFallingPlatform(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof FallingPlatform && fixtureB.getUserData() instanceof Player) {
                ((FallingPlatform) fixtureA.getUserData()).setFalling();
            }
        }
    }

    private void handleFootBeginContact(Fixture fixture) {
        if (fixture.getUserData() != null && fixture.getUserData().equals("foot")) {
            gameScreen.getPlayer().setNumFootContacts(gameScreen.getPlayer().getNumFootContacts() + 1);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        handleFootEndContact(fixtureA);
        handleFootEndContact(fixtureB);

        handleRatSensorEndContact(fixtureA, fixtureB);
        handleRatSensorEndContact(fixtureB, fixtureA);

        handleVikingSensorEndContact(fixtureA, fixtureB);
        handleVikingSensorEndContact(fixtureB, fixtureA);

        handleDragonSensorEndContact(fixtureA, fixtureB);
        handleDragonSensorEndContact(fixtureB, fixtureA);

        handleRatHitByPlayerEndContact(fixtureA, fixtureB);
        handleRatHitByPlayerEndContact(fixtureB, fixtureA);

        handleVikingHitByPlayerEndContact(fixtureA, fixtureB);
        handleVikingHitByPlayerEndContact(fixtureB, fixtureA);

        handleDragonHitByPlayerEndContact(fixtureA, fixtureB);
        handleDragonHitByPlayerEndContact(fixtureB, fixtureA);

        handlePlayerHitBySmthEndContact(fixtureA, fixtureB);
        handlePlayerHitBySmthEndContact(fixtureB, fixtureA);

        handleVikingTriggerEndContact(fixtureA, fixtureB);
        handleVikingTriggerEndContact(fixtureB, fixtureA);

        handleEntranceEndContact(fixtureA, fixtureB);
        handleEntranceEndContact(fixtureB, fixtureA);

        handleDragonInCenterEndContact(fixtureA, fixtureB);
        handleDragonInCenterEndContact(fixtureB, fixtureA);
    }

    private void handleDragonInCenterEndContact(Fixture fixtureB, Fixture fixtureA) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Dragon && fixtureB.getUserData() instanceof StartBossfightSensor) {
                ((Dragon) fixtureA.getUserData()).setInCenter(false);
            }
        }
    }

    private void handleDragonHitByPlayerEndContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Dragon && (fixtureB.getUserData().equals("rightAttack")
                    || fixtureB.getUserData().equals("leftAttack"))) {
                if (((Dragon) fixtureA.getUserData()).getBody().getFixtureList().get(0) == fixtureA) {
                    if (fixtureB.getUserData().equals("rightAttack")) {
                        gameScreen.getPlayer().removeEntityToHitTowardsRight((Dragon) fixtureA.getUserData());
                    } else {
                        gameScreen.getPlayer().removeEntityToHitTowardsLeft((Dragon) fixtureA.getUserData());
                    }
                }
            }
        }
    }

    private void handleDragonSensorEndContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Dragon
                && fixtureB.getUserData() != null && fixtureB.getUserData() instanceof DragonPlatform) {
            Dragon dragon = (Dragon) fixtureA.getUserData();
            if (dragon.getBody().getFixtureList().get(1) == fixtureA) {
                dragon.removeLeftSensorContact();
            } else if (dragon.getBody().getFixtureList().get(2) == fixtureA) {
                dragon.removeRightSensorContact();
            }
        }
    }

    private void handleEntranceEndContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Entrance && fixtureB.getUserData() instanceof Player) {
                if (((Player) fixtureB.getUserData()).getBody().getFixtureList().get(1) == fixtureB) {
                    ((Player) fixtureB.getUserData()).setTouchingEntrance(false);
                }
            }
        }
    }

    private void handleVikingTriggerEndContact(Fixture fixtureB, Fixture fixtureA) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Viking && fixtureB.getUserData() instanceof Player
                    && ((Player) fixtureB.getUserData()).getBody().getFixtureList().get(1) == fixtureB
                    && ((Viking) fixtureA.getUserData()).getBody().getFixtureList().get(3) == fixtureA) {
                ((Viking) fixtureA.getUserData()).setTriggered(false);
            }
        }
    }

    private void handleVikingHitByPlayerEndContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Viking && (fixtureB.getUserData().equals("rightAttack")
                    || fixtureB.getUserData().equals("leftAttack"))) {
                if (((Viking) fixtureA.getUserData()).getBody().getFixtureList().get(0) == fixtureA) {
                    if (fixtureB.getUserData().equals("rightAttack")) {
                        gameScreen.getPlayer().removeEntityToHitTowardsRight((Viking) fixtureA.getUserData());
                    } else {
                        gameScreen.getPlayer().removeEntityToHitTowardsLeft((Viking) fixtureA.getUserData());
                    }
                }
            }
        }
    }

    private void handleVikingSensorEndContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Viking
                && !fixtureB.isSensor()) {
            Viking viking = (Viking) fixtureA.getUserData();
            if (viking.getBody().getFixtureList().get(1) == fixtureA) {
                viking.removeLeftSensorContact();
            } else if (viking.getBody().getFixtureList().get(2) == fixtureA) {
                viking.removeRightSensorContact();
            }
        }
    }

    private void handlePlayerHitBySmthEndContact(Fixture fixtureB, Fixture fixtureA) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null && fixtureA.getUserData() instanceof Player) {
            if ((fixtureB.getUserData() instanceof Rat && !fixtureB.isSensor())
                    || (fixtureB.getUserData() instanceof Viking
                    && (fixtureB.getBody().getFixtureList().get(0) == fixtureB
                    || fixtureB.getBody().getFixtureList().get(4) == fixtureB)
                    || fixtureB.getUserData() instanceof Fireball) ){
                gameScreen.getPlayer().removeFixtureToBeHitBy(fixtureB);
            }
        }
    }

    private void handleRatHitByPlayerEndContact(Fixture fixtureB, Fixture fixtureA) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof Rat && (fixtureB.getUserData().equals("rightAttack")
                    || fixtureB.getUserData().equals("leftAttack"))) {
                if (((Rat) fixtureA.getUserData()).getBody().getFixtureList().get(0) == fixtureA) {
                    if (fixtureB.getUserData().equals("rightAttack")) {
                        gameScreen.getPlayer().removeEntityToHitTowardsRight((Rat) fixtureA.getUserData());
                    } else {
                        gameScreen.getPlayer().removeEntityToHitTowardsLeft((Rat) fixtureA.getUserData());
                    }
                }
            }
        }
    }

    private void handleRatSensorEndContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Rat
                && !fixtureB.isSensor()) {
            Rat rat = (Rat) fixtureA.getUserData();
            if (rat.getBody().getFixtureList().get(1) == fixtureA) {
                rat.removeLeftSensorContact();
            } else if (rat.getBody().getFixtureList().get(2) == fixtureA) {
                rat.removeRightSensorContact();
            }
        }
    }

    private void handleFootEndContact(Fixture fixture) {
        if (fixture.getUserData() != null && fixture.getUserData().equals("foot")) {
            gameScreen.getPlayer().setNumFootContacts(gameScreen.getPlayer().getNumFootContacts() - 1);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

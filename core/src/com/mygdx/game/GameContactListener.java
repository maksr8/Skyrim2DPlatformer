package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import objects.FallingPlatform;
import objects.Player;
import objects.Rat;

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

        handleRatHitByPlayerBeginContact(fixtureA, fixtureB);
        handleRatHitByPlayerBeginContact(fixtureB, fixtureA);
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

        handleRatHitByPlayerEndContact(fixtureA, fixtureB);
        handleRatHitByPlayerEndContact(fixtureB, fixtureA);
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

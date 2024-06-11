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

        handleFootBeginContact(contact, fixtureA);
        handleFootBeginContact(contact, fixtureB);
        handleFallingPlatformBeginContact(contact, fixtureA, fixtureB);
        handleFallingPlatformBeginContact(contact, fixtureB, fixtureA);
        handleRatLeftSensorBeginContact(contact, fixtureA);
        handleRatLeftSensorBeginContact(contact, fixtureB);
    }

    private void handleRatLeftSensorBeginContact(Contact contact, Fixture fixture) {
        if (fixture.getUserData() != null && fixture.getUserData() instanceof Rat) {
            Rat rat = (Rat) fixture.getUserData();
            if (rat.getBody().getFixtureList().get(1) == fixture) {
                rat.addLeftSensorContact();
            } else if (rat.getBody().getFixtureList().get(2) == fixture) {
                rat.addRightSensorContact();
            }
        }
    }

    private void handleFallingPlatformBeginContact(Contact contact, Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof FallingPlatform && fixtureB.getUserData() instanceof Player) {
                ((FallingPlatform) fixtureA.getUserData()).setFalling();
            }
        }
    }

    private void handleFootBeginContact(Contact contact, Fixture fixture) {
        if (fixture.getUserData() != null && fixture.getUserData().equals("foot")) {
            gameScreen.getPlayer().setNumFootContacts(gameScreen.getPlayer().getNumFootContacts() + 1);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        handleFootEndContact(contact, fixtureA);
        handleFootEndContact(contact, fixtureB);
        handleRatLeftSensorEndContact(contact, fixtureA);
        handleRatLeftSensorEndContact(contact, fixtureB);
    }

    private void handleRatLeftSensorEndContact(Contact contact, Fixture fixture) {
        if (fixture.getUserData() != null && fixture.getUserData() instanceof Rat) {
            Rat rat = (Rat) fixture.getUserData();
            if (rat.getBody().getFixtureList().get(1) == fixture) {
                rat.removeLeftSensorContact();
            } else if (rat.getBody().getFixtureList().get(2) == fixture) {
                rat.removeRightSensorContact();
            }
        }
    }

    private void handleFootEndContact(Contact contact, Fixture fixture) {
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

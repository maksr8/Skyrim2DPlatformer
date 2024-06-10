package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import objects.player.FallingPlatform;
import objects.player.Player;

public class GameContactListener implements ContactListener {
    GameScreen gameScreen;

    public GameContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        handleFootBeginContact(contact, fixtureA, fixtureB);
        handleFallingPlatformBeginContact(contact, fixtureA, fixtureB);
        handleFallingPlatformBeginContact(contact, fixtureB, fixtureA);
    }

    private void handleFallingPlatformBeginContact(Contact contact, Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            if (fixtureA.getUserData() instanceof FallingPlatform && fixtureB.getUserData() instanceof Player) {
                ((FallingPlatform) fixtureA.getUserData()).setFalling();
            }
        }
    }

    private void handleFootBeginContact(Contact contact, Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot")) {
            gameScreen.getPlayer().setNumFootContacts(gameScreen.getPlayer().getNumFootContacts() + 1);
        }
        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot")) {
            gameScreen.getPlayer().setNumFootContacts(gameScreen.getPlayer().getNumFootContacts() + 1);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        handleFootEndContact(contact, fixtureA, fixtureB);
    }

    private void handleFootEndContact(Contact contact, Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot")) {
            gameScreen.getPlayer().setNumFootContacts(gameScreen.getPlayer().getNumFootContacts() - 1);
        }
        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot")) {
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

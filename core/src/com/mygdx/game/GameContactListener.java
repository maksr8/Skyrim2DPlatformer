package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class GameContactListener implements ContactListener {
    GameScreen gameScreen;

    public GameContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

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

package com.ScreensClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mijuego.*;
import com.utilClasses.MasterClass;
import com.utilClasses.Player;
import io.github.com.mygdx.game.Main;


public class MenuScreenClass {
    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9f;
    private float movementBackground = 0;
    private float posY;

    private float contState = 0;

    private final float destinyYtitle = WORLD_HEIGHT / 2f;
    private final float destinyXtitle = WORLD_WIDTH / 4f;

    private Screen screenSelect;

    private boolean oneShot = true;
    private boolean twoShot = false;
    private boolean state = true;
    private boolean inputEnable = false;

    private Table table;

    private TextButton buttonPlay;
    private TextButton buttonExit;
    private TextButton buttonLevels;
    private TextButton buttonOptions;

    private Main main;

    public void MenuEtc(Sprite gear, Sprite gear2){
        gear2.setPosition(WORLD_WIDTH / 2 - 4.3f, WORLD_HEIGHT / 2F);
        gear2.setSize(3f,3f);
        gear2.setOrigin(gear2.getWidth() / 2f, gear2.getHeight() / 2f);


        gear.setPosition(WORLD_WIDTH / 2 + 1f, WORLD_HEIGHT / 2F);
        gear.setSize(3f,3f);
        gear.setOrigin(gear.getWidth() / 2f, gear.getHeight() / 2f);
    }



    public void initializeButtons(TextButton buttonPlay, TextButton buttonExit, TextButton buttonLevels, TextButton buttonOptions , Table table, Main main){
        this.buttonPlay = buttonPlay;
        this.buttonExit = buttonExit;
        this.buttonLevels = buttonLevels;
        this.buttonOptions = buttonOptions;
        this.table = table;
        this.main = main;

        addAnimation(buttonExit);
        addAnimation(buttonLevels);
        addAnimation(buttonOptions);
        addAnimation(buttonPlay);

        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonPlay.setVisible(false);
                buttonPlay.setTouchable(Touchable.disabled);
                twoShot = true;
                state = false;
                screenSelect = new ScreenGameplay(main, Player.getNumberLevel());
            }
        });

        buttonExit.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
               Gdx.app.exit();
            }
        });

        buttonLevels.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonLevels.setVisible(false);
                buttonLevels.setTouchable(Touchable.disabled);
                twoShot = true;
                state = false;
                screenSelect = new ScreenLevels(main);

            }
        });

        buttonOptions.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonOptions.setVisible(false);
                buttonOptions.setTouchable(Touchable.disabled);
                twoShot = true;
                state = false;
                screenSelect = new ScreenGameplay(main, -1);
            }
        });

        table.add(buttonPlay).width(350).height(80).padBottom(15).padRight(15).row();
        table.add(buttonLevels).width(350).height(80).padBottom(15).padRight(15).row();
        table.add(buttonOptions).width(350).height(80).padBottom(15).padRight(15).row();
        table.add(buttonExit).width(350).height(80).padBottom(15).padRight(15).row();

        table.pack();

        buttonPlay.setOrigin(buttonPlay.getWidth() / 2f, buttonPlay.getHeight() / 2f);
        buttonLevels.setOrigin(buttonLevels.getWidth() / 2f, buttonLevels.getHeight() / 2f);
        buttonExit.setOrigin(buttonExit.getWidth() / 2f, buttonExit.getHeight() / 2f);
        buttonOptions.setOrigin(buttonOptions.getWidth() / 2f, buttonOptions.getHeight() / 2f);
    }

    private void addAnimation(Actor button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1.2f, 1.2f, 0.1f, Interpolation.smooth));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.smooth));
                }
            }
        });
    }

    public void moveButtons(Stage stage){

        if (oneShot) {
            oneShot = false;
            animateButtonIn(buttonPlay, 2f);
            animateButtonIn(buttonLevels,   2.1f);
            animateButtonIn(buttonOptions,    2.3f);
            animateButtonIn(buttonExit,       2.5f);
        }

        if(!inputEnable && buttonExit.getColor().a >= 0.99f) {
            inputEnable = true;
            Gdx.input.setInputProcessor(stage);
        }

        if(twoShot){
            twoShot = false;
            table.setTouchable(Touchable.disabled);
            Gdx.input.setInputProcessor(null);
            animateButtonOut(buttonPlay, 0.2f);
            animateButtonOut(buttonLevels,   0.4f);
            animateButtonOut(buttonOptions,    0.6f);
            animateButtonOut(buttonExit,       0.8f);
        }
    }

    private void animateButtonIn(TextButton button, float delayTime) {
        button.clearActions();
        button.setScale(0.1f, 0.1f);
        button.getColor().a = 0f;

        button.addAction(Actions.sequence(
            Actions.delay(delayTime),
            Actions.parallel(
                Actions.scaleTo(1f, 1f, 0.5f, Interpolation.smooth),
                Actions.fadeIn(0.5f, Interpolation.smooth)
            )
        ));
    }

    private void animateButtonOut(TextButton button, float delayTime) {
        button.clearActions();
        button.getColor().a = 1f;


        button.addAction(Actions.sequence(
            Actions.delay(delayTime),
            Actions.parallel(
                Actions.scaleTo(0.1f, 0.1f, 0.5f, Interpolation.smooth),
                Actions.fadeOut(0.5f, Interpolation.smooth)
            )
        ));
    }



    public void Background(SpriteBatch c, Texture thunder, Texture backSpace, float delta){
        movementBackground += delta * 60;

        c.setColor(Color.WHITE);

        c.draw(backSpace, 0, -movementBackground / 2, WORLD_WIDTH, WORLD_HEIGHT * 1.2f);
        c.draw(backSpace, 0, 9f - movementBackground / 2, WORLD_WIDTH, WORLD_HEIGHT * 1.2f);


        c.draw(thunder, WORLD_WIDTH / 4, -movementBackground, WORLD_WIDTH * 0.5F, WORLD_HEIGHT * 2);
        c.draw(thunder, WORLD_WIDTH / 4, WORLD_HEIGHT * 2 - movementBackground, WORLD_WIDTH * 0.5F, WORLD_HEIGHT * 2);

        if (movementBackground >= 18f) {
            movementBackground = 0;
        }

    }


    public void MenuTitle(SpriteBatch c, Sprite gear, Sprite gear2, Texture Title, float cont, float delta){
        if(state) {
            gear.rotate(90 * delta);
            gear2.rotate(-90 * delta);

            float alpha = MathUtils.clamp(cont / 4, 0f, 1f);

            posY = Interpolation.elastic.apply(20, destinyYtitle, alpha);

            gear.setY(posY);
            gear2.setY(posY);

            gear.draw(c);
            gear2.draw(c);
            c.draw(Title, destinyXtitle, posY, WORLD_WIDTH / 2, WORLD_WIDTH / 4f);
        }
        if(!state){
            contState += delta;
            gear.rotate(90 * delta);
            gear2.rotate(-90 * delta);

            float alpha = MathUtils.clamp(contState / 4, 0f, 1f);

            posY = Interpolation.elastic.apply(destinyYtitle, destinyYtitle + 6f, alpha);

            gear.setY(posY);
            gear2.setY(posY);

            gear.draw(c);
            gear2.draw(c);
            c.draw(Title, destinyXtitle, posY, WORLD_WIDTH / 2, WORLD_WIDTH / 4f);
        }
    }

    public float shapeRenderer(float superCont, float delta, Main game){
       if(state) {
           if (superCont > 0) {
               superCont -= delta * 0.5f;
           } else {
               superCont = 0;
           }

           game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
           game.shapeRenderer.setColor(new Color(1f, 1f, 1f, superCont));
           game.shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
           game.shapeRenderer.end();
       }
       if(!state){
           if (superCont <= 1) {
               superCont += delta * 0.5f;
           } else {
               superCont = 1;
               Screen screen = game.getScreen();
               game.setScreen(screenSelect);
               screen.dispose();
           }

           game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
           game.shapeRenderer.setColor(new Color(1f, 1f, 1f, superCont));
           game.shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
           game.shapeRenderer.end();
       }
       return superCont;
    }

}

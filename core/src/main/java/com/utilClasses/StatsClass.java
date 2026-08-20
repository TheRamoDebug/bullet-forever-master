package com.utilClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;


public class StatsClass implements Disposable {
    private Stage stage;
    private Skin skin;
    private Table table;
    private BitmapFont bitmapFont;

    private Label lifes;
    private Label textCoins;
    private Label textNumberCoins;
    private Label textScore;
    private Label textScoreNumber;
    private Label textScoreInfinte;
    private Label textScoreInfinteNumber;

    private Image life1;
    private Image life2;
    private Image life3;
    private Image life4;
    private Image life5;
    private Image life6;

    private Texture texture;


    private int i = Player.getEnemiesDeath();
    private int score = 0;


    public StatsClass(Viewport viewport, SpriteBatch batch, int mode) {
        Player.defaultStats();
        texture = new Texture("Sprites/playerPlane.png");


        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
        table = new Table();
        table.setFillParent(true);
        table.align(Align.top);

        bitmapFont = skin.getFont("title");
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textCoins = new Label("", skin);
        textCoins.setFontScale(5, 4);
        table.add(textCoins).colspan(3).row();

        textNumberCoins = new Label("", skin);
        textNumberCoins.setFontScale(4,3);
        table.add(textNumberCoins).colspan(3).row();

        lifes = new Label("|*VIDAS*|", skin);
        lifes.setFontScale(4,3);
        table.add(lifes).colspan(3).padTop(60).row();


        life1 = new Image(texture);
        table.add(life1).right().size(180, 60);

        life2 = new Image(texture);
        table.add(life2).size(180, 60);

        life3 = new Image(texture);
        table.add(life3).left().size(180, 60).row();

        life4 = new Image(texture);
        table.add(life4).right().size(180, 60);

        life5 = new Image(texture);
        table.add(life5).center().size(180, 60);

        life6 = new Image(texture);
        table.add(life6).left().size(180, 60).row();




        life1.getColor().a = 0f;
        life2.getColor().a = 0f;
        life3.getColor().a = 0f;
        life4.getColor().a = 0f;
        life5.getColor().a = 0f;
        life6.getColor().a = 0f;

        drawLife();

        textScoreInfinte = new Label("", skin);
        textScoreInfinte.setFontScale(4,3);
        table.add(textScoreInfinte).colspan(3).padTop(60).row();

        textScoreInfinteNumber = new Label("", skin);
        textScoreInfinteNumber.setFontScale(4,3);
        table.add(textScoreInfinteNumber).colspan(3).row();

        if(mode != -1){
            textScoreInfinte.setVisible(false);
            textScoreInfinteNumber.setVisible(false);
        }

        textScore = new Label("", skin);
        textScore.setFontScale(4,3);
        table.add(textScore).padTop(30).colspan(3).row();

        textScoreNumber = new Label("", skin);
        textScoreNumber.setFontScale(4,3);
        table.add(textScoreNumber).colspan(3).row();





        stage.addActor(table);
    }


    public void actu(){
        textCoins.setText("*ELIMINADOS*");
        textNumberCoins.setText("|" + i + "|");

        textScore.setText("*PUNTUACION*");
        textScoreNumber.setText("|" + score + "|");

        textScoreInfinte.setText("*PUNTUACION INFINITA*");
        textScoreInfinteNumber.setText("|" + Player.maxScore() + "|");



        if(Player.canRestLife()){
            Player.changeBool();
            deleteLife();
        }
    }

    public void addScore(int score){
        this.score += score;
    }


    public void addI(){
        i += 1;
        Player.score();
    }

    public void drawLife(){
        fadeIn(life1);
        fadeIn(life2);
        fadeIn(life3);
        switch (Player.getHealth()){
            case 4  -> {
                fadeIn(life4);
            }
            case 5 -> {
                fadeIn(life4);
                fadeIn(life5);
            }
            case 6 -> {
                fadeIn(life4);
                fadeIn(life5);
                fadeIn(life6);
            }
        }
    }


    public void deleteLife(){
        switch (Player.getHealth()){
            case 5 -> {
                fadeOff(life6);
            }

            case 4 -> {
                fadeOff(life6);
                fadeOff(life5);
            }

            case 3 -> {
                fadeOff(life6);
                fadeOff(life5);
                fadeOff(life4);
            }

            case 2 -> {
                fadeOff(life6);
                fadeOff(life5);
                fadeOff(life4);
                fadeOff(life3);
            }

            case 1 -> {
                fadeOff(life6);
                fadeOff(life5);
                fadeOff(life4);
                fadeOff(life3);
                fadeOff(life2);
            }
            case 0 -> {
                fadeOff(life6);
                fadeOff(life5);
                fadeOff(life4);
                fadeOff(life3);
                fadeOff(life2);
                fadeOff(life1);
            }
        }
    }


    public void fadeIn(Image image){
        image.addAction(Actions.fadeIn(2, Interpolation.elastic));
    }

    public void fadeOff(Image image){
        image.addAction(Actions.fadeOut(0.5f, Interpolation.elastic));
    }




    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }



    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        texture.dispose();
    }
}

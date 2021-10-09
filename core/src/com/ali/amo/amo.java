package com.ali.amo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;


import java.sql.Time;
import java.util.Objects;
import java.util.Random;


public class amo extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
//	ShapeRenderer shapeRenderer;
    Texture gameover;
	Texture[] birds;
	int flapstate = 0;
	float birdY=0;


	float velocity=0;

	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
//	Preferences pref ;
//
//	int highscore = 0;
    FreeTypeFontGenerator fontGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	BitmapFont font;






	int gamestate = 0;
	float gravity = 1.8f;

	Texture toptube;
	Texture bottomtube;
	float gap = 420;
	float maxTubeoffset;
	Random randomGenerator;

	float tubevelocity = 4;

	int numberofTubes = 4;
	float[] tubeX = new float[numberofTubes];
	float[] tubeoffset = new float[numberofTubes];
	float distancebetweenTubes;
	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangle;
	public Music flap;
	public Music end;
	public Music scor;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
//		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-ExtraBold.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 80;
		fontParameter.borderWidth = 5;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.color = Color.WHITE;


		font = fontGenerator.generateFont(fontParameter);

//		pref = Gdx.app.getPreferences("gameprefernces");
//		highscore = pref.getInteger("highscore");

//		font.setColor(Color.WHITE);
//		font.getData().setScale(5);


		birds = new Texture[4];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird.png");




		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		maxTubeoffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distancebetweenTubes = Gdx.graphics.getWidth() * 3/ 4;
		toptubeRectangles = new Rectangle[numberofTubes];
		bottomtubeRectangle = new Rectangle[numberofTubes];
		end = Gdx.audio.newMusic(Gdx.files.internal("end.mp3"));


		scor = Gdx.audio.newMusic(Gdx.files.internal("point.mp3"));

		startgame();






	}

	public void startgame(){

		birdY = Gdx.graphics.getHeight()/2-birds[flapstate].getHeight()/2;
		flap = Gdx.audio.newMusic(Gdx.files.internal("touch.mp3"));




		for (int i=0; i<numberofTubes; i++)
		{

			tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + Gdx.graphics.getWidth() + i*distancebetweenTubes;
			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangle[i] = new Rectangle();

		}


	}





	@Override
	public void render ()
	{
		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



		if (gamestate == 1){

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2)
			{
				score++;

				Gdx.app.log("Scor", String.valueOf(score));

				if (scoringTube < numberofTubes-1)
				{
					scoringTube++;
					scor.play();



				}
				else
					{

						scoringTube = 0;
						scor.play();



				    }
			}


			if (Gdx.input.justTouched())
			{
				flap.play();
				flap.setVolume(0.3f);
				velocity =-26;




			}

			for (int i=0; i<numberofTubes; i++)
			{
				if (tubeX[i] < - toptube.getWidth())
				{
					tubeX[i] += numberofTubes*distancebetweenTubes;
					tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}
				else
					{

					tubeX[i] = tubeX[i] - tubevelocity;



				    }


				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);

				toptubeRectangles[i] = new Rectangle(tubeX[i] ,Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] ,toptube.getWidth() , toptube.getHeight());
				bottomtubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());

			}







			if (birdY > 0 )

			{


					velocity = velocity + gravity;
					birdY -= velocity;



			}

			else
				{

					gamestate = 2;
					end.play();



			     }

			if (birdY > Gdx.graphics.getHeight())
			{
				gamestate = 2;
				end.play();

			}





		}
		else if (gamestate == 0){

			if (Gdx.input.justTouched())
			{


				gamestate = 1;


			}

		}
		else if (gamestate == 2)
		{
//			int highscore = pref.getInteger("highscore",0);
//			if (highscore>=score)
//			{
//
//				font.draw(batch,String.valueOf(highscore),Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/1.6f);
//
//
//			}
//			else {
//
//				pref.putInteger("highscore",score);
//				pref.flush();
//			}


			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/2-gameover.getHeight()/2);





			if (Gdx.input.justTouched())
			{


				gamestate = 0;
				startgame();
				score = 0;
				scoringTube = 0;
				velocity = 0;

			}






		}




		if (flapstate==0)
		{
			flapstate=1;


		}
		else
			{


			 flapstate=0;

		   }


		batch.draw(birds[flapstate], Gdx.graphics.getWidth()/2-birds[flapstate].getWidth()/2,birdY);
		font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/1.35f);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2 , birdY + birds[flapstate].getHeight()/2 , birds[flapstate].getWidth()/2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);



		for (int i=0; i<numberofTubes; i++)
		{
//			shapeRenderer.rect(tubeX[i] ,Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i] ,toptube.getWidth() , toptube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());

			if (Intersector.overlaps(birdCircle,toptubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomtubeRectangle[i]))
			{


				gamestate = 2;




			}







		}










//		shapeRenderer.end();

	}






	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		end.dispose();
		scor.dispose();
		flap.dispose();

	}
}

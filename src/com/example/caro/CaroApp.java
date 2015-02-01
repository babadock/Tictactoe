package com.example.caro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaroApp extends ActionBarActivity {

	private CaroGame nGame;

	private Button mBoardButton[];

	private TextView mInfoTextView;
	private TextView mHumanCount;
	private TextView mTieCount;
	private TextView mAndroidCount;

	private int mHumanCounter = 0;
	private int mTieCounter = 0;
	private int mAndroidCounter = 0;

	private boolean mHumanFirst = true;
	private boolean mGameOver = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caro_app);
		nGame = new CaroGame();
		mBoardButton = new Button[nGame.getBOARD_SIZE()];
		mBoardButton[0] = (Button) findViewById(R.id.one);
		mBoardButton[1] = (Button) findViewById(R.id.two);
		mBoardButton[2] = (Button) findViewById(R.id.three);
		mBoardButton[3] = (Button) findViewById(R.id.four);
		mBoardButton[4] = (Button) findViewById(R.id.five);
		mBoardButton[5] = (Button) findViewById(R.id.six);
		mBoardButton[6] = (Button) findViewById(R.id.seven);
		mBoardButton[7] = (Button) findViewById(R.id.eight);
		mBoardButton[8] = (Button) findViewById(R.id.nine);

		mInfoTextView = (TextView) findViewById(R.id.information);
		mHumanCount = (TextView) findViewById(R.id.humanCount);
		mTieCount = (TextView) findViewById(R.id.tiesCount);
		mAndroidCount = (TextView) findViewById(R.id.androidCount);

		mHumanCount.setText(Integer.toString(mHumanCounter));
		mTieCount.setText(Integer.toString(mTieCounter));
		mAndroidCount.setText(Integer.toString(mAndroidCounter));

		startNewGame();
	}

	private void startNewGame() {
		mGameOver = false;
		nGame.clearBoard();

		for (int i = 0; i < nGame.getBOARD_SIZE(); i++) {
			mBoardButton[i].setText("");
			mBoardButton[i].setEnabled(true);
			mBoardButton[i].setOnClickListener(new ButtonClickListener(i));
		}

		if (mHumanFirst) {
			mInfoTextView.setText(R.string.first_human);
			mHumanFirst = false;
		} else {
			mInfoTextView.setText(R.string.turn_computer);
			int move = nGame.getComputerMove();
			setMove(nGame.ANDROID_PLAYER, move);
			mHumanFirst = true;
		}
	}

	private class ButtonClickListener implements View.OnClickListener {
		int location;

		public ButtonClickListener(int location) {
			this.location = location;
		}

		public void onClick(View v) {
			if (!mGameOver) {
				if (mBoardButton[location].isEnabled()) {
					setMove(nGame.HUMAN_PLAYER, location);

					int winner = nGame.checkForWinner();

					if (winner == 0) {
						mInfoTextView.setText(R.string.turn_computer);
						int move = nGame.getComputerMove();
						setMove(nGame.ANDROID_PLAYER, move);
						winner = nGame.checkForWinner();
					}

					if (winner == 0)
						mInfoTextView.setText(R.string.turn_human);
					else if (winner == 1) {
						mInfoTextView.setText(R.string.result_tie);
						mTieCounter++;
						mTieCount.setText(Integer.toString(mTieCounter));
						mGameOver = true;
					} else if (winner == 2) {
						mInfoTextView.setText(R.string.result_human_wins);
						mHumanCounter++;
						mHumanCount.setText(Integer.toString(mHumanCounter));
						mGameOver = true;
					} else {
						mInfoTextView.setText(R.string.result_android_wins);
						mAndroidCounter++;
						mAndroidCount
								.setText(Integer.toString(mAndroidCounter));
						mGameOver = true;
					}
				}
			}

		}
	}

	private void setMove(char player, int location) {
		nGame.setMove(player, location);
		mBoardButton[location].setEnabled(false);
		mBoardButton[location].setText(String.valueOf(player));
		if (player == nGame.HUMAN_PLAYER)
			mBoardButton[location].setTextColor(Color.GREEN);
		else
			mBoardButton[location].setTextColor(Color.RED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id) {
		case R.id.newGame:
			startNewGame();
			break;
		case R.id.exitGame:
			CaroApp.this.finish();
			break;
		}
		return true;
	}
}

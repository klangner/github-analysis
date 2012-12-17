package com.matrobot.gha;

import java.io.IOException;

public interface ICommand {

	public void run(Configuration params) throws IOException;
}

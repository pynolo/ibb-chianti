package it.burningboots.greeter.client;

import it.burningboots.greeter.shared.entity.Level;

public interface ILevelHandler {

	public void updateLevel(Level level);
	
	public void handleExceededLimit();
}

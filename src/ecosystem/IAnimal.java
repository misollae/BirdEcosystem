package ecosystem;

public interface IAnimal {
	public Animal reproduce(boolean mutate);
	public void eat(Terrain terrain);
	public void energy_consumption(float dt, Terrain terrain);
	public boolean die();
}

public class Cell {
	private Cell north;
	private Cell east;
	private Cell south;
	private Cell west;
	
	private boolean carvedNorth;
	private boolean carvedEast;
	
	private Status status;
	private Position position;
	
	public void carveWall(Direction dir) {
		if (dir == Direction.NORTH && north != null) {
			carvedNorth = true;
		} else if (dir == Direction.EAST && east != null) {
			carvedEast = true;
		} else if (dir == Direction.SOUTH && south != null) {
			south.carveWall(Direction.NORTH);
		} else if (dir == Direction.WEST && west != null) {
			west.carveWall(Direction.EAST);
		}
	}
	
	public void createWall(Direction dir) {
		if (dir == Direction.NORTH && north != null) {
			carvedNorth = false;
		} else if (dir == Direction.EAST && east != null) {
			carvedEast = false;
		} else if (dir == Direction.SOUTH && south != null) {
			south.createWall(Direction.NORTH);
		} else if (dir == Direction.WEST && west != null) {
			west.createWall(Direction.EAST);
		}
	}
	
	public void setStatus(Status newStatus) {
		status = newStatus;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean carved(Direction direction) {
		if (direction == Direction.NORTH) {
			return carvedNorth;
		} else if (direction == Direction.EAST) {
			return carvedEast;
		} else if (direction == Direction.SOUTH) {
			return south != null && south.carved(Direction.NORTH);
		}
		return west != null && west.carved(Direction.EAST);
	}
	
	public Cell getNeighbor(Direction direction) {
		if (direction == Direction.NORTH) {
			return north;
		} else if (direction == Direction.EAST) {
			return east;
		} else if (direction == Direction.SOUTH) {
			return south;
		}
		return west;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setNeighbor(Direction dir, Cell neighbor) {
		if (dir == Direction.NORTH) {
			north = neighbor;
		} else if (dir == Direction.EAST) {
			east = neighbor;
		} else if (dir == Direction.SOUTH) {
			south = neighbor;
		} else {
			west = neighbor;
		}
	}
	
	public Cell(Position position, Cell north, Cell east, Cell south, Cell west) {
		this.position = position;
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		status = Status.GRAY;
	}
	
	public Cell(Position position) {
		this(position, null, null, null, null);
	}
}
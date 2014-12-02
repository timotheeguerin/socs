Door(id) :: *[

	elevator?true -> openDoor();
	|
	elevator?false -> closeDoor();

]

FloorButton(floorNumber) :: *[
  persons?True -> elevator?floorNumber
]

ElevatorButton(floorNumber) :: *[
persons?True -> elevator?floorNumber
]

Elevator() :: (
  int currentFloor = 0;
  int x;
  Person person;
  List<Person>[] personForFloor;

  Queue<Integer> queue;
  *[
    floorButtons?(person, x) | elevatorButtons?(person, x) ->
      personForFloor[x].add(person)
      queue.isEmpty() ->
        elevatorEngine!x;
      queue.notContains(x) ->
        queue.add(x);
    |
    elevatorEngine?x ->
      currentFloor = x;
      doors[currentFloor]!True

      -- Tell the persons that need to leave or enter the elevator on this floor
      personForFloor[currentFloor]!True

      -- Wait for the persons to get on the elevator
      personForFloor[currentFloor]?True

      doors[currentFloor]!False

      personForFloor[currentFloor].clear();

      queue.remove();
      !queue.isEmpty() ->
        int nextFloor = queue.peek();
        elevatorEngine!nextFloor
  ]
)

ElevatorEngine :: * [
  int x;
  -- Wait for the elevator to order a move
  elevator?x;
  -- Move
  moveTo(x);
  -- Notify the elevator we arrived at destination
  elevator!x;
]


Person (id) :: (
  int currentFloor = rand(3);
  int destinationFloor;
  int x;

  * [
    -- Call the elevator by pressing the button
    floorButtons[currentFloor]!True ->

      -- Wait for the elevator
      elevator?x ->
        enterElevator();
        destination = rand(3);
        -- Tell the elevator the destination by pressing the elevator buttons
        elevatorButtons[destination]!True ->
          -- Wait again for the elevator to reach your destination
          elevator?x ->
            leaveElevator();

  ]
)


Door(0) || Door(1) || Door(2) ||
  ElevatorButton(0) || ElevatorButton(1) || ElevatorButton(2) ||
  FloorButton(0) || FloorButton(1) || FloorButton(2)
  || Elevator() || ElevatorEngine()
  || Person(0) || Person(1) || ... || Person(n-1)

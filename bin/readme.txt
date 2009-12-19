1.) Game / GUI - runs game, draws graphics, and handles user input
2.) Game State - basic game state and related state logic
3.) State Updater - thin interface between game state and routing which
basically serves as a message passing abstraction that (de-)marshalls
state change objects received from the network.
4.) Routing Network - distributes the game state among peers
5.) Network Peer Implementation - low-level networking functions
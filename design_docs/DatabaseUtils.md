# DatabaseUtils

## SqlObject
	- Will have various primitive data types (int, String, etc)
		- May also have methods for loading sub-objects (e.g. Country has ArrayList<City>)
			- Try to automate this
		- Relations (e.g. parentId / childId formalized)
			- Some fields can be marked as autoincrement?
	- Try to automate CRUD
	- Some way to notify observers when data is changed

## SqlManager (abstraction layer that works with SqlObjects and Cursors to query the database)
	- Things I need to be able to do
		- Load all workouts sorted by int order
			- Load lifts associated with workouts, sorted by order
		- Create a full set of WorkoutLog/LiftLog/SetLog based on Workout/List<Lift> selected
			- Should continue to be fully editable
				- Note: need to work on the UI for this
					- Set logs use the "back side" metaphor - (need to improve implementation though)
					- Workout / Lifts / Sets x Reps should have a menu item?
		- Load all workout logs between startDate & endDate
			- Choose between multiple workouts on a single date
			- Edit the workout (same UI as first creating the workout)
			- Delete workout
		- Load, delete, reorder, etc for workouts & lifts\
		- Load parent (from parentId)
		- Load children (requires some kind of child type definition)
		- Load days of week in order

}
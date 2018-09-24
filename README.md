Challenge to consume an API and create a simple rpg game.
- Retrofit to consume API
- I had to add tls 1.2 manually for API 19
- Picasso to load and cache image (I didn't use any UI function of Picasso)
- Maintain state of app, saving data (TODO a proper sync adapter and a local persistence layer, doing today)
- a different layout for landscape orientation
- two recyclerviews for each team
- I created a seekbar tlwith bind the criteria value
- A shuffle button to create transformers
- works well with different screens
- battle screen with animation (can skip animation)
- i assumed that in a run away, both transformers survive
- unit test for every aspect of the battle
- ui tests for creation and update trabsformer (for now)

![git demo](https://github.com/pebertli/transformers/blob/master/aeq_gif_1.gif)

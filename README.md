#Vagabond-X 


## Services:

To run this project use `lein ring server-headless`

The api is as follows:

	GET /posts - all posts
	GET /posts/:slug - get a single post
	GET /posts/author/:author - get all post by author

## Built using

  * [Liberator](http://clojure-liberator.github.io/liberator/)
  * [Compojure](https://github.com/weavejester/compojure)
  * [korma](http://sqlkorma.com)
  * [cheshire](https://github.com/dakrone/cheshire)
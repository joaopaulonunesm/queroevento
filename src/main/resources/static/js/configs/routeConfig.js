angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/index/home.html"
	});

	$routeProvider.when("/login", {
		templateUrl: "views/index/login.html"
	});

	$routeProvider.when("/category/:name", {
		templateUrl: "views/index/categoria.html"
	});

	$routeProvider.when("/event/:name", {
		templateUrl: "views/index/evento.html"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});
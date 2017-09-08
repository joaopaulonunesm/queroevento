angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/index/home.html"
	});

	$routeProvider.when("/login", {
		templateUrl: "views/index/login.html",
		controller: "loginCtrl"
	});

	$routeProvider.when("/category/:urlName", {
		templateUrl: "views/index/categoria.html",
		controller: "eventByCategoryCtrl"
	});

	$routeProvider.when("/event/:urlTitle", {
		templateUrl: "views/index/evento.html",
		controller: "eventByUrlTitleCtrl"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});
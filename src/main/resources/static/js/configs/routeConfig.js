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

	$routeProvider.when("/event/title/:urlTitle", {
		templateUrl: "views/index/evento.html",
		controller: "eventByUrlTitleCtrl"
	});
	
	$routeProvider.when("/event/search", {
		templateUrl: "views/index/buscaEvento.html",
	});
	
	$routeProvider.when("/:nameUrl", {
		templateUrl: "views/index/perfilEmpresa.html",
		controller: "eventByCompanyCtrl"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});
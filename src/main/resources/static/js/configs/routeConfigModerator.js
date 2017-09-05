angular.module("queroEventoApp").config(function ($routeProvider) {
	
	$routeProvider.when("/", {
		templateUrl: "views/moderator/component/dashboard.html"
	});
	
	$routeProvider.when("/events", {
		templateUrl: "views/moderator/component/evento.html"
	});
	
	$routeProvider.when("/categories", {
		templateUrl: "views/moderator/component/categoria.html"
	});
	
	$routeProvider.when("/offers", {
		templateUrl: "views/moderator/component/ofertas.html"
	});
	
	$routeProvider.when("/settings", {
		templateUrl: "views/moderator/component/configuracao.html"
	});
		
	$routeProvider.otherwise({redirectTo: "/"});
	
});